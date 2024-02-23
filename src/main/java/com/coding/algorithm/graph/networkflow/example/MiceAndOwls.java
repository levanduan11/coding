package com.coding.algorithm.graph.networkflow.example;

import com.coding.algorithm.graph.networkflow.FordFulkersonDfsSolverAdjacencyList;

import java.awt.*;
import java.awt.geom.Point2D;

public class MiceAndOwls {
    static class Mouse {
        Point2D point;

        public Mouse(int x, int y) {
            point = new Point(x, y);
        }
    }

    static class Hole {
        int capacity;
        Point2D point;

        public Hole(int x, int y, int cap) {
            point = new Point2D.Double(x, y);
            capacity = cap;
        }
    }

    static void solve(Mouse[] mice, Hole[] holes, int radius) {
        final int M = mice.length;
        final int H = holes.length;
        final int N = M + H + 2;
        final int S = N - 1;
        final int T = N - 2;
        var solver = new FordFulkersonDfsSolverAdjacencyList(N,S,T);
        for (int i = 0; i < M; i++) {
            solver.addEdge(S,i,1);
        }
        for (int i = 0; i < M; i++) {
            Point2D mouse = mice[i].point;
            for (int j = 0; j < H; j++) {
                Point2D hole = holes[j].point;
                if (mouse.distance(hole)<= radius)
                    solver.addEdge(i,M+j,1);
            }
        }
        for (int i = 0; i < H; i++) {
            solver.addEdge(M + i,T,holes[i].capacity);
        }
    }
}
