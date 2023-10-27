package com.coding.graph;


import java.util.*;
import java.util.prefs.Preferences;

public class ModifyGraphEdgeWeights {
    public int[][] modifiedGraphEdges1(int n, int[][] edges, int source, int destination, int target) {
        @SuppressWarnings("unchecked") Map<Integer, Integer>[] adjs = new Map[n];
        for (int i = 0; i < n; i++) {
            adjs[i] = new HashMap<>();
        }
        for (int[] edge : edges) {
            adjs[edge[0]].put(edge[1], edge[2]);
            adjs[edge[1]].put(edge[0], edge[2]);
        }
        int[] distTo = new int[n];
        Arrays.fill(distTo, Integer.MAX_VALUE);
        distTo[source] = 0;

        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(i -> i[1]));
        pq.add(new int[]{source, 0});

        dijkstra1(adjs, distTo, pq);

        if (distTo[destination] == target) {
            return fill(edges);
        } else if (distTo[destination] < target) {
            return new int[0][0];
        } else {
            for (int[] edge : edges) {
                if (edge[2] == -1) {
                    edge[2] = 1;
                    adjs[edge[0]].put(edge[1], 1);
                    adjs[edge[1]].put(edge[0], 1);
                    pq.clear();
                    pq.add(new int[]{edge[0], distTo[edge[0]]});
                    pq.add(new int[]{edge[1], distTo[edge[1]]});

                    dijkstra1(adjs, distTo, pq);
                    if (distTo[destination] == target) {
                        return fill(edges);
                    } else if (distTo[destination] < target) {
                        edge[2] += target - distTo[destination];
                        adjs[edge[0]].put(edge[1], edge[2]);
                        adjs[edge[1]].put(edge[0], edge[2]);
                        return fill(edges);
                    }
                }
            }
        }
        return new int[0][0];
    }

    private int[][] fill(int[][] edges) {
        for (int[] edge : edges) {
            if (edge[2] == -1) {
                edge[2] = (int) (2 * 1e9);
            }
        }
        return edges;
    }

    private void dijkstra1(Map<Integer, Integer>[] adjs, int[] distTo, PriorityQueue<int[]> pq) {
        while (!pq.isEmpty()) {
            int[] curr = pq.poll();

            for (Map.Entry<Integer, Integer> entry : adjs[curr[0]].entrySet()) {
                if (entry.getValue() > 0) {
                    int next = entry.getKey();
                    if (distTo[next] - entry.getValue() > distTo[curr[0]]) {
                        distTo[next] = distTo[curr[0]] + entry.getValue();
                        pq.add(new int[]{next, distTo[next]});
                    }
                }
            }
        }
    }

    int idx = 0, source, destination, target, n;
    int[] head, next, end, index;
    int[][] dis, edges;

    void add(int u, int v, int i) {
        next[idx] = head[u];
        head[u] = idx;
        end[idx] = v;
        index[idx] = i;
        idx++;
    }

    int dijkstra(int t) {
        boolean[] seen = new boolean[n];
        for (int i = 0; i < n; i++) {
            int u = -1;
            for (int j = 0; j < n; j++) {
                if (!seen[j] && (u == -1 || dis[t][j] < dis[t][u])) {
                    u = j;
                }
            }
            if (u == destination) return dis[t][destination];
            seen[u] = true;
            for (int idx = head[u]; idx != -1; idx = next[idx]) {
                int v = end[idx], eIdx = index[idx], w = edges[eIdx][2], cost = w;
                if (seen[v]) continue;
                if (w == -1) {
                    cost = 1;
                    if (t == 1) {
                        cost = target - (dis[0][destination] - dis[0][v] + dis[t][v]);
                        if (cost > 0) edges[eIdx][2] = cost;
                    }
                }
                dis[t][v] = Math.min(dis[t][v], dis[t][u] + cost);
            }
        }
        return dis[t][destination];
    }

    public int[][] modifiedGraphEdges(int n, int[][] edges, int source, int destination, int target) {
        int m = edges.length;
        this.source = source;
        this.destination = destination;
        this.target = target;
        this.n = n;
        head = new int[n];
        Arrays.fill(head, -1);
        next = new int[m * 2];
        end = new int[m * 2];
        index = new int[m * 2];
        this.edges = edges;

        for (int i = 0; i < m; i++) {
            int u = edges[i][0];
            int v = edges[i][1];
            int w = edges[i][2];
            add(u, v, i);
            add(v, u, i);
        }
        dis = new int[2][n];
        Arrays.fill(dis[0], Integer.MAX_VALUE);
        dis[0][source] = 0;
        Arrays.fill(dis[1], Integer.MAX_VALUE);
        dis[1][source] = 0;

        int steps = dijkstra(0);
        if (steps > target) return new int[0][];
        steps = dijkstra(1);
        if (steps < target) return new int[0][];
        for (int[] edge : edges) {
            if (edge[2] == -1) edge[2] = 1;
        }
        return edges;
    }

    public static void main(String[] args) {
        int[][] edges = {{4, 1, -1}, {2, 0, -1}, {0, 3, -1}, {4, 3, -1}};
        int n = 5, source = 0, destination = 1, target = 5;
        var o = new ModifyGraphEdgeWeights();


    }

}

class Solution {
    int idx = 0, source, destination, target, n;
    int[] head, next, end, index;
    int[][] dis, edges;

    void add(int u, int v, int i) {
        // for (int idx = head[u]; idx != -1; idx = next[idx]) {
        next[idx] = head[u];
        head[u] = idx;
        end[idx] = v;
        index[idx] = i;
        idx++;
    }

    int dijkstra(int t) {
        boolean[] seen = new boolean[n];
        for (int i = 0; i < n; i++) {
            int u = -1;
            for (int j = 0; j < n; j++) {
                if (!seen[j] && (u == -1 || dis[t][j] < dis[t][u])) {
                    u = j;
                }
            }
            if (u == destination) return dis[t][destination];
            seen[u] = true;
            for (int idx = head[u]; idx != -1; idx = next[idx]) {
                int v = end[idx], eIdx = index[idx], w = edges[eIdx][2], cost = w;
                if (seen[v]) continue;
                if (w == -1) {
                    cost = 1;
                    if (t == 1) {
                        cost = target - (dis[0][destination] - dis[0][v] + dis[t][u]);
                        if (cost > 0) edges[eIdx][2] = cost;
                    }
                }
                dis[t][v] = Math.min(dis[t][v], dis[t][u] + cost);
            }
        }
        return dis[t][destination];
    }

    public int[][] modifiedGraphEdges(int n, int[][] edges, int source, int destination, int target) {
        int m = edges.length;
        this.source = source;
        this.destination = destination;
        this.target = target;
        this.n = n;
        head = new int[n];
        Arrays.fill(head, -1);
        next = new int[m * 2];
        end = new int[m * 2];
        index = new int[m * 2];
        this.edges = edges;

        for (int i = 0; i < m; i++) {
            int u = edges[i][0], v = edges[i][1], w = edges[i][2];
            add(u, v, i);
            add(v, u, i);
        }

        dis = new int[2][n];
        Arrays.fill(dis[0], Integer.MAX_VALUE);
        dis[0][source] = 0;
        Arrays.fill(dis[1], Integer.MAX_VALUE);
        dis[1][source] = 0;

        int steps = dijkstra(0);
        if (steps > target) return new int[0][];

        steps = dijkstra(1);
        if (steps < target) return new int[0][];

        for (int[] edge : edges) {
            if (edge[2] == -1) edge[2] = 1;
        }
        return edges;
    }

    public static void main(String[] args) {
        int[][] edges = {{4, 1, -1}, {2, 0, -1}, {0, 3, -1}, {4, 3, -1}};
        int n = 5, source = 0, destination = 1, target = 5;
        var so = new Solution();
        var res = so.modifiedGraphEdges(n, edges, source, destination, target);
        for (int[] edge : res) {
            System.out.println(Arrays.toString(edge));
        }
    }
}
