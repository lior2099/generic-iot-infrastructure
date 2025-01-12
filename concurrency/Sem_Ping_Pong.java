/*
 FileName: Sem_Ping_Pong.java
 Author: Lior Shalom
 Date: 13/08/24
 reviewer:
*/


package il.co.ilrd.concurrency;

import java.util.concurrent.Semaphore;

public class Sem_Ping_Pong {
    private static Semaphore semPro = new Semaphore(1);
    private static Semaphore semCon = new Semaphore(0);


    static class SemProducerTask implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    semPro.acquire();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Ping");
                semCon.release();
            }
        }
    }

    static class SemConsumerTask implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    semCon.acquire();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Pong");
                semPro.release();

            }
        }
    }
}
