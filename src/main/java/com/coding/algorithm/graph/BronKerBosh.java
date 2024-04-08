package com.coding.algorithm.graph;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class BronKerBosh {
    public static void bronKerBosh(Set<Integer> R, Set<Integer> P, Set<Integer> X, Map<Integer, Set<Integer>> graph) {
        if (P.isEmpty() && X.isEmpty()) {
            System.out.println(R);
            return;
        }
        Iterator<Integer> iterator = P.iterator();
        while (iterator.hasNext()) {
            Integer next = iterator.next();
            Set<Integer> newR = new HashSet<>(R);
            newR.add(next);

            Set<Integer> newP = new HashSet<>(P);
            newP.retainAll(graph.get(next));

            Set<Integer> newX = new HashSet<>(X);
            newX.retainAll(graph.get(next));

            bronKerBosh(newR, newP, newX, graph);

            iterator.remove();
            X.add(next);
        }
    }

    public static void main(String[] args) {
        Set<Integer> R = new HashSet<>();
        Set<Integer> P = new HashSet<>();
        Set<Integer> X = new HashSet<>();
        Map<Integer, Set<Integer>> graph = Map.of(
                1, Set.of(2, 3),
                2, Set.of(1, 3, 4),
                3, Set.of(1, 2, 4),
                4, Set.of(2, 3)
        );
        P.addAll(graph.keySet());
        bronKerBosh(R, P, X, graph);
    }
}
