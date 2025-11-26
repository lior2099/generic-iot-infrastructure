/*------------------------------------------------------------------------
Name: Subscriber.java
Version : 1.06
Author: Lior shalom
Reviewer:
Date: 25/08/2024
------------------------------------------------------------------------*/


package observer;

import java.util.Objects;
import java.util.function.Consumer;

public class Subscriber<T> {

    private final Callback<T> callback;

    public Subscriber(Consumer<T> updateRoutine, Consumer<T> endServiceRoutine) {
        Objects.requireNonNull(updateRoutine);
        Objects.requireNonNull(endServiceRoutine);

        callback = new Callback<>(updateRoutine, endServiceRoutine);
    }

    public void unsubscribeFromAll() {
        callback.unregister();
    }

    public Callback<T> getCallback() {
        return callback;
    }
}
