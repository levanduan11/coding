package com.coding.algorithm.graph.networkflow;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.Map;

public class Dinics extends NetworkFlowSolverBase {
    private int[] level;

    public Dinics(int n, int s, int t) {
        super(n, s, t);
        level = new int[n];
    }

    @Override
    public void solve() {
        int[] next = new int[n];
        while (bfs()) {
            Arrays.fill(next, 0);
            for (long f = dfs(s, next, INF); f != 0; f = dfs(s, next, INF)) {
                maxFlow += f;
            }
        }
        for (int i = 0; i < n; i++) {
            if (level[i] != -1)
                minCut[i] = true;
        }
    }

    private boolean bfs() {
        Arrays.fill(level, -1);
        level[s] = 0;
        Deque<Integer> q = new ArrayDeque<>();
        q.offer(s);
        while (!q.isEmpty()) {
            int node = q.poll();
            for (Edge edge : graph[node]) {
                long cap = edge.remainingCapacity();
                if (cap > 0 && level[edge.to] == -1) {
                    level[edge.to] = level[node] + 1;
                    q.offer(edge.to);
                }
            }
        }
        return level[t] != -1;
    }

    private long dfs(int at, int[] next, long flow) {
        if (at == t)
            return flow;
        final int numEdges = graph[at].size();
        for (; next[at] < numEdges; next[at]++) {
            Edge edge = graph[at].get(next[at]);
            long cap = edge.remainingCapacity();
            if (cap > 0 && level[edge.to] == level[at] + 1) {
                long bottleNeck = dfs(edge.to, next, Math.min(flow, cap));
                if (bottleNeck > 0) {
                    edge.augment(bottleNeck);
                    return bottleNeck;
                }
            }
        }
        return 0;
    }
}
