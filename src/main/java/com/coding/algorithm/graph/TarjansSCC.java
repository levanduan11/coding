package com.coding.algorithm.graph;

import java.util.*;

public class TarjansSCC {
    private List<List<Integer>> graph;
    private List<List<Integer>> sccs;
    private int[] low;
    private int[] ids;
    private boolean[] onStack;
    private Deque<Integer> stack;
    private int id;
    private int sccCount;

    public TarjansSCC(List<List<Integer>> graph) {
        this.graph = graph;
        int n = graph.size();
        low = new int[n];
        ids = new int[n];
        onStack = new boolean[n];
        stack = new ArrayDeque<>();
        sccs = new ArrayList<>();
        Arrays.fill(ids, -1);
    }

    public List<List<Integer>> findSCCs() {
        for (int i = 0; i < graph.size(); i++) {
            if (ids[i] == -1) {
                dfs(i);
            }
        }
        return sccs;
    }

    public void dfs(int at) {
        stack.push(at);
        onStack[at] = true;
        ids[at] = low[at] = id++;

        for (int to : graph.get(at)) {
            if (ids[to] == -1) {
                dfs(to);
                low[at] = Math.min(low[at], low[to]);
            } else {
                low[at] = Math.min(low[at], ids[to]);
            }
        }
        if (ids[at] == low[at]) {
            List<Integer> scc = new ArrayList<>();
            for (int node = stack.pop(); ; node = stack.pop()) {
                onStack[node] = false;
                scc.add(node);
                if (node == at) {
                    break;
                }
                sccs.add(scc);
                sccCount++;
            }
        }
    }
}
