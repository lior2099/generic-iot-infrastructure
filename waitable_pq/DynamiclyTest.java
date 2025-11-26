/*
 FileName: dynamiclyTest.java
 Author: Lior Shalom
 Date: 18/08/24
 reviewer:
*/

package waitable_pq;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DynamiclyTest {

    @Test
    public void pqSizeEmpty() throws Exception {
        DynamicWaitablePQ<Integer> pq = new DynamicWaitablePQ<>();

        assertTrue(pq.isEmpty());
        assertEquals(0, pq.size());

        pq.enqueue(1);
        assertFalse(pq.isEmpty());
        assertEquals(1, pq.size());

    }

    @Test
    public void pqAddRemove() throws Exception {
        DynamicWaitablePQ<Integer> pq = new DynamicWaitablePQ<>(10);
        ArrayList<Integer> arrint = new ArrayList<>();


        arrint.add(10);
        arrint.add(5);
        arrint.add(63);
        arrint.add(77);
        arrint.add(84);
        arrint.add(2);

        ArrayList<Integer> arrintSort = new ArrayList<>(arrint); // make a new list Sort
        arrintSort.sort(Integer::compareTo);


        assertTrue(pq.isEmpty());
        assertEquals(0, pq.size());

        for (int i = 0; i < 6; i++) {
            pq.enqueue(arrint.get(i));
        }
        assertEquals(6, pq.size());

        for (int i = 0; i < 6; i++) {
            assertEquals(arrintSort.get(i), pq.dequeue());
        }

        assertTrue(pq.isEmpty());
        assertEquals(0, pq.size());

    }

    @Test
    public void pqEnqueueDequeueTreads() throws InterruptedException {
        DynamicWaitablePQ<Integer> pq = new DynamicWaitablePQ<>();
        Thread[] threadsAdds = new Thread[5];
        Thread threadRemove;
        ArrayList<Integer> arrint = new ArrayList<>();
        Collections.addAll(arrint, 10, 5, 63, 77, 84, 2, 15, 30, 47, 98);
        ArrayList<Integer> arrintSort = new ArrayList<>();
        Collections.addAll(arrintSort, 2, 5, 6, 7, 7, 8, 9, 10, 10, 10, 10, 10, 12, 15, 17, 21, 22, 27, 30, 33, 37, 39, 44,
                47, 51, 55, 58, 63, 63, 65, 67, 69, 71, 71, 77, 79, 80, 83, 84, 86, 88, 89, 92, 96, 98, 100, 107, 116, 125, 134);


        for (int i = 0; i < 5; i++) {//run on the treads
            final int theadId = i;
            threadsAdds[i] = new Thread(() -> {
                for (int j = 0; j < 10; j++) {
                    try {
                        pq.enqueue(j * theadId + arrint.get(j));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
//                   System.out.println(j *theadId + arrint.get(j));   to make the SortArr
                }
            }
            );
        }

        threadRemove = new Thread(() -> {
            for (int i = 0; i < 50; i++) {
//                assertTrue(arrintSort.contains(pq.dequeue()));
            }
        });

        for (int i = 0; i < 5; i++) {
            threadsAdds[i].start();
        }
        threadRemove.start();

        for (int i = 0; i < 5; i++) {
            threadsAdds[i].join();
        }
        threadRemove.join();

    }

    @Test
    public void pqRemove() throws Exception {
        DynamicWaitablePQ<Integer> pq = new DynamicWaitablePQ<>(Integer::compareTo);
        List<Integer> arrint = new ArrayList<>();
        Collections.addAll(arrint, 10, 5, 63, 77, 84, 2, 15, 30, 47, 98);

        assertTrue(pq.isEmpty());
        assertEquals(0, pq.size());

        for (Integer num : arrint) {
            pq.enqueue(num);
        }

        assertEquals(10, pq.size());

        assertTrue(pq.remove(77));
        assertFalse(pq.remove(77));
        assertFalse(pq.remove(7754));

        assertEquals(9, pq.size());

    }
}
