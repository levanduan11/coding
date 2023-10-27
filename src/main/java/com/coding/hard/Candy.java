package hard;

import java.util.Arrays;

public class Candy {
    public int candy(int[] ratings) {
        if (ratings.length == 1)
            return 1;
        int n = ratings.length, ans = 0;
        int[] arr = new int[n];
        Arrays.fill(arr, 1);
        for (int i = 1; i < n; i++) {
            if (ratings[i] > ratings[i - 1]) {
                arr[i] = Math.max(arr[i], arr[i - 1] + 1);
            }
        }
        for (int i = n - 2; i >= 0; i--) {
            if (ratings[i] > ratings[i + 1]) {
                arr[i] = Math.max(arr[i], arr[i + 1] + 1);
            }
        }
        for (int i : arr) {
           ans+=i;
        }
        return ans;
    }

    public static void main(String[] args) {
        var o = new Candy();
        int[] ratings = {1, 3, 2, 2, 1};
        var res = o.candy(ratings);
        System.out.println(res);
    }
}
