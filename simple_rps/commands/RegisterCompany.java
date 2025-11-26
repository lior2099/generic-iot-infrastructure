/*------------------------------------------------------------------------
Name: RegisterCompany.java
Author: Lior shalom
Reviewer:
Date: 25/08/2024
------------------------------------------------------------------------*/

package simple_rps.commands;

import il.co.ilrd.simple_rps.Params;
import org.json.JSONObject;

import java.util.IllegalFormatFlagsException;
import java.util.Iterator;

import static il.co.ilrd.util.Color.*;

public class RegisterCompany implements Command{
    private final JSONObject sendJson = new JSONObject();
    public static final String commandName = "registerCompany";

    public RegisterCompany(Params params){
        Iterator<Object> run = params.getParams().iterator();
        //    private final String companyName;
        JSONObject jsonObject = (JSONObject) run.next();
        sendJson.put("companyName" , jsonObject.getString("companyName"));
//        companyName = jsonObject.getString("company_name");
    }

    @Override
    public void execute() {
        CommandExecute execute = new CommandExecute();
        execute.post("http://10.1.0.21:8888/company" , sendJson);
//        System.out.println("Company name : " +GREEN_BOLD + companyName + RESET + " Was successfully Register" );
    }

}
