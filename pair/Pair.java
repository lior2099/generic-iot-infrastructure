/*
 FileName: Pair.java
 Author: Lior Shalom
 Date: 18/07/24
 reviewer: lior
*/

package pair;

import java.util.Comparator;
import java.util.Map;

public class Pair<K, V> implements Map.Entry<K, V> {
    private final K key;
    private V value;

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Pair)) {
            return false;
        }

        Pair<?, ?> that = (Pair<?, ?>) obj;
        if (that.key == null || that.value == null) {
            return false;
        }
        return key.equals(that.key)
                && value.equals(that.value);

    }

    @Override
    public int hashCode() {
//        return Objects.hash(getKey(), getValue());
        return (key.hashCode() ^ value.hashCode());
    }

    @Override
    public K getKey() {
        return key;
    }

    @Override
    public V getValue() {
        return value;
    }

    @Override
    public V setValue(V value) {
        V toSend = this.value;
        this.value = value;

        return toSend;
    }

    public static <E extends Comparable<E>> Pair<E, E> minMax(E[] arr) {

        return Pair.minMax(arr, E::compareTo);
    }

    public static <E> Pair<E, E> minMax(E[] arr, Comparator<E> comp) {
        E min = arr[0];
        E max = arr[0];
        int i = 0;

        if (arr.length % 2 != 0) {
            ++i;
        }
        for (; i < arr.length - 1; i += 2) {
            if (comp.compare(arr[i], arr[i + 1]) > 0) {
                if (comp.compare(max, arr[i]) < 0) {
                    max = arr[i];
                }
                if (comp.compare(min, arr[i + 1]) > 0) {
                    min = arr[i + 1];
                }
            } else {
                if (comp.compare(min, arr[i]) > 0) {
                    min = arr[i];
                }
                if (comp.compare(max, arr[i + 1]) < 0) {
                    max = arr[i + 1];
                }
            }

        }
        return of(min, max);
    }

    public static <K, V> Pair<V, K> swap(Pair<K, V> pair) {
        return of(pair.value, pair.key);
    }

    public static <K, V> Pair<K, V> of(K key, V value) {
        return new Pair<>(key, value);
    }
}
