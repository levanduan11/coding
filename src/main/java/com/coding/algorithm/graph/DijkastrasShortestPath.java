package com.coding.algorithm.graph;

import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

public class DijkastrasShortestPath {

    private static final double EPS = 1e-6;

    static class Edge {
        int from, to;
        double cost;

        public Edge(int from, int to, double cost) {
            this.from = from;
            this.to = to;
            this.cost = cost;
        }
    }

    static class Node implements Comparable<Node> {
        int id;
        double value;

        public Node(int id, double cost) {
            this.id = id;
            this.value = cost;
        }

        @Override
        public int compareTo(Node o) {
            return Double.compare(this.value, o.value);
        }
    }

    private int n;
    private double[] dist;
    private Integer[] prev;
    private List<List<Edge>> graph;

    public void addEdge(int from, int to, double cost) {
        graph.get(from).add(new Edge(from, to, cost));
    }

    public double runDijsktra(int start, int end) {
        dist = new double[n];
        Arrays.fill(dist, Double.POSITIVE_INFINITY);
        prev = new Integer[n];
        dist[start] = 0;

        PriorityQueue<Node> pq = new PriorityQueue<>();
        pq.offer(new Node(start, 0));

        boolean[] visited = new boolean[n];
        while (!pq.isEmpty()) {
            Node node = pq.poll();
            visited[node.id] = true;
            if (dist[node.id] < node.value)
                continue;
            for (Edge edge : graph.get(node.id)) {
                if (visited[edge.to])
                    continue;
                double newDist = dist[edge.from] + edge.cost;
                if (newDist < dist[edge.to]) {
                    dist[edge.to] = newDist;
                    prev[edge.to] = edge.from;
                    pq.offer(new Node(edge.to, dist[edge.to]));
                }
            }
            if (node.id == end)
                return dist[end];
        }
        return Double.POSITIVE_INFINITY;
    }
}
