package com.coding.algorithm.graph.networkflow;

import java.util.List;
import java.util.Objects;

public class BipartiteGraphCheckAdjacencyList {
    public static final int RED = 0b10, BLACK = (RED ^ 1);
    private int n;
    private int[] colors;
    private boolean solved;
    private boolean isBipartite;
    private List<List<Integer>> graph;

    public BipartiteGraphCheckAdjacencyList(List<List<Integer>> graph) {
        Objects.requireNonNull(graph);
        n = graph.size();
        this.graph = graph;
    }

    public boolean isBipartite() {
        if (!solved)
            solve();
        return isBipartite;
    }

    public int[] getTwoColoring() {
        return isBipartite() ? colors : null;
    }

    private void solve() {
        if (n <= 1)
            return;
        colors = new int[n];
        int nodesVisited = colorGraph(0, RED);
        isBipartite = (nodesVisited == n);
        solved = true;
    }

    private int colorGraph(int i, int color) {
        colors[i] = color;
        int nextColor = (color ^ 1);
        int visitCount = 1;
        List<Integer> edges = graph.get(i);
        for (int to : edges) {
            if (colors[to] == color)
                return -1;
            if (colors[to] == nextColor)
                continue;
            int count = colorGraph(to, nextColor);
            if (count == -1)
                return -1;
            visitCount += count;
        }
        return visitCount;
    }
}
