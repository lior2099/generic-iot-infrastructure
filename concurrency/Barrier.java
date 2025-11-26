/*
 FileName: Concurrency Barrier.java
 Author: Lior Shalom
 Date: 14/08/24
 reviewer: yarin
*/

package concurrency;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Barrier {

    private static final Semaphore sem = new Semaphore(0);
    private static final AtomicInteger messageV = new AtomicInteger(0);
    private static volatile int message = 0;
    private static final Lock lock = new ReentrantLock();
    private static final Condition lockCond = lock.newCondition();
    private static final int NUM_OF_CONSUMER = 7;
    private static final int NUM_OF_RUN_TEST = 13;


    private static class ProducerTask implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < NUM_OF_RUN_TEST; i++) {
                ProducerRun(i);
            }
        }

        public void ProducerRun(int i) {
            try {
                sem.acquire(NUM_OF_CONSUMER);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            message = i;
//            System.out.println("Producer : " + message);
            messageV.getAndIncrement();
            lock.lock();
            lockCond.signalAll();
            lock.unlock();
        }
    }

    private static class ConsumerTask implements Runnable {
        @Override
        public void run() {
            int consumed = 0;
            for (int i = 0; i < NUM_OF_RUN_TEST; i++) {
                ConsumerRun(consumed);
            }
        }

        private void ConsumerRun(int consumed) {
            consumed = messageV.get();
            sem.release();
            lock.lock();

            while (consumed == messageV.get()) {
                try {
                    lockCond.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    lock.unlock();
                }
            }
            System.out.println(message);
        }
    }

    public static void main(String[] args) {

        List<Thread> listProCom = new LinkedList<>();
        listProCom.add(new Thread(new ProducerTask()));
        for (int i = 0; i < 7; i++) {
            listProCom.add(new Thread(new ConsumerTask()));
        }

        for (Thread elem : listProCom) {
            elem.start();
        }

        for (Thread elem : listProCom) {
            try {
                elem.join();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
