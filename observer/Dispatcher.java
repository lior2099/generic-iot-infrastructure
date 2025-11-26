/*------------------------------------------------------------------------
Name: Dispatcher.java
Version : 1.06
Author: Lior shalom
Reviewer: Yarin
Date: 25/08/2024
------------------------------------------------------------------------*/

package observer;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;

public class Dispatcher<T> {
    private final Collection<Callback<T>> hashSetOfCallback = new HashSet<>();

    public void register(Callback<T> callback) {
        Objects.requireNonNull(callback);

        hashSetOfCallback.add(callback);
        callback.addDispatcher(this);
    }

    public void unregister(Callback<T> callback) {
        Objects.requireNonNull(callback);

        hashSetOfCallback.remove(callback);
    }

    public void updateAll(T data) {
        Objects.requireNonNull(data);

        for (Callback<T> elem : hashSetOfCallback) {
            elem.update(data);
        }
    }

    public void endService(T data) {
        Iterator<Callback<T>> iterator = hashSetOfCallback.iterator();
        Callback<T> callback = null;

        while (iterator.hasNext()) {
            callback = iterator.next();
            callback.endService(data);

            iterator.remove();
            callback.removeDispatcher(this);
        }
    }
}

