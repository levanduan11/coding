package hard;

import java.util.ArrayList;
import java.util.List;

public class PermutationSequence {

    int w;
    int c = 0;
    int k;
    List<String> strings;

    public String getPermutation(int n, int k) {
        this.strings = new ArrayList<>();
        this.w = n;
        this.k = k;
        print("", n);
        return strings.get(k - 1);
    }

    void print(String s, int n) {
        if (n == 0) {
            strings.add(s);
            c = c + 1;
            return;
        }
        for (int i = 1; i <= w; i++) {
            if (c == k) return;
            if (!isDuplicate(s, i)) {
                print(s + i, n - 1);
            }

        }
    }

    boolean isDuplicate(String s, int ch) {
        return s.indexOf(ch + '0') >= 0;
    }

    static String getPer(int n, int k) {
        int[] fact = new int[n];
        int i;
        fact[0] = 1;
        for (i = 1; i < n; i++) {
            fact[i] = i * fact[i - 1];
        }
        boolean[] tag = new boolean[n];
        StringBuilder sb = new StringBuilder();
        --k;
        for (i = n - 1; i >= 0; i--) {
            int t = k / fact[i];
            for (int j = 0; j < n; j++) {
                if (tag[j]) continue;
                if (t == 0) {
                    sb.append(j + 1);
                    tag[j] = true;
                    break;
                }
                --t;
            }
            k %= fact[i];
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        var per = new PermutationSequence();
        System.out.println(getPer(3,3));
    }
}
