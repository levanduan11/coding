package dsa.map;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.IntStream;

public class HashMap<K, V> implements Map<K, V> {
    private final Node<K, V>[] table;
    private static final int DEFAULT_CAPACITY = 8;
    private static final float THRESHOLD = 0.75f;
    private int size;

    @SuppressWarnings("unchecked")
    public HashMap(int len) {
        if (len <= 0 || len >= (Integer.MAX_VALUE >> 1)) {
            throw new IllegalArgumentException("Invalid length");
        }
        this.table = (Node<K, V>[]) new Node[len];
    }

    static class Node<K, V> implements Map.Entry<K, V> {
        K key;
        V value;
        Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
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
        public boolean equals(Object o) {
            if (this == o) return true;
            return o instanceof Map.Entry<?, ?> e
                    && Objects.equals(key, e.getKey())
                    && Objects.equals(value, e.getValue());
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(key) ^ Objects.hashCode(value);
        }

        @Override
        public V setValue(V v) {
            V old = this.value;
            this.value = v;
            return old;
        }
    }

    @Override
    public V put(K key, V value) {
        int len = table.length;
        if (size >= (THRESHOLD * len)) {
            grow();
        }
        V oldValue = get(key);
        int index = hash(key);
        Node<K, V> head = table[index];
        if (head == null) {
            table[index] = new Node<>(key, value);
            size++;
        } else {
            Node<K, V> next = head;
            Node<K, V> pre = next;
            exist_key:
            {
                for (; next != null; next = next.next) {
                    if (Objects.equals(next.getKey(), key)) {
                        next.setValue(value);
                        break exist_key;
                    }
                    pre = next;
                }
                pre.next = new Node<>(key, value);
                size++;
            }
        }
        return oldValue;
    }

    private void grow() {
        // Todo
    }

    @Override
    public V get(K key) {
        int index = hash(key);
        for (Node<K, V> current = table[index]; current != null; current = current.next) {
            if (current.getKey().equals(key)) {
                return current.getValue();
            }
        }
        return null;
    }

    public boolean contains(K k) {
        return get(k) != null;
    }

    @Override
    public V getOrDefault(K key, V defaultValue) {
        V v = get(key);
        return v != null ? v : defaultValue;
    }


    private int hash(K k) {
        int len = table.length;
        return Math.abs(Objects.hashCode(k)) % len;
    }

    public void forEach(Consumer<? super Map.Entry<K, V>> consumer) {
        for (Node<K, V> node : table) {
            for (Node<K, V> current = node; current != null; current = current.next) {
                consumer.accept(current);
            }
        }
    }

    public Collection<V> values() {
        List<V> res = new ArrayList<>();
        forEach(con -> res.add(con.getValue()));
        return res;
    }

    public Collection<K> keys() {
        List<K> res = new ArrayList<>();
        forEach(con -> res.add(con.getKey()));
        return res;
    }


    public static void main(String[] args) {
        Map<Boolean, List<Integer>> map0 = new HashMap<>(10);
        int[] source = IntStream.range(1, 10).toArray();
        for (int i : source) {
            boolean isOld = (i & 1) == 0;
            List<Integer> list = map0.getOrDefault(isOld, new ArrayList<>());
            list.add(i);
            map0.put(isOld, list);
        }
        Map<Person,String>map=new HashMap<>(10);
        map.put(new Person(1,"nguyen van a"),"nguyen van a");
        map.put(new Person(1,"nguyen van a"),"nguyen van a");
        map.put(new Person(1,"nguyen van a"),"nguyen van a");
        map.put(new Person(1,"nguyen van d"),"nguyen van d");
        System.out.println(map);
    }

}
record Person(int id,String name){
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person other = (Person) o;
        return other.id == id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}