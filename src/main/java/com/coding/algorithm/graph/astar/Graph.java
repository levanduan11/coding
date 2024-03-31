package com.coding.algorithm.graph.astar;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Graph<T extends GraphNode> {
    private final Set<T> nodes;
    private final Map<String, Set<String>> connections;

    public Graph(Set<T> nodes, Map<String, Set<String>> connections) {
        this.nodes = nodes;
        this.connections = connections;
    }

    public T getNode(String id) {
        return nodes.stream()
                .filter(n -> n.id().equals(id))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("Node not found"));
    }
    public Set<T> getConnections(T node) {
        return connections.get(node.id())
                .stream()
                .map(this::getNode)
                .collect(Collectors.toSet());
    }
}
