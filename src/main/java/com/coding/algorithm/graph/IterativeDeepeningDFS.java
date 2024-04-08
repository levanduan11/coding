package com.coding.algorithm.graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class IterativeDeepeningDFS {
    static class Node {
        String name;
        List<Node> children;

        Node(String name) {
            this.name = name;
            children = new ArrayList<>();
        }

        public void addChild(Node node) {
            children.add(node);
        }

        @Override
        public boolean equals(Object obj) {
            return (this == obj) || (obj instanceof Node that && this.name.equals(that.name));
        }
    }

    public static boolean runSearch(Node root, String goal) {
        int depth = 0;
        while (true) {
            Set<Node> visited = new HashSet<>();
            boolean found = dfs(root, goal, depth, visited);
            if (found) {
                return true;
            }
            depth++;
        }
    }

    private static boolean dfs(Node node, String goal, int depth, Set<Node> visited) {
        visited.add(node);
        if (node.name.equals(goal)) {
            return true;
        }
        if (depth == 0) {
            return false;
        } else {
            for (Node child : node.children) {
                boolean p = dfs(child, goal, depth - 1, visited);
                if (!visited.contains(child) && p) {
                    return true;
                }
            }
            return false;
        }
    }
}
