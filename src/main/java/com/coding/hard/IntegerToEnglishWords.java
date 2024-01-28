package com.coding.hard;

import com.coding.InputReader;
import com.coding.OutputWriter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class IntegerToEnglishWords {
    private static final String[] LESS_THAN_20 = {
            "", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine",
            "Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen",
            "Seventeen", "Eighteen", "Nineteen"};
    private static final String[] TENS = {
            "", "", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety"};
    private static final String[] THOUSANDS = {"", "Thousand", "Million", "Billion"};
    static String[] ones = {"", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen"};

    static String[] tens = {"", "Ten", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety"};
    static int billion = 1_000_000_000;
    static int million = 1_000_000;
    static int thousand = 1000;
    static int hundred = 100;

    public static String numberToWordsV2(int n) {
        if (n == 0)
            return "";
        else
            return backtrack(n).trim();
    }

    private static String backtrack(int n) {
        StringBuilder sb = new StringBuilder();
        if (n >= billion) {
            String prevDiv = backtrack(n / billion);
            String prevMod = backtrack(n % billion);
            sb.append(prevDiv)
                    .append(" Billion ")
                    .append(prevMod);
        } else if (n >= million) {
            String prevDiv = backtrack(n / million);
            String prevMod = backtrack(n % million);
            sb.append(prevDiv)
                    .append(" Million ")
                    .append(prevMod);
        } else if (n >= thousand) {
            String prevDiv = backtrack(n / thousand);
            String prevMod = backtrack(n % thousand);
            sb.append(prevDiv)
                    .append(" Thousand ")
                    .append(prevMod);
        } else if (n >= hundred) {
            String prevDiv = backtrack(n / hundred);
            String prevMod = backtrack(n % hundred);
            sb.append(prevDiv)
                    .append(" Hundred ")
                    .append(prevMod);
        } else if (n >= 20) {
            String prevMod = backtrack(n % 10);
            sb.append(tens[n / 10]).append(" ").append(prevMod);
        } else {
            sb.append(ones[n]);
        }
        return sb.toString().trim();
    }

    private static Map<Integer, String> map;

    static {
        map = new HashMap<>();
        map.put(1, "One");
        map.put(2, "Two");
        map.put(3, "Three");
        map.put(4, "Four");
        map.put(5, "Five");
        map.put(6, "Six");
        map.put(7, "Seven");
        map.put(8, "Eight");
        map.put(9, "Nine");
        map.put(10, "Ten");
        map.put(11, "Eleven");
        map.put(12, "Twelve");
        map.put(13, "Thirteen");
        map.put(14, "Fourteen");
        map.put(15, "Fifteen");
        map.put(16, "Sixteen");
        map.put(17, "Seventeen");
        map.put(18, "Eighteen");
        map.put(19, "Nineteen");
        map.put(20, "Twenty");
        map.put(30, "Thirty");
        map.put(40, "Forty");
        map.put(50, "Fifty");
        map.put(60, "Sixty");
        map.put(70, "Seventy");
        map.put(80, "Eighty");
        map.put(90, "Ninety");
        map.put(100, "Hundred");
        map.put(1000, "Thousand");
        map.put(1000000, "Million");
        map.put(1000000000, "Billion");
    }

    public static String numberToWordsV3(int num) {
        if (num == 0)
            return "Zero";
        StringBuilder sb = new StringBuilder();
        for (int i = 1_000_000_000; i >= 1000; i /= 1000) {
            if (num >= i) {
                String s = get3Digits(num / i);
                sb.append(s).append(' ').append(map.get(i));
                num %= i;
            }
        }
        if (num > 0) {
            String s = get3Digits(num);
            sb.append(s);
        }
        return sb.substring(1);
    }

    private static String get3Digits(int num) {
        StringBuilder sb = new StringBuilder();
        if (num >= 100) {
            sb.append(' ').append(map.get(num / 100))
                    .append(' ').append(map.get(100));
        }
        if (num > 0) {
            if (num < 20 || num % 10 == 0) {
                sb.append(' ').append(map.get(num));
            } else {
                sb.append(' ').append(map.get(num / 10 * 10))
                        .append(' ').append(map.get(num % 10));
            }
        }
        return sb.toString().trim();
    }

    public static String numberToWords(int num) {
        if (num == 0)
            return "Zero";
        int i = 0;
        StringBuilder word = new StringBuilder();
        while (num > 0) {
            if (num % 1000 != 0) {
                word.insert(0, helper(num % 1000) + " " + THOUSANDS[i] + " ");
            }
            num /= 1000;
            i++;
        }
        return Arrays.stream(word.toString().split(" "))
                .filter(s -> !s.isEmpty())
                .collect(Collectors.joining(" "));
    }

    private static String helper(int num) {
        if (num == 0)
            return "";
        else if (num < 20)
            return LESS_THAN_20[num];
        else if (num < 100) {
            String s = helper(num % 10);
            return TENS[num / 10] + " " + s;
        } else {
            String s = helper(num % 100);
            return LESS_THAN_20[num / 100] + " Hundred " + s;
        }
    }


    public static void main(String[] args) {
        String collect = InputReader.inputAsStrings("273.txt")
                .map(Integer::parseInt)
                .map(IntegerToEnglishWords::numberToWordsV3)
                .collect(Collectors.joining(System.lineSeparator()));
        OutputWriter.write(collect, "273_v3.txt");
    }
}
