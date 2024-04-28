package com.coding.datastructure.array;


public class CircularBuffer {
    private final int[] buffer;
    private int head;
    private int tail;
    private int count;

    public CircularBuffer(int capacity) {
        buffer = new int[capacity];
        head = 0;
        tail = 0;
        count = 0;
    }

    public boolean offer(int value) {
        if (count == buffer.length) {
            return false;
        }
        buffer[tail] = value;
        tail = (tail + 1) % buffer.length;
        count++;
        return true;
    }

    public Integer poll() {
        if (count == 0)
            return null;
        int value = buffer[head];
        head = (head + 1) % buffer.length;
        count--;
        return value;
    }

    public int size() {
        return count;
    }

    public boolean isEmpty() {
        return count == 0;
    }
}
