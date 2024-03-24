package com.coding.algorithm.graph;

import java.util.*;

public class Johnsons {
    private static void addTemporaryVertex(Graph graph) {
        int tempVertex = graph.graph.size();
        graph.addVertex(tempVertex);
        for (int i = 0; i < tempVertex; i++) {
            graph.addEdge(tempVertex, i, 0);
        }
    }

    private static boolean reweight(Graph graph, int[] h) {
        for (int u = 0; u < graph.graph.size(); u++) {
            for (Graph.Edge edge : graph.getEdges(u)) {
                if (u != edge.to) {
                    edge.weight = edge.weight + h[u] - h[edge.to];
                }
            }
        }
        return true;
    }

    static int[][] distance;

    public static int[][] getDistance(Graph graph) {
        run(graph);
        return distance;
    }

    private static void run(Graph graph) {
        Objects.requireNonNull(graph);
        addTemporaryVertex(graph);
        int[] h;
        int tempVertex = graph.graph.size() - 1;
        if (!BellmanFord.execute(graph, tempVertex)) {
            throw new RuntimeException("Negative Cycle");
        }
        h = BellmanFord.distance;
        graph.graph.remove(tempVertex);
        if (reweight(graph, h)) {
            distance = new int[graph.graph.size()][graph.graph.size()];
            for (int u = 0; u < graph.graph.size(); u++) {
                distance[u] = Dijkstra.execute(graph, u);
                for (int v = 0; v < graph.graph.size(); v++) {
                    if (distance[u][v] != Integer.MAX_VALUE)
                        distance[u][v] = distance[u][v] - h[u] + h[v];
                }
            }
        }
    }

    static class BellmanFord {
        public static boolean isNegativeCycle = false;
        public static int[] distance;

        public static boolean execute(Graph graph, int source) {
            int V = graph.graph.size();
            distance = new int[V];
            Arrays.fill(distance, Integer.MAX_VALUE);
            distance[source] = 0;
            for (int i = 1; i < V; i++) {
                for (int u = 0; u < V; u++) {
                    for (Graph.Edge edge : graph.getEdges(u)) {
                        if (distance[u] != Integer.MAX_VALUE && distance[u] + edge.weight < distance[edge.to]) {
                            distance[edge.to] = distance[u] + edge.weight;
                        }
                    }
                }
            }
            outer:
            for (int i = 1; i < V; i++) {
                for (int u = 0; u < V; u++) {
                    for (Graph.Edge edge : graph.getEdges(u)) {
                        if (distance[u] != Integer.MAX_VALUE && distance[u] + edge.weight < distance[edge.to]) {
                            isNegativeCycle = true;
                            return false;
                        }
                    }
                }
            }
            return true;
        }
    }

    static class Dijkstra {
        static class Vertex implements Comparable<Vertex> {
            public int id, distance;

            public Vertex(int id, int distance) {
                this.id = id;
                this.distance = distance;
            }

            @Override
            public int compareTo(Vertex other) {
                return Integer.compare(this.distance, other.distance);
            }
        }

        public static int[] execute(Graph graph, int source) {
            int V = graph.graph.size();
            int[] distance = new int[V];
            boolean[] visited = new boolean[V];
            Arrays.fill(distance, Integer.MAX_VALUE);
            distance[source] = 0;
            PriorityQueue<Vertex> pq = new PriorityQueue<>();
            pq.add(new Vertex(source, 0));
            while (!pq.isEmpty()) {
                Vertex current = pq.poll();
                if (visited[current.id])
                    continue;
                visited[current.id] = true;
                for (Graph.Edge edge : graph.getEdges(current.id)) {
                    if (distance[current.id] + edge.weight < distance[edge.to]) {
                        distance[edge.to] = distance[current.id] + edge.weight;
                        pq.add(new Vertex(edge.to, distance[edge.to]));
                    }
                }
            }
            return distance;
        }
    }

    static class Graph {
        private final Map<Integer, List<Graph.Edge>> graph;

        public Graph() {
            this.graph = new HashMap<>();
        }

        static class Edge {
            int to, weight;

            public Edge(int to, int weight) {
                this.to = to;
                this.weight = weight;
            }
        }

        public void addVertex(int vertex) {
            graph.putIfAbsent(vertex, new ArrayList<>());
        }

        public void addEdge(int from, int to, int weight) {
            graph.get(from).add(new Edge(to, weight));
        }

        public List<Edge> getEdges(int vertex) {
            return graph.get(vertex);
        }
    }
}
