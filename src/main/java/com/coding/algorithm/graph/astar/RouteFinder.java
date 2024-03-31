package com.coding.algorithm.graph.astar;

import java.util.*;

public class RouteFinder<T extends GraphNode> {
    private final Graph<T> graph;
    private final Scorer<T> nextNodeScorer;
    private final Scorer<T> targetScorer;

    public RouteFinder(Graph<T> graph, Scorer<T> nextNodeScorer, Scorer<T> targetScorer) {
        this.graph = graph;
        this.nextNodeScorer = nextNodeScorer;
        this.targetScorer = targetScorer;
    }

    public List<T> findRoute(T from, T to) {
        Map<T, RouteNode<T>> allNodes = new HashMap<>();
        PriorityQueue<RouteNode<T>> openSet = new PriorityQueue<>();
        RouteNode<T> start = new RouteNode<>(from, null, 0d, targetScorer.computeCost(from, to));
        allNodes.put(from, start);
        openSet.offer(start);
        while (!openSet.isEmpty()) {
            RouteNode<T> next = openSet.poll();
            if (next.getCurrent().equals(to)) {
                System.out.println("found");
                LinkedList<T> route = new LinkedList<>();
                RouteNode<T> current = next;
                do {
                    route.addFirst(current.getCurrent());
                    current = allNodes.get(current.getPrevious());
                } while (current != null);
                return route;
            } else {
                for (T connection : graph.getConnections(next.getCurrent())) {
                    double newScore = next.getRouteScore() + nextNodeScorer.computeCost(next.getCurrent(), connection);
                    RouteNode<T> nextNode = allNodes.getOrDefault(connection, new RouteNode<>(connection));
                    allNodes.put(connection, nextNode);
                    if (nextNode.getRouteScore() > newScore) {
                        nextNode.setPrevious(next.getCurrent());
                        nextNode.setRouteScore(newScore);
                        nextNode.setEstimatedScore(newScore + targetScorer.computeCost(connection, to));
                        openSet.offer(nextNode);
                        System.out.println("found better path");
                    }
                }
            }
        }
        throw new IllegalStateException("route not found");
    }
}
