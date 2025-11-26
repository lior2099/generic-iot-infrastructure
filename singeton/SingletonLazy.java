/*
 FileName: SingletonLazy.java
 Author: Lior Shalom
 Date: 31/07/24
 reviewer:Maya
*/

package singeton;

public final class SingletonLazy {
    private static SingletonLazy instance;

    private SingletonLazy() {
    }

    public static SingletonLazy getInstance() {
        if (instance == null) {
            instance = new SingletonLazy();
        }
        return instance;
    }

}