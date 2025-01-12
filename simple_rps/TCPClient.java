/*
 FileName: ILRD_Selector.java
 Author: Lior Shalom
 Date: 8/09/24
 reviewer:
*/

package il.co.ilrd.simple_rps;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import static il.co.ilrd.util.Color.*;

public class TCPClient {
    private SocketChannel channel = null;
    private BufferedReader input = null;
    private ByteBuffer buffer = null;
    private static final int BUFFER_SIZE = 1024;

    public TCPClient(String addr, int port) {
        try {
            channel = SocketChannel.open();
            channel.connect(new InetSocketAddress(addr, port));
            System.out.println("Connected to server");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Thread getMessages = new Thread( new GetMessages(channel));
        Thread sendMessage = new Thread( new SendMessages(channel));
        getMessages.start();

        sendMessage.run();

    }

    private static class SendMessages implements Runnable {
        private SocketChannel channel = null;
        private BufferedReader input = null;


        private SendMessages(SocketChannel channel) {
            this.channel = channel;
        }

        @Override
        public void run() {
            String message = " ";

            while (!message.equals("bye")) {
                input = new BufferedReader(new InputStreamReader(System.in));// Reading from ttyr

                try {
                    message = input.readLine();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
//                buffer = serialization(message);
                ByteBuffer buff = ByteBuffer.allocate(BUFFER_SIZE);
                buff.put(message.getBytes());
                buff.flip();
                try {
                    channel.write(buff);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            try {
                channel.close();
                input.close();
                System.out.println(RED_BOLD + "Bye bye server, it is my TCP thx for all the work" + RESET);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static class GetMessages implements Runnable {
        private SocketChannel channel = null;
        private ByteBuffer buffer = null;
        private static final int BUFFER_SIZE = 1024;

        private GetMessages(SocketChannel channel) {
            this.channel = channel;
        }

        @Override
        public void run() {

            String messageGot = "on ";

            while (!messageGot.equals("")) {

                buffer = ByteBuffer.allocate(BUFFER_SIZE);

                try {
                    channel.read(buffer);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                buffer.flip();

//                messageGot = deserialization(buffer);
                messageGot = new String(buffer.array(),buffer.position() , buffer.limit());

                System.out.println(PURPLE_BOLD + "Server: " + messageGot + RESET);

            }
            try {
                channel.close();
                System.out.println(RED_BOLD + "Bye bye server, it is my TCP thx for all the work" + RESET);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static String deserialization(ByteBuffer buffer) {
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);

        try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
             ObjectInputStream ois = new ObjectInputStream(bais)) {
            return (String) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static ByteBuffer serialization(String message) {

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ObjectOutputStream out = null;

        try {
            out = new ObjectOutputStream(output);
            out.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return ByteBuffer.wrap(output.toByteArray());
    }

    //run with port 5555 and ip localhost (loop ip)
    public static void main(String[] args) {
        TCPClient client = new TCPClient(args[0], Integer.parseInt(args[1]));
    }
}
