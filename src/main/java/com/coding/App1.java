package com.coding;

import java.io.*;
import java.util.*;

import static java.util.stream.IntStream.range;

public class App1 {
    static char[] chars = {
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'
    };

    public static void main(String[] args) throws IOException {
        var ran = new Random();
        int[] arr =range(0,50).map(i->ran.nextInt(50)).toArray();
        Arrays.sort(arr);
        int v =( (int)('A')) ^ 0x20;
        char c = 'f';
        System.out.println(showCharacter(c));
    }
    private static String showCharacter(char c) {
        String hex = "0123456789ABCDEF";
        char[] temp = {'\\', 'u', '\0', '\0', '\0', '\0'};
        for (int i = 0; i < 4; i++) {
            temp[5 - i] = hex.charAt(c & 0xF);
            c = (char) (c >> 4);
        }
        return String.copyValueOf(temp);
    }
}
