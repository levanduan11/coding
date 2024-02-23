package com.coding.algorithm.graph.networkflow;

import java.util.List;

public class CapacityScalingSolverAdjacencyList extends NetworkFlowSolverBase {
    private long delta;

    public CapacityScalingSolverAdjacencyList(int n, int s, int t) {
        super(n, s, t);
    }

    @Override
    public void addEdge(int from, int to, long capacity) {
        super.addEdge(from, to, capacity);
        delta = Math.max(delta, capacity);
    }

    @Override
    public void solve() {
        delta = Long.highestOneBit(delta);
        for (long f; delta > 0; delta /= 2) {
            do {
                markAllNodesAsUnvisited();
                f = dfs(s, INF);
                maxFlow += f;
            } while (f != 0);
        }
        for (int i = 0; i < n; i++) {
            if (visited(i))
                minCut[i] = true;
        }
    }

    private long dfs(int node, long flow) {
        if (node == t)
            return flow;
        List<Edge> edges = graph[node];
        visited(node);
        for (Edge edge : edges) {
            long cap = edge.remainingCapacity();
            if (cap >= delta && !visited(edge.to)) {
                long bottleNeck = dfs(edge.to, Math.min(flow, cap));
                if (bottleNeck > 0) {
                    edge.augment(bottleNeck);
                    return bottleNeck;
                }
            }
        }
        return 0;
    }
}
