/*
 FileName: LinkedListTest.java
 Author: Lior Shalom
 Date: 19/07/24
 reviewer: Yarin
*/

package generic_linked_list;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class LinkedListTest {

    @Test
    public void pushPopTest() {
        GenericLinkedList<Integer> testList = new GenericLinkedList<>();
        Integer[] testArray = {1, 2, 3, 4, 5};

        assertEquals(0, testList.count());
        assertTrue(testList.isEmpty());

        for (Integer num : testArray) {
            testList.pushFront(num);
        }

        assertEquals(5, testList.count());
        assertFalse(testList.isEmpty());

    }

    @Test
    public void findTest() {
        GenericLinkedList<Integer> testList = new GenericLinkedList<>();
        Integer[] testArray = {1, 2, 3, 4, 5};

        assertEquals(0, testList.count());
        assertTrue(testList.isEmpty());

        for (Integer num : testArray) {
            testList.pushFront(num);
        }

        assertEquals(5, testList.count());
        assertFalse(testList.isEmpty());

        assertNotNull(testList.find(3));
        assertNotNull(testList.find(5));

        assertEquals(5, testList.popFront());
        assertNull(testList.find(5));

    }

    @Test
    public void printTest() {
        GenericLinkedList<Integer> testList = new GenericLinkedList<>();
        Integer[] testArray = {1, 2, 3, 5, 7, 11, 13, 17};

        for (Integer num : testArray) {
            testList.pushFront(num);
        }

        GenericLinkedList.print(testList);
    }

    @Test
    public void reverseTest() {
        GenericLinkedList<Integer> testList = new GenericLinkedList<>();
        GenericLinkedList<Integer> reverseMe;
        Integer[] testArray = {1, 2, 3, 5, 7, 11, 13, 17};

        for (Integer num : testArray) {
            testList.pushFront(num);
        }
        System.out.println("list before reverse");
        GenericLinkedList.print(testList);
        reverseMe = GenericLinkedList.reverse(testList);

        System.out.println("\nlist after reverse");
        GenericLinkedList.print(reverseMe);

    }

    @Test
    public void mergeTest() {
        GenericLinkedList<Integer> List1 = new GenericLinkedList<>();
        GenericLinkedList<Integer> List2 = new GenericLinkedList<>();
        Integer[] Array1 = {1, 2, 3, 5, 7, 11, 13, 17};
        Integer[] Array2 = {4, 6, 8, 10, 12};

        for (Integer num : Array1) {
            List1.pushFront(num);
        }

        for (Integer num : Array2) {
            List2.pushFront(num);
        }


        System.out.println("list before merge");
        GenericLinkedList.print(List1);

        GenericLinkedList.merge(List1, List2);

        System.out.println("list after merge");
        GenericLinkedList.print(List1);

    }


}