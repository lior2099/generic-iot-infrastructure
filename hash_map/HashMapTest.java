/*
 FileName: HashMapTest.java
 Author: Lior Shalom
 Date: 7/08/24
 reviewer: Lior
*/

package hash_map;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


public class HashMapTest {
    @Test
    public void startTest() {
        ILRDHashMap<String, Integer> mapMe = new ILRDHashMap<>(0.50F);

        assertTrue(mapMe.isEmpty());
        assertEquals(0 , mapMe.size());

    }

    @Test
    public void putTest() {
        ILRDHashMap<String, Integer> mapMe = new ILRDHashMap<>(5);

        assertTrue(mapMe.isEmpty());
        assertEquals(0 , mapMe.size());
//        assertEquals(16 , mapMe.getCapacity());


        assertNull(mapMe.put("lior" , 100));
        assertEquals(1 , mapMe.size());
        assertEquals(100 , mapMe.get("lior"));

        assertEquals(100 ,  mapMe.put("lior" , 0));
        assertNull(mapMe.put("A" , 10));
        assertNull(mapMe.put("B" , 100));
        assertNull(mapMe.put("C" , 20));
        assertNull(mapMe.put("D" , 60));
        assertNull(mapMe.put("E" , 70));
        assertNull(mapMe.put("F" , 60));

        assertEquals(7 , mapMe.size());

    }


    @Test
    public void CapacityTest() {
        ILRDHashMap<String, Integer> mapMe = new ILRDHashMap<>(5);

        assertTrue(mapMe.isEmpty());
        assertEquals(0 , mapMe.size());
//        assertEquals(16 , mapMe.getCapacity());


        assertNull(mapMe.put("lior" , 100));
        assertEquals(1 , mapMe.size());
        assertEquals(100 , mapMe.get("lior"));

        assertEquals(100 ,  mapMe.put("lior" , 0));
        assertNull(mapMe.put("A" , 10));
        assertNull(mapMe.put("B" , 100));
        assertNull(mapMe.put("C" , 20));
        assertNull(mapMe.put("D" , 60));
        assertNull(mapMe.put("E" , 70));
        assertNull(mapMe.put("F" , 60));

        assertEquals(7 , mapMe.size());
        assertNull(mapMe.put("FD" , 60));
        assertNull(mapMe.put("Fs" , 60));
        assertNull(mapMe.put("Fgw" , 60));
        assertNull(mapMe.put("Faf" , 60));
//        assertEquals(16 , mapMe.getCapacity());
        assertNull(mapMe.put("Fsad" , 60));
//        assertEquals(32 , mapMe.getCapacity());
        assertNull(mapMe.put("Fafaf" , 60));
        assertNull(mapMe.put("Fsfafs" , 60));
        assertNull(mapMe.put("Fasfas" , 60));
        assertNull(mapMe.put("Faqwsa" , 60));
        assertEquals(16 , mapMe.size());

        for(Integer i = 0 ; i < 64 ; i++){
            mapMe.put(Integer.toString(i), i);
        }

        assertEquals(80 , mapMe.size());

    }

    @Test
    public void putAllTest() {
        ILRDHashMap<String, Integer> mapMe = new ILRDHashMap<>(5);
        Map<String, Integer> mapToSend = new HashMap<>();


        assertTrue(mapMe.isEmpty());
        assertEquals(0 , mapMe.size());
//        assertEquals(16 , mapMe.getCapacity());

        mapToSend.put("A", 20);
        mapToSend.put("B", 30);
        mapToSend.put("C", 40);
        mapToSend.put("D", 50);
        mapToSend.put("E", 60);

        ILRDHashMap<String, Integer> mapMe2 = new ILRDHashMap<>(mapToSend);

        assertNull(mapMe.put("lior" , 100));
        assertEquals(1 , mapMe.size());
        assertEquals(100 , mapMe.get("lior"));

        assertEquals(100 ,  mapMe.put("lior" , 0));

        mapMe.putAll(mapToSend);
        assertEquals(6 , mapMe.size());
        assertEquals(50 ,  mapMe.put("D" , 65));

        assertEquals(5 , mapMe2.size());
        assertEquals(mapMe.get("B") , mapMe2.get("B"));

    }

    @Test
    public void removeTest() {
        ILRDHashMap<String, Integer> mapMe = new ILRDHashMap<>(5);

        assertTrue(mapMe.isEmpty());
        assertEquals(0 , mapMe.size());
//        assertEquals(16 , mapMe.getCapacity());


        assertNull(mapMe.put("lior" , 100));
        assertEquals(1 , mapMe.size());
        assertEquals(100 , mapMe.get("lior"));

        assertEquals(100 ,  mapMe.put("lior" , 0));
        assertNull(mapMe.put("A" , 10));
        assertNull(mapMe.put("B" , 100));
        assertNull(mapMe.put("C" , 20));
        assertNull(mapMe.put("D" , 60));
        assertNull(mapMe.put("E" , 70));
        assertNull(mapMe.put("F" , 60));

        assertEquals(7 , mapMe.size());

        assertEquals(60 , mapMe.remove("D"));


        assertNull(mapMe.put("D" , 60));

    }
    @Test
    public void clearTest() {
        ILRDHashMap<String, Integer> mapMe = new ILRDHashMap<>(5);

        assertTrue(mapMe.isEmpty());
        assertEquals(0 , mapMe.size());
//        assertEquals(16 , mapMe.getCapacity());


        assertNull(mapMe.put("lior" , 100));
        assertEquals(1 , mapMe.size());
        assertFalse(mapMe.isEmpty());
        assertEquals(100 , mapMe.get("lior"));

        assertEquals(100 ,  mapMe.put("lior" , 0));
        assertNull(mapMe.put("A" , 10));
        assertNull(mapMe.put("B" , 100));
        assertNull(mapMe.put("C" , 20));
        assertNull(mapMe.put("D" , 60));
        assertNull(mapMe.put("E" , 70));
        assertNull(mapMe.put("F" , 60));

        assertEquals(7 , mapMe.size());

        assertFalse(mapMe.isEmpty());
        mapMe.clear();
        System.out.println(mapMe.keySet());
        assertEquals(0 , mapMe.size());
        assertTrue(mapMe.isEmpty());


    }

    @Test
    public void keySetTest() {
        ILRDHashMap<String, Integer> mapMe = new ILRDHashMap<>(5);

        assertTrue(mapMe.isEmpty());
        assertEquals(0 , mapMe.size());
//        assertEquals(16 , mapMe.getCapacity());


        assertNull(mapMe.put("0" , 100));
        assertEquals(1 , mapMe.size());
        assertEquals(100 , mapMe.get("0"));

//        assertEquals(100 ,  mapMe.put("lior" , 0));
        assertNull(mapMe.put("A" , 10));
        assertNull(mapMe.put("B" , 100));
        assertNull(mapMe.put("C" , 20));
        assertNull(mapMe.put("D" , 60));
        assertNull(mapMe.put("E" , 70));
        assertNull(mapMe.put("F" , 60));
        assertEquals(70 ,  mapMe.put("E" , 55));

        Iterator<String> keyIter = mapMe.keySet().iterator();
        System.out.println(mapMe.keySet());

        assertTrue(mapMe.containsKey("A"));
        assertFalse(mapMe.containsKey("X"));
        assertTrue(mapMe.containsValue(100));
        assertFalse(mapMe.containsValue(6954));

        assertEquals(7 , mapMe.size());


        Collection<Integer> vset = mapMe.values();

        if (!vset.isEmpty())
        {
            System.out.println(vset);
        }


    }

    @Test
    public void DicTest() throws IOException {
        ILRDHashMap<String, String> dictMap = new ILRDHashMap<>();
        File filePath = new File("/usr/share/dict/british-english");

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))){
            String currentWord;

            while ((currentWord = reader.readLine()) != null){
                dictMap.put(currentWord, currentWord);
            }

            assertTrue(dictMap.containsKey("pervade"));
            assertTrue(dictMap.containsKey("corpse's"));
            System.out.println(dictMap.keySet());
            assertTrue(dictMap.containsValue("testosterone"));
            assertTrue(dictMap.containsValue("interweaving"));
            assertFalse(dictMap.containsValue("KillMeAll"));

//            System.out.println(dictMap.size());

//            for (String word : dictMap.values()){
//                System.out.println(word);
//            }

            assertEquals(103494, dictMap.size());
        }
        catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Test
    public void nullTest() {
        ILRDHashMap<String, Integer> mapMe = new ILRDHashMap<>(5);

        assertTrue(mapMe.isEmpty());
        assertEquals(0 , mapMe.size());
//        assertEquals(16 , mapMe.getCapacity());


        assertNull(mapMe.put("lior" , 100));
        assertEquals(1 , mapMe.size());
        assertEquals(100 , mapMe.get("lior"));

        assertEquals(100 ,  mapMe.put("lior" , 0));
        assertNull(mapMe.put("A" , 10));
        assertNull(mapMe.put("F" , 60));
        assertNull(mapMe.put(null , 675));
        assertEquals(675 ,  mapMe.put(null , 44));
        assertEquals(44 ,  mapMe.remove(null));
        assertNull(mapMe.put(null , 547));
        assertNull(mapMe.put("D" , 10));

        System.out.println(mapMe.keySet());


        assertEquals(5 , mapMe.size());

    }

}
