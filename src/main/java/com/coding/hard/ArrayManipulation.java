package hard;

import java.util.List;

public class ArrayManipulation {
    static long ans;

    public static long arrayManipulation2(int n, List<List<Integer>> queries) {
        int m = queries.size();
        int[][] table = new int[m][n];
        ans = Long.MIN_VALUE;
        int r = 0;
        for (List<Integer> integers : queries) {
            int a = integers.get(0);
            int b = integers.get(1);
            int k = integers.get(2);
            updateTable(table, a, b, k, r);
            r++;
        }

        return ans;
    }

    private static void updateTable(int[][] table, int a, int b, int k, int r) {
        for (int j = r; j < table.length; j++) {
            int[] row = table[j];
            for (int i = a - 1; i < b; i++) {
                row[i] += k;
                ans = Math.max(ans, row[i]);
            }
        }
    }

    public static long arrayManipulation(int n, List<List<Integer>> queries) {
        long[] computation = new long[n];
        for (List<Integer> list : queries) {
            int a = list.get(0);
            int b = list.get(1);
            int k = list.get(2);
            computation[a] += k;
            if (b + 1 < n) {
                computation[b + 1] -= k;
            }
        }
        long max = 0, sum = 0;
        for (long l : computation) {
            sum += l;
            max = Math.max(max, sum);
        }
        return max;
    }

    public static void main(String[] args) {
        int n = 10;
        List<List<Integer>> queries = List.of(
                List.of(1, 5, 3),
                List.of(4, 8, 7),
                List.of(6, 9, 1)
        );
        long res = arrayManipulation(n, queries);
        System.out.println(res);
    }
}
