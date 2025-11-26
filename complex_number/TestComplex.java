/*
 FileName: TestComplex.java
 Author: Lior Shalom
 Date: 18/07/24
 reviewer: Lior
*/

package complex_number;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class TestComplex {
    ComplexNumber num1 = new ComplexNumber();
    ComplexNumber num2 = new ComplexNumber();


    @Test
    public void StartInit() {

        assertEquals(0.0, num1.getReal());
        assertEquals(0.0, num1.getImaginary());

        num1.setReal(10);
        num1.setImaginary(-7);
        assertEquals(10, num1.getReal());
        assertEquals(-7, num1.getImaginary());

        num1.setValue(6, 7);
        assertEquals(6, num1.getReal());
        assertEquals(7, num1.getImaginary());

    }

    @Test
    public void TestAddAndSubtract() {
        ComplexNumber num3;

        num1.setValue(6, 7);
        assertEquals(6, num1.getReal());
        assertEquals(7, num1.getImaginary());

        num2.setValue(4, 3);
        assertEquals(4, num2.getReal());
        assertEquals(3, num2.getImaginary());

        num3 = num1.add(num2);
        assertEquals(10, num3.getReal());
        assertEquals(10, num3.getImaginary());

        assertEquals(4, (num3.subtract(num1)).getReal());
        assertEquals(3, (num3.subtract(num1)).getImaginary());

    }

    @Test
    public void TestMultiandDiv() {
        ComplexNumber num3;
        ComplexNumber num4 = new ComplexNumber(-2, -5);

        num1.setValue(6, 7);
        assertEquals(6, num1.getReal());
        assertEquals(7, num1.getImaginary());

        num2.setValue(4, 3);
        assertEquals(4, num2.getReal());
        assertEquals(3, num2.getImaginary());

        num3 = num1.add(num2);
        assertEquals(10, num3.getReal());
        assertEquals(10, num3.getImaginary());

        assertEquals(10, (num3.multiplyBy(num2)).getReal());
        assertEquals(70, (num3.multiplyBy(num2)).getImaginary());

        assertEquals(-10, (num3.multiplyBy(num1)).getReal());
        assertEquals(130, (num3.multiplyBy(num1)).getImaginary());

        assertEquals(23, (num4.multiplyBy(num1)).getReal());
        assertEquals(-44, (num4.multiplyBy(num1)).getImaginary());

        assertEquals((26.0 / 17), (num3.divideBy(num1)).getReal());
        assertEquals((-2.0 / 17), (num3.divideBy(num1)).getImaginary());

        assertEquals((-0.92), (num4.divideBy(num2)).getReal());
        assertEquals((-0.56), (num4.divideBy(num2)).getImaginary());

    }

    @Test
    public void TestRealOrImgToString() {
        ComplexNumber num3;
        ComplexNumber num4 = new ComplexNumber(-2, 0);
        ComplexNumber num5 = new ComplexNumber(-6, -4);

        num1.setValue(6, 7);
        assertEquals(6, num1.getReal());
        assertEquals(7, num1.getImaginary());

        num2.setValue(4, 3);
        assertEquals(4, num2.getReal());
        assertEquals(3, num2.getImaginary());

        num3 = num1.add(num2);
        assertEquals(10, num3.getReal());
        assertEquals(10, num3.getImaginary());

        assertTrue(num3.isImg());
        assertTrue(num4.isReal());
        assertFalse(num3.isReal());
        assertFalse(num4.isImg());

        assertEquals("10.0+10.0i", num3.toString());
        assertEquals("-2.0+0.0i", num4.toString());
        assertEquals("-6.0-4.0i", num5.toString());
    }

    @Test
    public void TestEquals() {
        ComplexNumber num3 = new ComplexNumber();
        ComplexNumber num4 = new ComplexNumber();
        ComplexNumber num5 = new ComplexNumber(4, -3);

        num1.setValue(6, 7);
        assertEquals(6, num1.getReal());
        assertEquals(7, num1.getImaginary());

        num2.setValue(4, 3);
        assertEquals(4, num2.getReal());
        assertEquals(3, num2.getImaginary());

        num3.setValue(10, 10);
        num4.setValue(4, 3);
        assertEquals(10, num3.getReal());
        assertEquals(10, num3.getImaginary());

        num3.setValue(10, 10);
        assertFalse(num2.equals(num3));
        assertFalse(num2.equals(num5));
        assertTrue(num2.equals(num4));

        assertTrue(num2.hashCode() == num4.hashCode());
        assertFalse(num2.hashCode() == num5.hashCode());

    }

    @Test
    public void TestCompareToAndstr() {
        ComplexNumber num1 = new ComplexNumber(5, 6);
        ComplexNumber num2 = new ComplexNumber(5, 6);
        ComplexNumber num3 = new ComplexNumber(4, 3);
        ComplexNumber num4 = new ComplexNumber(4, -4);
        ComplexNumber num5 = new ComplexNumber(5, -4);
        ComplexNumber num6 = new ComplexNumber(5, 2);

        assertEquals(0, num1.compareTo(num2));
        assertEquals(1, num1.compareTo(num3));
        assertEquals(-1, num3.compareTo(num4));
        assertEquals(-1, num5.compareTo(num2));
        assertEquals(1, num2.compareTo(num6));

        String str1 = "5+6i";
        String str2 = "5-6i";

        assertEquals(5, (ComplexNumber.parse(str1)).getReal());
        assertEquals(6, (ComplexNumber.parse(str1)).getImaginary());
        assertEquals("5.0+6.0i", (ComplexNumber.parse(str1)).toString());

        assertEquals(5, (ComplexNumber.parse(str2)).getReal());
        assertEquals(-6, (ComplexNumber.parse(str2)).getImaginary());
        assertEquals("5.0-6.0i", (ComplexNumber.parse(str2)).toString());

    }


}
