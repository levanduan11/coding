package com.coding.datastructure.array;

public class DopeVector<T> {
    private final T[] array;
    private final int elementSize;
    private final int dimensions;
    private final int[] extents;

    public DopeVector(T[] inputArray, int elementSize) {
        this.array = inputArray;
        this.elementSize = elementSize;
        this.dimensions = 1;
        this.extents = new int[inputArray.length];
    }

    public T get(int index) {
        if (index < 0 || index >= extents[0])
            throw new ArrayIndexOutOfBoundsException("index out of bounds");
        return array[index];
    }

    public void set(int index, T value) {
        if (index < 0 || index >= extents[0])
            throw new ArrayIndexOutOfBoundsException("index out of bounds");
        array[index] = value;
    }

    public int length() {
        return extents[0];
    }

    public int size() {
        return elementSize;
    }

    public int dimensions() {
        return dimensions;
    }

    public int[] extents() {
        return extents;
    }
}
