package com.coding.algorithm.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class Prims {
    static class Edge implements Comparable<Edge> {
        int from, to, cost;

        @Override
        public int compareTo(Edge o) {
            return Integer.compare(cost, o.cost);
        }

        public Edge(int from, int to, int cost) {
            this.from = from;
            this.to = to;
            this.cost = cost;
        }
    }

    private final int n;
    private final List<List<Edge>> graph;
    private boolean solved;
    private boolean mstExists;
    private boolean[] visited;
    private PriorityQueue<Edge> pq;
    private long minCostSum;
    private Edge[] mstEdges;

    public Prims(List<List<Edge>> graph) {
        if (graph == null || graph.isEmpty())
            throw new IllegalArgumentException();
        this.n = graph.size();
        this.graph = graph;
    }

    public Edge[] getMst() {
        solve();
        return mstExists ? mstEdges : null;
    }

    public Long getMstCost() {
        solve();
        return mstExists ? minCostSum : null;
    }

    private void addEdges(int nodeIndex) {
        visited[nodeIndex] = true;
        List<Edge> edges = graph.get(nodeIndex);
        for (Edge e : edges) {
            if (!visited[e.to])
                pq.offer(e);
        }
    }

    private void solve() {
        if (solved)
            return;
        solved = true;
        int m = n - 1, edgeCount = 0;
        pq = new PriorityQueue<>();
        visited = new boolean[n];
        mstEdges = new Edge[m];
        addEdges(0);
        while (!pq.isEmpty() && edgeCount != m) {
            Edge edge = pq.poll();
            int nodeIndex = edge.to;
            if (visited[nodeIndex])
                continue;
            mstEdges[edgeCount++] = edge;
            minCostSum += edge.cost;
            addEdges(nodeIndex);
        }
        mstExists = (edgeCount == m);
    }

    static List<List<Edge>> createEmptyGraph(int n) {
        List<List<Edge>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }
        return graph;
    }

    static void addDirectedEdge(List<List<Edge>> g, int from, int to, int cost) {
        g.get(from).add(new Edge(from, to, cost));
    }

    static void addUndirectedEdge(List<List<Edge>> g, int from, int to, int cost) {
        addDirectedEdge(g, from, to, cost);
        addDirectedEdge(g, to, from, cost);
    }
}
