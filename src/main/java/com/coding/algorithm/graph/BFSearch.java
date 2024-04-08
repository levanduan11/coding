package com.coding.algorithm.graph;

import java.util.*;

public class BFSearch {
    static class Edge {
        int from, to, cost;

        public Edge(int from, int to, int cost) {
            this.from = from;
            this.to = to;
            this.cost = cost;
        }
    }

    private int n;
    private Integer[] prev;
    private List<List<Edge>> graph;

    public BFSearch(List<List<Edge>> graph) {
        if (graph == null)
            throw new IllegalArgumentException("Graph can't be null");
        n = graph.size();
        this.graph = graph;
        prev = new Integer[n];
    }

    public List<Integer> reconstructPath(int start, int end) {
        bfs(start);
        LinkedList<Integer> path = new LinkedList<>();
        for (Integer at = end; at != null; at = prev[at])
            path.addFirst(at);
        if (path.getFirst() == start)
            return path;
        path.clear();
        return path;
    }

    private void bfs(int start) {
        boolean[] visited = new boolean[n];
        Deque<Integer> queue = new ArrayDeque<>();

        queue.offer(start);
        visited[start] = true;

        while (!queue.isEmpty()) {
            int node = queue.poll();
            visited[node] = true;
            for (Edge edge : graph.get(node)) {
                if (!visited[edge.to]) {
                    visited[edge.to] = true;
                    prev[edge.to] = node;
                    queue.offer(edge.to);
                }
            }
        }
    }

    public static List<List<Edge>> createEmptyGraph(int n) {
        List<List<Edge>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }
        return graph;
    }

    public static void addDirectedEdge(List<List<Edge>> graph, int from, int to, int cost) {
        graph.get(from).add(new Edge(from, to, cost));
    }

    public static void addUndirectedEdge(List<List<Edge>> graph, int from, int to, int cost) {
        addDirectedEdge(graph, from, to, cost);
        addDirectedEdge(graph, to, from, cost);
    }
}
