/*
 FileName: SingletonDouble.java
 Author: Lior Shalom
 Date: 31/07/24
 reviewer:Maya
*/
package singeton;

public class SingletonDouble {
    private static volatile SingletonDouble instance = null;

    private SingletonDouble() {
    }

    public static SingletonDouble getInstance() {
        if (instance == null) {
            synchronized (SingletonDouble.class) {
                if (instance == null) {
                    instance = new SingletonDouble();
                }
            }
        }
        return instance;
    }


}