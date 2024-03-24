package com.coding.algorithm.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class EculideanMinimumSpanningTree {
    static class Point {
        double x;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }

        double y;

    }

    static class Edge implements Comparable<Edge> {
        public Edge(int start, int end, double weight) {
            this.start = start;
            this.end = end;
            this.weight = weight;
        }

        int start, end;
        double weight;

        @Override
        public int compareTo(Edge o) {
            return Double.compare(this.weight, o.weight);
        }
    }

    static class DisjointSet {
        int[] parent, rank;

        public DisjointSet(int size) {
            parent = new int[size];
            rank = new int[size];
            for (int i = 0; i < size; i++) {
                parent[i] = i;
                rank[i] = 0;
            }
        }

        public int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }

        public void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            if (rootX == rootY)
                return;
            if (rank[rootX] < rank[rootY])
                parent[rootX] = rootY;
            else if (rank[rootX] > rank[rootY])
                parent[rootY] = rootX;
            else {
                parent[rootY] = rootX;
                rank[rootX]++;
            }
        }

        public static double euclideanDistance(Point p1, Point p2) {
            double dx = p1.x - p2.x;
            double dy = p1.y - p2.y;
            return Math.sqrt(dx * dx + dy * dy);
        }

        public static List<Edge> euclideanMst(Point[] points) {
            int n = points.length;
            List<Edge> result = new ArrayList<>();
            PriorityQueue<Edge> edges = new PriorityQueue<>();
            for (int i = 0; i < n; i++) {
                for (int j = i + 1; j < n; j++) {
                    double distance = euclideanDistance(points[i], points[j]);
                    edges.add(new Edge(i, j, distance));
                }
            }
            DisjointSet disjointSet = new DisjointSet(n);
            while (!edges.isEmpty() && result.size() < n - 1) {
                Edge edge = edges.poll();
                if (disjointSet.find(edge.start) != disjointSet.find(edge.end)) {
                    result.add(edge);
                    disjointSet.union(edge.start, edge.end);
                }
            }
            return result;
        }
    }
}
