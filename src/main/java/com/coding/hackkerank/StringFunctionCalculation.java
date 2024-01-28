package com.coding.hackkerank;

import com.coding.InputReader;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

class StringCal {
    static class Node {
        static int[] a;
        final static int neg = -1;
        final int start;
        final int end;
        final int min;
        Node[] nodes;

        public Node(int start, int end) {
            this.start = start;
            this.end = end;
            if (start < end) {
                int min = neg;
                int[] pos = {start, (start + end) / 2, end};
                nodes = new Node[2];
                for (int i = 0; i < 2; i++) {
                    nodes[i] = new Node(pos[i] + i, pos[i + 1]);
                    if (min == -1 || a[nodes[i].min] < a[min])
                        min = nodes[i].min;
                }
                this.min = min;
            } else {
                min = start;
            }
        }

        public int query(int queryStart, int queryEnd) {
            if (queryEnd < start || end < queryStart) {
                return neg;
            }
            if (queryStart <= start && end <= queryEnd) {
                return min;
            }
            int ans = neg;
            for (Node node : nodes) {
                int temp = node.query(queryStart, queryEnd);
                if (temp > neg && (ans == neg || a[temp] < a[ans]))
                    ans = temp;
            }
            return ans;
        }
    }
    public void solve(String text){
        int[]lcp = XString.lcp(text);
        Node.a = lcp;
        Node root = new Node(0,lcp.length-2);
        ArrayDeque<Point> stack = new ArrayDeque<>();
        stack.push(new Point(0,lcp.length-2));
        long ans = text.length();
        while (!stack.isEmpty()){
            Point point = stack.pop();
            int start = point.x;
            int end = point.y;
            if (start<=end){
                int min = root.query(start,end);
                ans = Math.max(ans, (long) lcp[min] * (end -start +2));
                stack.push(new Point(start,min-1));
                stack.push(new Point(min+1,end));
            }
        }
        System.out.println(ans);
    }
}

class InputReader02 {
    private BufferedReader input;
    private StringTokenizer line = new StringTokenizer("");

    public InputReader02(InputStream in) {
        input = new BufferedReader(new InputStreamReader(in));
    }

    public void fill() {
        if (!line.hasMoreTokens()) {
            try {
                line = new StringTokenizer(input.readLine());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public String next() {
        fill();
        return line.nextToken();
    }
}

class OutputWriter02 {
    private PrintWriter output;

    public OutputWriter02(OutputStream out) {
        out = new PrintStream(out);
    }

    public void println(Object o) {
        output.println(o);
    }

    public void close() {
        output.close();
    }
}

class XString {
    public static int[] lcp(String str, int[] suffix) {
        final int n = suffix.length;
        int[] pos = new int[n];
        for (int i = 0; i < n; i++) {
            pos[suffix[i]] = i;
        }
        int[] lcp = new int[n];
        for (int i = 0, w = 0; i < n; i++) {
            if (pos[i] < n - 1) {
                int j = suffix[pos[i] + 1];
                boolean loopCondition = Math.max(i, j) + w < n && str.charAt(i + w) == str.charAt(j + w);
                while (loopCondition) {
                    w++;
                    loopCondition = Math.max(i, j) + w < n && str.charAt(i + w) == str.charAt(j + w);
                }

                lcp[pos[i]] = w;
                if (w > 0)
                    w--;
            }
        }
        return lcp;
    }
    public static int[]lcp(String str){
        int[]suffix = suffixArray(str);
        return lcp(str,suffix);
    }

    public static int[] suffixArray(String str) {
        final int n = str.length();
        Integer[] order = new Integer[n];
        for (int i = 0; i < n; i++) {
            order[i] = n - i - 1;
        }
        Arrays.sort(order, Comparator.comparingInt(str::charAt));
        int[] suffix = new int[n];
        int[] rank = new int[n];
        for (int i = 0; i < n; i++) {
            suffix[i] = order[i];
            rank[suffix[i]] = str.charAt(suffix[i]);
        }
        for (int len = 1; len < n; len <<= 1) {
            int[] r = Arrays.copyOf(rank, n);
            for (int i = 0; i < n; i++) {
                if (i > 0
                        && r[suffix[i - 1]] == r[suffix[i]]
                        && suffix[i - 1] + len < n
                        && r[suffix[i - 1] + len / 2] == r[suffix[i] + len / 2]) {
                    rank[suffix[i]] = rank[suffix[i - 1]];
                } else {
                    rank[suffix[i]] = i;
                }
            }
            int[] pos = new int[n];
            for (int i = 0; i < n; i++) {
                pos[i] = i;
            }
            int[] s = Arrays.copyOf(suffix, n);
            for (int i = 0; i < n; i++) {
                int t = s[i] - len;
                if (t >= 0)
                    suffix[pos[rank[t]]++] = t;
            }
        }
        return suffix;
    }
}

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

    public static void main(String[] args) throws Throwable {
        InputReader.inputAsStrings("string_function_calculation.txt")
                .forEach(s -> {
                    StringCal stringCal = new StringCal();
                    stringCal.solve(s);
                });
    }

    public static long solve(String str) {
        SuffixAutomata a = new SuffixAutomata(str);
        return a.root.dp();
    }

    private static class SuffixAutomata {
        private Vertex root;
        private Vertex last;

        private class Vertex {
            Vertex suffixLink;
            Vertex[] edges;
            int log;
            boolean visited = false;
            int terminals;

            public Vertex(Vertex o, int log) {
                edges = o.edges.clone();
            }

            public Vertex(int log) {
                edges = new Vertex[26];
                this.log = log;
            }

            long dp() {
                if (visited)
                    return 0L;
                visited = true;
                long r = 0;
                for (Vertex v : edges) {
                    if (v != null) {
                        long a = v.dp();
                        r = Math.max(r, a);
                        terminals += v.terminals;
                    }
                }
                return Math.max(r, (long) log * terminals);
            }
        }

        public SuffixAutomata(String str) {
            last = root = new Vertex(0);
            for (int i = 0; i < str.length(); i++) {
                addChar(str.charAt(i));
            }
            addTerminal();
        }

        private void addChar(char c) {
            Vertex cur = last;
            last = new Vertex(cur.log + 1);
            while (cur != null && cur.edges[c - 'a'] == null) {
                cur.edges[c - 'a'] = last;
                cur = cur.suffixLink;
            }
            if (cur != null) {
                Vertex q = cur.edges[c - 'a'];
                if (q.log == cur.log + 1) {
                    last.suffixLink = q;
                } else {
                    Vertex r = new Vertex(q, cur.log + 1);
                    r.suffixLink = q.suffixLink;
                    q.suffixLink = r;
                    last.suffixLink = r;
                    while (cur != null) {
                        if (cur.edges[c - 'a'] == q) {
                            cur.edges[c - 'a'] = r;
                        } else {
                            break;
                        }
                        cur = cur.suffixLink;
                    }
                }
            } else {
                last.suffixLink = root;
            }
        }

        private void addTerminal() {
            Vertex cur = last;
            while (cur != null) {
                cur.terminals++;
                cur = cur.suffixLink;
            }
        }
    }

    static class FastScanner {
        private static BufferedReader reader;
        private static StringTokenizer tokenizer;

        public FastScanner(InputStream in) {
            try {
                reader = new BufferedReader(new InputStreamReader(in));
                tokenizer = new StringTokenizer(reader.readLine().trim());
            } catch (IOException e) {
                System.err.println("cannot read input");
            }
        }

        public int numTokens() throws IOException {
            if (!tokenizer.hasMoreTokens()) {
                tokenizer = new StringTokenizer(reader.readLine().trim());
                return numTokens();
            } else {
                return tokenizer.countTokens();
            }
        }

        public boolean hasNext() throws IOException {
            if (!tokenizer.hasMoreTokens()) {
                tokenizer = new StringTokenizer(reader.readLine().trim());
                return hasNext();
            } else {
                return true;
            }
        }

        public String next() throws IOException {
            if (!tokenizer.hasMoreTokens()) {
                tokenizer = new StringTokenizer(reader.readLine().trim());
                return next();
            } else {
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
