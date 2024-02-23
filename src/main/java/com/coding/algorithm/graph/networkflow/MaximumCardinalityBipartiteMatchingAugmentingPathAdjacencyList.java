package com.coding.algorithm.graph.networkflow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MaximumCardinalityBipartiteMatchingAugmentingPathAdjacencyList {
    static final int FREE = -1;
    static int visitToken = 1;

    public static int mcbm(List<List<Integer>> graph, int n, int m) {
        int N = n + m, matches = 0;
        int[] visited = new int[n];
        int[] next = new int[N];
        Arrays.fill(next, FREE);
        for (int i = 0; i < n; i++) {
            matches += augment(graph, visited, next, i);
            visitToken++;
        }
        return matches;
    }

    private static int augment(List<List<Integer>> graph, int[] visited, int[] next, int at) {
        if (visited[at] == visitToken)
            return 0;
        for (int node : graph.get(at)) {
            int oppositeNode = next[node];
            if (oppositeNode == FREE) {
                next[node] = at;
                return 1;
            }
            if (augment(graph, visited, next, oppositeNode) != 0) {
                next[node] = at;
                return 1;
            }
        }
        return 0;
    }

    private static List<List<Integer>> createEmptyGraph(int n) {
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }
        return graph;
    }

    private static void addEdge(List<List<Integer>> graph, int from, int to) {
        graph.get(from).add(to);
    }
}
