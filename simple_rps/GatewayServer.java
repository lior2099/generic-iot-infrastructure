/*------------------------------------------------------------------------
Name: GatewayServer.java
Version : 1.00
Author: Lior shalom
Reviewer:
Date: 25/08/2024
------------------------------------------------------------------------*/

package il.co.ilrd.simple_rps;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import il.co.ilrd.http.SampleHttpServer;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static il.co.ilrd.util.Color.*;
import static il.co.ilrd.util.Color.RESET;

public class GatewayServer {
    private final ConnectionManager connectionManager;
    private final RPS<JSONObject> rps;

    public GatewayServer(Parser<JSONObject> parser) {
        connectionManager = new ConnectionManager(new onAccept(), new OnReadJson(), new Writer());
        rps = new RPS<>(parser);
    }

    public void start() {
        connectionManager.start();
    }

    public void stop() {
        connectionManager.stop();
    }

    private static void handleRequest(Message<String> request) {
        handleRequest(request);
    }

    public void addportTCP(int portNumber, String hostname) {
        connectionManager.addPort(portNumber, "TCP", hostname);
    }

    public void addportUDP(int portNumber, String hostname) {
        connectionManager.addPort(portNumber, "UDP", hostname);
    }

    public void addportHTTP(int portNumber, String hostname) {
        connectionManager.addPort(portNumber, "HTTP", hostname);
    }


    //**********************interface and method to send************************************

    private interface OnRead<T> {
        public void read(IConnection communicator, ByteBuffer buffer,
                         BiConsumer<IConnection, T> responder);
    }

    private interface IConnection {
        public void read(ByteBuffer buffer) throws IOException;

        public void write(ByteBuffer buffer) throws IOException;
    }

    private interface Handler {
        public void handle(SelectionKey key) throws IOException;
    }

    private static class JsonMessage implements Message<JSONObject> {
        private final IConnection communicator;
        private final JSONObject message;
        private final BiConsumer<IConnection, JSONObject> responder;

        private JsonMessage(IConnection communicator, JSONObject message, BiConsumer<IConnection, JSONObject> responder) {
            this.communicator = communicator;
            this.message = message;
            this.responder = responder;
        }

        @Override
        public JSONObject getMessage() {
            return message;
        }

        @Override
        public void sendResponse(String response) {
//            JSONObject jsonObject = new JSONObject("{ \"Massage\" : " + response + "}");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Massage", response);
            responder.accept(communicator, jsonObject);
        }
    }

    private static class onAccept implements Function<InetAddress, Boolean> {

        @Override
        public Boolean apply(InetAddress Address) {
            return true;
        }
    }

    private class OnReadJson implements OnRead<JSONObject> {

        @Override
        public void read(IConnection communicator, ByteBuffer buffer, BiConsumer<IConnection, JSONObject> responder) {
            try {
                communicator.read(buffer);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (NoRequestException e) {
                throw new IllegalArgumentException();
            }
//            String message = deserialization(buffer);
            String message = new String(buffer.array(), buffer.position(), buffer.limit());

            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(message);
            } catch (JSONException ignore) {
            }

            rps.handleMessage(new JsonMessage(communicator, jsonObject, responder));
        }
    }

    private static class Writer<T> implements BiConsumer<IConnection, JSONObject> {

        @Override
        public void accept(IConnection communicator, JSONObject responder) {
            try {
                communicator.write(ByteBuffer.wrap(responder.toString().getBytes()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }

    private static class ConnectionManager {
        private final Selector selector;
        private final HttpServer server;
        private final ByteBuffer buffer;
        private final static int BUFFER_SIZE = 1024;
        private Boolean isRunning = true;
        private Function<InetAddress, Boolean> onAccept;
        private OnRead<JSONObject> onRead;
        private BiConsumer<IConnection, JSONObject> writer;

        private ConnectionManager(Function<InetAddress, Boolean> onAccept,
                                  OnRead<JSONObject> onRead,
                                  BiConsumer<IConnection, JSONObject> writer) {

            this.onAccept = onAccept;
            this.onRead = onRead;
            this.writer = writer;

            try {
                selector = Selector.open();
                server = HttpServer.create();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            buffer = ByteBuffer.allocate(BUFFER_SIZE);

        }

        public void start() {
            server.start();

            try {
                System.out.println(GREEN_BOLD + "Server is up" + RESET);
                while (isRunning) {
                    selector.select();
                    Set<SelectionKey> selectionKeySet;

                    try {
                        selectionKeySet = selector.selectedKeys();
                    } catch (ClosedSelectorException e) {
                        return;
                    }
                    Iterator<SelectionKey> keyIterator = selectionKeySet.iterator();

                    while (keyIterator.hasNext()) {
                        SelectionKey key = keyIterator.next();
                        ((Handler) (key.attachment())).handle(key);
                        keyIterator.remove();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void stop() {
            isRunning = false;
            try {
                if (selector.isOpen()) {
                    selector.close();
                }
                server.stop(0);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        //**********************addPort implements************************************
        public void addPort(int port, String type, String hostname) {

            switch (type) {
                case "TCP":
                    addTcpPort(hostname, port);
                    break;
                case "UDP":
                    addUdpPort(hostname, port);
                    break;
                case "HTTP":
                    addHTTPPort(hostname, port);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid port type: " + type);
            }
        }

        //will add TCP_Port
        private void addTcpPort(String hostname, int port) {
            try {
                ServerSocketChannel tcpSocket = ServerSocketChannel.open();
                tcpSocket.bind(new InetSocketAddress(hostname, port));
                tcpSocket.configureBlocking(false);
                tcpSocket.register(selector, SelectionKey.OP_ACCEPT, new AcceptHandler());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            System.out.println(GREEN_BOLD_BRIGHT + "TCP port " + port + " added" + RESET);
        }

        //will add UDP_Port
        private void addUdpPort(String hostname, int port) {
            try {
                DatagramChannel udpChannel = DatagramChannel.open();
                udpChannel.bind(new InetSocketAddress(hostname, port));
                udpChannel.configureBlocking(false);
                udpChannel.register(selector, SelectionKey.OP_READ, new UDPHandler());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println(PURPLE_BOLD_BRIGHT + "UDP port " + port + " added" + RESET);
        }

        //will add HTTP_Port
        private void addHTTPPort(String hostname, int port) {

            try {
                server.bind(new InetSocketAddress(hostname, port), 0);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            server.createContext("/IOTs", new HTTPHandler());
        }


        //**********************Handler implements************************************
        private class AcceptHandler implements Handler {

            @Override
            public void handle(SelectionKey key) throws IOException {
                ServerSocketChannel serverSocket = (ServerSocketChannel) key.channel();
                SocketChannel client = serverSocket.accept();
                if (onAccept.apply(client.socket().getInetAddress()))
                    client.configureBlocking(false);
                client.register(selector, SelectionKey.OP_READ, new TCPHandler());
            }
        }

        private class UDPHandler implements Handler {

            @Override
            public void handle(SelectionKey key) throws IOException {

                UDPConnection udpCommunicator = new UDPConnection((DatagramChannel) key.channel());
                try {
                    onRead.read(udpCommunicator, ByteBuffer.allocate(BUFFER_SIZE), writer);
                } catch (RuntimeException ignore) {
                }
            }

        }

        private class TCPHandler implements Handler {

            @Override
            public void handle(SelectionKey key) throws IOException {

                TCPConnection tcpCommunicator = new TCPConnection((SocketChannel) key.channel());
                try {
                    onRead.read(tcpCommunicator, ByteBuffer.allocate(BUFFER_SIZE), writer);
                } catch (IllegalArgumentException ignore) {
                }
            }

        }

        private class HTTPHandler implements HttpHandler {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                JSONObject jsonBody = new JSONObject();

                if (!checkAccept(exchange)) {
                    sendHttp(exchange, jsonBody, "massage", "Invalid Accept method", 400);
                    return;
                }

                switch (exchange.getRequestMethod()) {
                    case "POST":
                        HTTPConnection httpConnection = new HTTPConnection(exchange);
                        onRead.read(httpConnection, buffer, writer);
                        break;
                    default:
                        sendHttp(exchange, jsonBody, "massage", "Invalid request method", 400);
                        break;
                }
            }
        }

        //**********************Communicator implements************************************
        private class UDPConnection implements IConnection {
            private DatagramChannel udpChannel;
            private SocketAddress address;

            public UDPConnection(DatagramChannel udpChannel) throws IOException {
                this.udpChannel = udpChannel;
            }

            @Override
            public void read(ByteBuffer buffer) throws IOException {
                buffer.clear();
                address = udpChannel.receive(buffer);
                if (address == null) {
                    throw new NoRequestException();
                }
                buffer.flip();
            }

            @Override
            public void write(ByteBuffer buffer) throws IOException {
                udpChannel.send(buffer, address);
                buffer.clear();
            }
        }

        private class TCPConnection implements IConnection {
            private SocketChannel tcpChannel;

            public TCPConnection(SocketChannel tcpChannel) throws IOException {
                this.tcpChannel = tcpChannel;
            }

            @Override
            public void read(ByteBuffer buffer) throws IOException {
                buffer.clear();
                int bytesRead = 0;

                bytesRead = tcpChannel.read(buffer);
                if (bytesRead == -1) {
                    tcpChannel.close();
                    throw new NoRequestException();
                } else {
                    while (bytesRead != 0 && bytesRead != -1) {
                        bytesRead = tcpChannel.read(buffer);
                    }
                }
                buffer.flip();
            }

            @Override
            public void write(ByteBuffer buffer) throws IOException {
                while (buffer.hasRemaining()) {
                    tcpChannel.write(buffer);
                }
                buffer.clear();
            }
        }

        private class HTTPConnection implements IConnection {
            private HttpExchange exchange;
            private JSONObject jsonBody;

            public HTTPConnection(HttpExchange exchange) {
                this.exchange = exchange;
                jsonBody = new JSONObject();
            }

            @Override
            public void read(ByteBuffer buffer) throws IOException {
                buffer.clear();
                InputStream inputStream = exchange.getRequestBody();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

                JSONTokener jsonTokener = new JSONTokener(reader);
                if (jsonTokener.more()) {
                    try {
                        jsonBody = (JSONObject) jsonTokener.nextValue();
                    } catch (JSONException e) {
                        sendHttp(exchange, jsonBody, "massage", "Invalid Accept method", 400);
                        reader.close();
                        return;
                    }
                }
                reader.close();

                buffer.put(jsonBody.toString().getBytes());
                buffer.flip();
            }

            @Override
            public void write(ByteBuffer buffer) throws IOException {

                jsonBody = new JSONObject(new String(buffer.array(), buffer.position(), buffer.limit()));
                int status = 400;

                if (jsonBody.getString("Massage").contains("success")) {
                   status = 200;
                }

                exchange.sendResponseHeaders(status, jsonBody.toString().getBytes().length);

                OutputStream os = exchange.getResponseBody();
                os.write(jsonBody.toString().getBytes());
                os.close();
                exchange.close();
                buffer.clear();
            }
        }

        private static boolean checkAccept(HttpExchange exchange) {
            boolean okType = false;
            boolean okAccept = false;

            if (exchange.getRequestHeaders().containsKey("Accept")) {
                for (String mime : exchange.getRequestHeaders().get("Accept")) {
                    if (mime.equals("application/json") || mime.equals("*/*")) {
                        okAccept = true;
                    }
                }
            }

            if (exchange.getRequestHeaders().containsKey("Content-Type")) {
                for (String mime : exchange.getRequestHeaders().get("Content-Type")) {
                    if (mime.equals("application/json") || mime.equals("text/plain")) {
                        okType = true;
                    }
                }
            }

            return (okAccept && okType);
        }

        private static void sendHttp(HttpExchange exchange, JSONObject jsonBody, String key, String body, int status) {
            jsonBody.put(key, body);
            try {
                exchange.sendResponseHeaders(status, jsonBody.toString().getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(jsonBody.toString().getBytes());
                os.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static class NoRequestException extends RuntimeException {

    }
}


