package com.coding.algorithm.graph;

import java.util.*;

public class Dijkstras {
    private static final double EPSILON = 1e-6;

    static class Edge {
        double cost;
        int from, to;

        public Edge(int from, int to, double cost) {
            this.from = from;
            this.to = to;
            this.cost = cost;
        }
    }

    static class Node implements Comparable<Node> {
        int id;
        double value;

        public Node(int id, double value) {
            this.id = id;
            this.value = value;
        }

        @Override
        public int compareTo(Node o) {
            return Double.compare(this.value, o.value);
        }
    }

    private int n;
    private double[] dist;
    private Integer[] prev;
    private List<List<Edge>> graph; // <from, <to, cost>>

    public Dijkstras(int n) {
        this.n = n;
        buildEmptyGraph();
    }

    public void addEdge(int from, int to, double cost) {
        graph.get(from).add(new Edge(from, to, cost));
    }

    public List<List<Edge>> getGraph() {
        return graph;
    }

    public List<Integer> reconstructPath(int start, int end) {
        if (end < 0 || end >= n || start < 0 || start >= n)
            throw new IllegalArgumentException();
        double dist = solve(start, end);
        LinkedList<Integer> path = new LinkedList<>();
        if (dist == Double.POSITIVE_INFINITY)
            return path;
        for (Integer s = end; s != null; s = prev[s])
            path.addFirst(s);
        return path;
    }


    public double solve(int start, int end) {
        dist = new double[n];
        Arrays.fill(dist, Double.POSITIVE_INFINITY);
        dist[start] = 0;
        PriorityQueue<Node> pq = new PriorityQueue<>();
        pq.offer(new Node(start, 0));
        boolean[] visited = new boolean[n];
        prev = new Integer[n];
        while (!pq.isEmpty()) {
            Node node = pq.poll();
            visited[node.id] = true;
            if (dist[node.id] < node.value)
                continue;
            List<Edge> edges = graph.get(node.id);
            for (Edge edge : edges) {
                if (visited[edge.to])
                    continue;
                double newDist = dist[node.id] + edge.cost;
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

    private void buildEmptyGraph() {
        graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }
    }

}
