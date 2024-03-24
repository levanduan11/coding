package com.coding.hard;

public class WildcardMatching {
    public static boolean isMatch(String s, String p) {
        int tIndex = 0;// Pointer for the s
        int pIndex = 0;// Pointer for the p
        int tStar = -1;// keep track of the position of '*' in the s
        int pStar = -1;// Kepp track of the position of '*' in the p

        while (tIndex < s.length()) {
            if (pIndex < p.length() &&
                    (p.charAt(pIndex) == '?' || p.charAt(pIndex) == s.charAt(tIndex))) {
                tIndex++;
                pIndex++;
            } else if (pIndex < p.length() && p.charAt(pIndex) == '*') {
                pStar = pIndex;
                tStar = tIndex;
                pIndex++;
            } else if (pStar != -1) {
                // If a '*' is found previously in the p,try to match the remaining s with the p after a '*'
                pIndex = pStar + 1;
                tIndex = ++tStar;
            }
        }
        while (pIndex < p.length() && p.charAt(pIndex) == '*') pIndex++; // Skip any remaining '*' characters in the p
        return pIndex == p.length();
    }

    public static void main(String[] args) {
        String s = "aa";
        String p = "*";
        System.out.println(isMatch(s, p));
    }
}
