/*------------------------------------------------------------------------
Name: UpdateIOT.java
Author: Lior shalom
Reviewer: Maya
Date: 25/08/2024
------------------------------------------------------------------------*/


package il.co.ilrd.simple_rps.commands;

import il.co.ilrd.simple_rps.Params;
import org.json.JSONObject;

import java.util.Iterator;

import static il.co.ilrd.util.Color.*;
import static il.co.ilrd.util.Color.RESET;

public class UpdateIOTDeviceStatus implements Command {
    private final JSONObject sendJson = new JSONObject();
    public static final String commandName = "updateIOT";

    public UpdateIOTDeviceStatus(Params params) {
        Iterator<Object> run = params.getParams().iterator();
        JSONObject jsonObject = (JSONObject) run.next();
        sendJson.put("companyName" , jsonObject.getString("companyName"));
        sendJson.put("model" , jsonObject.getString("model"));
        sendJson.put("version" , jsonObject.getString("version"));
        sendJson.put("SI" , jsonObject.getString("SI"));
        sendJson.put("updateInfo" , jsonObject.getJSONObject("updateInfo"));

//        companyName = jsonObject.getString("company_name");
//        productName = jsonObject.getString("product_name");
//        iotSI =  jsonObject.getString("SI");
//        updata = jsonObject.getString("update");
    }

    @Override
    public void execute() {
        CommandExecute execute = new CommandExecute();
        execute.post("http://10.1.0.21:8888/update" , sendJson);

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
//        printMe.append(CYAN_BOLD);
//        printMe.append(iotSI);
//        printMe.append(RESET);
//        if(this.updata.equals("ON") ){
//            printMe.append(YELLOW_BOLD);
//            printMe.append(" Was UpData");
//        }
//        else {
//            printMe.append(RED_BOLD);
//            printMe.append(" Was Not UpData!!!");
//        }
//        printMe.append(RESET);
//        System.out.println(printMe);

    }
}
