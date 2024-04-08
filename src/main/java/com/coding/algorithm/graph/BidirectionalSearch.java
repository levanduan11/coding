package com.coding.algorithm.graph;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

public class BidirectionalSearch {
    private int V;
    private LinkedList<Integer>[] adj;

    @SuppressWarnings("unchecked")
    public BidirectionalSearch(int v) {
        V = v;
        adj = new LinkedList[v];
        for (int i = 0; i < v; i++) {
            adj[i] = new LinkedList<>();
        }
    }

    public void addEdge(int v, int w) {
        adj[v].add(w);
        adj[w].add(v);
    }

    @SuppressWarnings("DataFlowIssue")
    public void bfs(Queue<Integer> queue, Boolean[] visited, int[] parent) {
        Objects.requireNonNull(queue);
        int current = queue.poll();
        for (int i : adj[current]) {
            if (!visited[i]) {
                visited[i] = true;
                parent[i] = current;
                queue.add(i);
            }
        }
    }

    public int isIntersected(Boolean[] visited1, Boolean[] visited2) {
        for (int i = 0; i < V; i++) {
            if (visited1[i] && visited2[i]) {
                return i;
            }
        }
        return -1;
    }

    public void printPath(int[] s_parent, int[] t_parent, int s, int t, int intersectNode) {
        LinkedList<Integer> path = new LinkedList<>();
        path.add(intersectNode);
        int i = intersectNode;
        while (i != s) {
            path.addFirst(s_parent[i]);
            i = s_parent[i];
        }
        i = intersectNode;
        while (i != t) {
            path.addLast(t_parent[i]);
            i = t_parent[i];
        }
        System.out.println("Path between " + s + " and " + t + " is :");
        for (int node : path) {
            System.out.print(node + " ");
        }
    }

    public int biDirSearch(int s, int t) {
        Boolean[] s_visited = new Boolean[V];
        Boolean[] t_visited = new Boolean[V];

        int[] s_parent = new int[V];
        int[] t_parent = new int[V];

        Queue<Integer> s_queue = new LinkedList<>();
        Queue<Integer> t_queue = new LinkedList<>();

        int intersectNode = -1;

        for (int i = 0; i < V; i++) {
            s_visited[i] = false;
            t_visited[i] = false;
        }
        s_queue.add(s);
        s_visited[s] = true;
        s_parent[s] = -1;

        t_queue.add(t);
        t_visited[t] = true;
        t_parent[t] = -1;

        while (!s_queue.isEmpty() && !t_queue.isEmpty()) {
            bfs(s_queue, s_visited, s_parent);
            bfs(t_queue, t_visited, t_parent);
            intersectNode = isIntersected(s_visited, t_visited);
            if (intersectNode != -1) {
                printPath(s_parent, t_parent, s, t, intersectNode);
                return intersectNode;
            }
        }
        return -1;
    }

}
