package hard;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ScrambleString {
    public static boolean isScramble(String s1, String s2) {
        if (s1 == null || s2 == null || s1.length() != s2.length()) {
            return false;
        }
        int len = s1.length();
        boolean[][][] dp = new boolean[len][len][len + 1];
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len; j++) {
                dp[i][j][1] = s1.charAt(i) == s2.charAt(j);
            }
        }
        for (int n = 1; n < len + 1; n++) {
            loop2:
            for (int i = 0; i + n - 1 < len; i++) {
                for (int j = 0; j + n - 1 < len; j++) {
                    if (n == 1) break loop2;
                    for (int sublen = 1; sublen < n; sublen++) {
                        boolean isSame = dp[i][j][sublen] && dp[i + sublen][j + sublen][n - sublen];
                        boolean isSwapped = dp[i][j + n - sublen][sublen] && dp[i + sublen][j][n - sublen];
                        if (isSame || isSwapped) {
                            dp[i][j][n] = true;
                            break;
                        }
                    }
                }
            }
        }
        return dp[0][0][len];
    }

    public static boolean isScramble2(String s1, String s2) {
        if (s1 == null || s2 == null || s1.length() != s2.length()) {
            return false;
        }
        if (s1.isEmpty()) {
            return true;
        }
        if (s1.length() == 1) {
            return s1.charAt(0) == s2.charAt(0);
        }
        if (s1.length() == 2) {
            boolean isSame = s1.equals(s2);
            boolean isSwapped = s1.charAt(0) == s2.charAt(1) && s1.charAt(1) == s2.charAt(0);
            return isSwapped || isSame;
        }
        for (int i = 1; i < s1.length(); i++) {
            boolean v1 = isScramble2(s1.substring(0, i), s2.substring(0, i));
            boolean v2 = isScramble2(s1.substring(i), s2.substring(i));
            boolean isThisScramble = v1 && v2;
            if (isThisScramble) {
                return true;
            }
        }
        return false;
    }

    static Map<String, Boolean> map = new HashMap<>();

    public static boolean isScramble3(String s1, String s2) {
        int n = s1.length();
        if (s1.equals(s2)) {
            return true;
        }
        int[] a = new int[26], b = new int[26], c = new int[26];
        if (map.containsKey(s1 + s2)) {
            return map.get(s1 + s2);
        }
        for (int i = 1; i <= n - 1; i++) {
            int j = n - i;
            a[s1.charAt(i - 1) - 'a']++;
            b[s2.charAt(i - 1) - 'a']++;
            c[s2.charAt(j) - 'a']++;
            boolean v1 = isScramble3(s1.substring(0, i), s2.substring(0, i));
            boolean v2 = isScramble3(s1.substring(i), s2.substring(i));
            if (Arrays.equals(a, b) && v1 && v2) {
                map.put(s1 + s2, true);
                return true;
            }
            boolean v3 = isScramble3(s1.substring(0, i), s2.substring(j)); 
            boolean v4 = isScramble3(s1.substring(i), s2.substring(0, j));
            if (Arrays.equals(a, c) && v3 && v4) {
                map.put(s1 + s2, true);
                return true;
            }
        }
        map.put(s1 + s2, false);
        return false;
    }

    public static void main(String[] args) {
        String s = "great";
        String s2 = "rgeat";
        System.out.println(isScramble(s, s2));
        System.out.println(isScramble2(s, s2));
        System.out.println(isScramble3(s, s2));
    }
}
