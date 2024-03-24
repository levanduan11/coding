package com.coding.algorithm.graph.networkflow;

import java.util.Random;

public class Karger {
    static int kargerMinCut(Graph graph) {
        int V = graph.V, E = graph.E;
        Edge[] edges = graph.edges;
        Subset[] subsets = new Subset[V];
        for (int v = 0; v < V; v++) {
            subsets[v] = new Subset(v, 0);
        }
        int vertices = V;
        final var ran = new Random();
        while (vertices > 2) {
            int i = ran.nextInt(E);
            int subset1 = find(subsets, edges[i].src);
            int subset2 = find(subsets, edges[i].dest);
            if (subset1 == subset2)
                continue;
            System.out.println("Contracting edge " + edges[i].src + "-" + edges[i].dest);
            vertices--;
            union(subsets, subset1, subset2);
        }
        int cutedges = 0;
        for (int i = 0; i < E; i++) {
            int subset1 = find(subsets, edges[i].src);
            int subset2 = find(subsets, edges[i].dest);
            if (subset1 != subset2)
                cutedges++;
        }
        return cutedges;
    }

    static int find(Subset[] subsets, int i) {
        if (subsets[i].parent != i) {
            subsets[i].parent = find(subsets, subsets[i].parent);
        }
        return subsets[i].parent;
    }

    static void union(Subset[] subsets, int x, int y) {
        int xRoot = find(subsets, x);
        int yRoot = find(subsets, y);
        if (subsets[xRoot].rank < subsets[yRoot].rank)
            subsets[xRoot].parent = yRoot;
        else {
            if (subsets[xRoot].rank > subsets[yRoot].rank)
                subsets[yRoot].parent = xRoot;
            else {
                subsets[yRoot].parent = xRoot;
                subsets[xRoot].rank++;
            }
        }
    }

    static class Edge {
        final int src, dest;

        Edge(int src, int dest) {
            this.src = src;
            this.dest = dest;
        }
    }

    static class Graph {
        final int V, E;
        Edge[] edges;

        Graph(int v, int e) {
            V = v;
            E = e;
            this.edges = new Edge[E];
        }
    }

    static class Subset {
        int parent;
        int rank;

        Subset(int parent, int rank) {
            this.parent = parent;
            this.rank = rank;
        }
    }
}
