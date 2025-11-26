/*
 FileName: Testlist.java
 Author: Lior Shalom
 Date: 16/07/24
 reviewer: Maya
*/

package linked_list;

public class Testlist {
    public static void main(String[] args) {
        ILRDLinkedList list = new ILRDLinkedList();
        ILRDIterator iter_begin = null;
        Integer[] testArray = {1, 2, 3, 4, 5};


        System.out.println("Test 1: list isEmpty is  (need to be True): " + list.isEmpty());
        System.out.println("Test 2 : list size is is (need to be 0): " + list.size() + "\n");

        list.pushFront(testArray[0]);
        list.pushFront(testArray[1]);

        System.out.println("Test 3: list size is is (need to be 2) : " + list.size() + "\n");
        list.pushFront(testArray[2]);
        list.pushFront(testArray[3]);
        list.pushFront(testArray[4]);

        System.out.println("Test 4: list find is returning (need to be null): " + list.find(6));
        System.out.println("Test 5: list find is returning (need to be addr find 3): " + list.find(3));
        System.out.println("Test 6: list find is returning (need to be addr find 5): " + list.find(5) + "\n");

        iter_begin = list.begin();
        System.out.println("Test 7: list begin is returning (need to be addr): " + iter_begin + "\n");

        System.out.println("Test 8: list size is is (need to be 5): " + list.size() + "\n");
        System.out.println("Test 9: list isEmpty is (need to be False): " + list.isEmpty() + "\n");

        System.out.println("Test 10: print list until Empty (5, 4, 3, 2, 1) ");

        while (!list.isEmpty()) {
            System.out.println(list.popFront());
        }

        System.out.println("Test 11: list isEmpty is (need to be True): " + list.isEmpty() + "\n");
        System.out.println("Test 12: list size is is (need to be 0): " + list.size() + "\n");

    }
}
