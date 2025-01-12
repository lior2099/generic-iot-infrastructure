/*
 FileName: DisplayUser.java
 Author: Lior Shalom
 Date: 21/07/24
 reviewer: Maya
*/


package il.co.ilrd.vending_machine;

public class DisplayUser implements Display {
    @Override
    public void write(String str) {
        System.out.println(str);
    }
}
