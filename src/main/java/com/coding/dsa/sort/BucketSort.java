package com.coding.dsa.sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BucketSort {
    public static void sort(double[] array) {
        int n = array.length;
        if (n < 1)
            return;
        List<List<Double>> buckets = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            buckets.add(new ArrayList<>());
        }
        for (double v : array) {
            int bucketIndex = (int) (n * v);
            buckets.get(bucketIndex).add(v);
        }
        for (List<Double> bucket : buckets) {
            bucket.sort(Double::compareTo);
        }
        int idx = 0;
        for (List<Double> bucket : buckets) {
            for (Double value : bucket) {
                array[idx++] = value;
            }
        }
    }

    public static void main(String[] args) {
        double[] array = {0.8, 0.3, 0.2, 0.7, 0.1, 0.6, 0.5, 0.4};
        sort(array);
        System.out.println(Arrays.toString(array));
    }
}
