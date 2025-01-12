/*------------------------------------------------------------------------
Name: Callback.java
Version : 1.06
Author: Lior shalom
Reviewer: Yarin
Date: 25/08/2024
------------------------------------------------------------------------*/

package il.co.ilrd.observer;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Consumer;

public class Callback<T> {
    private final Collection<Dispatcher<T>> hashSetOfDispatcher = new HashSet<>();
    private final Consumer<T> updateRoutine;
    private final Consumer<T> endServiceRoutine;

    public Callback(Consumer<T> updateRoutine, Consumer<T> endServiceRoutine) {
        Objects.requireNonNull(updateRoutine);
        Objects.requireNonNull(endServiceRoutine);

        this.updateRoutine = updateRoutine;
        this.endServiceRoutine = endServiceRoutine;
    }

    public void update(T data) {
        updateRoutine.accept(data);
    }

    public void unregister() {
        Iterator<Dispatcher<T>> iterator = hashSetOfDispatcher.iterator();
        Dispatcher<T> dispatcher = null;


        while (iterator.hasNext()) {
            dispatcher = iterator.next();
            iterator.remove();
            dispatcher.unregister(this);
        }
    }

    public void endService(T data) {
        endServiceRoutine.accept(data);
    }

    public void addDispatcher(Dispatcher<T> dispatcher) {
        Objects.requireNonNull(dispatcher);

        hashSetOfDispatcher.add(dispatcher);
    }

    public void removeDispatcher(Dispatcher<T> dispatcher) {
        Objects.requireNonNull(dispatcher);

        hashSetOfDispatcher.remove(dispatcher);
        dispatcher.unregister(this);
    }

}