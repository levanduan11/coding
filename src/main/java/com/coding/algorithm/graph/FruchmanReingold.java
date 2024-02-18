package com.coding.algorithm.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FruchmanReingold {
   public static class Node {
        double x, y;
        double dx, dy;

        public Node(double x, double y) {
            this.x = x;
            this.y = y;
            this.dx = 0;
            this.dy = 0;
        }
    }

    public static class Edge {
        Node source, target;

        public Edge(Node source, Node target) {
            this.source = source;
            this.target = target;
        }
    }

    private final List<Node> nodes;
    private final List<Edge> edges;
    private final double areaWidth;
    private final double areaHeight;
    private final double k;
    private double temperature;

    public FruchmanReingold(int numNode, int numEdges, double areaWidth, double areaHeight) {
        this.nodes = generateRandomNodes(numNode, areaWidth, areaHeight);
        this.edges = generateRandomEdges(numEdges, nodes);
        this.areaWidth = areaWidth;
        this.areaHeight = areaHeight;
        this.k = Math.sqrt((areaWidth * areaHeight) / numNode);
        this.temperature = areaWidth / 10.0;
    }

    public void simulate(int numIterations) {
        for (int i = 0; i < numIterations; i++) {
            calculateForces();
            updateNodePositions();
            coolTemperature(i, numIterations);
        }
    }

    private List<Node> generateRandomNodes(int numNodes, double areaWidth, double areaHeight) {
        List<Node> nodes = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < numNodes; i++) {
            double x = random.nextDouble() * areaWidth;
            double y = random.nextDouble() * areaHeight;
            nodes.add(new Node(x, y));
        }
        return nodes;
    }

    private List<Edge> generateRandomEdges(int numEdges, List<Node> nodes) {
        List<Edge> edges = new ArrayList<>();
        Random random = new Random();
        for (int i = 0, n = nodes.size(); i < numEdges; i++) {
            Node source = nodes.get(random.nextInt(n));
            Node target = nodes.get(random.nextInt(n));
            edges.add(new Edge(source, target));
        }
        return edges;
    }

    private void calculateForces() {
        for (Node node : nodes) {
            node.dx = 0;
            node.dy = 0;
        }
        for (Edge edge : edges) {
            double vx = edge.target.x - edge.source.x;
            double vy = edge.target.y - edge.source.y;
            double distance = Math.max(0.1, Math.sqrt(vx * vx + vy * vy));
            double force = (distance * distance) / k;

            double dx = (vx / distance) * force;
            double dy = (vy / distance) * force;

            edge.source.dx += dx;
            edge.source.dy += dy;
            edge.target.dx -= dx;
            edge.target.dy -= dy;
        }
        for (Node node : nodes) {
            double displacement = Math.sqrt(node.dx * node.dx + node.dy * node.dy);
            displacement = Math.min(temperature, displacement) / displacement;
            node.x += node.dx * displacement;
            node.y += node.dy * displacement;

            node.x = Math.max(0, Math.min(areaWidth, node.x));
            node.y = Math.max(0, Math.min(areaHeight, node.y));
        }
    }

    private void updateNodePositions() {
        for (Node node : nodes) {
            node.x = Math.max(0, Math.min(areaWidth, node.x));
            node.y = Math.max(0, Math.min(areaHeight, node.y));
        }
    }
    private void coolTemperature(int iteration,int numIterations){
        temperature *= (1.0 -(double) iteration/numIterations);
    }

    public List<Node> getNodes() {
        return nodes;
    }
}
