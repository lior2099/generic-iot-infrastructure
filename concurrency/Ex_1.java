/*
 FileName: Concurrency ex_1.java
 Author: Lior Shalom
 Date: 13/08/24
 reviewer:
*/

package concurrency;

import static java.lang.Thread.sleep;

class TaskThread extends Thread {
    private volatile boolean running = true;

    @Override
    public void run() {
        while (running) {
            System.out.println("Task is running...");

            try {
                sleep(1000); // sleeping for 1 sec
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Task thread is stopping...");
    }

    public void StopMe() {
        running = false;
    }
}

class RunnableThread implements Runnable {
    private volatile boolean running = true;

    @Override
    public void run() {
        while (running) {
            System.out.println("Runnable is running...");

            try {
                sleep(2000); // sleeping for 2 sec
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Runnable thread is stopping...");
    }

    public void StopMe() {
        running = false;
    }

}

public class Ex_1 {

    public static void main(String[] args) throws InterruptedException {
        TaskThread taskThread = new TaskThread();
        RunnableThread runMe = new RunnableThread();
        Thread runMeThread = new Thread(runMe);

        taskThread.start();
        runMeThread.start();
        sleep(6000);
        taskThread.StopMe();
        runMe.StopMe();

        taskThread.join();
        runMeThread.join();

        System.out.println("Main thread has stopped.");
    }
}


