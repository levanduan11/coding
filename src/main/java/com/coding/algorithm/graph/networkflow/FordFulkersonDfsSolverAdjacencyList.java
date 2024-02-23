package com.coding.algorithm.graph.networkflow;

import java.util.List;

public class FordFulkersonDfsSolverAdjacencyList extends NetworkFlowSolverBase {

    public FordFulkersonDfsSolverAdjacencyList(int n, int s, int t) {
        super(n, s, t);
    }

    @Override
    public void solve() {
        for (long f = dfs(s, INF); f != 0; f = dfs(s, INF)) {
            markAllNodesAsUnvisited();
            maxFlow += f;
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
            long rcap = edge.remainingCapacity();
            if (rcap > 0 && !visited(edge.to)) {
                long bottleNeck = dfs(edge.to, Math.min(flow, rcap));
                if (bottleNeck > 0) {
                    edge.augment(bottleNeck);
                    return bottleNeck;
                }
            }
        }
        return 0;
    }
}
