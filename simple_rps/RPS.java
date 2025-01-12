/*------------------------------------------------------------------------
Name: Rps.java
Author: Lior shalom
Reviewer: Maya   / plug & play - ido
Date: 25/08/2024
------------------------------------------------------------------------*/
package il.co.ilrd.simple_rps;

import il.co.ilrd.directory_watcher.DirectoryWatcher;
import il.co.ilrd.dynamic_jar_loader.DynamicJarLoader;
import il.co.ilrd.factory.Factory;
import il.co.ilrd.observer.Callback;
import il.co.ilrd.pair.Pair;
import il.co.ilrd.simple_rps.commands.*;
import il.co.ilrd.thread_pool.ThreadPool;
import org.json.JSONException;

import java.lang.reflect.InvocationTargetException;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.util.IllegalFormatFlagsException;
import java.util.function.Consumer;
import java.util.function.Function;

public class RPS<T> {
    private final ThreadPool pool;
    private final Factory<String, Command, Params> factory;
    private final Parser<T> parser;
    private final PlugAndPlay plugAndPlay;

    public RPS(Parser<T> parser) {
        pool = new ThreadPool();
        factory = new Factory<>();
        this.parser = parser;
        initFactory();
        plugAndPlay = new PlugAndPlay();
    }

    private void initFactory() {
        factory.add("registerCompany", RegisterCompany::new);
        factory.add("registerProduct", RegisterProduct::new);
        factory.add("registerIOTDevice", RegisterIOTDevice::new);
        factory.add("updateIOTDeviceStatus", UpdateIOTDeviceStatus::new);
    }

    public void handleMessage(Message<T> message) {
        pool.execute(new RequestRun(message));
    }

    private class RequestRun implements Runnable {
        private Message<T> request;
        private Pair<String, Params> bufferRequest;

        private RequestRun(Message<T> request) {
            this.request = request;
        }

        @Override
        public void run() {
            if (null == request.getMessage()){
                request.sendResponse("bad request - format not in JSON ");
            }
            try {
                bufferRequest = parser.parse(request.getMessage());
            }catch (JsonParser.ParesrExceptionRunTime e){
                request.sendResponse("bad request - cant find command key ");
                return;
            }
            try {
                Command command = factory.create(bufferRequest.getKey(), bufferRequest.getValue());
                command.execute();
            } catch (NullPointerException e){
                request.sendResponse("Command unknown " + e.getMessage());
                return;
            } catch (JSONException d){
                request.sendResponse(" bad parameter " + d.getMessage());
                return;
            } catch (CommandExecute.CommandExecuteException f){
                request.sendResponse(" fail when connection to the server  " + f.getMessage());
            }
            request.sendResponse("success");
        }
    }

    public void waitForEnd() throws InterruptedException {
        pool.shutdown();
        pool.awaitTermination();
    }

    private class PlugAndPlay {
        private static final String DIR_PATH = "/home/lior/Desktop/git/lior.shalom/java/projects/src/il/co/ilrd/dynamic_jar_loader";
        private static final String INTERFACE_NAME = "il.co.ilrd.simple_rps.commands.Command";
        private static final String JAR_PATH = "/home/lior/Desktop/git/lior.shalom/java/projects/src/il/co/ilrd/dynamic_jar_loader/djl.jar";
        private static final String COMMAND_NAME_FILED = "commandName";

        private DirectoryWatcher directoryWatcher;
        private DynamicJarLoader djl;


        private PlugAndPlay() {
            initWatcher();
            directoryWatcher.start();
        }

        private void initWatcher() {
            Callback<WatchEvent<?>> callback = new Callback<>(new UpdateRoutine(), new EndRoutine());

            directoryWatcher = new DirectoryWatcher(DIR_PATH);
            directoryWatcher.subscribe(callback);
            djl = new DynamicJarLoader(INTERFACE_NAME, JAR_PATH);
        }

        private class UpdateRoutine implements Consumer<WatchEvent<?>> {
            private String key = null;

            @Override
            public void accept(WatchEvent<?> s) {

                if (s.kind().equals(StandardWatchEventKinds.ENTRY_CREATE) && s.context().toString().endsWith(".jar")) {

                    for (Class<?> clazz : djl.load()) {
                        key = getKeyFactory(clazz);
                        ClassFunction funcClazz = new ClassFunction(clazz);

                        factory.add(key, funcClazz);
                    }
                }
            }
        }

        private String getKeyFactory(Class<?> clazz) {

            Object field = null;

            try {
                field = clazz.getField(COMMAND_NAME_FILED).get(null);  // we know that it is String and it static so null
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            if (field instanceof String) {
                return (String) field;
            }
            return null;
        }

        private class ClassFunction implements Function<Params, Command> {
            private Class<?> clazz;

            private ClassFunction(Class<?> clazz) {
                this.clazz = clazz;
            }

            @Override
            public Command apply(Params params) {
                try {
                    return (Command) clazz.getConstructor(Params.class).newInstance(params);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                         NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        private class EndRoutine implements Consumer<WatchEvent<?>> {
            @Override
            public void accept(WatchEvent<?> s) {

                System.out.println("Bye");
            }
        }
    }
}
