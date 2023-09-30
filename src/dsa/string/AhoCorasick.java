package dsa.string;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class AhoCorasick {
    static int MAXS = 500;
    static int MAXC = 26;
    static int[] out = new int[MAXS];
    static int[] f = new int[MAXS];
    static int[][] g = new int[MAXS][MAXC];

    static int buildMatchingMachine(String[] arr, int k) {
        Arrays.fill(out, 0);
        for (int i = 0; i < MAXS; i++) {
            Arrays.fill(g[i], -1);
        }
        int states = 1;
        for (int i = 0; i < k; i++) {
            String word = arr[i];
            int currentState = 0;
            for (int j = 0; j < word.length(); j++) {
                int ch = word.charAt(j) - 'a';
                if (g[currentState][ch] == -1)
                    g[currentState][ch] = states++;
                currentState = g[currentState][ch];
            }
            out[currentState] |= (1 << i);
        }
        for (int ch = 0; ch < MAXC; ch++) {
            if (g[0][ch] == -1)
                g[0][ch] = 0;
        }
        Arrays.fill(f, -1);
        Queue<Integer> q = new LinkedList<>();
        for (int ch = 0; ch < MAXC; ch++) {
            if (g[0][ch] != 0) {
                f[g[0][ch]] = 0;
                q.add(g[0][ch]);
            }
        }
        while (!q.isEmpty()) {
            int state = q.peek();
            q.remove();
            for (int ch = 0; ch < MAXC; ch++) {
                if (g[state][ch] != -1) {
                    int failure = f[state];
                    while (g[failure][ch] == -1)
                        failure = f[failure];
                    failure = g[failure][ch];
                    f[g[state][ch]] = failure;
                    out[g[state][ch]] |= out[failure];
                    q.add(g[state][ch]);
                }
            }
        }
        return states;
    }

    static int findNextState(int currentState, char nextInput) {
        int answer = currentState;
        int ch = nextInput - 'a';
        while (g[answer][ch] == -1)
            answer = f[answer];
        return g[answer][ch];
    }

    static int searchWords(String[] arr, int k, String text) {
        buildMatchingMachine(arr, k);
        int res = 0;
        int currentState = 0;
        for (int i = 0; i < text.length(); i++) {
            currentState = findNextState(currentState, text.charAt(i));
            if (out[currentState] == 0)
                continue;
            for (int j = 0; j < k; j++) {
                if ((out[currentState] & (1 << j)) > 0) {
                    System.out.println("Word " + arr[j] +
                            " appears from " +
                            (i - arr[j].length() + 1) + " to " + i + "\n");
                }
            }
        }
        return res;
    }

    public static void main(String[] args) {

        String[] arr = {"he", "she", "hers", "his"};
        String[] arr1 = {"b", "c", "aa", "d", "b"};
        String text = "ahishers";
        String text1 = "caaab";
        int k = arr.length;
        int k1 = arr1.length;
        searchWords(arr, k, text);
    }
}
