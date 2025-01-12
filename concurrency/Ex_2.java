/*
 FileName: Concurrency ex_2.java
 Author: Lior Shalom
 Date: 13/08/24
 reviewer:
*/

package il.co.ilrd.concurrency;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class Ex_2 {
    private static int counter = 0;
    static int NUM_OF_INCREMENTS = 10_000_000;
    private static int syncCounter = 0;
    private static int syncBlockCounter = 0;
    private static final Object lock = new Object();
    private static AtomicInteger atomicCounter = new AtomicInteger(0);
    private static int reentCounter = 0;
    private static Lock reenrlock = new ReentrantLock();

    static class CounterTask implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < NUM_OF_INCREMENTS; i++) {
                counter++;
            }
        }
    }

    static class SyncCounterTask implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < NUM_OF_INCREMENTS; i++) {
                syncCounterAdd();
            }
        }

        private static synchronized void syncCounterAdd() {
            syncCounter++;
        }
    }

    static class SyncBlockCounterTask implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < NUM_OF_INCREMENTS; i++) {
                synchronized (lock) {
                    syncBlockCounter++;
                }
            }
        }
    }

    static class AtomicTask implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < NUM_OF_INCREMENTS; i++) {
                atomicCounter.getAndIncrement();
            }
        }
    }

    static class ReentTask implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < NUM_OF_INCREMENTS; i++) {
                reenrlock.lock();
                reentCounter++;
                reenrlock.unlock();
            }
        }
    }


    public static void main(String[] args) {

        System.out.println("BasicTest will run now");

        try {
            BasicTest();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("\nSyncTest will run now");
        try {
            SyncTest();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("\nSyncBlockTest will run now");
        try {
            SyncBlockTest();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("\nAtomicTest will run now");
        try {
            AtomicTest();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("\nReentTest will run now");
        try {
            ReentTest();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void BasicTest() throws InterruptedException {
        long startTime = System.currentTimeMillis();

        Thread thread1 = new Thread(new CounterTask());
        Thread thread2 = new Thread(new CounterTask());

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;

        System.out.println("BasicTest : Final counter value: " + counter);
        System.out.println("BasicTest : Execution time: " + executionTime + " ms");
    }

    private static void SyncTest() throws InterruptedException {
        long startTime = System.currentTimeMillis();

        Thread thread1 = new Thread(new SyncCounterTask());
        Thread thread2 = new Thread(new SyncCounterTask());

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;

        System.out.println("SyncTest : Final counter value: " + syncCounter);
        System.out.println("SyncTest : Execution time: " + executionTime + " ms");
    }

    private static void SyncBlockTest() throws InterruptedException {
        long startTime = System.currentTimeMillis();

        Thread thread1 = new Thread(new SyncBlockCounterTask());
        Thread thread2 = new Thread(new SyncBlockCounterTask());

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;

        System.out.println("SyncTest : Final counter value: " + syncBlockCounter);
        System.out.println("SyncTest : Execution time: " + executionTime + " ms");
    }

    private static void AtomicTest() throws InterruptedException {
        long startTime = System.currentTimeMillis();

        Thread thread1 = new Thread(new AtomicTask());
        Thread thread2 = new Thread(new AtomicTask());

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;

        System.out.println("SyncTest : Final counter value: " + atomicCounter);
        System.out.println("SyncTest : Execution time: " + executionTime + " ms");
    }

    private static void ReentTest() throws InterruptedException {
        long startTime = System.currentTimeMillis();

        Thread thread1 = new Thread(new ReentTask());
        Thread thread2 = new Thread(new ReentTask());

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;

        System.out.println("SyncTest : Final counter value: " + reentCounter);
        System.out.println("SyncTest : Execution time: " + executionTime + " ms");
    }


}






