package com.coding.algorithm.generalcombinatorial;

import static java.util.stream.IntStream.range;

public class XorShift32 {
    private int state;

    public XorShift32(int seed) {
        if (seed == 0)
            throw new IllegalArgumentException();
        else
            state = seed;
    }

    public int nextInt() {
        state ^= (state << 13);
        state ^= (state >>> 17);
        state ^= (state << 5);
        return state;
    }

    public static void main(String[] args) {
        var o = new XorShift32(9);
        range(1, 100)
                .map(i -> o.nextInt())
                .forEach(System.out::println);
    }
}
