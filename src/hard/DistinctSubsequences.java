package hard;

public class DistinctSubsequences {
    static String s;
    static String t;
    static int sn;
    static int tn;
    static Integer[][] memo;

    static int check(int si, int ti) {
        if (si == sn || ti == tn || sn - si < tn - ti)
            return ti == tn ? 1 : 0;
        if (ti == tn)
            return 1;
        if (si == sn)
            return 0;
        if (memo[si][ti] != null)
            return memo[si][ti];
        int count = check(si + 1, ti);
        if (s.charAt(si) == t.charAt(ti)) {
            int v = check(si + 1, ti + 1);
            count += v;
        }
        memo[si][ti] = count;
        return count;
    }

    public static int numDistinct2(String s, String t) {
        DistinctSubsequences.s = s;
        DistinctSubsequences.t = t;
        sn = s.length();
        tn = t.length();
        memo = new Integer[sn][tn];
        return check(0, 0);
    }

    public static int numDistinct(String s, String t) {
        if (s == null || t == null)
            return 0;
        if (s.length() < t.length())
            return 0;
        int tLength = t.length();
        int sLength = s.length();
        int[][] dp = new int[tLength + 1][sLength + 1];
        dp[0][0] = 1;
        for (int i = 1; i <= sLength; i++) {
            dp[0][i] = 1;
        }
        for (int i = 1; i < tLength; i++) {
            dp[i][0] = 0;
        }
        for (int i = 1; i <= tLength; i++) {
            for (int j = 1; j <= sLength; j++) {
                if (t.charAt(i - 1) == s.charAt(j - 1)) {
                    dp[i][j] = dp[i][j - 1] + dp[i - 1][j - 1];
                } else {
                    dp[i][j] = dp[i][j - 1];
                }
            }
        }
        return dp[tLength][sLength];
    }

    public static void main(String[] args) {
        String s = "rabbbit";
        String t = "rabbit";
        System.out.println(numDistinct(s, t));
        System.out.println(numDistinct2(s, t));
    }
}
