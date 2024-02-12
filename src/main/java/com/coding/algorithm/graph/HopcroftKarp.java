package com.coding.algorithm.graph;

import java.util.*;

public class HopcroftKarp {
    private static final int NIL = 0;
    private static final int INF = Integer.MAX_VALUE;
    private final List<List<Integer>> graph;
    private final int[] pairU;
    private final int[] pairV;
    private final int[] dist;

    public HopcroftKarp(int u, int v) {
        graph = new ArrayList<>();
        for (int i = 0; i <= u; i++) {
            graph.add(new ArrayList<>());
        }
        pairU = new int[u + 1];
        pairV = new int[v + 1];
        dist = new int[u + 1];
    }

    public void addEdge(int u, int v) {
        graph.get(u).add(v);
    }

    private boolean bfs() {
        Queue<Integer> queue = new LinkedList<>();
        for (int u = 1; u < pairU.length; u++) {
            if (pairU[u] == NIL) {
                dist[u] = 0;
                queue.add(u);
            } else {
                dist[u] = INF;
            }
        }
        dist[NIL] = INF;
        while (!queue.isEmpty()) {
            int u = queue.poll();
            if (dist[u] < dist[NIL]) {
                for (int v : graph.get(u)) {
                    if (dist[pairV[v]] == INF) {
                        dist[pairV[v]] = dist[u] + 1;
                        queue.add(pairV[v]);
                    }
                }
            }
        }
        return dist[NIL] != INF;
    }

    private boolean dfs(int u) {
        if (u != NIL) {
            for (int v : graph.get(u)) {
                boolean dfs = dfs(pairV[v]);
                if (dist[pairV[v]] == dist[u] + 1 && dfs) {
                    pairV[v] = u;
                    pairU[u] = v;
                    return true;
                }
            }
            dist[u] = INF;
            return false;
        }
        return false;
    }
    public int maxMatching(){
        int matching = 0;
        while (bfs()){
            for (int u = 1; u < pairU.length; u++) {
                if (pairU[u] == NIL && dfs(u)){
                    matching++;
                }
            }
        }
        return matching;
    }

    public static void main(String[] args) {
        int u = 3;
        int v = 3;
        var o = new HopcroftKarp(u,v);
        o.addEdge(1,1);
        o.addEdge(1,2);
        o.addEdge(2,2);
        o.addEdge(2,3);
        o.addEdge(3,3);

        int res = o.maxMatching();
        System.out.println(res);
    }
}

class HopcroftKarpV2{

    static final int NIL = 0;
    static final int INF = Integer.MAX_VALUE;

    static class BipGraph
    {
        int m, n;
        List<Integer>[] adj;
        int[] pairU, pairV, dist;
        int hopcroftKarp()
        {
            pairU = new int[m + 1];
            pairV = new int[n + 1];
            dist = new int[m + 1];
            Arrays.fill(pairU, NIL);
            Arrays.fill(pairV, NIL);
            int result = 0;
            while (bfs())
            {

                for(int u = 1; u <= m; u++)

                    if (pairU[u] == NIL && dfs(u))
                        result++;
            }
            return result;
        }
        boolean bfs()
        {

            Queue<Integer> Q = new LinkedList<>();

            for(int u = 1; u <= m; u++)
            {
                if (pairU[u] == NIL)
                {

                    dist[u] = 0;
                    Q.add(u);
                }

                else
                    dist[u] = INF;
            }
            dist[NIL] = INF;

            while (!Q.isEmpty())
            {
                int u = Q.poll();
                if (dist[u] < dist[NIL])
                {
                    for(int i : adj[u])
                    {
                        if (dist[pairV[i]] == INF)
                        {
                            dist[pairV[i]] = dist[u] + 1;
                            Q.add(pairV[i]);
                        }
                    }
                }
            }
            return (dist[NIL] != INF);
        }
        boolean dfs(int u)
        {
            if (u != NIL)
            {
                for(int i : adj[u])
                {
                    if (dist[pairV[i]] == dist[u] + 1)
                    {
                        if (dfs(pairV[i]))
                        {
                            pairV[i] = u;
                            pairU[u] = i;
                            return true;
                        }
                    }
                }
                dist[u] = INF;
                return false;
            }
            return true;
        }

        // Constructor
        @SuppressWarnings("unchecked")
        public BipGraph(int m, int n)
        {
            this.m = m;
            this.n = n;
            adj = new ArrayList[m + 1];
            Arrays.fill(adj, new ArrayList<>());
        }
        void addEdge(int u, int v)
        {
            adj[u].add(v);
        }
    }
    public static void main(String[] args)
    {

        BipGraph g = new BipGraph(4, 4);
        g.addEdge(1, 2);
        g.addEdge(1, 3);
        g.addEdge(2, 1);
        g.addEdge(3, 2);
        g.addEdge(4, 2);
        g.addEdge(4, 4);

        System.out.println("Size of maximum matching is " +
                g.hopcroftKarp());
    }
}
