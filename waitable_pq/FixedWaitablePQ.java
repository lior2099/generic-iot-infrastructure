/*
 FileName: FixedWaitablePQ.java
 Author: Lior Shalom
 Date: 18/08/24
 reviewer:
*/

package il.co.ilrd.waitable_pq;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;


public class FixedWaitablePQ<E> {
    private static final int MINIMUM_CAPACITY = 16;
    private static final int MAX_ARRAY_SIZE = 2147483639;

    private int capacity;
    private final Comparator<? super E> comparator;
    private final Object lock;
    private final Queue<E> pq;
    private final Semaphore semIsFull;
    private final Semaphore semIsEmpty;

    public FixedWaitablePQ() {
        this(MINIMUM_CAPACITY);
    }

    public FixedWaitablePQ(int initialCapacity) {
        this(initialCapacity, null);
    }

    public FixedWaitablePQ(int initialCapacity, Comparator<? super E> comparator) {
        this.comparator = comparator;
        initialCapacity = Math.min(MAX_ARRAY_SIZE, initialCapacity);
        this.capacity = Math.max(MINIMUM_CAPACITY, initialCapacity);

        semIsFull = new Semaphore(capacity);
        semIsEmpty = new Semaphore(0);
        lock = new Object();

        pq = new PriorityQueue<>(this.capacity, this.comparator);
    }

    public FixedWaitablePQ(Comparator<? super E> comparator) {
        this(MINIMUM_CAPACITY, comparator);
    }

    public void enqueue(E element) throws InterruptedException {

        semIsFull.acquire();

        synchronized (lock) {
            pq.add(element);
            semIsEmpty.release();
        }

    }

    public E dequeue() throws InterruptedException {

        E element = null;
        semIsEmpty.acquire();

        synchronized (lock) {
            element = pq.poll();
            semIsFull.release();
        }

        return element;
    }

    public int size() {
        return pq.size();
    }

    public boolean isEmpty() {
        return pq.isEmpty();
    }

    public boolean remove(E elementToRemove) throws InterruptedException {
        boolean isFound = false;

        if (semIsEmpty.tryAcquire(0 , TimeUnit.SECONDS)) {
            synchronized (lock) {
                isFound = pq.remove(elementToRemove);
            }

            if (!isFound) {
                semIsEmpty.release();
            }
        }
        return isFound;
    }
}





