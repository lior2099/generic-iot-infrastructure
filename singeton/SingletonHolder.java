
/*
 FileName: SingletonHolder.java
 Author: Lior Shalom
 Date: 31/07/24
 reviewer:Maya
*/

package singeton;

public class SingletonHolder {
    private static final class SingHolder {
        private static final SingletonHolder INSTANCE = new SingletonHolder();
    }

    private SingletonHolder() {
    }

    public static SingletonHolder getInstance() {
        return SingHolder.INSTANCE;
    }
}

