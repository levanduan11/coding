package com.coding.algorithm.graph;

import java.util.*;

public class BeamStackSearch {
    static class Node {
        static class Position {
            int x;
            int y;

            public Position(int x, int y) {
                this.x = x;
                this.y = y;
            }
        }

        Position data;
        List<Node> children = new ArrayList<>();

        public Node(int x, int y) {
            this.data = new Position(x, y);
        }

        public void generateChildren() {
            int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
            for (int[] direction : directions) {
                int x = this.data.x + direction[0];
                int y = this.data.y + direction[1];
                if (isValid(x, y)) {
                    children.add(new Node(x, y));
                }
            }
        }

        public int heuristic(Node goal) {
            return Math.abs(this.data.x - goal.data.x) + Math.abs(this.data.y - goal.data.y);
        }
    }

    private final int beamWidth;
    private static final boolean[][] obstacle = {
            {false, false, false, false, true},
            {false, true, true, false, false},
            {false, false, false, false, false},
            {false, true, true, false, false},
            {false, false, false, false, false}};
    private static final int gridWidth = 5;
    private static final int gridHeight = 5;
    private static final Node goal = new Node(gridWidth - 1, gridHeight - 1);

    private static boolean isValid(int x, int y) {
        return x >= 0 && x < gridWidth && y >= 0 && y < gridHeight && !obstacle[x][y];
    }

    public BeamStackSearch(int beamWidth) {
        this.beamWidth = beamWidth;
    }

    private boolean isGoal(Node node) {
        return node.data.x == goal.data.x && node.data.y == goal.data.y;
    }

    public Optional<Node> search(Node root) {
        PriorityQueue<Node> stack = new PriorityQueue<>(Comparator.comparingInt(n -> n.heuristic(goal)));
        stack.add(root);
        while (!stack.isEmpty()) {
            Node current = stack.poll();
            if (isGoal(current)) {
                return Optional.of(current);
            }
            current.generateChildren();
            PriorityQueue<Node> next = new PriorityQueue<>(Comparator.comparingInt(n -> n.heuristic(goal)));
            next.addAll(current.children);
            for (int i = 0; i < Math.min(beamWidth, next.size()); i++) {
                stack.add(next.poll());
            }
        }
        return Optional.empty();
    }

    public static void main(String[] args) {
        Node root = new Node(0, 0);
        BeamStackSearch search = new BeamStackSearch(2);
        Optional<Node> result = search.search(root);
        result.ifPresentOrElse(
                node -> System.out.println("Found path to goal: " + node.data.x + ", " + node.data.y),
                () -> System.out.println("No path to goal"));
    }
}
