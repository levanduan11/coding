package com.coding.algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChuliuEdmonds {
    static class Graph {
        public Graph(int v) {
            V = v;
            this.edges = new ArrayList<>();
        }

        int V;
        List<Edge> edges;

        public void addEdge(int u, int v, int weight) {
            edges.add(new Edge(u, v, weight));
        }

        public List<Edge> findMinimumSpanning(int root) {
            List<Edge> result = new ArrayList<>();
            int[] parent = new int[V];
            for (int i = 0; i < V; i++) {
                if (i == root)
                    continue;
                int minWeight = Integer.MAX_VALUE;
                for (Edge edge : edges) {
                    if (edge.end == i && edge.weight < minWeight) {
                        parent[i] = edge.start;
                        minWeight = edge.weight;
                    }
                }
            }
            boolean[] visited = new boolean[V];
            Arrays.fill(visited, false);
            visited[root] = true;
            for (int i = 0; i < V; i++) {
                if (i == root || visited[i])
                    continue;
                List<Integer> cycle = new ArrayList<>();
                while (!visited[i]) {
                    visited[i] = true;
                    cycle.add(i);
                    i = parent[i];
                }
                int minWeight = Integer.MAX_VALUE;
                Edge minWeightEdge = null;
                for (int node : cycle) {
                    for (Edge edge : edges) {
                        if (edge.start == parent[node] && edge.end == node && edge.weight < minWeight) {
                            minWeight = edge.weight;
                            minWeightEdge = edge;
                        }
                    }
                }
                for (int node : cycle) {
                    result.removeIf(edge -> edge.end == node);
                    result.add(minWeightEdge);
                }
            }
            return result;
        }
    }

    static class Edge {
        int start;
        int end;
        int weight;

        public Edge(int start, int end, int weight) {
            this.start = start;
            this.end = end;
            this.weight = weight;
        }
    }
}
