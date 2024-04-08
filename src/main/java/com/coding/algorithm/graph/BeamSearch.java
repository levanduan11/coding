package com.coding.algorithm.graph;

import java.util.*;

public class BeamSearch {
    static final int[][] DIRECTIONS = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
    static final int BEAM_WIDTH = 2;

    public static List<Node> findPath(int startX, int startY, int goalX, int goalY, int[][] grid) {
        PriorityQueue<Node> beam = new PriorityQueue<>(Comparator.comparingInt(Node::getCost));
        beam.add(new Node(startX, startY, 0, null));
        while (!beam.isEmpty()) {
            PriorityQueue<Node> nextBeam = new PriorityQueue<>(Comparator.comparingInt(Node::getCost));
            for (int i = 0; i < BEAM_WIDTH && !beam.isEmpty(); i++) {
                Node current = beam.poll();
                if (current.x == goalX && current.y == goalY)
                    return buildPath(current);
                for (int[] direction : DIRECTIONS) {
                    int newX = current.x + direction[0];
                    int newY = current.y + direction[1];
                    if (isValid(newX, newY, grid)) {
                        nextBeam.add(new Node(newX, newY, current.cost + 1, current));
                    }
                }
            }
            beam = nextBeam;
        }
        return Collections.emptyList();
    }

    static boolean isValid(int x, int y, int[][] grid) {
        return x >= 0 && x < grid.length && y >= 0 && y < grid[0].length && grid[x][y] == 0;
    }

    static List<Node> buildPath(Node node) {
        LinkedList<Node> path = new LinkedList<>();
        for (Node current = node; current != null; current = current.parent) {
            path.addFirst(current);
        }
        return path;
    }

    static class Node {
        int x, y, cost;
        Node parent;

        Node(int x, int y, int cost, Node parent) {
            this.x = x;
            this.y = y;
            this.cost = cost;
            this.parent = parent;
        }

        public int getCost() {
            return cost;
        }
    }
}
