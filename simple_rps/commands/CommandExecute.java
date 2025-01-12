package il.co.ilrd.simple_rps.commands;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class CommandExecute {

    public void post(String uri, JSONObject data)  {
        try ( HttpClient client = HttpClient.newHttpClient()){
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(uri))
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .header("Access-Control-Allow-Origin", "*")
                    .POST(HttpRequest.BodyPublishers.ofString(data.toString()))
                    .build();

            HttpResponse<String> response = null;
            try {
                response = client.send(request, HttpResponse.BodyHandlers.ofString());
            } catch (IOException | InterruptedException e) {
                throw new CommandExecuteException("fail to send request :  " + e );
            }
        }
    }


    public static void main(String[] args) {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("companyName" ,"ColdCat");
        jsonObject.put("model" ,"F63");
        jsonObject.put("version" ,"1.05");

        CommandExecute execute = new CommandExecute();
        try {
            execute.post("http://10.1.0.21:8888/company" , jsonObject);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static class CommandExecuteException extends RuntimeException{

        public CommandExecuteException(Throwable cause){
          super(cause);
        }

        public CommandExecuteException(String message){
            super(message);
        }
    }
}
