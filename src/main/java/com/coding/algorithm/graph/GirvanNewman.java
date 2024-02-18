package com.coding.algorithm.graph;

import java.util.*;

public class GirvanNewman {
    public static void main(String[] args) {
        Map<Integer, Set<Integer>> graph = createGraph();
        List<Set<Integer>>clusters = applyGirvanNewman(graph);
        displayClusters(clusters);
    }
    private static Map<Integer, Set<Integer>> createGraph() {
        Map<Integer, Set<Integer>> graph = new HashMap<>();
        for (int i = 1; i <= 6; i++) {
            graph.put(i, new HashSet<>());
        }
        addEdge(graph, 1, 2);
        addEdge(graph, 1, 3);
        addEdge(graph, 2, 3);
        addEdge(graph, 4, 5);
        addEdge(graph, 4, 6);
        addEdge(graph, 5, 6);
        return graph;
    }

    private static void addEdge(Map<Integer, Set<Integer>> graph, int s, int t) {
        graph.get(s).add(t);
        graph.get(t).add(s);
    }
    private static List<Set<Integer>>applyGirvanNewman(Map<Integer,Set<Integer>>graph){
        List<Set<Integer>>clusters = new ArrayList<>();
        while (!graph.isEmpty()){
            Set<Integer>cluster = findCluster(graph);
            clusters.add(cluster);
            removeEdgesWithHighBetween(graph,cluster);
        }
        return clusters;
    }
    private static Set<Integer> findCluster(Map<Integer, Set<Integer>> graph) {
        Set<Integer> cluster = new HashSet<>();
        Set<Integer> visited = new HashSet<>();
        for (int vertex : graph.keySet()) {
            if (!visited.contains(vertex)) {
                dfs(vertex, graph, visited, cluster);
            }
        }
        return cluster;
    }

    private static void dfs(int vertex, Map<Integer, Set<Integer>> graph, Set<Integer> visited, Set<Integer> cluster) {
        visited.add(vertex);
        cluster.add(vertex);
        for (int neighbor : graph.get(vertex)) {
            if (!visited.contains(neighbor)) {
                dfs(neighbor, graph, visited, cluster);
            }
        }
    }

    private static void removeEdgesWithHighBetween(Map<Integer, Set<Integer>> graph, Set<Integer> cluster) {
        Map<Integer, Double> edgeBetween = calculateEdgeBetween(graph);
        int highestBetweenEdge = -1;
        double maxBetween = Double.MIN_VALUE;
        for (int vertex : cluster) {
            for (int neighbor : graph.get(vertex)) {
                int edge = edgeHash(vertex, neighbor);
                double between = edgeBetween.getOrDefault(edge, 0.0);
                if (between > maxBetween) {
                    maxBetween = between;
                    highestBetweenEdge = edge;
                }
            }
        }
        if (highestBetweenEdge != -1) {
            int source = highestBetweenEdge / 10;
            int target = highestBetweenEdge % 10;
            graph.get(source).remove(target);
            graph.get(target).remove(source);
        }
    }

    private static Map<Integer, Double> calculateEdgeBetween(Map<Integer, Set<Integer>> graph) {
        Map<Integer, Double> edgeBetween = new HashMap<>();
        for (int vertex : graph.keySet()) {
            Queue<Integer> queue = new LinkedList<>();
            Map<Integer, Integer> distance = new HashMap<>();
            Map<Integer, Double> dependency = new HashMap<>();
            queue.offer(vertex);
            distance.put(vertex, 0);
            dependency.put(vertex, 1.0);
            while (!queue.isEmpty()) {
                int current = queue.poll();
                for (int neighbor : graph.get(current)) {
                    if (!distance.containsKey(neighbor)) {
                        queue.offer(neighbor);
                        distance.put(neighbor, distance.get(current) + 1);
                    }
                    if (distance.get(neighbor) == distance.get(current) + 1) {
                        dependency.put(neighbor, dependency.getOrDefault(neighbor, 0.0) + dependency.get(current));
                    }
                }
            }
            for (int target : graph.keySet()) {
                if (target != vertex) {
                    int edge = edgeHash(vertex, target);
                    double edgeBetweenValue = edgeBetween.getOrDefault(edge, 0.0);
                    edgeBetween.put(edge, edgeBetweenValue + (dependency.getOrDefault(target, 0.0) / dependency.get(vertex)));
                }
            }
        }
        return edgeBetween;
    }

    private static int edgeHash(int s, int t) {
        return s * 10 + t;
    }

    private static void displayClusters(List<Set<Integer>> clusters) {
        for (int i = 0; i < clusters.size(); i++) {
            System.out.println("Cluster " + (i + i) + ": " + clusters.get(i));
        }
    }
}
