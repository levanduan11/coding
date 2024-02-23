package com.coding.algorithm.graph.pagerank;

import java.util.HashMap;
import java.util.Map;

public class Node implements Comparable<Node> {
    public static double DEFAULT_EDGE_WEIGHT = 0.0;
    public Map<Node, Double> outgoingEdges;
    public Map<Node, Double> incomingEdges;
    public double rank;
    public String key;
    public boolean marked = false;
    public String data;

    public Node(final String key, final String data) {
        this.rank = 1.0D;
        this.key = key;
        this.data = data;
        this.outgoingEdges = new HashMap<>();
        this.incomingEdges = new HashMap<>();
    }

    @Override
    public int compareTo(Node that) {
        return Double.compare(that.rank, this.rank);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Node node)
            return this.key.equals(node.key);
        else
            return false;
    }

    public void connect(final Node that, Double weight) {
        this.outgoingEdges.put(that, weight);
        that.incomingEdges.put(this, weight);
    }

    public void disconnect(final Node that) {
        this.outgoingEdges.remove(that);
        this.incomingEdges.remove(this);
    }

    public String id() {
        return Integer.toString(hashCode(), 16);
    }

    public static void buildNode(final Graph graph, final String key, final String data) {
        graph.put(key, graph.getOrDefault(key, new Node(key, data)));
    }

    @Override
    public String toString() {
        final var sb = new StringBuilder();
        sb.append("Key: ").append(this.key);
        sb.append(", Data: ").append(this.data);
        sb.append(", Rank: ").append(this.rank);
        sb.append(", Outgoing Edges: ").append(this.outgoingEdges.size());
        sb.append(", Incoming Edges: ").append(this.incomingEdges.size());
        return sb.toString();
    }
}
