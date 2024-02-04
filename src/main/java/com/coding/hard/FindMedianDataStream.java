package com.coding.hard;

import com.coding.InputReader;
import com.coding.OutputWriter;

import java.util.ArrayList;
import java.util.List;

class MedianFinder {
    int[] numbers;
    private static final int INITIAL_CAP = 1000;
    private int size;

    public MedianFinder() {
        numbers = new int[INITIAL_CAP];
        size = 0;
    }

    public void addNum(int num) {
        int n = numbers.length;
        if (size == n) {
            int newCapacity = n * 2;
            int[] newArray = new int[newCapacity];
            System.arraycopy(numbers, 0, newArray, 0, n);
            numbers = newArray;
        }
        if (size == 0) {
            numbers[size++] = num;
        } else {
            insertSort(numbers, num);
        }

    }

    private void insertSort(int[] numbers, int num) {
        int i;
        for (i = size; i >= 1; ) {
            int n = numbers[i - 1];
            if (num < n) {
                numbers[i] = numbers[i - 1];
                i--;
            } else {
                numbers[i] = num;
                size++;
                break;
            }
        }
        if (i == 0) {
            numbers[i] = num;
            size++;
        }
    }

    public double findMedian() {
        int len = size;
        int mid = len / 2;
        if (len % 2 == 0) {
            int prev = mid - 1;
            return (double) (numbers[mid] + numbers[prev]) / 2;
        } else {
            return numbers[mid];
        }

    }
}

public class FindMedianDataStream {
    public static void main(String[] args) {
        MedianFinder medianFinder = new MedianFinder();
        String[] inputs = InputReader.inputAsStrings("295.txt")
                .toArray(String[]::new);
        String[] actions = inputs[0].substring(1, inputs[0].length() - 1).split(",");
        String[] strings = inputs[1].split(",");
        List<Tuple2> list = zip(actions, strings);
        StringBuilder out = new StringBuilder();
        list.forEach(tuple2 -> {
            String t1 = tuple2._1();
            if (t1.equals("\"addNum\"")) {
                medianFinder.addNum(tuple2._2());
            } else if (t1.equals("\"findMedian\"")) {
                out.append(medianFinder.findMedian());
                out.append(System.lineSeparator());
            }
        });
        OutputWriter.write(out.toString(), "295.txt");
    }

    static List<Tuple2> zip(String[] arr1, String[] arr2) {
        List<Tuple2> res = new ArrayList<>();
        for (int i = 0; i < arr1.length; i++) {
            String s = arr1[i];
            String v = arr2[i];

            if (v.equals("[]")) {
                res.add(new Tuple2(s, null));
                continue;
            }
            int n = 0;
            for (char c : v.toCharArray()) {
                if (c == '[' || c == ']' || c == ' ')
                    continue;
                n = n * 10 + (c - '0');
            }
            Tuple2 tuple2 = new Tuple2(s, n);
            res.add(tuple2);
        }
        return res;
    }
}

record Tuple2(String _1, Integer _2) {
}