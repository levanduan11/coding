package com.coding.algorithm.graph;

import java.util.*;

public class PathBasedStrongComponent {
    private int index;
    private List<List<Integer>> adjList;
    private boolean[] visited;
    private Stack<Integer> S, P;
    private List<List<Integer>> sccs;

    public PathBasedStrongComponent(List<List<Integer>> adjList) {
        this.adjList = adjList;
        visited = new boolean[adjList.size()];
        S = P = new Stack<>();
        sccs = new ArrayList<>();
    }

    public List<List<Integer>> findSCCS() {
        Arrays.fill(visited, false);
        for (int i = 0; i < adjList.size(); i++) {
            if (!visited[i]) {
                dfs(i);
            }
        }
        return sccs;
    }

    private void dfs(int v) {
        int depth = index++;
        visited[v] = true;
        S.push(v);
        P.push(v);
        boolean root = true;
        for (int w : adjList.get(v)) {
            if (!visited[w]) {
                dfs(w);
            } else if (P.contains(w)) {
                while (P.peek() != w) {
                    P.pop();
                }
            }
        }
        if (root) {
            List<Integer> scc = new ArrayList<>();
            int w;
            do {
                w = S.pop();
                scc.add(w);
                P.remove(w);
            } while (w != v);
        }
    }

}
