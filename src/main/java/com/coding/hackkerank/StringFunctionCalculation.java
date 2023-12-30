package com.coding.hackkerank;

import com.coding.InputReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

public class StringFunctionCalculation {
    public static int maxValue01(String t) {
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0, n = t.length(); i < n; i++) {
            for (int j = 0; j < n - i; j++) {
                String s = t.substring(j, j + i + 1);
                map.put(s, map.getOrDefault(s, 0) + 1);
            }
        }
        return map.entrySet().stream()
                .map(entry
                        -> Map.entry(entry.getKey(), entry.getKey().length() * entry.getValue()))
                .map(Map.Entry::getValue)
                .reduce(Integer.MIN_VALUE, Integer::max);
    }

    public static void main(String[] args) throws Throwable{
        InputReader.inputAsStrings("string_function_calculation.txt")
                .forEach(s -> {
                    long res = solve(s);
                    System.out.println(res);
                });
    }
    public static long solve(String str){
        SuffixAutomata a = new SuffixAutomata(str);
        return a.root.dp();
    }
    private static class SuffixAutomata{
        private Vertex root;
        private Vertex last;
        private class Vertex{
            Vertex suffixLink;
            Vertex[]edges;
            int log;
            boolean visited = false;
            int terminals;
            public Vertex(Vertex o,int log){
                edges = o.edges.clone();
            }
            public Vertex(int log){
                edges = new Vertex[26];
                this.log = log;
            }
            long dp(){
                if (visited)
                    return 0L;
                visited = true;
                long r = 0;
                for (Vertex v : edges) {
                    if (v!=null){
                        long a = v.dp();
                        r = Math.max(r,a);
                        terminals += v.terminals;
                    }
                }
                return Math.max(r, (long) log * terminals);
            }
        }
        public SuffixAutomata(String str){
            last = root = new Vertex(0);
            for (int i = 0; i < str.length(); i++) {
                addChar(str.charAt(i));
            }
            addTerminal();
        }
        private void addChar(char c){
            Vertex cur = last;
            last = new Vertex(cur.log + 1);
            while (cur !=null && cur.edges[c -'a'] == null){
                cur.edges[c-'a']=last;
                cur = cur.suffixLink;
            }
            if (cur!=null){
                Vertex q = cur.edges[c -'a'];
                if (q.log == cur.log + 1){
                    last.suffixLink = q;
                }else {
                    Vertex r = new Vertex(q,cur.log + 1);
                    r.suffixLink = q.suffixLink;
                    q.suffixLink = r;
                    last.suffixLink = r;
                    while (cur!=null){
                        if (cur.edges[c -'a'] ==q){
                            cur.edges[c -'a'] = r;
                        }else {
                            break;
                        }
                        cur = cur.suffixLink;
                    }
                }
            }else {
                last.suffixLink = root;
            }
        }
        private void addTerminal(){
            Vertex cur = last;
            while (cur !=null){
                cur.terminals++;
                cur = cur.suffixLink;
            }
        }
    }
    static class FastScanner{
        private static BufferedReader reader;
        private static StringTokenizer tokenizer;

        public FastScanner(InputStream in){
            try {
                reader = new BufferedReader(new InputStreamReader(in));
                tokenizer = new StringTokenizer(reader.readLine().trim());
            }catch (IOException e){
                System.err.println("cannot read input");
            }
        }
        public int numTokens() throws IOException {
            if (!tokenizer.hasMoreTokens()){
                tokenizer = new StringTokenizer(reader.readLine().trim());
                return numTokens();
            }else {
                return tokenizer.countTokens();
            }
        }
        public boolean hasNext() throws IOException {
            if (!tokenizer.hasMoreTokens()){
                tokenizer = new StringTokenizer(reader.readLine().trim());
                return hasNext();
            }else {
                return true;
            }
        }
        public String next() throws IOException {
            if (!tokenizer.hasMoreTokens()){
                tokenizer = new StringTokenizer(reader.readLine().trim());
                return next();
            }else {
                return tokenizer.nextToken();
            }
        }
        public double nextDouble() throws IOException {
            return Double.parseDouble(next());
        }
        public float nextFloat() throws IOException {
            return Float.parseFloat(next());
        }
        public long nextLong() throws IOException {
            return Long.parseLong(next());
        }
        public int nextInt() throws IOException {
            return Integer.parseInt(next());
        }
        public int[] nextIntArray() throws IOException {
            String[] line = reader.readLine().trim().split(" ");
            int[] out = new int[line.length];
            for (int i = 0; i < line.length; i++) {
                out[i] = Integer.parseInt(line[i]);
            }
            return out;
        }
        public double[] nextDoubleArray() throws IOException {
            String[] line = reader.readLine().trim().split(" ");
            double[] out = new double[line.length];
            for (int i = 0; i < line.length; i++) {
                out[i] = Double.parseDouble(line[i]);
            }
            return out;
        }
        public String nextLine() throws IOException {
            return reader.readLine().trim();
        }
    }
}
