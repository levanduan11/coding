package com.coding.algorithm.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class LongestPathDAG {
    static class Graph {
        private int vertices;
        private List<Integer>[] adjList;

        Graph(int vertices) {
            this.vertices = vertices;
            //noinspection unchecked
            this.adjList = new List[vertices];
            for (int i = 0; i < vertices; i++) {
                this.adjList[i] = new ArrayList<>();
            }
        }

        void addEdge(int u, int v) {
            adjList[u].add(v);
        }

        void topologicalSortUtil(int v, boolean[] visited, Stack<Integer> stack) {
            visited[v] = true;
            for (int neighbor : adjList[v]) {
                if (!visited[neighbor])
                    topologicalSortUtil(neighbor, visited, stack);
            }
            stack.push(v);
        }

        Stack<Integer> topologicalSort() {
            Stack<Integer> stack = new Stack<>();
            boolean[] visited = new boolean[vertices];
            for (int i = 0; i < vertices; i++) {
                if (!visited[i])
                    topologicalSortUtil(i, visited, stack);
            }
            return stack;
        }

        int longestPath(int source) {
            int[] dist = new int[vertices];
            Arrays.fill(dist, Integer.MIN_VALUE);
            dist[source] = 0;
            Stack<Integer> stack = topologicalSort();
            while (!stack.isEmpty()) {
                int u = stack.pop();
                if (dist[u] != Integer.MIN_VALUE) {
                    for (int neighbor : adjList[u]) {
                        if (dist[neighbor] < dist[u] + 1)
                            dist[neighbor] = dist[u] + 1;
                    }
                }
            }
            return Arrays.stream(dist).max().orElse(Integer.MIN_VALUE);
        }
    }
}
