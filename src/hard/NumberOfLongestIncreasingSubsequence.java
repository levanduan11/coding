package hard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Solution {
    public int findNumberOfLIS(int[] nums) {
        int num = nums.length;
        List<int[]>[] len = new ArrayList[num];
        for (int i = 0; i < num; i++) {
            len[i] = new ArrayList<>();
        }
        int size = 0;
        for (int n : nums) {
            int index = bSearchLength(len, size, n);
            int count = 1;

            if (index > 0) {
                List<int[]> t = len[index - 1];
                int p = bSearchIndex(t, n);
                count = t.get(t.size() - 1)[1] - (p == 0 ? 0 : t.get(p - 1)[1]);
            }
            if (len[index].size() == 0) {
                len[index].add(new int[]{n, count});
                size++;
            } else {
                List<int[]> t = len[index];
                int[] last = t.get(t.size() - 1);
                int ch = last[1] + count;
                if (last[0] == n) {
                    last[1] += count;
                } else {
                    t.add(new int[]{n, last[1] + count});
                }
            }
        }
        return len[size - 1].get(len[size - 1].size() - 1)[1];
    }

    private int bSearchIndex(List<int[]> t, int n) {
        int left = 0, right = t.size() - 1;
        while (left < right) {
            int mid = left + (right - left) / 2;
            int r = t.get(mid)[0];
            if (n <= r)
                left = mid + 1;
            else
                right = mid;
        }
        return left;
    }

    private int bSearchLength(List<int[]>[] len, int right, int n) {
        int left = 0;
        while (left < right) {
            int mid = (left + right) / 2;
            int r = len[mid].get(len[mid].size() - 1)[0];
            if (n > r)
                left = mid + 1;
            else
                right = mid;
        }
        return left;
    }

    public static void main(String[] args) {
        var o = new Solution();
        int[] arr = {1, 3, 5, 4, 7};
        System.out.println(o.findNumberOfLIS(arr));
    }
}

public class NumberOfLongestIncreasingSubsequence {
    public int findNumberOfLIS(int[] nums) {
        int n = nums.length;
        int[] length = new int[n];
        int[] count = new int[n];

        Arrays.fill(length, 1);
        Arrays.fill(count, 1);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (nums[j] < nums[i]) {
                    if (length[j] + 1 > length[i]) {
                        length[i] = length[j] + 1;
                        count[i] = 0;
                    }
                    if (length[j] + 1 == length[i]) {
                        count[i] += count[j];
                    }
                }
            }
        }

        int maxLength = 0;
        int result = 0;

        for (int len : length) {
            maxLength = Math.max(len, maxLength);
        }
        for (int i = 0; i < n; i++) {
            if (length[i] == maxLength) {
                result += count[i];
            }
        }
        return result;
    }

    public static void main(String[] args) {
        var o = new NumberOfLongestIncreasingSubsequence();
        int[] arr = {2, 2, 2, 2, 2};
        System.out.println(o.findNumberOfLIS(arr));
    }
}
