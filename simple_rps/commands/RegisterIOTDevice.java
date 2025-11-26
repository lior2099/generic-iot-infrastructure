/*------------------------------------------------------------------------
Name: RegisterIOT.java
Author: Lior shalom
Reviewer:
Date: 25/08/2024
------------------------------------------------------------------------*/


package simple_rps.commands;

import il.co.ilrd.simple_rps.Params;
import org.json.JSONObject;

import java.util.Iterator;

import static il.co.ilrd.util.Color.*;
import static il.co.ilrd.util.Color.RESET;

public class RegisterIOTDevice implements Command {
    private final JSONObject sendJson = new JSONObject();
    public static final String commandName = "registerIOT";


    public RegisterIOTDevice(Params params) {
        Iterator<Object> run = params.getParams().iterator();
        JSONObject jsonObject = (JSONObject) run.next();
        sendJson.put("companyName" , jsonObject.getString("companyName"));
        sendJson.put("model" , jsonObject.getString("model"));
        sendJson.put("version" , jsonObject.getString("version"));
        sendJson.put("SI" , jsonObject.getString("SI"));
        sendJson.put("userInfo" , jsonObject.getJSONObject("userInfo"));

//        companyName = jsonObject.getString("company_name");
//        productName = jsonObject.getString("product_name");
//        iotSI = jsonObject.getString("SI");
    }

    @Override
    public void execute() {

        CommandExecute execute = new CommandExecute();
        execute.post("http://10.1.0.21:8888/device" , sendJson);

//
//        StringBuilder printMe = new StringBuilder();
//        printMe.append("Company name : ");
//        printMe.append(GREEN_BOLD);
//        printMe.append(companyName);
//        printMe.append(RESET);
//        printMe.append(" and Product name is : ");
//        printMe.append(BLUE_BOLD);
//        printMe.append(productName);
//        printMe.append(RESET);
//        printMe.append(" and Iot SI is : ");
//        printMe.append(RED_BOLD);
//        printMe.append(iotSI);
//        printMe.append(RESET);
//        printMe.append(" Was successfully Register");
//
//        System.out.println(printMe);
    }
}
