/*
 FileName: SingletonTest.java
 Author: Lior Shalom
 Date: 31/07/24
 reviewer:Maya
*/

package il.co.ilrd.singeton;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SingletonTest {

    @Test
    public void Eager() {
        System.out.println("Testing Eager  :)");
        SingletonEager eagerInstance1 = SingletonEager.getInstance();
        SingletonEager eagerInstance2 = SingletonEager.getInstance();
        assertEquals(eagerInstance1, eagerInstance2);
    }

    @Test
    public void Lazy() {
        System.out.println("Testing Lazy  :)");
        SingletonLazy lazyInstance1 = SingletonLazy.getInstance();
        SingletonLazy lazyInstance2 = SingletonLazy.getInstance();
        assertEquals(lazyInstance1, lazyInstance2);
    }

    @Test
    public void Double() {
        System.out.println("Testing Double  :)");
        SingletonDouble doubleInstance1 = SingletonDouble.getInstance();
        SingletonDouble doubleInstance2 = SingletonDouble.getInstance();
        assertEquals(doubleInstance1, doubleInstance2);
    }

    @Test
    public void Holder() {
        System.out.println("Testing Holder  :)");
        SingletonHolder holderInstance1 = SingletonHolder.getInstance();
        SingletonHolder holderInstance2 = SingletonHolder.getInstance();
        assertEquals(holderInstance1, holderInstance2);
    }

    @Test
    public void Enum() {
        System.out.println("Testing Enum  :)");
        SingletonEnum enumInstance1 = SingletonEnum.getInstance();
        SingletonEnum enumInstance2 = SingletonEnum.getInstance();
        assertEquals(enumInstance1, enumInstance2);
    }

}


