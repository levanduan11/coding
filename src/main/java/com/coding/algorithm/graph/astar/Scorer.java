package com.coding.algorithm.graph.astar;

public interface Scorer<T extends GraphNode> {
    double computeCost(T from, T to);
}
