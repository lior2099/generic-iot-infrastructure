/*
 FileName: ILRDLinkedList.java
 Author: Lior Shalom
 Date: 16/07/24
 reviewer: Maya
*/

package linked_list;

public class ILRDLinkedList {
    private Node head;

    public ILRDLinkedList() {
        head = null;
    }

    public ILRDLinkedList(Object data) {
        head = new Node(data);
    }

    public void pushFront(Object data) {
        head = new Node(data, head);
    }

    public Object popFront() {

        Object data_pop = head.getData();
        head = head.getNext();
        return data_pop;
    }

    public int size() {
        int count = 0;
        ListIterator iterCount = new ListIterator(head);

        while (iterCount.hasNext()) {
            ++count;
            iterCount.next();
        }
        return count;
    }

    public boolean isEmpty() {
        return null == head;
    }

    public ILRDIterator begin() {
        return new ListIterator(head);
    }

    public ILRDIterator find(Object data) {
        ListIterator iterRun = new ListIterator(head);

        while (iterRun.hasNext()) {
            if (data.equals(iterRun.getData())) {
                return iterRun;
            }
            iterRun.next();
        }
        return null;
    }

    private class Node {
        private Object data;
        private Node next;

        public Node() {
            data = null;
            next = null;
        }

        public Node(Object data_send) {
            data = data_send;
            next = null;
        }

        public Node(Object data_send, Node next_send) {
            data = data_send;
            next = next_send;
        }

        public Object getData() {
            return data;
        }

        public Node getNext() {
            return next;
        }

    }

    private static class ListIterator implements ILRDIterator {
        private Node currNode;

        public ListIterator(Node currNode_send) {
            currNode = currNode_send;
        }

        @Override
        public boolean hasNext() {
            if (null == currNode) {
                return false;
            }
            return true;
        }

        @Override
        public Object next() {

            Object data = currNode.getData();
            currNode = currNode.getNext();

            return data;
        }

        private Object getData() {
            return currNode.getData();
        }

    }
}
