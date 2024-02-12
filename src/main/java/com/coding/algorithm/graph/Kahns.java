package com.coding.algorithm.graph;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

public class Kahns {
    public int[] kahns(List<List<Integer>> g) {
        int n = g.size();
        int[] inDegree = new int[n];
        for (List<Integer> edges : g) {
            for (int to : edges) {
                inDegree[to]++;
            }
        }
        Queue<Integer> q = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            if (inDegree[i] == 0)
                q.offer(i);
        }
        int index = 0;
        int[] order = new int[n];
        while (!q.isEmpty()) {
            int at = q.poll();
            order[index++] = at;
            for (int to : g.get(at)) {
                inDegree[to]--;
                if (inDegree[to] == 0)
                    q.offer(to);
            }
        }
        if (index != n)
            throw new IllegalArgumentException();
        else
            return order;
    }
}
