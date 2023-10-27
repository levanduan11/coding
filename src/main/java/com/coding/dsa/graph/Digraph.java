package com.coding.dsa.graph;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.*;

public class Digraph {
    private static final String NEWLINE = System.lineSeparator();
    private final int V;
    private int E;
    private final Set<Integer>[] adj;
    private final int[] indegree;

    public Digraph(int V) {
        if (V < 0) throw new IllegalArgumentException();
        this.V = V;
        this.E = 0;
        indegree = new int[V];
        //noinspection unchecked
        adj = new HashSet[V];
        for (int v = 0; v < V; v++) {
            adj[v] = new HashSet<>();
        }
    }

    public Digraph(DataInputStream in) {
        if (in == null)
            throw new IllegalArgumentException();
        try {
            this.V = in.readInt();
            if (V < 0)
                throw new IllegalArgumentException();
            indegree = new int[V];
            //noinspection unchecked
            adj = new HashSet[V];
            for (int v = 0; v < V; v++) {
                adj[v] = new HashSet<>();
            }
            int E = in.readInt();
            if (E < 0)
                throw new IllegalArgumentException();
            for (int i = 0; i < E; i++) {
                int v = in.readInt();
                int w = in.readInt();
                addEdge(v, w);
            }
        } catch (NoSuchElementException ex) {
            throw new IllegalArgumentException(ex);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public Digraph(Digraph G){
        if (G == null)
            throw new IllegalArgumentException();
        this.V = G.V();
        this.E = G.E();
        if (V < 0)
            throw new IllegalArgumentException();
        indegree=new int[V];
        for (int v = 0; v < V; v++) {
            this.indegree[v]=G.indegree(v);
        }
        adj = new HashSet[V];
        for (int v = 0; v < V; v++) {
            adj[v]=new HashSet<>();
        }
        for (int v = 0; v < G.V(); v++) {
            Deque<Integer>reverse = new ArrayDeque<>();
            for (int w : G.adj[v]) {
                reverse.push(w);
            }
            for (int w : reverse) {
                adj[v].add(w);
            }
        }
    }

    public int V() {
        return V;
    }

    public int E() {
        return E;
    }

    private void validateVertex(int v) {
        if (v < 0 || v >= V)
            throw new IllegalArgumentException();
    }

    public void addEdge(int v, int w) {
        validateVertex(v);
        validateVertex(w);
        adj[v].add(w);
        indegree[w]++;
    }

    public Iterable<Integer> adj(int v) {
        validateVertex(v);
        return adj[v];
    }

    public int outdegree(int v) {
        validateVertex(v);
        return adj[v].size();
    }

    public int indegree(int v) {
        validateVertex(v);
        return indegree[v];
    }

    public Digraph reverse() {
        Digraph reverse = new Digraph(V);
        for (int v = 0; v < V; v++) {
            for (int w : adj(v)) {
                reverse.addEdge(w, v);
            }
        }
        return reverse;
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        return sb.toString();
    }
}
