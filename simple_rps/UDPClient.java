/*
 FileName: UDPClient.java
 Author: Lior Shalom
 Date: 5/09/24
 reviewer:
*/

package il.co.ilrd.simple_rps;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SocketChannel;

import static il.co.ilrd.util.Color.*;


public class UDPClient {
    private DatagramChannel channel;
    private SocketAddress address;
    private final int BUFFER_SIZE = 1024;  // to ask what is your profession
    private byte[] buffer;

    public UDPClient(String addr, int port) {
        try {
            channel = DatagramChannel.open();
            address = new InetSocketAddress(addr, port);
            System.out.println("Connected to server");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

//            Thread getMessages = new Thread(new GetMessages());
        Thread sendMessage = new Thread(new SendMessages(channel, address));
//            getMessages.start();

        sendMessage.run();


    }


    private static class SendMessages implements Runnable {
        private DatagramChannel channel;
        private SocketAddress address;
        private final int BUFFER_SIZE = 1024;  // to ask what is your profession
        private byte[] buffer;
        private BufferedReader input = null;

        private SendMessages(DatagramChannel channel, SocketAddress address) {
            this.channel = channel;
            this.address = address;
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

//                ByteBuffer buff = serialization(message);
                ByteBuffer buff = ByteBuffer.allocate(BUFFER_SIZE);
                buff.put(message.getBytes());
                buff.flip();
                try {
                    channel.send(buff, address);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                new GetMessages(channel).run();

            }
        }
    }

    private static class GetMessages implements Runnable {
        private DatagramChannel channel;
        private SocketAddress address;
        private final int BUFFER_SIZE = 1024;  // to ask what is your profession
        private ByteBuffer buffer = null;

        private GetMessages(DatagramChannel channel) {
            this.channel = channel;
        }

        @Override
        public void run() {
            buffer = ByteBuffer.allocate(BUFFER_SIZE);

            try {
                channel.receive(buffer);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            buffer.flip();

            String massage = new String(buffer.array(),0 , buffer.limit());

            System.out.println(PURPLE_BOLD + "Server: " + massage + RESET);

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
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return ByteBuffer.wrap(output.toByteArray());
    }


    //run with port 6060 and ip 127.0.0.1 (loop ip)

    public static void main(String[] args) {
       UDPClient client = new UDPClient(args[0], Integer.parseInt(args[1]));
    }
}