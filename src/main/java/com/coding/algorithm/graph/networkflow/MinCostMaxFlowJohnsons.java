package com.coding.algorithm.graph.networkflow;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

public class MinCostMaxFlowJohnsons extends NetworkFlowSolverBase {

    public MinCostMaxFlowJohnsons(int n, int s, int t) {
        super(n, s, t);
    }

    @Override
    public void solve() {
        init();
        List<Edge> path;
        while (!(path = getAugmentPath()).isEmpty()) {
            long bottleNeck = Long.MAX_VALUE;
            for (Edge edge : path) {
                bottleNeck = Math.min(bottleNeck, edge.remainingCapacity());
            }
            for (Edge edge : path) {
                edge.augment(bottleNeck);
                minCost += bottleNeck * edge.originalCost;
            }
            maxFlow += bottleNeck;
        }
    }

    private List<Edge> getAugmentPath() {
        class Node implements Comparable<Node> {
            int id;
            long value;

            public Node(int id, long value) {
                this.id = id;
                this.value = value;
            }

            @Override
            public int compareTo(Node o) {
                return Long.compare(this.value, o.value);
            }
        }
        long[] dist = new long[n];
        Arrays.fill(dist, INF);
        dist[s] = 0;
        markAllNodesAsUnvisited();
        Edge[] prev = new Edge[n];
        PriorityQueue<Node> pq = new PriorityQueue<>();
        pq.offer(new Node(s, 0));
        while (!pq.isEmpty()) {
            Node node = pq.poll();
            visit(node.id);
            if (dist[node.id] < node.value)
                continue;
            List<Edge> edges = graph[node.id];
            for (Edge edge : edges) {
                if (visited(edge.to))
                    continue;
                long newDist = dist[edge.from] + edge.cost;
                if (edge.remainingCapacity() > 0 && newDist < dist[edge.to]) {
                    prev[edge.to] = edge;
                    dist[edge.to] = newDist;
                    pq.offer(new Node(edge.to, dist[edge.to]));
                }
            }
        }
        LinkedList<Edge> path = new LinkedList<>();
        if (dist[t] == INF)
            return path;
        adjustEdgeCosts(dist);
        for (Edge edge = prev[t]; edge != null; edge = prev[edge.from])
            path.addFirst(edge);
        return path;
    }

    private void init() {
        long[] dist = new long[n];
        Arrays.fill(dist, INF);
        dist[s] = 0;
        for (int i = 0; i < n - 1; i++) {
            for (List<Edge> edges : graph) {
                for (Edge edge : edges) {
                    if (edge.remainingCapacity() > 0 && dist[edge.from] + edge.cost < dist[edge.to])
                        dist[edge.to] = dist[edge.from] + edge.cost;
                }
            }
        }
        adjustEdgeCosts(dist);
    }

    private void adjustEdgeCosts(long[] dist) {
        for (int from = 0; from < n; from++) {
            for (Edge edge : graph[from]) {
                if (edge.remainingCapacity() > 0)
                    edge.cost += dist[from] - dist[edge.to];
                else
                    edge.cost = 0;
            }
        }
    }
}


