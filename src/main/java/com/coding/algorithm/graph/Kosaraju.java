package com.coding.algorithm.graph;

import java.util.*;

public class Kosaraju {
    private final List<List<Integer>> graph;
    private final List<List<Integer>> transposeGraph;
    private final boolean[] visited;
    private final Deque<Integer> stack;

    public Kosaraju(int numberOfVertices) {
        graph = new ArrayList<>();
        transposeGraph = new ArrayList<>();
        visited = new boolean[numberOfVertices];
        stack = new ArrayDeque<>();
        for (int i = 0; i < numberOfVertices; i++) {
            graph.add(new ArrayList<>());
            transposeGraph.add(new ArrayList<>());
        }
    }

    public void addEdge(int from, int to) {
        graph.get(from).add(to);
        transposeGraph.get(to).add(from);
    }

    private void fillOrder(int v) {
        visited[v] = true;
        for (int n : graph.get(v)) {
            if (!visited[n]) {
                fillOrder(n);
            }
        }
        stack.push(v);
    }

    private void dfs(int v, List<Integer> component) {
        visited[v] = true;
        component.add(v);
        for (int n : transposeGraph.get(v)) {
            if (!visited[n]) {
                dfs(n, component);
            }
        }
    }

    public List<List<Integer>> findSCCs() {
        Arrays.fill(visited, false);
        for (int i = 0; i < graph.size(); i++) {
            if (!visited[i]) {
                fillOrder(i);
            }
        }
        Arrays.fill(visited, false);
        List<List<Integer>> sccs = new ArrayList<>();
        while (!stack.isEmpty()) {
            int v = stack.pop();
            if (!visited[v]) {
                List<Integer> component = new ArrayList<>();
                dfs(v, component);
                sccs.add(component);
            }
        }
        return sccs;
    }
}
