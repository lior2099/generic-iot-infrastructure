/*
 FileName: AtomicPing_pong.java
 Author: Lior Shalom
 Date: 14/08/24
 reviewer: yarin
*/

package concurrency;

import java.util.concurrent.atomic.AtomicBoolean;

public class AtomicPing_pong {
    private static AtomicBoolean atomicFlag = new AtomicBoolean(true);


    static class AtomicProducerTask implements Runnable {
        @Override
        public void run() {
            while (true) {

                if (atomicFlag.get()) {
                    System.out.println("Ping");
                    atomicFlag.set(false);
                }
            }
        }
    }

    static class AtomicConsumerTask implements Runnable {
        @Override
        public void run() {
            while (true) {

                if (!atomicFlag.get()) {
                    System.out.println("Pong");
                    atomicFlag.set(true);
                }
            }
        }
    }

}
