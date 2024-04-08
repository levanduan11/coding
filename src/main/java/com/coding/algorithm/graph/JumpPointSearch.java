package com.coding.algorithm.graph;

import java.util.*;

public class JumpPointSearch {
    private static final int[][] DIRS = new int[][]{{0, 1}, {1, 0}, {0, -1}, {-1, 0}};

    public List<Node> findPath(Node start, Node goal, Node[][] grid) {
        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingInt(node -> node.f));
        Map<Node, Node> cameFrom = new HashMap<>();
        Map<Node, Integer> gScore = new HashMap<>();
        gScore.put(start, 0);
        start.f = heuristic(start, goal);
        openSet.add(start);

        while (!openSet.isEmpty()) {
            Node current = openSet.poll();
            if (current.equals(goal)) {
                return reconstructPath(cameFrom, current);
            }
            for (int[] dir : DIRS) {
                jump(current, dir, goal, grid, openSet, cameFrom, gScore);
            }
        }
        return Collections.emptyList();
    }

    private int heuristic(Node a, Node b) {
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
    }

    private List<Node> reconstructPath(Map<Node, Node> cameFrom, Node current) {
        LinkedList<Node> path = new LinkedList<>();
        while (cameFrom.containsKey(current)) {
            path.addFirst(current);
            current = cameFrom.get(current);
        }
        return path;
    }

    private Node jump(
            Node current,
            int[] direction,
            Node goal,
            Node[][] grid,
            PriorityQueue<Node> openSet,
            Map<Node, Node> cameFrom,
            Map<Node, Integer> gScore) {
        int dx = direction[0], dy = direction[1];
        int x = current.x + dx, y = current.y + dy;

        if (x < 0 || x >= grid.length || y < 0 || y >= grid[0].length)
            return null;
        Node next = grid[x][y];
        if (next.isObstacle)
            return null;
        if (next.equals(goal))
            return next;
        if (hasForceNeighbor(next, direction, grid))
            return next;
        return jump(next, direction, goal, grid, openSet, cameFrom, gScore);
    }

    private boolean hasForceNeighbor(Node node, int[] direction, Node[][] grid) {
        int dx = direction[0], dy = direction[1];
        int x = node.x + dx, y = node.y + dy;

        if (dx != 0) {
            if ((isInsideGrid(x, y + 1, grid) && grid[x][y + 1].isObstacle) && isInsideGrid(x + dx, y + 1, grid) && !grid[x + dx][y + 1].isObstacle) {
                return true;
            }
            return (isInsideGrid(x, y - 1, grid) && grid[x][y - 1].isObstacle) && isInsideGrid(x + dx, y - 1, grid) && !grid[x + dx][y - 1].isObstacle;
        } else if (dy != 0) {
            if ((isInsideGrid(x + 1, y, grid) && grid[x + 1][y].isObstacle) && isInsideGrid(x + 1, y + dy, grid) && !grid[x + 1][y + dy].isObstacle) {
                return true;
            }
            return (isInsideGrid(x - 1, y, grid) && grid[x - 1][y].isObstacle) && isInsideGrid(x - 1, y + dy, grid) && !grid[x - 1][y + dy].isObstacle;
        }
        return false;
    }

    private boolean isInsideGrid(int x, int y, Node[][] grid) {
        return x >= 0 && x < grid.length && y >= 0 && y < grid[0].length;
    }

    static class Node {
        int x, y, g, h, f;
        boolean isObstacle;
        Node parent;

        public Node(int x, int y) {
            this.x = x;
            this.y = y;
            this.isObstacle = false;
            this.parent = null;
            this.g = 0;
            this.h = 0;
            this.f = 0;
        }

        public Node(int x, int y, boolean isObstacle) {
            this(x, y);
            this.isObstacle = isObstacle;
        }

        public void setParent(Node parent) {
            this.parent = parent;
        }

        public void calculateF() {
            this.f = this.g + this.h;
        }

        @Override
        public boolean equals(Object obj) {
            return (this == obj) || (obj instanceof Node that && this.x == that.x && this.y == that.y);
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }
}
