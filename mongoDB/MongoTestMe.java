package mongoDB;

import com.mongodb.client.*;
import pair.Pair;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.bson.Document;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MongoTestMe {

    @Test
    public void RegisterProductTest() {
        final String URI = "mongodb://localhost:27017";

        MongoDB mongoDB = new MongoDB(URI);
        MongoDB.Product product = new MongoDB.Product("ColdCat" , "X772" , "1.0");

        try {
            mongoDB.RegisterProduct(product);
        } catch (Exception e) {
            System.out.println("Product is all rdy register");
        }


    }

    @Test
    public void RegisterIoTDeviceTest() {
        final String URI = "mongodb://localhost:27017";

        MongoDB mongoDB = new MongoDB(URI);
        MongoDB.Product product = new MongoDB.Product("ColdCat" , "X772" , "1.0");
        JSONObject userInfo = new JSONObject();
        userInfo.put("userName" , "lior" );
        userInfo.put("gmail" , "lior@gmail.com" );

        try {
            mongoDB.RegisterIoTDevice(product, "154225456" , userInfo);
        } catch (Exception e) {
            System.out.println("Product is all rdy register");
        }

    }

    @Test
    public void UpdateIoTDeviceStatus() {
        final String URI = "mongodb://localhost:27017";

        MongoDB mongoDB = new MongoDB(URI);
        MongoDB.Product product = new MongoDB.Product("ColdCat" , "X772" , "1.0");
        JSONObject updateInfo = new JSONObject();
        updateInfo.put("temp" , "37_C" );
        updateInfo.put("storage" , "65%" );

        mongoDB.UpdateIoTDeviceStatus(product, "154225456" , updateInfo);

    }

    @Test
    public void GetInfo() {
        final String URI = "mongodb://localhost:27017";

        MongoClient mongoClient = MongoClients.create(URI);
        MongoDatabase database = mongoClient.getDatabase("ColdCat");
        MongoCollection<Document> collection = database.getCollection("_154225456");

        FindIterable<Document> cursor = collection.find();

        for (Document doc : cursor){
            for (Object obj : doc.entrySet()){
                System.out.println(obj);
            }
        }


    }






}
