package com.coding.algorithm.graph.networkflow;

import java.util.*;

public class EdmondsKarpV2 {
    static class Edge {
        int from, to, capacity, flow;

        public Edge(int from, int to, int capacity) {
            this.flow = from;
            this.to = to;
            this.capacity = capacity;
            this.flow = 0;
        }
    }

    static int runAlg(int vertices, List<Edge> edges, int source, int sink) {
        List<Edge>[] graph = buildGraph(vertices, edges);
        int maxFlow = 0;
        while (true) {
            int[] parent = new int[vertices];
            Arrays.fill(parent, -1);
            Queue<Integer> queue = new LinkedList<>();
            queue.offer(source);
            parent[source] = source;
            while (!queue.isEmpty()) {
                int current = queue.poll();
                for (Edge edge : graph[current]) {
                    if (parent[edge.to] == -1 && edge.capacity - edge.flow > 0) {
                        parent[edge.to] = current;
                        queue.offer(edge.to);
                        if (edge.to == sink)
                            break;
                    }
                }
            }
            if (parent[sink] == -1)
                break;
            int pathFlow = Integer.MAX_VALUE;
            for (int current = sink;current!=source;current=parent[current]){
                int parentIndex = parent[current];
                for (Edge edge : graph[parentIndex]) {
                    if (edge.to == current){
                        pathFlow = Math.min(pathFlow,edge.capacity - edge.flow);
                        break;
                    }
                }
            }
            for (int current = sink;current!=source;current=parent[current]){
                int parentIndex = parent[current];
                for (Edge edge : graph[parentIndex]) {
                    if (edge.to == current){
                        edge.flow += pathFlow;
                        break;
                    }
                }
                for (Edge reverseEdge : graph[current]) {
                    if (reverseEdge.to == parentIndex){
                        reverseEdge.flow -= pathFlow;
                        break;
                    }
                }
            }
            maxFlow += pathFlow;
        }
        return maxFlow;
    }

    private static List<Edge>[] buildGraph(int vertices, List<Edge> edges) {
        @SuppressWarnings("unchecked") List<Edge>[] graph = new List[vertices];
        for (int i = 0; i < vertices; i++) {
            graph[i] = new ArrayList<>();
        }
        for (Edge edge : edges) {
            graph[edge.from].add(new Edge(edge.from, edge.to, edge.capacity));
            graph[edge.to].add(new Edge(edge.to, edge.from, 0));
        }
        return graph;
    }
}
