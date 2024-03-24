package com.coding.algorithm.graph.networkflow;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class PushRelabelMaxFlow {
    List<Node> nodes;

    public PushRelabelMaxFlow(int nodeCount) {
        nodes = new ArrayList<>();
        for (int i = 0; i < nodeCount; i++) {
            nodes.add(new Node(0, 0));
        }
    }

    public void addEdge(int source, int dest, int capacity) {
        nodes.get(source).addEdge(nodes.get(dest), 0, capacity);
    }

    public int getMaxFlow() {
        preflow(nodes.get(0));
        Node activeNode = getActiveNode();
        while (activeNode != null) {
            if (!push(activeNode)) {
                relabel(activeNode);
            }
            activeNode = getActiveNode();
        }
        return nodes.get(nodes.size() - 1).excessFlow;
    }

    private void preflow(Node s) {
        s.height = nodes.size();
        for (Edge edge : s.edges) {
            edge.flow = edge.capacity;
            edge.dest.excessFlow += edge.flow;
            edge.dest.addEdge(s, -edge.flow, 0);
        }
    }

    private boolean push(Node n) {
        for (Edge edge : n.edges) {
            if ((n.height > edge.dest.height) && (edge.flow != edge.capacity)) {
                int flow = Math.min(edge.capacity - edge.flow, n.excessFlow);
                n.excessFlow -= flow;
                edge.dest.excessFlow += flow;
                edge.flow += flow;
                updateReverseEdge(edge, flow);
                return true;
            }
        }
        return false;
    }

    private void relabel(Node n) {
        int minAdjHeight = Integer.MAX_VALUE;
        for (Edge e : n.edges) {
            if ((e.flow != e.capacity) && (e.dest.height < minAdjHeight)) {
                minAdjHeight = e.dest.height;
                n.height = minAdjHeight + 1;
            }
        }
    }

    private Node getActiveNode() {
        for (int i = 1; i < nodes.size() - 1; i++) {
            if (nodes.get(i).excessFlow > 0)
                return nodes.get(i);
        }
        return null;
    }

    private void updateReverseEdge(Edge edge, int flow) {
        for (Edge e : edge.dest.edges) {
            if (e.dest.equals(edge.source)) {
                e.flow -= flow;
            }
        }
        edge.dest.addEdge(edge.source, -flow, 0);
    }

    static class Node {
        int height;
        int excessFlow;
        List<Edge> edges;

        public Node(int height, int excessFlow) {
            this.height = height;
            this.excessFlow = excessFlow;
            this.edges = new ArrayList<>();
        }

        void addEdge(Node dest, int flow, int capacity) {
            Edge edge = new Edge(this, dest, flow, capacity);
            edges.add(edge);
        }
    }

    static class Edge {
        Node source;
        Node dest;
        int flow;
        int capacity;

        public Edge(Node source, Node dest, int flow, int capacity) {
            this.source = source;
            this.dest = dest;
            this.flow = flow;
            this.capacity = capacity;
        }
    }
}
