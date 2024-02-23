package com.coding.algorithm.graph.networkflow;

public class FordFulkersonDfsSolverAdjacencyMatrix {
    static int visitedToken = 1;

    public static int fordFulkerson(int[][] caps, int source, int sink) {
        int n = caps.length;
        int[] visited = new int[n];
        boolean[] minCut = new boolean[n];
        for (int maxFlow = 0; ; ) {
            int flow = dfs(caps, visited, source, sink, Integer.MAX_VALUE);
            visitedToken++;
            maxFlow += flow;
            if (flow == 0)
                return maxFlow;
        }
    }

    private static int dfs(int[][] caps, int[] visited, int node, int sink, int flow) {
        if (node == sink)
            return flow;

        int[] cap = caps[node];
        visited[node] = visitedToken;
        for (int i = 0; i < cap.length; i++) {
            if (visited[i] != visitedToken && cap[i] > 0) {
                if (cap[i] < flow)
                    flow = cap[i];
                int dfsFlow = dfs(caps, visited, i, sink, flow);
                if (dfsFlow > 0) {
                    caps[node][i] -= dfsFlow;
                    caps[i][node] += dfsFlow;
                    return dfsFlow;
                }
            }
        }
        return 0;
    }
}
