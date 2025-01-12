/*------------------------------------------------------------------------
Name: TestRps.java
Author: Lior shalom
Reviewer: Maya
Date: 25/08/2024
------------------------------------------------------------------------*/


package il.co.ilrd.simple_rps;

import org.json.JSONObject;
import org.junit.Test;

public class TestRps {
    @Test
    public void simpleRpsTest() throws InterruptedException {

        GatewayServer server = new GatewayServer(new JsonParser());

        server.addportTCP(5050  , "10.10.1.93");
        server.addportUDP(6060  , "10.10.1.93");
        server.addportHTTP(7070, "10.10.1.93");

        server.start();

//        Thread.sleep(10000);


//        String companyName = "Lior_ltd";
//        Rps newRps = new Rps(new SampleParser<String>());

//        server.handleRequest("registerCompany@company_name=lol");
//         { "command" : "registerCompany" , "company_name" : "lol" }
//        server.handleRequest("registerProduct@company_name=lol#product_name=RobotCat");
//        { "command" : "registerProduct" , "company_name" : "lol" , "product_name" : "RobotCat"}
//        server.handleRequest("registerIOTDevice@company_name=Pizza_Lover#product_name=CookieMaker#SI=666");
//        { "command" : "registerIOTDevice" , "company_name" : "lol" , "product_name" : "RobotCat", "SI" : "666" }
//        server.handleRequest("updateIOTDeviceStatus@company_name=Cat_Lover#product_name=RobotCat#SI=6#update=ON");
//
//        { "command" : "registerIOTDevice" , "company_name" : "lol" , "product_name" : "RobotCat", "SI" : "666", "update" : "ON" }

//        Thread.sleep(500);
//        System.out.println("add class now");
//        Thread.sleep(10000);
//
//
//        server.handleRequest("updateIOTDeviceStatus@company_name=lior_New_Lover#product_name=RobotCat#SI=6#update=ON");
//        server.handleRequest("updateIOTDeviceStatus@company_name=Cat_Lover#product_name=RobotCat#SI=6#update=OFF");


//        Thread.sleep(10000);

    }
}
