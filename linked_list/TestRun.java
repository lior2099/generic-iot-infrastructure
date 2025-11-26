/*
 FileName: TestRun.java
 Author: Lior Shalom
 Date: 16/07/24
 reviewer: Maya
*/

package linked_list;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class TestRun {

    ILRDLinkedList list = new ILRDLinkedList();
    ILRDIterator iter_begin = null;
    Integer[] testArray = {1, 2, 3, 4, 5};

    @Test
    public void testStartList() {
        assertEquals(true, list.isEmpty());
        assertEquals(0, list.size());
    }

    @Test
    public void testAdd() {
        list.pushFront(testArray[0]);
        list.pushFront(testArray[1]);

        assertEquals(false, list.isEmpty());
        assertEquals(2, list.size());
    }

    @Test
    public void testFind() {
        list.pushFront(testArray[0]);
        list.pushFront(testArray[1]);
        list.pushFront(testArray[2]);
        list.pushFront(testArray[3]);
        list.pushFront(testArray[4]);

        assertEquals(false, list.isEmpty());
        assertEquals(5, list.size());
        assertNull(list.find(6));
        assertNotNull(list.find(1));
        assertNotNull(list.find(5));
    }

    @Test
    public void testBeginAndPop() {
        list.pushFront(testArray[0]);
        list.pushFront(testArray[1]);
        list.pushFront(testArray[2]);
        list.pushFront(testArray[3]);
        list.pushFront(testArray[4]);

        assertEquals(false, list.isEmpty());
        assertEquals(5, list.size());

        iter_begin = list.begin();
        assertNotNull(iter_begin);

        while (!list.isEmpty()) {
            list.popFront();
        }

        assertEquals(true, list.isEmpty());
        assertEquals(0, list.size());

    }

}
