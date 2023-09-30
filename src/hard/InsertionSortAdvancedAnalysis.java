package hard;

import java.util.Scanner;


public class InsertionSortAdvancedAnalysis {
    static int count = 0;

    public static int insertionSort(int[] arr) {
        count = 0;
        mergeSort(arr, 0, arr.length - 1);
        return count;
    }

    static void mergeSort(int[] arr, int p, int r) {
        int q = (p + r) >>> 1;
        if (p < r) {
            mergeSort(arr, p, q);
            mergeSort(arr, q + 1, r);
            merge(arr, p, q, r);
        }
    }

    static void merge(int[] arr, int p, int q, int r) {
        int[] left = new int[q - p + 1];
        int[] right = new int[r - q];
        for (int i = 0, n = left.length; i < n; i++) {
            left[i] = arr[p + i];
        }
        for (int i = 0, n = right.length; i < n; i++) {
            right[i] = arr[q + i + 1];
        }
        int i = 0, j = 0;
        for (int k = p, n = left.length, m = right.length; k <= r; k++) {
            if (i < n && j < m) {
                if (left[i] < right[j]) {
                    arr[k] = left[i++];
                } else {
                    arr[k] = right[j++];
                    count += n - i;
                }
            } else {
                if (i < n)
                    arr[k] = left[i++];
                else if (j < m)
                    arr[k] = right[j++];
            }
        }
    }

    static int getSum(int[] BITree, int index) {
        int sum = 0;
        while (index > 0) {
            sum += BITree[index];
            index = index - (index & (-index));
        }
        return sum;
    }

    static void updateBIT(int[] BITree, int n, int index, int val) {
        while (index <= n) {
            BITree[index] += val;
            index = index + (index & (-index));
        }
    }

    static long getInvCount(int[] arr, int n) {
        long invcount = 0;
        int maxElement = 0;
        for (int i = 0; i < n; i++) {
            if (maxElement < arr[i])
                maxElement = arr[i];
        }
        int[] BIT = new int[maxElement + 1];
        for (int i = 1; i <= maxElement; i++) {
            BIT[i] = 0;
        }
        for (int i = n - 1; i >= 0; i--) {
            invcount += getSum(BIT, arr[i] - 1);
            updateBIT(BIT, maxElement, arr[i], 1);
        }
        return invcount;
    }

    public static void main(String[] args) {
        int[] arr = {2, 1, 3, 1, 2};
        //System.out.println(getInvCount(arr,arr.length));
        System.out.println(37 - (37 & (-37)));
        System.out.println(Integer.toBinaryString(10));
        System.out.println(Integer.toBinaryString(-10));
        System.out.println(Integer.toBinaryString(10 & (-10)));
        Scanner s = new Scanner(System.in);
        int totalTime = s.nextInt();
        long[] inversion = new long[totalTime];
        int length = 0;
        for (int i = 1; i <= totalTime; i++) {
            int fromArray = s.nextInt();
            int[] data = new int[fromArray];
            length = data.length;
            for (int j = 0; j < fromArray; j++) {
                data[j] = s.nextInt();
            }
            inversion[i - 1] = getInvCount(data, length);
        }
        for (long l : inversion) {
            System.out.println(l);
        }
    }
}
