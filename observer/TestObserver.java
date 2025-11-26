/*------------------------------------------------------------------------
Name: TestObserver.java
Version : 1.06
Author: Lior shalom
Reviewer: Yarin
Date: 25/08/2024
------------------------------------------------------------------------*/

package observer;

import java.util.function.Consumer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestObserver {

    @Test
    public void publishStringTest() {
        Publisher<String> stringPublisher = new Publisher<>();

        Consumer<String> routine = new StringUpdateRoutine();
        Subscriber<String> stringSubscriber1 = new Subscriber<>(routine, routine);
        Subscriber<String> stringSubscriber2 = new Subscriber<>(routine, routine);

        stringPublisher.subscribe(stringSubscriber1);
        stringPublisher.subscribe(stringSubscriber2);

        stringPublisher.publish("Hi everybody, I just uploaded a new video");
    }

    @Test
    public void publishIntegerTest() {
        Publisher<Integer> integerPublisher = new Publisher<>();

        Consumer<Integer> routine = new IntegerUpdateRoutine();
        Subscriber<Integer> integerSubscriber1 = new Subscriber<>(routine,routine);
        Subscriber<Integer> integerSubscriber2 = new Subscriber<>(routine,routine);

        integerPublisher.subscribe(integerSubscriber1);
        integerPublisher.subscribe(integerSubscriber2);

        integerPublisher.publish(100);
    }

    @Test
    public void publishThrowableTest() {
        Publisher<Throwable> throwablePublisher = new Publisher<>();

        Consumer<Throwable> routine = new ThrowableUpdateRoutine();
        Subscriber<Throwable> throwableSubscriber1 = new Subscriber<>(routine, routine);
        Subscriber<Throwable> throwableSubscriber2 = new Subscriber<>(routine, routine);
        Object test1 = null;
        Object test2 = new Object();

        throwablePublisher.subscribe(throwableSubscriber1);
        throwablePublisher.subscribe(throwableSubscriber2);

        try {
            test1.equals(test2);
        } catch (NullPointerException e) {
            throwablePublisher.publish(e);
        }
    }

    @Test
    public void unsubscribeTest() {
        Publisher<String> stringPublisher = new Publisher<>();

        Consumer<String> sub1Routine = new StringUpdateRoutine();
        Consumer<String> sub2Routine = new StringUpdateRoutine();

        Subscriber<String> stringSubscriber1 = new Subscriber<>(sub1Routine, sub2Routine);
        Subscriber<String> stringSubscriber2 = new Subscriber<>(sub2Routine, sub2Routine);

        stringPublisher.subscribe(stringSubscriber1);
        stringPublisher.subscribe(stringSubscriber2);

        stringPublisher.publish("Hi everybody, I just uploaded a new video");
        assertEquals(((StringUpdateRoutine)sub1Routine).getUpdate(),
                ((StringUpdateRoutine)sub2Routine).getUpdate());

        stringPublisher.unsubscribe(stringSubscriber1);

        stringPublisher.publish("New video coming tonight");
        assertNotEquals(((StringUpdateRoutine)sub1Routine).getUpdate(),
                ((StringUpdateRoutine)sub2Routine).getUpdate());
    }

    @Test
    public void unsubscribeAllTest() {
        Publisher<String> stringPublisher = new Publisher<>();

        Consumer<String> sub1Routine = new StringUpdateRoutine();
        Consumer<String> sub2Routine = new StringUpdateRoutine();

        Subscriber<String> stringSubscriber1 = new Subscriber<>(sub1Routine, sub1Routine);
        Subscriber<String> stringSubscriber2 = new Subscriber<>(sub2Routine, sub2Routine);

        String firstUpdate = "Hi everybody, I just uploaded a new video";
        String endingService = "Sorry no more videos from me";
        String secondUpdate = "SIKE new video tomorrow";

        stringPublisher.subscribe(stringSubscriber1);
        stringPublisher.subscribe(stringSubscriber2);

        stringPublisher.publish(firstUpdate);
        assertEquals(((StringUpdateRoutine)sub1Routine).getUpdate(),
                ((StringUpdateRoutine)sub2Routine).getUpdate());

        stringPublisher.unsubscribeAll(endingService);

        assertEquals(((StringUpdateRoutine)sub1Routine).getUpdate(),
                ((StringUpdateRoutine)sub2Routine).getUpdate());

        stringPublisher.publish(secondUpdate);

        assertNotEquals(secondUpdate, ((StringUpdateRoutine)sub1Routine).getUpdate());
        assertNotEquals(secondUpdate, ((StringUpdateRoutine)sub2Routine).getUpdate());
    }

    @Test
    public void unsubscribeFromAllTest() {
        Publisher<String> stringPublisher1 = new Publisher<>();
        Publisher<String> stringPublisher2 = new Publisher<>();
        Consumer<String> subRoutine = new StringUpdateRoutine();
        Consumer<String> sub2Routine = new StringUpdateRoutine();
        Subscriber<String> stringSubscriber = new Subscriber<>(subRoutine, sub2Routine);

        String firstUpdate = "Hi everybody, I just uploaded a new video";
        String secondUpdate = "Hello there's a new video uploaded later today";
        String thirdUpdate = "Sorry to see you leave";

        stringPublisher1.subscribe(stringSubscriber);
        stringPublisher2.subscribe(stringSubscriber);

        stringPublisher1.publish(firstUpdate);
        assertEquals(firstUpdate, ((StringUpdateRoutine)subRoutine).getUpdate());

        stringPublisher2.publish(secondUpdate);
        assertEquals(secondUpdate, ((StringUpdateRoutine)subRoutine).getUpdate());

        stringSubscriber.unsubscribeFromAll();

        stringPublisher1.publish(thirdUpdate);
        assertNotEquals(thirdUpdate, ((StringUpdateRoutine)subRoutine).getUpdate());

        stringPublisher2.publish(thirdUpdate);
        assertNotEquals(thirdUpdate, ((StringUpdateRoutine)subRoutine).getUpdate());

    }

    private static class StringUpdateRoutine implements Consumer<String> {
        private String update;
        @Override
        public void accept(String s) {
            update = s;
            System.out.println(s);
        }

        private String getUpdate() {
            return update;
        }
    }

    private static class IntegerUpdateRoutine implements Consumer<Integer> {

        @Override
        public void accept(Integer num) {
            System.out.println(num);
        }
    }

    private static class ThrowableUpdateRoutine implements Consumer<Throwable> {

        @Override
        public void accept(Throwable exception) {
            System.out.println("THIS IS OK!");
            exception.printStackTrace();
        }
    }
}
