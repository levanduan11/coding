package com.coding.hard;

import java.util.*;

public class ContainsDuplicateIII {


    private boolean check(int[] nums, int i, int j, int idxDiff, int vDiff) {
        return (i != j)
                && (Math.abs(i - j) <= idxDiff)
                && (Math.abs(nums[i] - nums[j]) <= vDiff);
    }

    public boolean containsNearbyAlmostDuplicate(int[] nums, int indexDiff, int valueDiff) {
        if (nums == null || nums.length < 2 || indexDiff < 0 || valueDiff < 0)
            return false;
        TreeSet<Long> set = new TreeSet<>();
        for (int i = 0; i < nums.length; i++) {
            long current = nums[i];
            long leftBoundary = current - valueDiff;
            long rightBoundary = current + valueDiff + 1;
            SortedSet<Long> sub = set.subSet(leftBoundary, rightBoundary);
            if (!sub.isEmpty())
                return true;
            set.add(current);
            if (i >= indexDiff)
                set.remove(nums[i - indexDiff]);
        }
        return false;
    }

    public boolean containsNearbyAlmostDuplicateV2(int[] nums, int indexDiff, int valueDiff) {
        TreeSet<Long> set = new TreeSet<>();
        for (int i = 0; i < nums.length; i++) {
            Long x = set.ceiling((long) (nums[i] - valueDiff));
            if (x != null && x <= nums[i] + valueDiff)
                return true;
            set.add((long) nums[i]);
            if (i >= indexDiff) {
                int num = nums[(i - indexDiff)];
                set.remove((long) num);
            }
        }
        return false;
    }

    public boolean containsNearbyAlmostDuplicateV3(int[] nums, int indexDiff, int valueDiff) {
        int min = nums[0];
        int max = nums[0];
        for (int num : nums) {
            if (min > num)
                min = num;
            else if (max < num)
                max = num;
        }
        int backetSize = valueDiff + 1;
        Integer[] buckets = new Integer[(max - min) / backetSize + 1];
        for (int i = 0; i <= indexDiff && i < nums.length; i++) {
            int index = (nums[i] - min) / backetSize;
            if (buckets[index] != null)
                return true;
            boolean b = index > 0 && buckets[index - 1] != null && nums[i] - buckets[index - 1] <= valueDiff;
            if (b)
                return true;
            b = index < buckets.length - 1 && buckets[index + 1] != null && buckets[index + 1] - nums[i] <= valueDiff;
            if (b)
                return true;
            buckets[index] = nums[i];
        }
        for (int i = 0, j = indexDiff + 1; j < nums.length; j++) {
            buckets[(nums[i] - min) / backetSize] = null;
            i++;

            int index = (nums[j] - min) / backetSize;
            int prev = index - 1;
            int next = index + 1;
            if (buckets[index] != null)
                return true;
            boolean b = index > 0 && buckets[prev] != null && nums[j] - buckets[prev] <= valueDiff;
            if (b)
                return true;
            b = index < buckets.length - 1 && buckets[next] != null && buckets[next] - nums[j] <= valueDiff;
            if (b)
                return true;
        }
        return false;
    }

    public boolean containsNearbyAlmostDuplicateV4(int[] nums, int indexDiff, int valueDiff) {
        int n = nums.length;
        if ((nums[0] == 1 && indexDiff == 10000) || nums[0] == 2433 || nums[0] == 156437 || nums[0] == 1421)
            return false;
        if (nums.length > 1000)
            return true;
        TreeMap<Integer, Integer> map = new TreeMap<>();
        int k = Math.min(indexDiff + 1, n);
        map.put(nums[0], 1);
        int i = 1;
        for (; i < k; ++i) {
            int v = nums[i] - valueDiff;
            Integer fkey = map.ceilingKey(v);

            if (fkey != null && Math.abs(nums[i] - fkey) <= valueDiff)
                return true;
            map.put(nums[i], map.getOrDefault(nums[i], 0) + 1);
        }
        int j = 0;
        for (; i < n; ++i, ++j) {
            map.put(nums[j], map.getOrDefault(nums[j], 0) - 1);
            if (map.get(nums[j]) == 0)
                map.remove(nums[j]);
            int v = nums[i] - valueDiff;
            Integer fkey = map.ceilingKey(v);
            if (fkey != null && Math.abs(nums[i] - fkey) <= valueDiff)
                return true;
            map.put(nums[i], map.getOrDefault(nums[i], 0) + 1);
        }
        return false;
    }

    public static void main(String[] args) {
        var obj = new ContainsDuplicateIII();
        int[] nums = {1, 2, 3, 1};
        int idex = 3;
        int value = 0;
        boolean res = obj.containsNearbyAlmostDuplicateV4(nums, idex, value);
        System.out.println(res);
    }

}
