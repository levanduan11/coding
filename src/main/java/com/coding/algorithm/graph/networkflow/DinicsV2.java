package com.coding.algorithm.graph.networkflow;

import java.util.*;

public class DinicsV2 {
    static class FlowEdge {
        int from, to, capacity, flow;

        public FlowEdge(int from, int to, int capacity) {
            this.from = from;
            this.to = to;
            this.capacity = capacity;
            this.flow = 0;
        }
    }

    private static int runDinics(int vertices, List<FlowEdge> edges, int source, int sink) {
        List<FlowEdge>[] graph = buildGraph(vertices, edges);
        int maxFlow = 0;
        int[] dist;
        while ((dist = bfs(graph, source, sink)) != null) {
            int blockingFlow;
            while ((blockingFlow = blockingFlow(source, sink, Integer.MAX_VALUE, graph, dist)) > 0) {
                maxFlow += blockingFlow;
            }
        }
        return maxFlow;
    }

    private static List<FlowEdge>[] buildGraph(int vertices, List<FlowEdge> edges) {
        @SuppressWarnings("unchecked") List<FlowEdge>[] graph = new ArrayList[vertices];
        for (int i = 0; i < vertices; i++) {
            graph[i] = new ArrayList<>();
        }
        for (FlowEdge edge : edges) {
            graph[edge.from].add(new FlowEdge(edge.from, edge.to, edge.capacity));
            graph[edge.to].add(new FlowEdge(edge.to, edge.from, 0));
        }
        return graph;
    }

    private static int[] bfs(List<FlowEdge>[] graph, int source, int sink) {
        int[] dist = new int[graph.length];
        Arrays.fill(dist, -1);
        dist[source] = 0;
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(source);
        while (!queue.isEmpty()) {
            int current = queue.poll();
            for (FlowEdge edge : graph[current]) {
                if (dist[edge.to] == -1 && edge.capacity - edge.flow > 0) {
                    dist[edge.to] = dist[current] + 1;
                    queue.offer(edge.to);
                }
            }
        }
        return (dist[sink] != -1) ? dist : null;
    }

    private static int blockingFlow(int source, int sink, int flow, List<FlowEdge>[] graph, int[] dist) {
        if (source == sink)
            return flow;
        else {
            for (FlowEdge edge : graph[source]) {
                if (dist[edge.to] == dist[source] + 1 && edge.capacity - edge.flow > 0) {
                    int blockingFlow = blockingFlow(edge.to, sink, Math.min(flow, edge.capacity - edge.flow), graph, dist);
                    if (blockingFlow > 0) {
                        edge.flow += blockingFlow;
                        for (FlowEdge reverseEdge : graph[edge.to]) {
                            if (reverseEdge.to == source) {
                                reverseEdge.flow -= blockingFlow;
                                break;
                            }
                        }
                        return blockingFlow;
                    }
                }
            }
        }
        return 0;
    }
}
