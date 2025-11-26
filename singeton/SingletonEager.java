/*
 FileName: SingletonEager.java
 Author: Lior Shalom
 Date: 31/07/24
 reviewer:Maya
*/

package singeton;

public class SingletonEager {
    private static final SingletonEager instance = new SingletonEager();

    private SingletonEager() {
    }

    public static SingletonEager getInstance() {
        return instance;
    }
}