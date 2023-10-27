package com.coding.dsa.string;

import com.coding.dsa.graph.Digraph;
import com.coding.dsa.graph.DirectedDFS;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

public class NFA {
    private Digraph graph;
    private String regexp;
    private final int m;

    public NFA(String regexp) {
        this.regexp = regexp;
        m = regexp.length();
        Deque<Integer> ops = new ArrayDeque<>();
        graph = new Digraph(m + 1);
        for (int i = 0; i < m; i++) {
            int lp = i;
            char ch = regexp.charAt(i);
            if (ch == '(' || ch == '|')
                ops.push(i);
            else if (ch == ')') {
                int or = ops.pop();

                if (regexp.charAt(or) == '|') {
                    lp = ops.pop();
                    graph.addEdge(lp, or + 1);
                    graph.addEdge(or, i);
                } else if (regexp.charAt(or) == '(')
                    lp = or;
                else assert false;
            }
            if (i < m - 1 && regexp.charAt(i + 1) == '*') {
                graph.addEdge(lp, i + 1);
                graph.addEdge(i + 1, lp);
            }
            if (ch == '(' || ch == '*' || ch == ')')
                graph.addEdge(i, i + 1);
        }
        if (!ops.isEmpty())
            throw new IllegalArgumentException();
    }

    public boolean recognizes(String txt) {
        DirectedDFS dfs = new DirectedDFS(graph, 0);
        Set<Integer> pc = new HashSet<>();
        for (int v = 0; v < graph.V(); v++) {
            if (dfs.marked(v))
                pc.add(v);
        }
        for (int i = 0; i < txt.length(); i++) {
            char ch = txt.charAt(i);
            if (ch == '*' || ch == '|' || ch == '(' || ch == ')')
                throw new IllegalArgumentException();
            Set<Integer> match = new HashSet<>();
            for (int v : pc) {
                if (v == m)
                    continue;
                char c = regexp.charAt(v);
                if (ch == c || ch == '.')
                    match.add(v + 1);
            }
            if (match.isEmpty())
                continue;
            dfs = new DirectedDFS(graph, match);
            pc = new HashSet<>();
            for (int v = 0; v < graph.V(); v++) {
                if (dfs.marked(v))
                    pc.add(v);
            }
            if (pc.isEmpty())
                return false;
        }
        for (int v : pc) {
            if (v == m)
                return true;
        }
        return false;
    }

    public static void main(String[] args) {
        String p = "((A*B|AC)D)";
        String txt = "AAAAABD";
        var s = new NFA(p);
        var res = s.recognizes(txt);
    }
}
