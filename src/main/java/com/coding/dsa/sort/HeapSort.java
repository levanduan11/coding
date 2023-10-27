package dsa.sort;

import java.util.Arrays;

public class HeapSort {
    void heapify(int[] arr, int N, int i) {
        int largest = i;
        int l = 2 * i + 1;
        int r = l + 1;
        if (l < N && arr[l] > arr[largest])
            largest = l;
        if (r < N && arr[r] > arr[largest])
            largest = r;
        if (largest != i) {
            int swap = arr[i];
            arr[i] = arr[largest];
            arr[largest] = swap;
            heapify(arr, N, largest);
        }
    }

    public void sort(int[] arr) {
        int N = arr.length;
        for (int i = N / 2 - 1; i >= 0; i--) {
            heapify(arr, N, i);
        }
        for (int i = N - 1; i >= 0; i--) {
            int temp = arr[0];
            arr[0] = arr[i];
            arr[i] = temp;

            heapify(arr, i, 0);
        }
    }

    public static void main(String[] args) {
        int[]arr={12,11,13,5,6,7};
        int N = arr.length;
        var ob =new HeapSort();
        ob.sort(arr);
        System.out.println(Arrays.toString(arr));
    }
}
