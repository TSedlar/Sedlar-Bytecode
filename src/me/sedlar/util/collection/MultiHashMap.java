package me.sedlar.util.collection;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 * @since Oct 13, 2014 - 6:13 PM
 */
public class MultiHashMap<K, V> extends HashMap<K, List<V>> {

    /**
     * Gets the first value for the given key.
     *
     * @param key the key to grab from.
     * @return the first value for the given key.
     */
    public V first(String key) {
        return containsKey(key) ? get(key).get(0) : null;
    }

    /**
     * Puts a single value into the map.
     *
     * @param key the key to place into.
     * @param value the value to place into the map.
     * @return the inputted value.
     */
    public V add(K key, V value) {
        if (containsKey(key)) {
            get(key).add(value);
        } else {
            List<V> list = new LinkedList<>();
            list.add(value);
            super.put(key, list);
        }
        return value;
    }
}
