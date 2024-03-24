package com.coding.hard;

import com.coding.InputReader;
import com.coding.OutputWriter;

import java.util.*;
import java.util.stream.Collectors;

public class CountSmallerNumbersAfterSelf {
    public static List<Integer> countSmallerV4(int[] nums) {
        int len = nums.length;
        List<Integer> ans = new ArrayList<>();
        List<Integer> arr = new ArrayList<>();
        for (int num : nums) {
            arr.add(num);
        }
        arr.sort(Integer::compareTo);
        for (int num : nums) {
            int index = binarySearch(arr, num);
            ans.add(index);
            arr.remove(index);
        }
        return ans;
    }

    public static int binarySearch(List<Integer> arr, int target) {
        int s = 0;
        int e = arr.size() - 1;
        int mid = 0;
        while (s <= e) {
            mid = s + (e - s) / 2;
            int val = arr.get(mid);
            if (val < target)
                s = mid + 1;
            else
                e = mid - 1;
        }
        if (arr.get(s) == target)
            return s;
        else
            return mid;
    }

    public static List<Integer> countSmallerV3(int[] nums) {
        int min, max;
        min = max = nums[0];
        for (final int v : nums) {
            min = Math.min(min, v);
            max = Math.max(max, v);
        }
        final int delta = -min + 1;
        final int[] arr = new int[max + delta + 1];
        final int[] res = new int[nums.length];
        for (int i = nums.length; --i >= 0; ) {
            final int v = nums[i] + delta;
            res[i] = get(arr, v - 1);
            add(arr, v);
        }
        return new AbstractList<>() {
            @Override
            public Integer get(int index) {
                return res[index];
            }

            @Override
            public int size() {
                return res.length;
            }
        };
    }

    static int get(final int[] arr, int v) {
        int sum = 0;
        for (; v > 0; v &= ~(v & -v)) {
            sum += arr[v];
        }
        return sum;
    }

    static void add(final int[] arr, int v) {
        for (; v < arr.length; v += (v & -v)) {
            arr[v]++;
        }
    }

    static class BinaryIndexedTree {
        private int n;
        private int[] c;

        public BinaryIndexedTree(int n) {
            this.n = n;
            this.c = new int[n + 1];
        }

        public void update(int x, int delta) {
            while (x <= n) {
                c[x] += delta;
                x += lowbit(x);
            }
        }

        public int query(int x) {
            int s = 0;
            while (x > 0) {
                s += c[x];
                x &= ~lowbit(x);
            }
            return s;
        }

        public static int lowbit(int x) {
            return x & -x;
        }
    }

    public static List<Integer> countSmaller(int[] nums) {
        Set<Integer> s = new HashSet<>();
        for (int num : nums) {
            s.add(num);
        }
        List<Integer> alls = new ArrayList<>(s);
        alls.sort(Comparator.comparingInt(a -> a));
        int n = alls.size();
        Map<Integer, Integer> m = new HashMap<>(n);
        for (int i = 0; i < n; i++) {
            m.put(alls.get(i), i + 1);
        }
        BinaryIndexedTree tree = new BinaryIndexedTree(n);
        LinkedList<Integer> ans = new LinkedList<>();
        for (int i = nums.length; --i >= 0; ) {
            int x = m.get(nums[i]);
            tree.update(x, 1);
            ans.addFirst(tree.query(x - 1));
        }
        return ans;
    }

    public static List<Integer> countSmallerV2(int[] nums) {
        List<Integer> result = new ArrayList<>();
        List<Integer> sorted = new ArrayList<>();
        if (nums == null || nums.length == 0)
            return result;
        for (int i = nums.length; --i >= 0; ) {
            int left = 0;
            int right = sorted.size();
            while (left < right) {
                int mid = left + (right - left) / 2;
                if (nums[i] <= sorted.get(mid))
                    right = mid;
                else
                    left = mid + 1;
            }
            sorted.add(left, nums[i]);
            result.add(0, left);
        }
        return result;
    }

    public static void main(String[] args) {
        String out = InputReader.inputAsStrings("315.txt")
                .filter(Objects::nonNull)
                .map(s -> s.split(","))
                .map(strings -> {
                    int[] nums = new int[strings.length];
                    for (int i = 0; i < strings.length; i++) {
                        nums[i] = Integer.parseInt(strings[i]);
                    }
                    return nums;
                })
                .map(CountSmallerNumbersAfterSelf::countSmallerV4)
                .map(List::toString)
                .collect(Collectors.joining(System.lineSeparator()));
        OutputWriter.write(out, "315.txt");
    }
}
