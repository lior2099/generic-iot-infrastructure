/*
 FileName: designFactory.java
 Author: Lior Shalom
 Date: 14/08/24
 reviewer: Haim
*/

package il.co.ilrd.factory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Factory<K, T, D> {

    private final Map<K, Function<D, ? extends T>> funcMap = new HashMap<>();

    public T create(K key) {
        return create(key, null);
    }

    public T create(K key, D parameter) {
        return funcMap.get(key).apply(parameter);
    }

    public void add(K key, Function<D, ? extends T> func) {
        funcMap.put(key, func);
    }

}