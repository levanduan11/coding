package com.coding.algorithm.sequence.approximatesequencematching;

import java.util.Comparator;

public class MatchingRating implements Comparator<String> {
    public static String preprocess(String text) {
        text = text.toUpperCase().replaceAll("[^A-Z]", "");
        text = text.replaceAll("[AEIOU]", "");
        text = removeDuplicate(text);
        return text;
    }

    public static String removeDuplicate(String text) {
        char[] chars = text.toCharArray();
        var sb = new StringBuilder();
        sb.append(chars[0]);
        for (int i = 1, n = chars.length; i < n; i++) {
            if (sb.charAt(sb.length() - 1) != chars[i])
                sb.append(chars[i]);
        }
        return sb.toString();
    }


    @Override
    public int compare(String s1, String s2) {
        s1 = preprocess(s1);
        s2 = preprocess(s2);
        int lenDiff = Math.abs(s1.length() - s2.length());
        int maxLen = Math.max(s1.length(), s2.length());
        if (lenDiff > (maxLen / 3))
            return -1;
        int limit = (Math.min(s1.length(), s2.length()) + 2) / 3;
        return calculateRatingDistance(s1, s2, limit);
    }

    private static int calculateRatingDistance(String s1, String s2, int limit) {
        int discrepancies = 0;
        for (int i = 0, n = Math.min(s1.length(), s2.length()); i < n; i++) {
            if (s1.charAt(i) != s2.charAt(i))
                discrepancies++;
        }
        return discrepancies <= limit ? discrepancies : -1;
    }

    public static void main(String[] args) {
        String s1 = "Smith";
        String s2 = "Smythe";
        int result = new MatchingRating().compare(s1, s2);
        System.out.println(result);
    }
}
