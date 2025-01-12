/*
 FileName: Concurrency ping_pong.java
 Author: Lior Shalom
 Date: 13/08/24
 reviewer:
*/

package il.co.ilrd.concurrency;

import java.util.concurrent.atomic.*;

import static java.lang.Thread.sleep;

import java.util.concurrent.*;

public class Ping_pong {

    public static void main(String[] args) throws InterruptedException {

//        AtomicTest();
        SemTest();

    }

    private static void AtomicTest() throws InterruptedException {

        Thread thread1 = new Thread(new AtomicPing_pong.AtomicProducerTask());
        Thread thread2 = new Thread(new AtomicPing_pong.AtomicConsumerTask());

        thread1.start();
        thread2.start();


        thread1.join();
        thread2.join();

    }

    private static void SemTest() throws InterruptedException {

        Thread thread1 = new Thread(new Sem_Ping_Pong.SemProducerTask());
        Thread thread2 = new Thread(new Sem_Ping_Pong.SemConsumerTask());

        thread1.start();
        thread2.start();


        thread1.join();
        thread2.join();

    }


}



