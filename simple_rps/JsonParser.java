/*------------------------------------------------------------------------
Name: JsonParser.java
Author: Lior shalom
Reviewer:
Date: 06/10/2024
------------------------------------------------------------------------*/

package simple_rps;

import il.co.ilrd.pair.Pair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class JsonParser implements Parser<JSONObject>{

    @Override
    public Pair<String, Params> parse(JSONObject request) {

        Objects.requireNonNull(request);
        JsonParser.ParamsParser paramObj = new JsonParser.ParamsParser();

        if (request.has("command")){
            paramObj.addParam(request);
        } else {
            throw new ParesrExceptionRunTime("Command not in format ");
        }

        return new Pair<>(request.get("command").toString(), paramObj);

    }


    private static class ParamsParser implements Params {
        private final Collection<Object> list = new ArrayList<>();

        @Override
        public void addParam(Object param) { list.add(param);
        }

        @Override
        public Collection<Object> getParams() {
            return list;
        }
    }

    public static class ParesrExceptionRunTime extends RuntimeException {
        public ParesrExceptionRunTime(String message) {
            super(message);
        }
    }
}
