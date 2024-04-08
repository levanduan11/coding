package com.coding.algorithm.graph;

import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

public class BestFirstSearch {
    static class Edge {
        int from, to, cost;

        public Edge(int from, int to, int cost) {
            this.from = from;
            this.to = to;
            this.cost = cost;
        }
    }

    static class Node implements Comparable<Node> {
        int id, cost;

        public Node(int id, int cost) {
            this.id = id;
            this.cost = cost;
        }

        @Override
        public int compareTo(Node o) {
            return Integer.compare(this.cost, o.cost);
        }
    }

    private List<List<Edge>> graph;
    private Integer[] prev;

    public BestFirstSearch(List<List<Edge>> graph) {
        this.graph = graph;
    }

    public void addDirectedEdge(int from, int to, int cost) {
        graph.get(from).add(new Edge(from, to, cost));
    }

    public void addUndirectedEdge(int from, int to, int cost) {
        addDirectedEdge(from, to, cost);
        addDirectedEdge(to, from, cost);
    }

    public void bestFirstSearch(int start, int end) {
        boolean[] visited = new boolean[graph.size()];
        PriorityQueue<Node> pq = new PriorityQueue<>();
        pq.add(new Node(start, 0));
        visited[start] = true;
        while (!pq.isEmpty()) {
            Node current = pq.poll();
            if (current.id == end) {
                System.out.println("Path found with cost: " + current.cost);
                return;
            }
            for (Edge edge : graph.get(current.id)) {
                if (!visited[edge.to]) {
                    visited[edge.to] = true;
                    prev[edge.to] = current.id;
                    pq.add(new Node(edge.to, edge.cost + current.cost));
                }

            }
        }
        System.out.println("Path not found");
    }

    public List<Integer> reconstructPath(int start, int end) {
        bestFirstSearch(start, end);
        LinkedList<Integer> path = new LinkedList<>();
        for (Integer at = end; at != null; at = prev[at])
            path.addFirst(at);
        if (path.getFirst() == start)
            return path;
        path.clear();
        return path;
    }
}
