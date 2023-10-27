package com.coding;

import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) throws Throwable {
        TreeMap<Double, Integer> map = new TreeMap<>(Map.of(
                1.0, 1, 2.0, 2, 3.0, 3, 4.0, 4, 5.0, 5, 5.5, 15,
                6.0, 6, 7.0, 7, 8.0, 8, 9.5, 9
        ));

    }

    static int[] delDup(int[] arr) {
        int max = Arrays.stream(arr).max().orElse(Integer.MAX_VALUE);
        long[] masks = nBits(max + 1);
        for (int a : arr) {
            setBit(masks, a);
        }
        int c = 0;
        for (int i = 0, n = arr.length; i < n; i++) {
            if (check(masks, arr[i])) {
                arr[c++] = arr[i];
                clear(masks, arr[i]);
            }
        }
        return Arrays.copyOf(arr, c);
    }

    static long[] nBits(int n) {
        if (n < 1)
            throw new IllegalStateException();
        return new long[(n >> 6) + 1];
    }

    static void setBit(long[] bits, int index) {
        bits[index >> 6] |= (1L << index);
    }

    static void clear(long[] bits, int i) {
        bits[i >> 6] &= ~(1L << i);
    }

    static boolean check(long[] bits, int i) {
        return (bits[i >> 6] & (1L << i)) != 0;
    }
}

class List<E> extends ArrayList<E> {
    public List(java.util.List<E> list) {
        super(list);
    }

    public List() {
    }

    @SuppressWarnings("unchecked")
    public <R> List<R> map(Function<? super E, ? extends R> fn) {
        Object[] elementData = toArray();
        List<R> res = new List<>();
        for (Object elementDatum : elementData) {
            E e = (E) elementDatum;
            R r = fn.apply(e);
            res.add(r);
        }
        return res;
    }
}