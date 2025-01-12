package il.co.ilrd.simple_rps;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.function.BiConsumer;

public class Notusing {
//    private static class StringMessage implements Message<String> {
//        private final GatewayServer.IConnection communicator;
//        private final String message;
//        private final BiConsumer<GatewayServer.IConnection, String> responder;
//
//        private StringMessage(GatewayServer.IConnection communicator, String message, BiConsumer<GatewayServer.IConnection, String> responder) {
//            this.communicator = communicator;
//            this.message = message;
//            this.responder = responder;
//        }
//
//        @Override
//        public String getMessage() {
//            return message;
//        }
//
//        @Override
//        public void sendResponse(String response) {
//            responder.accept(communicator, response);
//        }
//    }     ////////////here
//private static String deserialization(ByteBuffer buffer) {
//
//    byte[] bytes = new byte[buffer.remaining()];
//    buffer.get(bytes);
//
//    try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
//         ObjectInputStream ois = new ObjectInputStream(bais)) {
//
//        return (String) ois.readObject();
//    } catch (IOException | ClassNotFoundException ignore) {
//        //throw new RuntimeException(e);
//        return null;
//    }
//}
//
//    private static ByteBuffer serialization(String message) {
//
//        ByteArrayOutputStream output = new ByteArrayOutputStream();
//        ObjectOutputStream out = null;
//
//        try {
//            out = new ObjectOutputStream(output);
//            out.writeObject(message);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        return ByteBuffer.wrap(output.toByteArray());
//    }
//

}
