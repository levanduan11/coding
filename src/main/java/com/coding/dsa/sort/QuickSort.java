package com.coding.dsa.sort;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

public class QuickSort {
    static <T> void swap(int i, int j, T[] arr) {
        T temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    static <T> void sort(T[] arr, Comparator<? super T> comparator) {
        Objects.requireNonNull(comparator);
        sort(0, arr.length - 1, arr, comparator);
    }

    static <T extends Comparable<T>> void sort(T[] arr) {
        sort(0, arr.length - 1, arr);
    }

    static <T> void sort(int l, int h, T[] arr, Comparator<? super T> comparator) {
        if (l >= h) return;
        T pivot = arr[h];
        int i = (l - 1);
        for (int j = l; j < h; j++) {
            int c = comparator.compare(arr[j], pivot);
            if (c < 0) {
                i++;
                swap(i, j, arr);
            }
        }
        swap(i + 1, h, arr);
        sort(l, i, arr, comparator);
        sort(i + 1, h, arr, comparator);
    }

    static <T extends Comparable<T>> void sort(int l, int h, T[] arr) {
        if (l >= h) return;
        T pivot = arr[h];
        int i = (l - 1);
        for (int j = l; j < h; j++) {
            Comparable<? super T> comparable = arr[j];
            if (comparable.compareTo(pivot) < 0) {
                i++;
                swap(i, j, arr);
            }
        }
        swap(i + 1, h, arr);
        sort(l, i, arr);
        sort(i + 1, h, arr);
    }

    public static void main(String[] args) {
        Integer[] arr = {9, 8, 7, 6, 5, 0, 1, 2, 3, 10, 11, 12};
        sort(0, arr.length - 1, arr);
        Person[] people = {
                new Person(9, "nguyen van a"),
                new Person(8, "nguyen van c"),
                new Person(7, "nguyen van b")
        };
        sort(people, Comparator.comparing(Person::name));
        String[] strings = {"z", "d", "c", "b", "a"};
        sort(strings);
        System.out.println(Arrays.toString(strings));
    }
}

record Person(int id, String name) implements Comparable<Person> {
    @Override
    public int compareTo(Person o) {
        return 0;
    }

    @Override
    public String toString() {
        return name + " : " + id;
    }
}