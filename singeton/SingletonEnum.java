
/*
 FileName: SingletonEnum.java
 Author: Lior Shalom
 Date: 31/07/24
 reviewer:Maya
*/

package singeton;

public enum SingletonEnum {
    INSTANCE;

    public static SingletonEnum getInstance() {
        return (INSTANCE);
    }
}

