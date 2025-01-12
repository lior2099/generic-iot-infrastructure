/*
 FileName: Concurrency LinkedList_sync.java
 Author: Lior Shalom
 Date: 13/08/24
 reviewer:
*/

package il.co.ilrd.concurrency;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Semaphore;

class LinkedList_sync {
    public static List<Integer> link = new LinkedList<>();
    private static int counter = 0;
    private static Semaphore sem = new Semaphore(0);


    private static class syncProducerTask implements Runnable {
        @Override
        public void run() {
            synchronized (link) {
                link.add(counter);
                counter++;
            }
        }
    }

    private static class syncConsumerTask implements Runnable {
        @Override
        public void run() {
            boolean flagConsumIt = false;

            while (!flagConsumIt) {
                synchronized (link) {
                    if (!link.isEmpty()) {
                        System.out.println(link.remove(0));
                        flagConsumIt = true;
                    }
                }
            }
        }
    }

    private static class semProducerTask implements Runnable {
        @Override
        public void run() {
            synchronized (link) {
                link.add(counter);
                counter++;
                sem.release();
            }
        }
    }

    private static class semConsumerTask implements Runnable {
        @Override
        public void run() {
            try {
                sem.acquire();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            synchronized (link) {
                System.out.println(link.remove(0));
            }
        }
    }


    public static void main(String[] args) throws InterruptedException {
//        syncTest();
        semTest();

    }

    private static void syncTest() throws InterruptedException {

        List<Thread> listProCom = new LinkedList<>();
        for (int i = 0; i < 20; i++) {
            listProCom.add(new Thread(new syncProducerTask()));
            listProCom.add(new Thread(new syncConsumerTask()));
        }

        for (Thread elem : listProCom) {
            elem.start();
        }

        for (Thread elem : listProCom) {
            elem.join();
        }


    }

    private static void semTest() throws InterruptedException {

        List<Thread> listProCom = new LinkedList<>();
        for (int i = 0; i < 20; i++) {
            listProCom.add(new Thread(new semProducerTask()));
            listProCom.add(new Thread(new semConsumerTask()));
        }

        for (Thread elem : listProCom) {
            elem.start();
        }

        for (Thread elem : listProCom) {
            elem.join();
        }


    }

}
