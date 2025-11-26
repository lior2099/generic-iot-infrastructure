/*
 FileName: mongoDB.java
 Author: Lior Shalom
 Date: 6/11/24
 reviewer:
*/

package mongoDB;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import org.bson.BsonDateTime;
import org.bson.Document;
import org.json.JSONObject;

import java.time.LocalDateTime;


public class MongoDB {
    private final String url;

    public MongoDB(String url){
        this.url = url;
    }


    public void RegisterCompany(String companyName) {

    }

    public void RegisterProduct(Product product) {
        try (MongoClient mongoClient = MongoClients.create(url)){
            MongoDatabase database = mongoClient.getDatabase(product.companyName);

            database.createCollection("_" + product.model + "_" + product.version);


        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public void RegisterIoTDevice(Product product , String SI , JSONObject userInfo) {
        try (MongoClient mongoClient = MongoClients.create(url)){
            MongoDatabase database = mongoClient.getDatabase(product.companyName);
            MongoCollection<Document> prodColl = database.getCollection("_" + product.model + "_" + product.version);

            Document addLine = new Document("_id", SI)
                    .append("CreatedAT", LocalDateTime.now())
                    .append("UserInfo", userInfo.toMap());

            prodColl.insertOne(addLine);

            database.createCollection("_"+ SI);

        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public void UpdateIoTDeviceStatus(Product product , String SI , JSONObject updateInfo) {
        try (MongoClient mongoClient = MongoClients.create(url)){
            MongoDatabase database = mongoClient.getDatabase(product.companyName);
            MongoCollection<Document> collection = database.getCollection("_" + SI);

            Document addLine = new Document()
                    .append("CreatedAT", LocalDateTime.now())
                    .append("UserInfo", updateInfo.toMap());

            collection.insertOne(addLine);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    protected static class Product {
        protected String companyName;
        protected String model;
        protected String version;

        protected Product(String companyName, String model, String version) {
            this.companyName = companyName;
            this.model = model;
            this.version = version;
        }

    }


}
