package com.coding.algorithm.graph;

import java.util.*;

public class UniformCostSearch {
    public static String search(Node start, String goal) {
        PriorityQueue<Node> frontier = new PriorityQueue<>();
        Set<Node> expanded = new HashSet<>();
        Map<Node, Node> cameFrom = new HashMap<>();

        start.cost = 0;
        frontier.add(start);

        while (!frontier.isEmpty()) {
            Node current = frontier.poll();
            if (current.id.equals(goal)) {
                return reconstructPath(cameFrom, current);
            }
            if (!expanded.contains(current)) {
                expanded.add(current);
                for (Edge edge : current.edges) {
                    Node neighbor = edge.target;
                    double newCost = current.cost + edge.cost;
                    if (!expanded.contains(neighbor) && newCost < neighbor.cost) {
                        neighbor.cost = newCost;
                        cameFrom.put(neighbor, current);
                        frontier.add(neighbor);
                    }
                }
            }
        }
        return "No path found";
    }

    private static String reconstructPath(Map<Node, Node> cameFrom, Node current) {
        LinkedList<String> path = new LinkedList<>();
        while (current != null) {
            path.addFirst(current.id);
            current = cameFrom.get(current);
        }
        return String.join("->", path);
    }

    static class Node implements Comparable<Node> {
        String id;
        List<Edge> edges;
        double cost;

        Node(String id) {
            this.id = id;
            this.edges = new ArrayList<>();
            this.cost = Double.POSITIVE_INFINITY;
        }

        public void addEdge(Node target, double cost) {
            edges.add(new Edge(target, cost));
        }

        @Override
        public boolean equals(Object obj) {
            return (this == obj) || (obj instanceof Node that && this.id.equals(that.id));
        }

        @Override
        public int hashCode() {
            return id.hashCode();
        }

        @Override
        public int compareTo(Node o) {
            return Double.compare(this.cost, o.cost);
        }
    }

    static class Edge {
        Node target;
        double cost;

        Edge(Node target, double cost) {
            this.target = target;
            this.cost = cost;
        }
    }
}
