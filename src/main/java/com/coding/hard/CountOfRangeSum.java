package com.coding.hard;

import com.coding.InputReader;
import com.coding.OutputWriter;

import java.util.Arrays;
import java.util.stream.Collectors;

public class CountOfRangeSum {
    @FunctionalInterface
    public interface BiIntFn {
        long apply(int a, int b);
    }

    static class SegmentTree {
        private int N;
        private long[] tree;
        private BiIntFn action;

        public SegmentTree(int[] values) {
            tree = new long[2 * (N = values.length)];
            for (int i = 0; i < N; i++) {
                modify(i, values[i]);
            }
        }

        public SegmentTree(int[] values, BiIntFn action) {
            this.action = action;
            tree = new long[2 * (N = values.length)];
            for (int i = 0; i < N; i++) {
                modify(i, values[i]);
            }
        }

        public void modify(int p, int v) {
            tree[p + N] =
                    action == null
                            ? Long.sum(tree[p + N], v)
                            : action.apply((int) tree[p + N], v);
            for (p += N; p > 1; p >>= 1) {
                tree[p >> 1] =
                        action == null
                                ? Long.sum(tree[p], tree[p ^ 1])
                                : action.apply((int) tree[p], (int) tree[p ^ 1]) + Long.sum(tree[p], tree[p ^ 1]);
            }
        }

        public long sumQuery(int l, int r) {
            long res = 0;
            for (l += N, r += N + 1; l < r; l >>= 1, r >>= 1) {
                if ((l & 1) != 0)
                    res += tree[l++];
                if ((r & 1) != 0)
                    res += tree[--r];
            }
            return res;
        }

        public void update(int p, int v) {
            p += N;
            tree[p] = v;
            for (p >>= 1; p > 0; p >>= 1) {
                tree[p] =
                        action == null
                                ? Long.sum(tree[p << 1], tree[p << 1 | 1])
                                : action.apply((int) tree[p << 1], (int) tree[p << 1 | 1]) + tree[p << 1] + tree[p << 1 | 1];
            }
        }

        public long[] getTree() {
            return tree;
        }
    }

    static class FenwickTree {
        private int N;
        private int[] tree;

        public FenwickTree(int n) {
            N = n;
            tree = new int[N + 1];
        }

        public void update(int p, int v) {
            while (p <= N) {
                tree[p] += v;
                p += p & -p;
            }
        }

        public int query(int p) {
            int res = 0;
            while (p > 0) {
                res += tree[p];
                p &= ~(p & -p);
            }
            return res;
        }
    }


    public static int countRangeSum(int[] nums, int lower, int upper) {
        int n = nums.length;
        long[] s = new long[n + 1];
        for (int i = 0; i < n; i++) {
            s[i + 1] = s[i] + nums[i];
        }
        long[] arr = new long[n * 3 + 3];
        for (int i = 0, j = 0; i <= n; i++, j += 3) {
            arr[j] = s[i];
            arr[j + 1] = s[i] - lower;
            arr[j + 2] = s[i] - upper;
        }
        Arrays.sort(arr);
        int m = 0;
        for (int i = 0; i < arr.length; i++) {
            if (i == 0 || arr[i] != arr[i - 1])
                arr[m++] = arr[i];
        }
        FenwickTree fenwickTree = new FenwickTree(m);
        int ans = 0;
        for (long x : s) {
            int l = search(arr, m, x - upper);
            int r = search(arr, m, x - lower);
            ans += fenwickTree.query(r) - fenwickTree.query(l - 1);
            int mm = search(arr, m, x);
            fenwickTree.update(mm, 1);
        }
        return ans;
    }

    private static int search(long[] nums, int r, long x) {
        int l = 0;
        while (l < r) {
            int mid = (l + r) >> 1;
            if (nums[mid] >= x)
                r = mid;
            else
                l = mid + 1;
        }
        return l + 1;
    }

    static int lo;
    static int up;

    public static int countRangeSumV2(int[] nums, int lower, int upper) {
        lo = lower;
        up = upper;
        int N = nums.length;
        long[] prefix = new long[N + 1];
        prefix[0] = 0;
        for (int i = 0; i < N; i++) {
            prefix[i + 1] = prefix[i] + nums[i];
        }
        int res = countinueMergeSort(prefix, new long[prefix.length], 0, prefix.length - 1);
        return res;
    }

    private static int countinueMergeSort(long[] prefix, long[] helper, int l, int r) {
        if (l >= r)
            return 0;
        int mid = l + (r - l) / 2;
        int leftCount = countinueMergeSort(prefix, helper, l, mid);
        int rightCount = countinueMergeSort(prefix, helper, mid + 1, r);
        int count = leftCount + rightCount;
        int start = mid + 1, end = mid + 1;
        for (int i = l; i <= mid; i++) {
            while (start <= r && prefix[start] - prefix[i] < lo)
                start++;
            while (end <= r && prefix[end] - prefix[i] <= up)
                end++;
            count += end - start;
        }
        merge(prefix, helper, l, r);
        return count;
    }

    private static void merge(long[] prefix, long[] helper, int l, int r) {
        int mid = l + (r - l) / 2;
        int i = l, j = mid + 1;
        for (int k = l; k <= r; k++) {
            if (i > mid) {
                helper[k] = prefix[j++];
            } else if (j > r) {
                helper[k] = prefix[i++];
            } else if (prefix[i] < prefix[j]) {
                helper[k] = prefix[i++];
            } else {
                helper[k] = prefix[j++];
            }
        }
        System.arraycopy(helper, l, prefix, l, r - l + 1);
    }

    public static void main(String[] args) {
        String out = InputReader.inputAsStrings("327.txt")
                .map(s -> {
                    String[] strings = s.split(",");
                    int n = strings.length - 1;
                    int u = Integer.parseInt(strings[n]);
                    int l = Integer.parseInt(strings[n - 1]);
                    int[] nums = new int[n - 1];
                    for (int i = 0; i < n - 1; i++) {
                        nums[i] = Integer.parseInt(strings[i]);
                    }
                    return countRangeSumV2(nums, l, u);
                })
                .map(String::valueOf)
                .collect(Collectors.joining(System.lineSeparator()));
        OutputWriter.write(out, "327_V2.txt");
    }
}
