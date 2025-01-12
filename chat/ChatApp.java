/*
 FileName: ChatApp.java
 Author: Lior Shalom
 Date: 10/09/24
 reviewer: Yarin
*/


package il.co.ilrd.chat;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.*;

import static il.co.ilrd.util.Color.*;
import static il.co.ilrd.util.Color.RESET;

public class ChatApp {
    private final ByteBuffer buffer;
    private final static int BUFFER_SIZE = 1024;
    private final ManagerConnections managerConnections;
    private final MessageHandlingManager handlingManager;

    public ChatApp(int port) {
        buffer = ByteBuffer.allocate(BUFFER_SIZE);
        managerConnections = new ManagerConnections(5555);
        handlingManager = new MessageHandlingManager();
    }

    public void stopServer() {
        managerConnections.stop();
    }

    public void startServer() {
        managerConnections.start();
    }

    private void passMaeeage(Communicator communicator) throws IOException {
        handlingManager.handleMessage(communicator);
    }

    private class ManagerConnections {

        private Selector selector;
        private Boolean isRunning = true;

        public ManagerConnections(int port) {
            try {
                selector = Selector.open();
                ServerSocketChannel tcpSocket = ServerSocketChannel.open();
                tcpSocket.bind(new InetSocketAddress(port));
                tcpSocket.configureBlocking(false);
                tcpSocket.register(selector, SelectionKey.OP_ACCEPT);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public void stop() {
            isRunning = false;
            try {
                selector.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public void start() {
            try {
                System.out.println(GREEN_BOLD + "Server is up" + RESET);
                while (isRunning) {
                    selector.select();
                    Set<SelectionKey> selectionKeySet;

                    try {
                        selectionKeySet = selector.selectedKeys();
                    }catch (ClosedSelectorException e){
                        return;
                    }

                    Iterator<SelectionKey> keyIterator = selectionKeySet.iterator();

                    while (keyIterator.hasNext()) {
                        SelectionKey key = keyIterator.next();

                        if (key.isAcceptable()) {
                            register(key);
                        } else if (key.isReadable()) {
                            passMaeeage((Communicator) key.attachment());
                        }
                        keyIterator.remove();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void register(SelectionKey key) throws IOException {
            ServerSocketChannel serverSocket = (ServerSocketChannel) key.channel();
            SocketChannel client = serverSocket.accept();
            client.configureBlocking(false);
            client.register(selector, SelectionKey.OP_READ, new Communicator(client));
        }
    }

    private class Communicator {
        private SocketChannel client;

        Communicator(SocketChannel channel) {
            this.client = channel;
        }

        public void write(String str, ByteBuffer buffer) {

            buffer.clear();
            buffer.put(str.getBytes());

            buffer.flip();
            try {
                while (buffer.hasRemaining()){
                    client.write(buffer);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public int read(ByteBuffer buffer) throws IOException {
            buffer.clear();
            int bytesRead = 0;

            bytesRead = client.read(buffer);

            if (bytesRead == -1) {
                client.close();
            } else {
                while (bytesRead != 0) {
                    bytesRead = client.read(buffer);
                }

                buffer.flip();
            }
            return bytesRead;
        }
    }

    private class MessageHandlingManager {
        private Map<Communicator, String> users;
        private Message message;
        private MessageParser messageParser;
        private ByteBuffer buffer;

        private MessageHandlingManager() {
            users = new HashMap<>();
            messageParser = new MessageParser();
            this.buffer = ByteBuffer.allocate(BUFFER_SIZE);
        }

        private void handleMessage(Communicator communicator) throws IOException {
            int bytesRead = communicator.read(buffer); //
            if (bytesRead == -1) {
                return;
            }
            message = messageParser.parse(buffer);
            Request.values()[message.getType().ordinal()].handle(message, communicator, this);
        }

        private class MessageParser {

            public Message parse(ByteBuffer buffer) {
                Objects.requireNonNull(buffer);

                // Convert ByteBuffer to byte array
                byte[] bytes = new byte[buffer.remaining()];
                buffer.get(bytes);

                // Deserialize byte array
                try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
                     ObjectInputStream ois = new ObjectInputStream(bais)) {
                    return (Message) ois.readObject();
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private enum Request {
        MESSAGE {
            @Override
            void handle(Message message, Communicator communicator, MessageHandlingManager manager) {
                String username = manager.users.get(communicator);
                String sendMessage = (username + ": " + message.getBody());

                sendToAll(sendMessage, communicator, manager);
            }
        },

        REGISTER {
            @Override
            void handle(Message message, Communicator communicator, MessageHandlingManager manager) {
                if (!UserNameOk(communicator, manager)) {
                    communicator.write("REGISTER fail userName all rdy register", manager.buffer);
                    return;
                }

                String oldName = null;
                oldName = manager.users.put(communicator, message.getBody());

                if (oldName == null) {
                    communicator.write("REGISTER OK", manager.buffer);

                    String sendMessage = ("the User : " + message.getBody() + " -  has enter the game");
                    sendToAll(sendMessage, communicator, manager);
                } else {
                    communicator.write("REGISTER NewName", manager.buffer);
                    String sendMessage = ("the User : " + message.getBody() + " -  has enter the game");
                    sendToAll(sendMessage, communicator, manager);
                }
            }
        },

        UNREGISTER {
            @Override
            void handle(Message message, Communicator communicator, MessageHandlingManager manager) {
                communicator.write("bye", manager.buffer);

                String username = manager.users.get(communicator);
                String sendMessage = ("the User : " + username + "- left the chat !!!!");

                sendToAll(sendMessage, communicator, manager);
                manager.users.remove(communicator);

                try {
                    communicator.client.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        abstract void handle(Message message, Communicator communicator, MessageHandlingManager manager);

        private static void sendToAll(String message, Communicator communicator, MessageHandlingManager manager) {

            for (Communicator clint : manager.users.keySet()) {
                if (!clint.equals(communicator)) {
                    clint.write(message, manager.buffer);
                }
            }
        }

        private static boolean UserNameOk(Communicator communicator, MessageHandlingManager manager) {

            String userName = manager.message.getBody();

            for (String name : manager.users.values()) {
                if (name.equals(userName)) {
                    return false;
                }
            }
            return true;
        }
    }
}
