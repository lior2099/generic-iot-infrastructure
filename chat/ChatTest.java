package il.co.ilrd.chat;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;

import static il.co.ilrd.util.Color.*;
import static il.co.ilrd.util.Color.RESET;

public class ChatTest {


    @Test
    public void openOneUser() throws InterruptedException, IOException {
        ChatApp chatApp = new ChatApp(5555);

        new Thread(chatApp::startServer).start();



        TCPClinit clinit1 = new TCPClinit("localhost" , 5555);

        clinit1.register("lior The King");
        clinit1.sendText("yaaaa i am here");
        clinit1.unRegister();

//        Thread.sleep(1000);
//        chatApp.stopServer();
    }

    @Test
    public void openTwoUser() throws InterruptedException, IOException {
        ChatApp chatApp = new ChatApp(5555);

        new Thread(chatApp::startServer).start();



        TCPClinit clinit1 = new TCPClinit("localhost" , 5555);
        TCPClinit clinit2 = new TCPClinit("localhost" , 5555);

        clinit1.register("lior The K");
        clinit2.register("Maya The Q");

        clinit1.sendText("hi maya");
        clinit2.sendText("hi the king");


        Thread.sleep(1000);

        clinit1.unRegister();
        clinit2.unRegister();

        Thread.sleep(1000);
        chatApp.stopServer();
    }

    @Test
    public void openOneUser10Text() throws InterruptedException, IOException {
        ChatApp chatApp = new ChatApp(5555);

        new Thread(chatApp::startServer).start();


        TCPClinit clinit1 = new TCPClinit("localhost" , 5555);
        TCPClinit clinit2 = new TCPClinit("localhost" , 5555);

        clinit1.register("lior The King");
        clinit2.register("Maya The Q");

        for (int i = 0 ; i < 5 ; i ++){
            Thread.sleep(500);
            clinit1.sendText("hi for the : " + i);
        }

        clinit2.sendText("Lior stopppppp!!!!!");


        Thread.sleep(500);
        clinit1.unRegister();
        clinit2.unRegister();
        Thread.sleep(1000);

        chatApp.stopServer();
    }

    @Test
    public void open3User10Text() throws InterruptedException, IOException {
        ChatApp chatApp = new ChatApp(5555);

        new Thread(chatApp::startServer).start();

        TCPClinit clinit1 = new TCPClinit("localhost" , 5555);
        TCPClinit clinit2 = new TCPClinit("localhost" , 5555);
        TCPClinit clinit3 = new TCPClinit("localhost" , 5555);
        TCPClinit clinit4 = new TCPClinit("localhost" , 5555);

        clinit1.register("lior The King");
        clinit2.register("Maya The Q");
        clinit3.register("Shalom The Tea");
        clinit4.register("Maya The Q");

        for (int i = 0 ; i < 5 ; i ++){
            Thread.sleep(500);
            clinit1.sendText("hi for the : " + i);
        }

        clinit2.sendText("Lior stopppppp!!!!!");


        Thread.sleep(500);
        clinit1.unRegister();
        Thread.sleep(500);
        clinit2.unRegister();
        Thread.sleep(500);
        clinit3.unRegister();
        Thread.sleep(3000);

        chatApp.stopServer();
    }

    @Test
    public void openfromTeam() throws InterruptedException, IOException {
        ChatApp chatApp = new ChatApp(5555);

        new Thread(chatApp::startServer).start();


        TCPClinit clinit1 = new TCPClinit("10.10.1.182" , 5005);
        TCPClinit clinit2 = new TCPClinit("10.10.1.182" , 5005);

        clinit1.register("shalom The King");
        clinit2.register("Maya The Q");

        for (int i = 0 ; i < 5 ; i ++){
            Thread.sleep(500);
            clinit1.sendText("hi for the : " + i);
        }

        clinit2.sendText("Lior stopppppp!!!!!");


        Thread.sleep(500);
        clinit1.unRegister();
        clinit2.unRegister();
        Thread.sleep(1000);

//        chatApp.stopServer();
    }

    @Test
    public void openYarin() throws InterruptedException, IOException {
        TCPClinit clinit1 = new TCPClinit("10.10.1.142" , 7000);
        TCPClinit clinit2 = new TCPClinit("10.10.1.142" , 7000);

        clinit1.register("shalom The King");
        clinit2.register("Maya The Q");

        for (int i = 0 ; i < 5 ; i ++){
            Thread.sleep(500);
            clinit1.sendText("hi for the : " + i);
        }

        clinit2.sendText("Lior stopppppp!!!!!");


        Thread.sleep(500);
        clinit1.unRegister();
        clinit2.unRegister();
        Thread.sleep(1000);

    }


    @Test
    public void Runforteam() throws InterruptedException, IOException {
        ChatApp chatApp = new ChatApp(5555);

        new Thread(chatApp::startServer).start();

        Thread.sleep(500000);

//        chatApp.stopServer();
    }

    @Test
    public void chaetYarin() throws InterruptedException, IOException {
        BufferedReader input = null;

        TCPClinit clinit1 = new TCPClinit("10.10.1.142" , 7000);
        clinit1.register("shalom");

        input = new BufferedReader(new InputStreamReader(System.in));// Reading from server

        String message = " ";

        while (!message.equals("bye")) {
            System.out.print(CYAN_BOLD +  "TCP Client : " + RESET);
            message = input.readLine();

           clinit1.sendText(message);

        }

        clinit1.unRegister();

    }



}
