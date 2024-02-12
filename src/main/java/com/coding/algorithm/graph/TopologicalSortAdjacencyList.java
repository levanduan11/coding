package com.coding.algorithm.graph;

import java.util.List;
import java.util.Map;

public class TopologicalSortAdjacencyList {
    public static class Edge {
        int from, to, weight;

        public Edge(int from, int to, int weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }
    }

    private static int dfs(
            int i, int at, boolean[] visited, int[] ordering, Map<Integer, List<Edge>> graph) {
        visited[at] = true;
        List<Edge> edges = graph.get(at);
        if (edges != null) {
            for (Edge edge : edges) {
                if (!visited[edge.to]) {
                    i = dfs(i, edge.to, visited, ordering, graph);
                }
            }
        }
        ordering[i] = at;
        return i - 1;
    }

    public static int[] topologicalSort(Map<Integer, List<Edge>> graph, int numNodes) {
        int[] ordering = new int[numNodes];
        boolean[] visited = new boolean[numNodes];
        int i = numNodes - 1;
        for (int at = 0; at < numNodes; at++) {
            if (!visited[at]) {
                i = dfs(i, at, visited, ordering, graph);
            }
        }
        return ordering;
    }

    public static Integer[] dagShortestPath(Map<Integer, List<Edge>> graph, int start, int numNodes) {
        int[] topsort = topologicalSort(graph, numNodes);
        Integer[] dist = new Integer[numNodes];
        dist[start] = 0;
        for (int i = 0; i < numNodes; i++) {
            int nodeIndex = topsort[i];
            if (dist[nodeIndex] != null) {
                List<Edge> adjacentEdges = graph.get(nodeIndex);
                if (adjacentEdges != null) {
                    for (Edge edge : adjacentEdges) {
                        int newDist = dist[nodeIndex] + edge.weight;
                        if (dist[edge.to] == null)
                            dist[edge.to] = newDist;
                        else
                            dist[edge.to] = Math.min(dist[edge.to], newDist);
                    }
                }
            }
        }
        return dist;
    }
}
