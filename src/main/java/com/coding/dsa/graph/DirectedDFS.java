package com.coding.dsa.graph;

public class DirectedDFS {
    private boolean[] marked;
    private int count;

    public DirectedDFS(Digraph G, int s) {
        marked = new boolean[G.V()];
        validateVertex(s);
        dfs(G, s);
    }

    public DirectedDFS(Digraph G, Iterable<Integer> sources) {
        marked = new boolean[G.V()];
        validateVertices(sources);
        for (int v : sources) {
            if (!marked[v])
                dfs(G, v);
        }
    }

    public void dfs(Digraph G, int v) {
        count++;
        marked[v] = true;
        for (int w : G.adj(v)) {
            if (!marked[w])
                dfs(G, w);
        }
    }

    public boolean marked(int v) {
        validateVertex(v);
        return marked[v];
    }

    public int count() {
        return count;
    }

    public void validateVertex(int v) {
        int V = marked.length;
        if (v < 0 || v >= V)
            throw new IllegalArgumentException();
    }

    public void validateVertices(Iterable<Integer> vertices) {
        if (vertices == null)
            throw new IllegalArgumentException();
        int vertexCount = 0;
        for (Integer v : vertices) {
            vertexCount++;
            if (v == null)
                throw new IllegalArgumentException();
            validateVertex(v);
        }
        if (vertexCount == 0)
            throw new IllegalArgumentException();
    }
}
