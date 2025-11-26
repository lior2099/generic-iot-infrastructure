/*
 FileName: DynamicWaitablePQ.java
 Author: Lior Shalom
 Date: 18/08/24
 reviewer:
*/

package waitable_pq;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.lang.Exception;

public class DynamicWaitablePQ<E> {
    private static final int MINIMUM_CAPACITY = 16;

    private final Comparator<? super E> comparator;
    private final Queue<E> pq;
    private final Lock lock = new ReentrantLock();
    private final Condition notEmptyCon = lock.newCondition();

    public DynamicWaitablePQ() {
        this(MINIMUM_CAPACITY, null);
    }

    public DynamicWaitablePQ(int initialCapacity) {
        this(initialCapacity, null);
    }

    public DynamicWaitablePQ(int initialCapacity, Comparator<? super E> comparator) {
        this.comparator = comparator;

        pq = new PriorityQueue<>(Math.max(MINIMUM_CAPACITY, initialCapacity), this.comparator);
    }

    public DynamicWaitablePQ(Comparator<? super E> comparator) {
        this(MINIMUM_CAPACITY, comparator);
    }

    public void enqueue(E element) throws Exception {

        if (comparator == null && !(element instanceof Comparable)) {
            throw new IncorrectCompareTypeException("element can't be compare to ");
        }

        lock.lock();

        try {
            pq.add(element);
            notEmptyCon.signalAll();
        }finally {
            lock.unlock();
        }
    }

    public E dequeue() throws InterruptedException {
        E element;

        lock.lock();
        try {
            while (pq.isEmpty()) {
                notEmptyCon.await();
            }
            element = pq.poll();
        }
        finally {
            lock.unlock();
        }
        return element;
    }

    public int size() {
        return pq.size();
    }

    public boolean isEmpty() {
        return pq.isEmpty();
    }

    public boolean remove(E elementToRemove) {
        synchronized (lock) {
            return pq.remove(elementToRemove);

        }
    }

    private static class IncorrectCompareTypeException extends RuntimeException {
        public IncorrectCompareTypeException(String errorMessage) {
            super(errorMessage);
        }
    }

}

