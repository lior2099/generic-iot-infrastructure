/*------------------------------------------------------------------------
Name: ThreadPool.java
Author: Lior shalom
Reviewer:
Date: 19/08/2024
------------------------------------------------------------------------*/

package il.co.ilrd.thread_pool;

import il.co.ilrd.waitable_pq.FixedWaitablePQ;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.util.concurrent.Executors.callable;

// chech if - at setnumOfThreads
public class ThreadPool implements Executor {
    private static final int DEAFULT_NUM_OF_THREADS = 8;
    private final Priority DEFAULT_PRIORITY = Priority.MEDIUM;
    private final static int HIGHER_PRIORITY = Priority.HIGH.ordinal() + 1;
    private final static int PUSH_NEW_THREADS_PRIORITY = HIGHER_PRIORITY + 1;
    private final static int SHUTDOWN_PRIORITY = Priority.LOW.ordinal() -1;
    private final static int LAST_TASK_PRIORITY = SHUTDOWN_PRIORITY - 1;
    private final int QFIXFACTOR = 3;

    private final Collection<Thread> pool;
    private final FixedWaitablePQ<Task<?>> taskQueue;
    private int numOfThreads;

    private final Semaphore semNotDone = new Semaphore(0);
    private final Semaphore semShutDown = new Semaphore(0);
    private final Lock lockPool = new ReentrantLock();
    private final Condition condLockPool = lockPool.newCondition();


    private volatile boolean isPause = false;
    private boolean isShutDownTime = false;


    public ThreadPool() {
        this(DEAFULT_NUM_OF_THREADS);
    }

    public ThreadPool(int numOfThreads) {
        if(numOfThreads < 1){
            throw new IllegalArgumentException("numOfThreads need to be bigger then zero");
        }

        this.numOfThreads = numOfThreads;
        pool = new ArrayList<>(numOfThreads);
        taskQueue = new FixedWaitablePQ<>(numOfThreads*QFIXFACTOR);
        addThreadsToPool(numOfThreads);
        startAllTasks();
    }

    @Override
    public void execute(Runnable runnable) {
        submit(runnable , DEFAULT_PRIORITY);
    }

    public Future<?> submit(Runnable runnable, Priority priority) {

        return submit(callable(runnable), priority);
    }

    public <V> Future<V> submit(Runnable runnable, Priority priority, V returnValue) {

        return submit(callable(runnable , returnValue), priority);
    }

    public <V> Future<V> submit(Callable<V> callable) {

        return submit(callable, DEFAULT_PRIORITY);
    }

    public <V> Future<V> submit(Callable<V> callable, Priority priority) {
        Objects.requireNonNull(callable);

        if(isShutDownTime){
            throw new IllegalArgumentException();
        }

        Task<V> newTask = new Task<>(priority , callable);

        try {
            taskQueue.enqueue(newTask);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return newTask.future;
    }

    public void setNumOfThreads(int numOfThreads) {
        if(numOfThreads < 1){
            throw new IllegalArgumentException("numOfThreads need to be bigger then zero");
        }

        int addOrRemove = this.numOfThreads - numOfThreads;
        Task<?> newTask = new Task<>(PUSH_NEW_THREADS_PRIORITY , new ShutDownThread());
        Thread threadToAdd  = null;

        while (addOrRemove < 0 ){
            this.numOfThreads++;
            threadToAdd = new WorkerThread();
            pool.add(threadToAdd);
            threadToAdd.start();
            addOrRemove++;
        }

        for (int i = 0 ; i < addOrRemove ; i++){
            try {
                taskQueue.enqueue(newTask);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public void pause() {

        isPause = true;

        for (int i = 0 ; i < pool.size() ; i++){
            submit(new PauseThread() , HIGHER_PRIORITY);
        }
    }

    public void resume() {
        isPause = false;

        lockPool.lock();
        condLockPool.signalAll();
        lockPool.unlock();
    }

    public void shutdown() {
        isShutDownTime = true;

        Task<?> newTask = new Task<>(SHUTDOWN_PRIORITY , new ShutDownThread());
        Task<?> lestTask = new Task<>(LAST_TASK_PRIORITY  , new ShutDownLestThread());

        for (int i = 0 ; i < pool.size() - 1 ; i++){
            try {
                taskQueue.enqueue(newTask);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            taskQueue.enqueue(lestTask);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        boolean isShoutDown = false;

        isShoutDown = semShutDown.tryAcquire(timeout, unit);

        if(isShoutDown){
            semShutDown.release();
        }
        return isShoutDown;
    }

    public void awaitTermination() throws InterruptedException {

        semShutDown.acquire();
        semShutDown.release();
    }

    private void addThreadsToPool(int size){
        for (int i = 0 ; i < size ; i++){
            pool.add(new WorkerThread());
        }
    }

    private void startAllTasks(){
       for (Thread thread : pool){
           thread.start();
       }
    }

    private  <V> Future<V> submit(Callable<V> callable, int priority) {

        Task<V> newTask = new Task<>(priority , callable);

        try {
            taskQueue.enqueue(newTask);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return newTask.future;
    }

    private final class WorkerThread extends Thread {
        private boolean isRuning = true;

        @Override
        public void run() {
            while (isRuning){
                Task<?> taskToRun = null;

                try {
                    taskToRun = taskQueue.dequeue();
                    taskToRun.runTask();
                } catch (InterruptedException ignored) { }
            }

        }
    }

    private class Task<V>  implements Comparable<Task<V>> {

        private final int priority;
        private final Callable<V> operation;
        private final Future<V> future;
        private boolean gotException = false;
        private Throwable whatException;

        public V result = null;
        private volatile boolean isDone = false;
        private volatile boolean isCancelled = false;


        private Task(Callable<V> operation) {
            this(DEFAULT_PRIORITY, operation);
        }

        private Task(Priority priority, Callable<V> operation) {
            this.priority = priority.ordinal();
            this.operation = operation;
            this.future = new TaskFuture();
        }

        private Task(int priority, Callable<V> operation) {
            this.priority = priority;
            this.operation = operation;
            this.future = new TaskFuture();
        }

        @Override
        public int compareTo(Task<V> vTask) {
            return Integer.compare(vTask.priority , this.priority);
        }

        private Future<V> getFuture() {
            return future;
        }

        private void runTask(){
            try {
                result = operation.call();
            } catch (Exception e) {
                whatException = e;
                gotException = true;
            }
            isDone = true;
            semNotDone.release();
        }


        private class TaskFuture implements Future<V> {

            @Override
            public boolean cancel(boolean b) {
                if(isDone()){
                    return false;
                }

                try {
                    isCancelled = taskQueue.remove(Task.this);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                return isCancelled;
            }

            @Override
            public boolean isCancelled() {
                return isCancelled;
            }

            @Override
            public boolean isDone() {
                if(isCancelled){
                    return true;
                }
                return isDone;
            }

            @Override
            public V get() throws InterruptedException, ExecutionException {
                semNotDone.acquire();
                semNotDone.release();
                if(gotException){
                    throw new ExecutionException(whatException);
                }
                return result;
            }

            @Override
            public V get(long l, TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {
                boolean gotData = false;

                gotData = semNotDone.tryAcquire(l , timeUnit);
                if (gotData){
                    semNotDone.release();

                    if(gotException){
                        throw new ExecutionException(whatException);
                    }
                } else {
                    throw new TimeoutException();
                }

                return result;
            }
        }

    }

    private class PauseThread implements Callable<Integer> {

        @Override
        public Integer call() throws Exception {
                 lockPool.lock();
                 while (isPause){
                     condLockPool.await();
                 }
                 lockPool.unlock();
                 return null;
        }
    }

    private class ShutDownThread implements Callable<Integer> {

        @Override
        public Integer call() throws Exception {
            ((WorkerThread)(Thread.currentThread())).isRuning = false;
            synchronized (ThreadPool.this){
                pool.remove(Thread.currentThread());
                numOfThreads--;
            }

            return null;
        }
    }

    private class ShutDownLestThread implements Callable<Integer> {

        @Override
        public Integer call() throws Exception {

            semShutDown.release();
            ((WorkerThread)(Thread.currentThread())).isRuning = false;
            synchronized (ThreadPool.this){
                pool.remove(Thread.currentThread());
            }
            return null;
        }
    }




}