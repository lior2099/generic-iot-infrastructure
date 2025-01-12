/*
 FileName: TCPClinit.java
 Author: Lior Shalom
 Date: 10/09/24
 reviewer:
*/


package il.co.ilrd.chat;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import static il.co.ilrd.util.Color.*;

public class TCPClinit {
    private SocketChannel socket = null;
    private BufferedReader input = null;
    private static final int BUFFER_SIZE = 1024;
    ByteBuffer buffer;
    ByteBuffer bufferRead;
    Boolean stillUp = true;

    public TCPClinit(String addr, int port) throws IOException {
        socket = SocketChannel.open();
        socket.connect(new InetSocketAddress(addr, port));
        bufferRead = ByteBuffer.allocate(BUFFER_SIZE);
    }

    public void register(String data) throws IOException {

        Message message = new Message(Message.MethodType.REGISTER, data);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ObjectOutputStream out = null;

        out = new ObjectOutputStream(output);
        out.writeObject(message);
        buffer = ByteBuffer.wrap(output.toByteArray());
        out.flush();
        socket.write(buffer);
        buffer.clear();
        int bytesRead = socket.read(buffer);
        if (bytesRead == -1) {
            return;
        }
        buffer.flip();

        String serverResponse = new String(buffer.array(), 0, buffer.limit()).trim();
        System.out.println(GREEN_BOLD + serverResponse + RESET);

        new Thread(() -> {
            try {
                read();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();

    }

    public void unRegister() throws IOException {

        Message message = new Message(Message.MethodType.UNREGISTER,null);
        stillUp = false;

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ObjectOutputStream out = null;

        out = new ObjectOutputStream(output);
        out.writeObject(message);
        buffer = ByteBuffer.wrap(output.toByteArray());
        out.flush();
        socket.write(buffer);
        buffer.clear();
        read();
    }

    public void sendText(String data) throws IOException {

        Message message = new Message(Message.MethodType.SEND_MESSAGE, data);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ObjectOutputStream out = null;

        out = new ObjectOutputStream(output);
        out.writeObject(message);
        buffer = ByteBuffer.wrap(output.toByteArray());
        out.flush();
        socket.write(buffer);

    }

    private void read() throws IOException {

        while (stillUp){
            bufferRead.clear();
            int bytesRead = socket.read(bufferRead);
            if (bytesRead == -1) {
                return;
            }
            bufferRead.flip();

            String serverResponse = new String(bufferRead.array(), 0, bufferRead.limit()).trim();
            if(serverResponse.equals("bye")){
                return;
            }
            if (serverResponse.equals("REGISTER FAIL")){
                throw new RuntimeException("the user is all rdy on");
            }
            System.out.println( serverResponse );
        }
    }


    //run with port 5555 and ip localhost (loop ip)
    public static void main(String[] args) throws IOException {
        BufferedReader input = null;

        input = new BufferedReader(new InputStreamReader(System.in));// Reading from server

        String message = " ";

        System.out.print(CYAN_BOLD +  "add user name : " + RESET);
        message = input.readLine();

        TCPClinit clinit1 = new TCPClinit("10.10.1.93" , 5555);
        clinit1.register(message);

        while (!message.equals("bye")) {
            message = input.readLine();

            clinit1.sendText(message);
        }

        clinit1.unRegister();
    }

}

