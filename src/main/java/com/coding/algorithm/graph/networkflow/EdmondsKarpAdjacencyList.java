package com.coding.algorithm.graph.networkflow;

import com.coding.algorithm.graph.networkflow.NetworkFlowSolverBase;

import java.util.ArrayDeque;
import java.util.Queue;

public class EdmondsKarpAdjacencyList extends NetworkFlowSolverBase {
    public EdmondsKarpAdjacencyList(int n, int s, int t) {
        super(n, s, t);
    }

    @Override
    public void solve() {
        long flow;
        do {
            markAllNodesAsUnvisited();
            flow = bfs();
            maxFlow += flow;
        } while (flow != 0);
        for (int i = 0; i < n; i++) {
            if (visited(i))
                minCut[i] = true;
        }
    }

    private long bfs() {
        Edge[] prev = new Edge[n];
        Queue<Integer> q = new ArrayDeque<>();
        visited(s);
        q.offer(s);
        while (!q.isEmpty()) {
            int node = q.poll();
            if (node == t)
                break;
            for (Edge edge : graph[node]) {
                long cap = edge.remainingCapacity();
                if (cap > 0 && !visited(edge.to)) {
                    visited(edge.to);
                    prev[edge.to] = edge;
                    q.offer(edge.to);
                }
            }
        }
        if (prev[t] == null)
            return 0;
        long bottleNeck = Long.MAX_VALUE;
        for (Edge edge = prev[t]; edge != null; edge = prev[edge.from]) {
            bottleNeck = Math.min(bottleNeck, edge.remainingCapacity());
        }
        for (Edge edge = prev[t]; edge != null; edge = prev[edge.from])
            edge.augment(bottleNeck);
        return bottleNeck;
    }
}
