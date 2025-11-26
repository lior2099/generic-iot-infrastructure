
/*
 FileName: HashMap.java
 Author: Lior Shalom
 Date: 7/08/24
 reviewer: Lior
*/


package hash_map;

import il.co.ilrd.pair.Pair;

import java.util.*;
import java.util.Map;

/**
 * Geniric HashMap
 * DEFAULT_INITIAL_CAPACITY = 16 , DEFAULT_LOAD_FACTOR = 0.75F
 *
 * @param <K>
 * @param <V>
 */
public class ILRDHashMap<K extends Comparable<? super K>, V> implements Map<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75F;

    private List<List<Entry<K, V>>> buckets;
    private int capacity;
    private final float loadFactor;
    private int size;
    private int modCount;
    private Set<K> keySet = null;
    private Collection<V> values = null;
    private Set<Entry<K, V>> entrySet = null;

    public ILRDHashMap() {
        this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public ILRDHashMap(float loadFactor) {
        this(DEFAULT_INITIAL_CAPACITY, loadFactor);
    }

    public ILRDHashMap(int capacity) {
        this(capacity, DEFAULT_LOAD_FACTOR);
    }

    public ILRDHashMap(int capacity, float loadFactor) {
        if (0 > capacity || 0 >= loadFactor) {
            throw new IllegalArgumentException("Negative num");
        }
        capacity = Math.max(DEFAULT_INITIAL_CAPACITY, capacity);
        this.capacity = capacity;
        this.loadFactor = loadFactor;


        buckets = new ArrayList<>(this.capacity);

        while (capacity-- > 0) {
            buckets.add(new LinkedList<>());
        }
    }

    public ILRDHashMap(Map<? extends K, ? extends V> map) {
        this(map.size(), DEFAULT_LOAD_FACTOR);
        putAll(map);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return 0 == size;
    }

    @Override
    public boolean containsKey(Object o) {
        int index = getIndex(o);

        Entry<K, V> node = findEntry(o);

        node = findEntry(o);

        return node != null;
    }

    @Override
    public boolean containsValue(Object o) {
        return values().contains(o);
    }

    @Override
    public V get(Object key) {

        Entry<K, V> node = findEntry(key);

        if (null != node){
            return node.getValue();
        }

        return null;
    }

    @Override
    public V put(K k, V v) {
        modCount++;
        int index = getIndex(k);

        V dataReturn = remove(k);

        rehash();

        buckets.get(index).add(new Pair<>(k, v));
        size++;

        return dataReturn;
    }

    @Override
    public V remove(Object o) {
        int index = getIndex(o);

        Iterator<Entry<K, V>> listIter = buckets.get(index).iterator();
        Entry<K, V> node = null;

        while (listIter.hasNext()) {  //method
            node = listIter.next();
            if (node.getKey() == null ? null == o : node.getKey().equals(o)) {
                V toSendBack = node.getValue();
                listIter.remove();
                size--;
                modCount++;
                return toSendBack;
            }
        }
        return null;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> map) {
        for (Entry<? extends K, ? extends V> elem : map.entrySet()) {
            put(elem.getKey(), elem.getValue());
        }
    }

    @Override
    public void clear() {
        for (List<Entry<K, V>> list : buckets) {
            list.clear();
        }

        this.size = 0;
        modCount++;
    }

    @Override
    public Set<K> keySet() {
        if (keySet == null){
            keySet = new KeySet();
        }
        return keySet;
    }

    @Override
    public Collection<V> values() {
        if (values == null){
            values = new Values();
        }
        return values;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        if (entrySet == null){
            entrySet = new EntrySet();
        }
        return entrySet;

    }

    private void rehash() {
        List<List<Entry<K, V>>> oldBuckets = buckets;

        if (size < loadFactor * capacity) {
            return;
        }

        capacity <<= 1;
        size = 0;
        buckets = new ArrayList<>(capacity);

        for (int i = 0; i < capacity; i++) {    // method
            buckets.add(i, new LinkedList<>());
        }

        for (List<Entry<K, V>> list : oldBuckets) {
            for (Entry<K, V> elem : list)
                put(elem.getKey(), elem.getValue());
        }
    }

    private Entry<K, V> findEntry(Object key){

        int index = getIndex(key);

        for (Entry<K, V> node : buckets.get(index)) {
            if (null == node.getKey() ? null == key : node.getKey().equals(key)) {
                return node;
            }
        }
        return null;
    }

    private int getIndex(Object o) {
        if (o == null) {
            return 0;
        }
        return Math.abs(o.hashCode() % capacity);
    }

//    public int getCapacity(){
//        return capacity;
//    }


    private class EntrySet extends AbstractSet<Entry<K, V>> {
        @Override
        public Iterator<Entry<K, V>> iterator() {
            return new EntryIterator();
        }

        @Override
        public int size() {
            return size;
        }

        private class EntryIterator implements Iterator<Entry<K, V>> {
            private final Iterator<List<Entry<K, V>>> outerIter;
            private Iterator<Entry<K, V>> innerOuter;
            private final int initialModCount;

            private EntryIterator() {
                initialModCount = modCount;
                outerIter = buckets.iterator();
                innerOuter = outerIter.next().iterator();
            }

            @Override
            public boolean hasNext() {
                advanceOuter();
                return innerOuter.hasNext();
            }

            @Override
            public Entry<K, V> next() {
                if (initialModCount != modCount) {
                    throw new ConcurrentModificationException();
                }
                advanceOuter();
                return innerOuter.next();
            }

            private void advanceOuter(){
                while (!innerOuter.hasNext() && outerIter.hasNext()){
                    innerOuter = outerIter.next().iterator();
                }
            }
        }
    }

    private class KeySet extends AbstractSet<K> {

        @Override
        public Iterator<K> iterator() {
            return new KeyIterator();
        }

        @Override
        public int size() {
            return size;
        }

        private class KeyIterator implements Iterator<K> {
            private final Iterator<Entry<K, V>> entryIter;


            private KeyIterator() {
                entryIter = entrySet().iterator();
            }

            @Override
            public boolean hasNext() {
                return entryIter.hasNext();
            }

            @Override
            public K next() {
                return entryIter.next().getKey();
            }
        }
    }
    private class Values extends AbstractCollection<V> {

        @Override
        public Iterator<V> iterator() {
            return new ValueIterator();
        }

        @Override
        public int size() {
            return size;
        }

        private class ValueIterator implements Iterator<V> {
            private final Iterator<Entry<K, V>> entryIter;


            private ValueIterator() {
                entryIter = entrySet().iterator();
            }

            @Override
            public boolean hasNext() {
                return entryIter.hasNext();
            }

            @Override
            public V next() {
                return entryIter.next().getValue();
            }
        }
    }

}
