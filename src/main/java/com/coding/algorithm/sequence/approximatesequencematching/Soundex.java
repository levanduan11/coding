package com.coding.algorithm.sequence.approximatesequencematching;

public class Soundex {
    public static String soundex(String text) {
        char[] x = text.toUpperCase().toCharArray();
        char firstLetter = x[0];
        for (int i = 0, n = x.length; i < n; i++) {
            switch (x[i]) {
                case 'B', 'F', 'P', 'V' -> x[i] = '1';
                case 'C', 'G', 'J', 'K', 'Q', 'S', 'X', 'Z' -> x[i] = '2';
                case 'D', 'T' -> x[i] = '3';
                case 'L' -> x[i] = '4';
                case 'M', 'N' -> x[i] = '5';
                case 'R' -> x[i] = 6;
                default -> x[i] = '0';
            }
        }
        StringBuilder output = new StringBuilder("" + firstLetter);
        for (int i = 1, n = x.length; i < n; i++) {
            if (x[i] != x[i - 1] && x[i] != '0') {
                output.append(x[i]);
            }
        }
        output.append("0000");
        return output.substring(0, 4);
    }

    public static void main(String[] args) {
        String in = "John";
        System.out.println(soundex(in));
    }
}
