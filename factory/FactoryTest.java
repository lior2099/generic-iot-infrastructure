/*
 FileName: factoryTest.java
 Author: Lior Shalom
 Date: 14/08/24
 reviewer: ido
*/

package il.co.ilrd.factory;

import org.junit.jupiter.api.Test;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;


public class FactoryTest {
    private final Factory<String, String, Integer> factory = new Factory<>();

    //Lambda Expression
    @Test
    public void testLambda() {
        factory.add("Lior", (Integer i) -> "Lior age : " + i);
        String result = factory.create("Lior", 33);
        assertEquals("Lior age : 33", result);
    }

    // Static Method
    @Test
    public void testStatic() {
        factory.add("static", FactoryTest::staticMethod);
        String result = factory.create("static", 10);
        assertEquals("Result: 10", result);
    }



    //  Instance Method Reference
    @Test
    public void testInstance() {
//        StringMe instance = new StringMe();
//        factory.add("instance", instance::process);
//        String result = factory.Create("instance", 4);
//        assertEquals("how much pizza can i eat : 4", result);

        factory.add("method of a specific instance", new StringMe(4)::process);
        String result2 = factory.create("method of a specific instance", 7);
        assertEquals("how much pizza can i eat : 74", result2);
    }

    // Anonymous Class
    @Test
    public void testAnonymous() {
        factory.add("anonymous", new Function<Integer, String>() {
            @Override
            public String apply(Integer i) {
                return "Anonymous : " + i;
            }
        });
        String result = factory.create("anonymous", 69);
        assertEquals("Anonymous : 69", result);
    }

    @Test
    public void testParticularType() {
        factory.add("particularType", Integer::toHexString);  //will make int to hex
        String result = factory.create("particularType", (255 << 8) + 255);
        assertEquals("ffff", result);
    }

    // add Method for static
    public static String staticMethod(Integer i) {
        return "Result: " + i;
    }

    //    instance method
    private static class StringMe {
        public int data;

        public StringMe() {
            data = 1;
        }

        public StringMe(int data) {
            this.data = data;
        }

        public String process(Integer i) {
            return "how much pizza can i eat : " + i + data;
        }
    }

}
