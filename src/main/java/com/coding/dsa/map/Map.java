package com.coding.dsa.map;

import java.util.Collection;
import java.util.function.Consumer;

public interface Map<K, V> {
    V put(K key, V value);

    V get(K key);

    void forEach(Consumer<? super Entry<K,V>>consumer);
    Collection<K>keys();
    Collection<V>values();
    boolean contains(K k);

    V getOrDefault(K key,V defaultValue);

    interface Entry<K, V> {
        K getKey();

        V getValue();

        V setValue(V v);

    }
}
