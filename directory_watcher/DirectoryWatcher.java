/*------------------------------------------------------------------------
Name: DirectoryWatcher.java
Version : 1.00
Author: Lior shalom
Reviewer: maya
Date: 28/08/2024
------------------------------------------------------------------------*/

package il.co.ilrd.directory_watcher;

import il.co.ilrd.observer.Callback;
import il.co.ilrd.observer.Dispatcher;

import javax.swing.filechooser.FileSystemView;
import java.io.IOException;
import java.nio.file.ClosedWatchServiceException;
import java.nio.file.FileSystems;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchService;
import java.nio.file.WatchKey;
import java.nio.file.WatchEvent;
import java.nio.file.StandardWatchEventKinds;
import java.util.Objects;

public class DirectoryWatcher {
    private final Path directoryPath;
    private final WatchService watchService;
    private final Dispatcher<WatchEvent<?>> dispatcher;
    private WatchKey key;
    private Thread watcherThread;
    private boolean isRunning = true;

    public DirectoryWatcher(String directory) {
        try {
            directoryPath = Paths.get(directory);
        } catch (InvalidPathException e) {
            throw new InvalidPathException(e.getInput() , e.getReason());
        }

        try {
            watchService = FileSystems.getDefault().newWatchService();
        } catch (IOException e) {
            throw new DirWatchingException(e);
        }

        dispatcher = new Dispatcher<>();
    }

    public void subscribe(Callback<WatchEvent<?>> callback) {
        Objects.requireNonNull(callback);

        dispatcher.register(callback);
    }

    public void unsubscribe(Callback<WatchEvent<?>> callback) {
        Objects.requireNonNull(callback);

        dispatcher.unregister(callback);
    }

    public void start() {
        isRunning = true;
        watcherThread = new Thread(new WatcherThread());
        watcherThread.start();
    }

    public void stop() {
        isRunning = false;

        try {
            watchService.close();
            watcherThread.join();
        } catch (IOException | InterruptedException e) {
            throw new DirWatchingException(e);
        }

        dispatcher.endService(null);
    }

    private class WatcherThread implements Runnable {

        @Override
        public void run() {
            try {
                key = directoryPath.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY ,
                        StandardWatchEventKinds.ENTRY_CREATE , StandardWatchEventKinds.ENTRY_DELETE , StandardWatchEventKinds.OVERFLOW);
            } catch (IOException e) {
                throw new DirWatchingException(e);
            }

            while (isRunning) {
                try {
                    key = watchService.take();
                } catch (InterruptedException e) {
                    throw new DirWatchingException(e);
                } catch (ClosedWatchServiceException e) {
                    return;
                }

                for (WatchEvent<?> event : key.pollEvents()) {
//                    System.out.println("Event kind:" + event.kind() + ". File affected: " + event.context() + ".");
                    dispatcher.updateAll(event);
                }

                if (!key.reset()) {
                    throw new DirWatchingException("Reset key fail");
                }
            }
        }
    }

    private static final class DirWatchingException extends RuntimeException {
        private DirWatchingException(String message) {
            this(message, null);
        }

        private DirWatchingException(Exception e) {
            this(null, e);
        }

        private DirWatchingException(String message, Exception e) {
            super(message, e);
        }
    }
}