package il.co.ilrd.directory_watcher;

import il.co.ilrd.observer.Callback;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.WatchEvent;
import java.util.function.Consumer;

import static java.lang.Thread.sleep;

public class TestDirWatcher {

    @Test
    public void testDir() throws IOException, InterruptedException {
        DirectoryWatcher directoryWatcher;
        Consumer<WatchEvent<?>> routine = new StringUpdateRoutine();
        Consumer<WatchEvent<?>> endroutine = new EndRoutine();
        Callback<WatchEvent<?>> callback = new Callback<>(routine, endroutine);
        directoryWatcher = new DirectoryWatcher("/home/lior/Desktop/git/lior.shalom/java/projects/src/il/co/ilrd/directory_watcher");

        directoryWatcher.subscribe(callback);
        directoryWatcher.start();
        System.out.println("u have 10 sec to test this, edit a file | make new one | remove, to see");
        sleep(10000);
        directoryWatcher.stop();

    }

    @Test
    public void testDirStop() throws InterruptedException {
        DirectoryWatcher directoryWatcher;
        Consumer<WatchEvent<?>> routine = new StringUpdateRoutine();
        Consumer<WatchEvent<?>> endroutine = new EndRoutine();
        Callback<WatchEvent<?>> callback = new Callback<>(routine, endroutine);
        directoryWatcher = new DirectoryWatcher("/home/lior/Desktop/git/lior.shalom/java/projects/src/il/co/ilrd/directory_watcher");

        directoryWatcher.subscribe(callback);
        directoryWatcher.start();

        sleep(1000);
        directoryWatcher.unsubscribe(callback);
        directoryWatcher.stop();

    }

    @Test
    public void testPath() throws InterruptedException {
        DirectoryWatcher directoryWatcher;
        DirectoryWatcher directoryWatcherfile;
        DirectoryWatcher directoryWatchernull;
        Consumer<WatchEvent<?>> routine = new StringUpdateRoutine();
        Consumer<WatchEvent<?>> endroutine = new EndRoutine();
        Callback<WatchEvent<?>> callback = new Callback<>(routine, endroutine);
        directoryWatcher = new DirectoryWatcher("/home/lior/Desktop/git/lior.shalom/java/projects/src/il/co/ilrd/directory_watcher");
//        directoryWatchernull = new DirectoryWatcher(null);
//        directoryWatcherfile = new DirectoryWatcher("/home/lior/Desktop/git/lior.shalom/java/projects/src/il/co/ilrd/directory_watcher/file.txt");


        directoryWatcher.subscribe(callback);
//        directoryWatchernull.subscribe(callback);
//        directoryWatcherfile.subscribe(callback);

        directoryWatcher.start();
//        directoryWatchernull.start();
//        directoryWatcherfile.start();

        sleep(1000);
        directoryWatcher.unsubscribe(callback);
        directoryWatcher.stop();

    }

    private static class StringUpdateRoutine implements Consumer<WatchEvent<?>> {
        private String update;
        @Override
        public void accept(WatchEvent<?> s) {
            update = "now";
            System.out.println(s);
        }

        private String getUpdate() {
            return update;
        }
    }

    private static class EndRoutine implements Consumer<WatchEvent<?>> {
        private String update;
        @Override
        public void accept(WatchEvent<?> s) {
            update = "Bye";
            System.out.println(s);
        }

        private String getUpdate() {
            return update;
        }
    }

    static class threadDir implements Runnable{
        DirectoryWatcher directoryWatcher;
        Callback<WatchEvent<?>> callback;

        private threadDir(DirectoryWatcher directoryWatcher, Callback<WatchEvent<?>> callback){
            this.directoryWatcher = directoryWatcher;
            this.callback = callback;
        }

        @Override
        public void run() {

            directoryWatcher.subscribe(callback);

                directoryWatcher.start();

        }
    }

}
