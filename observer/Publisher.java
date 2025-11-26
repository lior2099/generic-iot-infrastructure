/*------------------------------------------------------------------------
Name: Publisher.java
Version : 1.06
Author: Lior shalom
Reviewer: Yarin
Date: 25/08/2024
------------------------------------------------------------------------*/

package observer;

import java.util.Objects;

public class Publisher<T> {
    private Dispatcher<T> dispatcher;

    public Publisher() {
        dispatcher = new Dispatcher<>();
    }

    public void subscribe(Subscriber<T> subscriber) {
        Objects.requireNonNull(subscriber);

        dispatcher.register(subscriber.getCallback());
    }

    public void unsubscribe(Subscriber<T> subscriber) {
        Objects.requireNonNull(subscriber);

        dispatcher.unregister(subscriber.getCallback());
    }

    public void unsubscribeAll(T data) {
        dispatcher.endService(data);
    }

    public void publish(T update) {
        dispatcher.updateAll(update);
    }
}

