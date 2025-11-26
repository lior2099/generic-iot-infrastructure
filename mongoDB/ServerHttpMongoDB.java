/*
 FileName: ServerHttpMongoDB.java
 Author: Lior Shalom
 Date: 07/11/24
 reviewer:Haiam
*/


package mongoDB;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;


public class ServerHttpMongoDB {
    private static final String URI = "mongodb://localhost:27017";
    private static JSONObject jsonResponse;
    private static MongoDB mongoDB;

    public static void main(String[] args) throws IOException {
        mongoDB = new MongoDB(URI);

        System.out.println("Server is running on port 8888 with ip : 10.1.0.21");
        HttpServer server = HttpServer.create(new InetSocketAddress("10.1.0.21", 8888), 0);

        server.createContext("/company", new HandlerCompany());
        server.createContext("/product", new HandlerProduct());
        server.createContext("/device", new HandlerIotDevice());
        server.createContext("/update", new HandlerUpdateStatus());

        server.setExecutor(null);
        server.start();
    }

    private static class HandlerCompany implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {

            Headers headers = Init(exchange);
            if (null == headers) {
                return;
            }

            if ("POST".equals(exchange.getRequestMethod())) {
                jsonResponse = new JSONObject();
                jsonResponse.put("message", "Register product was Done");

            } else {
                jsonResponse.put("message", "This is a default JSON response!");
            }

            exchange.sendResponseHeaders(200, jsonResponse.toString().length());
            OutputStream os = exchange.getResponseBody();
            os.write(jsonResponse.toString().getBytes());
            System.out.println(" ");
            os.close();
            exchange.close();
        }
    }

    private static class HandlerProduct implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {

            Headers headers = Init(exchange);
            if (null == headers) {
                return;
            }

            if ("POST".equals(exchange.getRequestMethod())) {
                jsonResponse = getJson(exchange);
                MongoDB.Product product = getProduct(exchange, jsonResponse);
                mongoDB.RegisterProduct(product);
                jsonResponse = new JSONObject();
                jsonResponse.put("message", "Register product was Done");

            } else {
                jsonResponse.put("message", "This is a default JSON response!");
            }

            exchange.sendResponseHeaders(200, jsonResponse.toString().length());
            OutputStream os = exchange.getResponseBody();
            os.write(jsonResponse.toString().getBytes());
            System.out.println(" ");
            os.close();
            exchange.close();

        }
    }

    private static class HandlerIotDevice implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            Headers headers = Init(exchange);
            if (null == headers) {
                return;
            }

            if ("POST".equals(exchange.getRequestMethod())) {
                jsonResponse = getJson(exchange);
                MongoDB.Product product = getProduct(exchange, jsonResponse);
                try {
                    mongoDB.RegisterIoTDevice(product, getIotInfo(exchange, jsonResponse), getUserInfo(exchange, jsonResponse));
                } catch (RuntimeException e) {
                    sendBadMessage(exchange, "SI all rdy Register", 401);
                }
                jsonResponse = new JSONObject();
                jsonResponse.put("message", "Register Device was Done");

            } else {
                jsonResponse.put("message", "This is a default JSON response!");
            }

            exchange.sendResponseHeaders(200, jsonResponse.toString().length());
            OutputStream os = exchange.getResponseBody();
            os.write(jsonResponse.toString().getBytes());
            System.out.println(" ");
            os.close();
            exchange.close();
        }
    }

    private static class HandlerUpdateStatus implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            Headers headers = Init(exchange);
            if (null == headers) {
                return;
            }

            if ("POST".equals(exchange.getRequestMethod())) {
                jsonResponse = getJson(exchange);
                MongoDB.Product product = getProduct(exchange, jsonResponse);
                mongoDB.UpdateIoTDeviceStatus(product, getIotInfo(exchange, jsonResponse), getUpdata(exchange, jsonResponse));
                jsonResponse = new JSONObject();
                jsonResponse.put("message", "Register Device was Done");

            } else {
                jsonResponse.put("message", "This is a default JSON response!");
            }

            exchange.sendResponseHeaders(200, jsonResponse.toString().length());
            OutputStream os = exchange.getResponseBody();
            os.write(jsonResponse.toString().getBytes());
            System.out.println(" ");
            os.close();
            exchange.close();
        }
    }

    private static Headers Init(HttpExchange exchange) throws IOException {
        Headers headers = exchange.getResponseHeaders();

        headers.add("Content-Type", "application/json");
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        headers.add("Access-Control-Allow-Headers", "Content-Type, Accept");

        if (!checkAccept(exchange)) {
            sendBadMessage(exchange, "Invalid request method", 400);
            return null;
        }

        jsonResponse = new JSONObject();

        return headers;
    }

    private static void sendBadMessage(HttpExchange exchange, String message, int status) throws IOException {
        jsonResponse = new JSONObject();
        jsonResponse.put("message", message);
        exchange.sendResponseHeaders(status, jsonResponse.toString().getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(jsonResponse.toString().getBytes());
        os.close();
    }

    private static JSONObject getJson(HttpExchange exchange) throws IOException {
        try (InputStream inputStream = exchange.getRequestBody()) {
            String stringBody = new String(inputStream.readAllBytes());
            try {
                jsonResponse = new JSONObject(stringBody);
            } catch (JSONException e) {
                sendBadMessage(exchange, "Bad JSON  -  " + e, 403);
            }
        }

        return jsonResponse;
    }

    private static MongoDB.Product getProduct(HttpExchange exchange, JSONObject data) throws IOException {
        String campyName = (String) data.remove("companyName");
        String model = (String) data.remove("model");
        String version = (String) data.remove("version");

        if (null == campyName || null == model || null == version) {
            sendBadMessage(exchange, "missing product info", 405);
        }

        MongoDB.Product product = new MongoDB.Product(campyName, model, version);
        return product;
    }

    private static String getIotInfo(HttpExchange exchange, JSONObject data) throws IOException {
        String SI = (String) data.remove("SI");

        if (null == SI) {
            sendBadMessage(exchange, "missing Device info", 407);
        }

        return SI;
    }

    private static JSONObject getUserInfo(HttpExchange exchange, JSONObject data) throws IOException {

        JSONObject userInfo = new JSONObject();

        try {
            userInfo.put("UserInfo", jsonResponse.get("userInfo").toString());
        } catch (JSONException ignore) {
            userInfo.put("UserInfo", " ");
        }

        return userInfo;
    }

    private static JSONObject getUpdata(HttpExchange exchange, JSONObject data) throws IOException {

        JSONObject updateInfo = new JSONObject();

        try {
            updateInfo.put("UpdateInfo", jsonResponse.get("updateInfo").toString());
        } catch (JSONException ignore) {
            updateInfo.put("UpdateInfo", " ");
        }

        return updateInfo;
    }

    private static boolean checkAccept(HttpExchange exchange) {
        boolean okType = false;

        if (exchange.getRequestHeaders().containsKey("Accept")) {
            for (String mime : exchange.getRequestHeaders().get("Accept")) {
                if (mime.equals("application/json") || mime.equals("*/*")) {
                    okType = true;
                }
            }
        }
        return okType;
    }

}



