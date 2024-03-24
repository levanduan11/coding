package com.coding.hard;

import java.util.Arrays;

public class MedianTwoSortedArrays {
    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        int len = nums1.length + nums2.length;
        if (len == 0) return 0.0;
        int[] mergedArr = new int[len];
        for (int k = 0, j = 0, i = 0; k < len; k++) {
            if (i >= nums1.length) {
                mergedArr[k] = nums2[j++];
            } else if (j >= nums2.length) {
                mergedArr[k] = nums1[i++];
            } else if (nums1[i] < nums2[j]) {
                mergedArr[k] = nums1[i++];
            } else {
                mergedArr[k] = nums2[j++];
            }
        }

        if (len % 2 != 0) {
            int mid = len / 2;
            return mergedArr[mid];
        } else {
            int m1=len/2;
            int m2=m1-1;
            return (double) (mergedArr[m1] + mergedArr[m2]) /2;
        }
    }

    public static void main(String[] args) {
        int[] nums1 = {1, 2};
        int[] nums2 = {3,4};
        var o = new MedianTwoSortedArrays();
        System.out.println(o.findMedianSortedArrays(nums1, nums2));
    }
}
