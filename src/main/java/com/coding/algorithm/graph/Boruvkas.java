package com.coding.algorithm.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Boruvkas {
    static class Edge implements Comparable<Edge> {
        int u, v, cost;

        public Edge(int u, int v, int cost) {
            this.u = u;
            this.v = v;
            this.cost = cost;
        }

        @Override
        public String toString() {
            return String.format("%d %d, cost:%d", u, v, cost);
        }

        @Override
        public int compareTo(Edge o) {
            return Integer.compare(cost, o.cost);
        }
    }

    private final int n, m;
    private final Edge[] graph;
    private boolean solved;
    private boolean mstExists;
    private long minCostSum;
    private List<Edge> mst;

    public Boruvkas(int n, int m, Edge[] graph) {
        if (graph == null)
            throw new IllegalArgumentException();
        this.graph = graph;
        this.n = n;
        this.m = m;
    }

    public List<Edge> getMst() {
        solve();
        return mstExists ? mst : null;
    }

    public Long getMstCost() {
        solve();
        return mstExists ? minCostSum : null;
    }

    private void solve() {
        if (solved)
            return;
        mst = new ArrayList<>();
        UnionFind uf = new UnionFind(n);
        int[] cheapest = new int[n];
        Arrays.fill(cheapest, -1);
        while (mst.size() != n - 1) {
            Arrays.fill(cheapest, -1);
            boolean stop = true;
            for (int i = 0; i < graph.length; i++) {
                Edge e = graph[i];
                if (e.u == e.v)
                    continue;
                int uc = uf.id[e.u], vc = uf.id[e.v];
                if (uc == vc)
                    continue;
                if (cheapest[vc] == -1 || e.cost < graph[cheapest[vc]].cost) {
                    stop = false;
                    cheapest[vc] = i;
                }
                if (cheapest[uc] == -1 || e.cost < graph[cheapest[uc]].cost) {
                    stop = false;
                    cheapest[uc] = i;
                }
            }
            if (stop)
                break;
            for (int i = 0; i < n; i++) {
                if (cheapest[i] == -1)
                    continue;
                Edge e = graph[cheapest[i]];
                if (uf.connected(e.u, e.v))
                    continue;
                mst.add(e);
                minCostSum += e.cost;
                uf.union(e.u, e.v);
            }
        }
        mstExists = (mst.size() == n - 1);
        solved = true;
    }

    private static class UnionFind {
        int components;
        int[] id, sz;

        public UnionFind(int n) {
            components = n;
            id = new int[n];
            sz = new int[n];
            for (int i = 0; i < n; i++) {
                id[i] = i;
                sz[i] = i;
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

        public boolean connected(int p, int q) {
            return find(p) == find(q);
        }

        public int size(int p) {
            return sz[find(p)];
        }

        public void union(int p, int q) {
            int roo1 = find(p);
            int roo2 = find(q);
            if (sz[roo1] < sz[roo2]) {
                sz[roo2] += sz[roo1];
                id[roo1] = roo2;
            } else {
                sz[roo1] += sz[roo2];
                id[roo2] = roo1;
            }
            components--;
        }
    }
}
