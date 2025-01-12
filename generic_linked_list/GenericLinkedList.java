/*
 FileName: ComplexNumber.java
 Author: Lior Shalom
 Date: 19/07/24
 reviewer: Yarin
*/


package il.co.ilrd.generic_linked_list;

import java.util.Iterator;
import java.util.ConcurrentModificationException;

public class GenericLinkedList<E> implements Iterable<E> {
    private Node<E> head;
    private int modCount;

    public GenericLinkedList() {

    }

    public Iterator<E> iterator() {
        return new IteratorIMP<>(head, modCount);
    }

    public void pushFront(E data) {
        this.head = new Node<E>(data, this.head);
        modCount++;
    }

    public E popFront() {
        E to_send_back = head.getData();
        head = head.getNext();
        modCount++;
        return to_send_back;
    }

    public int count() {
        int counter = 0;

        for (E run : this) {
            counter++;
        }
        return counter;
    }

    public boolean isEmpty() {
        return head == null;
    }

    public Iterator<E> find(Object obj) {
        Node<E> runner = head;

        while (runner != null) {
            if (runner.getData().equals(obj)) {
                return new IteratorIMP(runner, modCount);
            }
            runner = runner.getNext();
        }
        return null;

    }

    public static <E> void print(GenericLinkedList<E> list) {
        for (E run : list) {
            System.out.println(run);
        }
    }

    public static <E> GenericLinkedList<E> merge(GenericLinkedList<E> dest, GenericLinkedList<E> src) {
        Node<E> runner = dest.head;

        while (runner.next != null) {
            runner = runner.getNext();
        }
        runner.next = src.head;

        src.head = null;

        return dest;

    }

    public static <E> GenericLinkedList<E> reverse(GenericLinkedList<E> list) {
        GenericLinkedList reverseMe = new GenericLinkedList();

        for (E runner : list) {
            reverseMe.pushFront(runner);
        }

        return reverseMe;
    }


    private class IteratorIMP<E> implements Iterator<E> {

        private Node<E> next;
        private final int initialModCount;

        private IteratorIMP(Node<E> next, int modCount) {
            this.next = next;
            initialModCount = modCount;
        }

        @Override
        public boolean hasNext() {
            if (initialModCount != modCount) {
                throw new ConcurrentModificationException();
            }

            return next != null;
        }

        @Override
        public E next() {
            if (initialModCount != modCount) {
                throw new ConcurrentModificationException();
            }

            E to_send_back = next.getData();
            next = next.getNext();

            return to_send_back;
        }
    }

    private static class Node<T> {
        private T data;
        private Node<T> next;

        private Node(T data, Node<T> next) {
            this.data = data;
            this.next = next;
        }

        private T getData() {
            return data;
        }

        private Node<T> getNext() {
            return next;
        }

    }
}