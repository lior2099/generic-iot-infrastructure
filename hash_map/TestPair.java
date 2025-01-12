/*
 FileName: Pair.java
 Author: Lior Shalom
 Date: 18/07/24
 reviewer: lior
*/

package il.co.ilrd.hash_map;

import il.co.ilrd.pair.Pair;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class TestPair {

    @Test
    public void getSetTest() {
        Pair<String, Integer> newPair = new Pair<>("lior" , 100);

        assertEquals(100 , newPair.getValue());
        assertEquals("lior" , newPair.getKey());

        newPair.setValue(200);

        assertEquals(200 , newPair.getValue());
    }

    @Test
    public void hashCodeTest() {
        Pair<String, Integer> newPair = new Pair<>("lior" , 100);

       System.out.println("print hashCode : " + newPair.hashCode());
    }

    @Test
    public void equalsTest() {
        Pair<String, Integer> newPair = new Pair<>("lior" , 100);
        Pair<String, Integer> newPair2 = new Pair<>("Maya" , 30);
        Pair<String, Integer> newPair3 = new Pair<>("Maya" , 30);

        assertNotEquals(newPair, newPair2);
        assertNotEquals(5, newPair);

        assertEquals(newPair3, newPair2);
        newPair3.setValue(100);
        assertNotEquals(newPair3, newPair2);

    }

    @Test
    public void ofTest() {
        Pair<String, Integer> newPair ;

        newPair =  Pair.of("dude" , 100);

        assertEquals(100 , newPair.getValue());
        assertEquals("dude" , newPair.getKey());

    }

    @Test
    public void swapTest() {
        Pair<String, Integer> newPair = new Pair<>("lior" , 100);
        Pair<Integer, String> sawpPair ;

        assertEquals(100 , newPair.getValue());
        assertEquals("lior" , newPair.getKey());

        sawpPair =  Pair.swap(newPair);

        assertEquals(100 , sawpPair.getKey());
        assertEquals("lior" , sawpPair.getValue());

    }

    @Test
    public void minMaxTest() {
        Pair<Integer, Integer> newPair ;
        Pair<Integer, Integer> newPair2 ;

        Integer[] testArray = {6, 2, 174, 5, 55, 11, 1 , 66 };

        newPair = Pair.minMax(testArray);

        assertEquals(174 , newPair.getValue());
        assertEquals(1 , newPair.getKey());

        newPair2 = Pair.minMax(testArray , Integer::compareTo);
        assertEquals(174 , newPair2.getValue());
        assertEquals(1 , newPair2.getKey());

    }


}
