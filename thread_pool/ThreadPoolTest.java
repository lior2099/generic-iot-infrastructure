/*------------------------------------------------------------------------
Name: ThreadPoolTest.java
Author: Lior shalom (base on lior)
Reviewer:
Date: 19/08/2024
------------------------------------------------------------------------*/

package il.co.ilrd.thread_pool;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

public class ThreadPoolTest {

    @Test
    public void createTest() {
        ThreadPool pool = new ThreadPool();

        assertNotNull(pool);
    }

    @Test
    public void executeRunnableTest_when_singleThreadPool() {
        ThreadPool pool = new ThreadPool(1);
        pool.execute(new BasicRunnable());
        pool.shutdown();
    }

    @Test
    public void executeRunnableTest_when_multiThreadPool_and_more_tasks_than_threads() throws InterruptedException {
        ThreadPool pool = new ThreadPool(3);
        for (int i = 0; 10 > i; ++i) {
            pool.execute(new BasicRunnable());
        }
        Thread.sleep(3000);
        pool.shutdown();
    }

    @Test
    public void execute_when_multiThreadPool_and_computational_tasks() throws InterruptedException {
        ThreadPool pool = new ThreadPool(3);
        for (int i = 0; 10 > i; ++i) {
            pool.execute(new RandomNumDigitSummer());
        }
        Thread.sleep(2000);
        pool.shutdown();
    }

    @Test
    public void submitCallableTest_when_singleThreadPool() throws InterruptedException {
        ThreadPool pool = new ThreadPool(1);
        pool.submit(new BasicCallable());
        Thread.sleep(500);
        pool.shutdown();
    }

    @Test
    public void submitCallableTest_when_multiThreadPool() throws InterruptedException {
        ThreadPool pool = new ThreadPool(3);
        for (int i = 0; 10 > i; ++i) {
            pool.submit(new BasicCallable());
        }
        Thread.sleep(2000);
        pool.shutdown();
    }

    @Test
    public void submitRunnableTest_with_priority() throws InterruptedException {
        ThreadPool pool = new ThreadPool(3);
        int NUMBER_OF_INPUTS = 10;
        RunnableWithReturnValue[] runnableWithInputArray = new RunnableWithReturnValue[NUMBER_OF_INPUTS];
        for (int i = 0; NUMBER_OF_INPUTS > i; ++i) {
            runnableWithInputArray[i] = new RunnableWithReturnValue(i);
        }
        Priority[] priorityArray = {Priority.LOW, Priority.MEDIUM, Priority.HIGH,
                Priority.LOW, Priority.MEDIUM, Priority.HIGH, Priority.LOW,
                Priority.MEDIUM, Priority.HIGH, Priority.HIGH};

        for (int i = 0; 10 > i; ++i) {
            pool.submit(runnableWithInputArray[i], priorityArray[i]);
        }

        Thread.sleep(2000);
        pool.shutdown();
    }

    @Test
    public void submitRunnableTest_with_priority_and_with_return_value()
            throws InterruptedException, ExecutionException {
        ThreadPool pool = new ThreadPool(3);
        int NUMBER_OF_INPUTS = 10;
        Runnable[] runnableWithInputArray = new RunnableWithReturnValue[NUMBER_OF_INPUTS];
        for (int i = 0; NUMBER_OF_INPUTS > i; ++i) {
            runnableWithInputArray[i] = new RunnableWithReturnValue(i);
        }
        Priority[] priorityArray = {Priority.LOW, Priority.MEDIUM, Priority.HIGH,
                Priority.LOW, Priority.MEDIUM, Priority.HIGH, Priority.LOW,
                Priority.MEDIUM, Priority.HIGH, Priority.HIGH};

        List<Future<Integer>> futureList = new ArrayList<>(NUMBER_OF_INPUTS);

        for (int i = 0; NUMBER_OF_INPUTS > i; ++i) {
            futureList.add(i, pool.submit(runnableWithInputArray[i], priorityArray[i], i));
        }

        Thread.sleep(2000);

        for (int i = 0; NUMBER_OF_INPUTS > i; ++i) {
            assertEquals(i, futureList.get(i).get());
        }

        pool.shutdown();
    }

    @Test
    public void submitCallableTest_with_priority_and_with_return_value()
            throws InterruptedException, ExecutionException {
        ThreadPool pool = new ThreadPool(3);
        int NUMBER_OF_INPUTS = 10;
        List<Callable<Integer>> callableWithReturnValList = new ArrayList<>(NUMBER_OF_INPUTS);
        for (int i = 0; NUMBER_OF_INPUTS > i; ++i) {
            callableWithReturnValList.add(i, new PowerCalculator(i));
        }

        Priority[] priorityArray = {Priority.LOW, Priority.MEDIUM, Priority.HIGH,
                Priority.LOW, Priority.MEDIUM, Priority.HIGH, Priority.LOW,
                Priority.MEDIUM, Priority.HIGH, Priority.HIGH};

        List<Future<Integer>> futureList = new ArrayList<>(NUMBER_OF_INPUTS);

        for (int i = 0; NUMBER_OF_INPUTS > i; ++i) {
            futureList.add(i, pool.submit(callableWithReturnValList.get(i), priorityArray[i]));
        }

        for (int i = 0; NUMBER_OF_INPUTS > i; ++i) {
            assertEquals((i * i), futureList.get(i).get());
        }

        pool.shutdown();
    }

    @Test
    public void futureCancelTest_when_mayInterrupt_is_false() {
        ThreadPool pool = new ThreadPool(1);
        Future<Integer> future = pool.submit(new SleepingCallable());

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException();
        }

        assertFalse(future.cancel(false));

        try {
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            throw new RuntimeException();
        }
        assertFalse(future.isCancelled());
        assertTrue(future.isDone());

        pool.shutdown();
    }

    @Test
    public void futureCancelTest_when_thread_is_done() {
        ThreadPool pool = new ThreadPool(1);
        Future<Integer> future = pool.submit(new SleepingCallable());

        try {
            Thread.sleep(12000);
        } catch (InterruptedException e) {
            throw new RuntimeException();
        }

        assertFalse(future.cancel(true));
        assertFalse(future.isCancelled());
        assertTrue(future.isDone());

        pool.shutdown();
    }

    @Test
    public void futureCancelTest_when_task_still_in_queue() {
        ThreadPool pool = new ThreadPool(1);
        pool.submit(new SleepingCallable());
        pool.submit(new SleepingCallable());
        Future<Integer> future = pool.submit(new SleepingCallable());

        assertTrue(future.cancel(true));

        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            throw new RuntimeException();
        }

        assertTrue(future.isCancelled());
        assertTrue(future.isDone());

        pool.shutdown();
    }

    @Test
    public void futureCancelTest_when_task_throw_exception() throws InterruptedException {
        ThreadPool pool = new ThreadPool(1);
        Future<Boolean> future = pool.submit(new ExceptionThrowingCallable(null));

        Thread.sleep(1000);

        assertFalse(future.cancel(true));
        assertFalse(future.isCancelled());
        assertTrue(future.isDone());

        pool.shutdown();
    }

    @Test
    public void getTest_with_timeout_when_less_than_sleeping_time_then_throw_TimeoutException() throws InterruptedException {
        ThreadPool pool = new ThreadPool(1);
        Future<Integer> future = pool.submit(new SleepingCallable());
        try {
            future.get(5, TimeUnit.SECONDS);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            System.out.println("Timed out");
        }

        Thread.sleep(6000);
        pool.shutdown();
    }

    @Test
    public void getTest_with_timeout_when_more_than_sleeping_time_then_wait_for_result() throws InterruptedException {
        ThreadPool pool = new ThreadPool(1);
        Future<Integer> future = pool.submit(new SleepingCallable());
        try {
            assertEquals(0, future.get(15, TimeUnit.SECONDS));
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            System.out.println("Timed out");
        }

        pool.shutdown();
    }

    @Test
    public void getTest_without_timeout_then_wait_for_result() throws InterruptedException {
        ThreadPool pool = new ThreadPool(1);
        Future<Integer> future = pool.submit(new SleepingCallable());
        try {
            assertEquals(0, future.get());
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        pool.shutdown();
    }

    @Test
    public void testPauseAndResume()
            throws InterruptedException, ExecutionException {
        ThreadPool pool = new ThreadPool(3);
        int NUMBER_OF_INPUTS = 20;

        for (int i = 0; NUMBER_OF_INPUTS > i; ++i) {
            pool.submit(new BasicCallablePrint(i), Priority.MEDIUM);
        }

//        pool.resume();    test to see if it will file ( work well)
        pool.pause();
        System.out.println("main thrad send pause now  zzZZZzzZZZZ");
        Thread.sleep(2000);

        System.out.println("main thrad send resume now");
        pool.resume();

        Thread.sleep(3000);

//        pool.shutdown();
    }

    @Test
    public void testShutDown()
            throws InterruptedException, ExecutionException {
        ThreadPool pool = new ThreadPool(3);
        int NUMBER_OF_INPUTS = 10;
        List<Future<Integer>> futureListAfter = new ArrayList<>(5);

        List<Callable<Integer>> callableList = new ArrayList<>(NUMBER_OF_INPUTS);
        for (int i = 0; NUMBER_OF_INPUTS > i; ++i) {
            callableList.add(i, new ShutDownCall(i));
        }

        List<Future<Integer>> futureList = new ArrayList<>(NUMBER_OF_INPUTS);

        for (int i = 0; NUMBER_OF_INPUTS > i; ++i) {
            futureList.add(i, pool.submit(callableList.get(i), Priority.MEDIUM));
        }

        for (int i = 0; NUMBER_OF_INPUTS > i; ++i) {
            assertEquals(i, futureList.get(i).get());
        }

        Thread.sleep(1000);  // sleep for 1 sec to make sure all is done
        pool.shutdown();


        for (int i = 0; 5 > i; ++i) {
            try {
                futureListAfter.add(i, pool.submit(callableList.get(i), Priority.MEDIUM));
            }
            catch (IllegalArgumentException e)
            {
                System.out.println("u cant do this ");
            }

        }

    }


    @Test
    public void testAwaitTermination()
            throws InterruptedException, ExecutionException {
        ThreadPool pool = new ThreadPool(3);
        int NUMBER_OF_INPUTS = 9;
        List<Future<Integer>> futureListAfter = new ArrayList<>(5);

        List<Callable<Integer>> callableList = new ArrayList<>(NUMBER_OF_INPUTS + 1);
        for (int i = 0; NUMBER_OF_INPUTS > i; ++i) {
            callableList.add(i, new ShutDownCall(i));
        }
        callableList.add(9, new AwaitTermination(9));

        NUMBER_OF_INPUTS = 10;

        List<Future<Integer>> futureList = new ArrayList<>(NUMBER_OF_INPUTS);

        for (int i = 0; NUMBER_OF_INPUTS > i; ++i) {
            futureList.add(i, pool.submit(callableList.get(i), Priority.MEDIUM));
        }

        for (int i = 0; NUMBER_OF_INPUTS  - 1 > i; ++i) {   // test for 9 task without the lest
            assertEquals(i, futureList.get(i).get());
        }

//        pool.awaitTermination(); will make deadLock
        pool.shutdown();
        assertFalse(pool.awaitTermination(1 , TimeUnit.SECONDS));
//        System.out.println("after try 1 ");
        pool.awaitTermination();


//        Thread.sleep(4000); // sleep for 2 sec to make sure all is done
        assertEquals(9, futureList.get(9).get());

        for (int i = 0; 5 > i; ++i) {
            try {
                futureListAfter.add(i, pool.submit(callableList.get(i), Priority.MEDIUM));
            }
            catch (IllegalArgumentException e)
            {
                System.out.println("u cant do this ");
            }

        }


    }


    @Test
    public void shutdownAndAwaitTest_with_out_timeout() {
        ThreadPool threadPool = new ThreadPool(5);
        for (int i = 0; 30 > i; ++i) {
            threadPool.execute(new WaitingTask());
        }

        threadPool.shutdown();
        try {
            threadPool.awaitTermination();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void shutdownAndAwaitTest_with_timeout() {
        ThreadPool threadPool = new ThreadPool(5);
        for (int i = 0; 30 > i; ++i) {
            threadPool.execute(new WaitingTask());
        }

        threadPool.shutdown();
        boolean result1 = false;
        try {
            result1 = threadPool.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        boolean result2 = false;
        try {
            result2 = threadPool.awaitTermination(20, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        assertFalse(result1);
        assertTrue(result2);
    }


    @Test
    public void setNumOfThreadTest_when_num_is_smaller_than_size() {
        ThreadPool threadPool = new ThreadPool(5);
        for (int i = 0; 30 > i; ++i) {
            threadPool.execute(new WaitingTask());
        }

        threadPool.setNumOfThreads(3);

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        threadPool.shutdown();
        try {
            threadPool.awaitTermination();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void setNumOfThreadTest_when_num_is_bigger_than_size() {
        ThreadPool threadPool = new ThreadPool(3);
        for (int i = 0; 30 > i; ++i) {
            threadPool.execute(new WaitingTask());
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        threadPool.setNumOfThreads(5);

        threadPool.shutdown();
        try {
            threadPool.awaitTermination();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }





//
//    @Test
//    public void testSetNumOfThreads() throws InterruptedException, ExecutionException {
//        ThreadPool pool = new ThreadPool(3);
//        int NUMBER_OF_INPUTS = 20;
//        List<Future<Integer>> futureListAfter = new ArrayList<>(5);
//
//        List<Callable<Integer>> callableList = new ArrayList<>(NUMBER_OF_INPUTS + 1);
//        for (int i = 0; NUMBER_OF_INPUTS > i; ++i) {
//            callableList.add(i, new BasicCallablePrintNosleep(i));
//        }
//
//        List<Future<Integer>> futureList = new ArrayList<>(NUMBER_OF_INPUTS);
//
//        for (int i = 0; NUMBER_OF_INPUTS > i; ++i) {
//            futureList.add(i, pool.submit(callableList.get(i), Priority.MEDIUM));
//        }
//
//
//        for (int i = 0; NUMBER_OF_INPUTS > i; ++i) {   // test for 9 task without the lest
//            assertEquals(i, futureList.get(i).get());
//        }
//
//
////        pool.awaitTermination(); will make deadLock
//        pool.shutdown();
//
//        pool.awaitTermination();
//
//        System.out.println("sssssssssssssss");
//
//        futureList.clear();
//
//        pool.setNumOfThreads(10);
//
//
//
//        for (int i = 0; NUMBER_OF_INPUTS > i; ++i) {
//            futureList.add(i, pool.submit(callableList.get(i), Priority.MEDIUM));
//        }
//
//
//        for (int i = 0; NUMBER_OF_INPUTS  - 1 > i; ++i) {   // test for 9 task without the lest
//            assertEquals(i, futureList.get(i).get());
//        }
//
//
//
//    }


    /*------------------------------ End of Test ----------------------------------------------------*/

    private static class BasicRunnable implements Runnable {
        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + " says hi");
        }
    }

    private static class RunnableWithReturnValue implements Runnable {
        private final int returnValue;

        private RunnableWithReturnValue(int returnValue) {
            this.returnValue = returnValue;
        }
        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + " says hi with returnValue " + returnValue);
        }
    }

    private static class PowerCalculator implements Callable<Integer> {
        private final int num;

        private PowerCalculator(int num) {
            this.num = num;
        }

        @Override
        public Integer call() throws Exception {
            return num * num;
        }
    }

    private static class BasicCallablePrint implements Callable<Integer> {
        int num;

        BasicCallablePrint(int num){
            this.num = num;
        }

        @Override
        public Integer call() throws Exception {
            System.out.println(Thread.currentThread().getName() + " got " + num);
            Thread.sleep(1000);

            return num;
        }
    }

    private static class BasicCallablePrintNosleep implements Callable<Integer> {
        int num;

        BasicCallablePrintNosleep(int num){
            this.num = num;
        }

        @Override
        public Integer call() throws Exception {
            System.out.println(Thread.currentThread().getName() + " got " + num);

            return num;
        }
    }

    private static class ShutDownCall implements Callable<Integer> {
        int num;

        ShutDownCall(int num){
            this.num = num;
        }

        @Override
        public Integer call() throws Exception {

            return num;
        }
    }

    private static class AwaitTermination implements Callable<Integer> {
        int num;

        AwaitTermination(int num){
            this.num = num;
        }

        @Override
        public Integer call() throws Exception {

            Thread.sleep(3000);
//            System.out.println("lset one ");
            return num;
        }
    }

    private static class BasicCallable implements Callable<Integer> {

        @Override
        public Integer call() throws Exception {
            int num = new Random().nextInt(10);
            System.out.println(Thread.currentThread().getName() + " got " + num);

            return num;
        }
    }

    private static class SleepingCallable implements Callable<Integer> {
        private static final AtomicInteger ID = new AtomicInteger(0);

        @Override
        public Integer call() throws Exception {
            System.out.println("Sleepy thread number " + ID.incrementAndGet() +
                    " goes to sleep zzz");
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                System.out.println("Sleepy thread was interrupted :(");
                return 0;
            }

            System.out.println("Sleepy thread was not interrupted :)");

            return 0;
        }
    }

    private static class ExceptionThrowingCallable implements Callable<Boolean> {
        private final Object other;

        private ExceptionThrowingCallable(Object o) {
            other = o;
        }

        @Override
        public Boolean call() throws Exception {
            boolean result = false;
            try {
                result = other.equals(this);
            } catch (NullPointerException e) {
                System.out.println("null was sent from the task");
                throw e;
            }
            System.out.println("the result is: " + result);
            return result;
        }
    }

    private static class RandomNumDigitSummer implements Runnable {
        @Override
        public void run() {
            int num = Math.abs(new Random().nextInt());
            int sum = 0;

            System.out.print("For number: " + num + " the sum of digits is: ");
            while (num > 0) {
                sum += (num % 10);
                num /= 10;
            }
            System.out.println(sum);
        }
    }

    private static class WaitingTask implements Runnable {
        private static AtomicInteger taskId = new AtomicInteger(0);

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + " is waiting for 2 secs " +
                    taskId.incrementAndGet());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }


}
