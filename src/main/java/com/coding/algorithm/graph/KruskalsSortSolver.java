package com.coding.algorithm.graph;

import java.util.List;
import java.util.PriorityQueue;

public class KruskalsSortSolver {
    private int n;
    private List<Edge> edges;
    private boolean solved;
    private boolean mstExists;
    private Edge[] mst;
    private long mstCost;

    public KruskalsSortSolver(List<Edge> edges, int n) {
        if (edges == null || n <= 1)
            throw new IllegalArgumentException();
        this.edges = edges;
        this.n = n;
    }

    public Edge[] getMst() {
        run();
        return mstExists ? mst : null;
    }

    public Long getMstCost() {
        run();
        return mstExists ? mstCost : null;
    }

    private void run() {
        if (solved)
            return;
        PriorityQueue<Edge> pq = new PriorityQueue<>(edges);
        UnionFind uf = new UnionFind(n);
        int index = 0;
        mst = new Edge[n - 1];
        while (!pq.isEmpty()) {
            Edge edge = pq.poll();
            if (uf.connect(edge.u, edge.v))
                continue;
            uf.union(edge.u, edge.v);
            mstCost += edge.cost;
            mst[index++] = edge;
            if (uf.size(0) == n)
                break;
        }
        mstExists = (uf.size(0) == n);
        solved = true;
    }

    static class Edge implements Comparable<Edge> {
        int u, v, cost;

        public Edge(int u, int v, int cost) {
            this.u = u;
            this.v = v;
            this.cost = cost;
        }

        @Override
        public int compareTo(Edge o) {
            return Integer.compare(cost, o.cost);
        }
    }

    static class UnionFind {
        private int[] id, sz;

        public UnionFind(int n) {
            id = new int[n];
            sz = new int[n];
            for (int i = 0; i < n; i++) {
                id[i] = i;
                sz[i] = 1;
            }
        }

        public int find(int p) {
            int root = p;
            while (root != id[root])
                root = id[root];
            while (p != root) {
                int next = id[p];
                id[p] = root;
                p = next;
            }
            return root;
        }

        public boolean connect(int p, int q) {
            return find(p) == find(q);
        }

        public int size(int p) {
            return sz[find(p)];
        }

        public void union(int p, int q) {
            int root1 = find(p);
            int root2 = find(q);
            if (root1 == root2)
                return;
            if (sz[root1] < sz[root2]) {
                sz[root2] += sz[root1];
                id[root1] = root2;
            } else {
                sz[root1] += sz[root2];
                id[root2] = root1;
            }

        }
    }
}
