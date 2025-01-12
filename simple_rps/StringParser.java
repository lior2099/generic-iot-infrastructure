/*------------------------------------------------------------------------
Name: SampleParser.java
Author: Lior shalom
Reviewer:Maya
Date: 25/08/2024
------------------------------------------------------------------------*/

package il.co.ilrd.simple_rps;

import il.co.ilrd.pair.Pair;

import java.util.*;
import java.util.function.Function;

public class StringParser implements Parser<String> {
    private static final String COMMAND_REGEX = "@";
    private static final String PARAMETER_REGEX = "#";

    private Map<String, Function<String, Params>> parseCommands;

    public StringParser(){
        parseCommands = new HashMap<>();
        initMap();
    }

    @Override
    public Pair<String, Params> parse(String request) {
        Params params = null;
        String[] bufferRequest;

        Objects.requireNonNull(request);

        bufferRequest = request.split(COMMAND_REGEX);
        String command = bufferRequest[0];

        Function<String, Params> parserByCommand = parseCommands.get(command);

        if (parserByCommand != null) {
            params = parserByCommand.apply(bufferRequest[1]);
        } else {
            throw new ParesrExceptionRunTime("Command not in format ");
        }
        return new Pair<>(command, params);
    }

    private void initMap() {
        parseCommands.put("registerCompany", new RegisterCompany());
        parseCommands.put("registerProduct", new RegisterProduct());
        parseCommands.put("registerIOT", new RegisterIOT());
        parseCommands.put("updateIOT", new UpdateIOT());
    }

    private static class RegisterCompany implements Function<String, Params> {

        @Override
        public Params apply(String request) {
            Objects.requireNonNull(request);

            StringParser.ParamsParser paramObj = new ParamsParser();
            paramObj.addParam(parseCompanyName(request));
            return paramObj;
        }
    }

    private static class RegisterProduct implements Function<String, Params> {
        private static final int NUM_OF_ARGS = 2;

        @Override
        public Params apply(String request) {
            Objects.requireNonNull(request);

            String[] bufferParams = request.split(PARAMETER_REGEX);
            if (NUM_OF_ARGS != bufferParams.length) {
                throw new ParesrExceptionRunTime("invalid argument send");
            }
            StringParser.ParamsParser paramObj = new ParamsParser();

            paramObj.addParam(parseCompanyName(bufferParams[0]));
            paramObj.addParam(parseProductName(bufferParams[1]));
            return paramObj;
        }
    }

    private static class RegisterIOT implements Function<String, Params> {
        private static final int NUM_OF_ARGS = 3;

        @Override
        public Params apply(String request) {

            if (request == null ){
                throw new ParesrExceptionRunTime("Bad argument !!!");
            }

            String[] bufferParams = request.split(PARAMETER_REGEX);

            if (NUM_OF_ARGS != bufferParams.length) {
                throw new ParesrExceptionRunTime("invalid argument send");
            }
            StringParser.ParamsParser paramObj = new ParamsParser();
            paramObj.addParam(parseCompanyName(bufferParams[0]));
            paramObj.addParam(parseProductName(bufferParams[1]));
            int iot = Integer.parseInt(parseIOT(bufferParams[2]));

            paramObj.addParam(iot);
            return paramObj;

        }
    }

    private static class UpdateIOT implements Function<String, Params> {
        private static final int NUM_OF_ARGS = 4;

        @Override
        public Params apply(String request) {
            String[] bufferParams = request.split(PARAMETER_REGEX);

            if (NUM_OF_ARGS != bufferParams.length) {
                throw new ParesrExceptionRunTime("invalid argument send");
            }

            StringParser.ParamsParser paramObj = new ParamsParser();
            paramObj.addParam(parseCompanyName(bufferParams[0]));
            paramObj.addParam(parseProductName(bufferParams[1]));
            int iot = Integer.parseInt(parseIOT(bufferParams[2]));

            paramObj.addParam(iot);
            paramObj.addParam(parseUpdate(bufferParams[3]));
            return paramObj;
        }
    }

    private static class ParamsParser implements Params {
        private final Collection<Object> list = new ArrayList<>();

        @Override
        public void addParam(Object param) {
            list.add(param);
        }

        @Override
        public Collection<Object> getParams() {
            return list;
        }
    }

    private static String checkAndGet(String request, String namePrefix) {
        String returnParam = null;
        final int prefixLength = namePrefix.length();

        if (request.regionMatches(0, namePrefix, 0, prefixLength)) {
            returnParam = request.substring(prefixLength);
        } else {
            throw new ParesrExceptionRunTime("invalid argument send");
        }
        return returnParam;
    }

    private static String parseCompanyName(String request) {
        final String prefix = "company_name=";
        return checkAndGet(request, prefix);
    }

    private static String parseProductName(String request) {
        final String prefix = "product_name=";
        return checkAndGet(request, prefix);
    }

    private static String parseIOT(String request) {
        final String prefix = "SI=";
        return checkAndGet(request, prefix);
    }

    private static String parseUpdate(String request) {
        final String prefix = "update=";
        return checkAndGet(request, prefix);
    }

    public static class ParesrExceptionRunTime extends RuntimeException {
        public ParesrExceptionRunTime(String message) {
            super(message);
        }
    }


}



