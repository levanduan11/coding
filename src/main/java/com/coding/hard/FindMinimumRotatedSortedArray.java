package com.coding.hard;

import java.util.Arrays;

public class FindMinimumRotatedSortedArray {
    public int findMin(int[] nums) {
        return Arrays.stream(nums).reduce(Integer::min).orElse(Integer.MAX_VALUE);
    }
    public static void main(String[] args) {
        var o = new FindMinimumRotatedSortedArray();
        int[]nums = {1,3,5};
        int res = o.findMin(nums);
        System.out.println(res);
    }
}
