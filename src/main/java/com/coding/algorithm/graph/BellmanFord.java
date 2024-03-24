package com.coding.algorithm.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BellmanFord {
    public static class Edge {
        double cost;
        int from, to;

        public Edge(int from, int to, double cost) {
            this.from = from;
            this.to = to;
            this.cost = cost;
        }
    }

    @SuppressWarnings("unchecked")
    public static List<Edge>[] createGraph(final int V) {
        List<Edge>[] graph = new List[V];
        for (int i = 0; i < V; i++) {
            graph[i] = new ArrayList<>();
        }
        return graph;
    }

    public static void addEdge(List<Edge>[] graph, int from, int to, double cost) {
        graph[from].add(new Edge(from, to, cost));
    }

    public static double[] bellmanFord(List<Edge>[] graph, int V, int start) {
        double[] dist = new double[V];
        Arrays.fill(dist, Double.POSITIVE_INFINITY);
        dist[start] = 0;
        for (int i = 0; i < V - 1; i++) {
            for (List<Edge> edges : graph) {
                for (Edge edge : edges) {
                    if (dist[edge.from] + edge.cost < dist[edge.to])
                        dist[edge.to] = dist[edge.from] + edge.cost;
                }
            }
        }
        for (int i = 0; i < V - 1; i++) {
            for (List<Edge> edges : graph) {
                for (Edge edge : edges) {
                    if (dist[edge.from] + edge.cost < dist[edge.to])
                        dist[edge.to] = Double.NEGATIVE_INFINITY;
                }
            }
        }
        return dist;
    }
}
