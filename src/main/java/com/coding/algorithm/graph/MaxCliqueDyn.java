package com.coding.algorithm.graph;

import java.util.ArrayList;
import java.util.List;

public class MaxCliqueDyn {
    private Graph graph;
    private List<Integer> maxClique;
    private int[] colors;

    public MaxCliqueDyn(Graph graph) {
        this.graph = graph;
        maxClique = new ArrayList<>();
        colors = new int[graph.getNumVertices()];
    }

    public List<Integer> getMaxClique() {
        return maxClique;
    }

    public void findMaxClique() {
        List<Integer> potentialClique = new ArrayList<>();
        List<Integer> candidates = new ArrayList<>();
        List<Integer> alreadyFound = new ArrayList<>();

        for (int i = 0; i < graph.getNumVertices(); i++) {
            candidates.add(i);
        }
        expand(potentialClique, candidates, alreadyFound);
    }

    private void expand(List<Integer> potentialClique, List<Integer> candidates, List<Integer> alreadyFound) {
        List<Integer> candidatesCopy = new ArrayList<>(candidates);
        if (candidates.isEmpty() && alreadyFound.isEmpty()) {
            if (potentialClique.size() > maxClique.size()) {
                maxClique = new ArrayList<>(potentialClique);
                System.out.println("Max Clique: " + maxClique);
            }
            return;
        }
        while (!candidatesCopy.isEmpty()) {
            int candidate = candidatesCopy.getFirst();
            potentialClique.add(candidate);
            List<Integer> newCandidates = new ArrayList<>();
            List<Integer> newAlreadyFound = new ArrayList<>();

            for (int newCandidate : candidates) {
                if (graph.getNeighbours(candidate).contains(newCandidate)) {
                    newCandidates.add(newCandidate);
                }
            }

            for (int newFound : alreadyFound) {
                if (graph.getNeighbours(candidate).contains(newFound)) {
                    newAlreadyFound.add(newFound);
                }
            }

            if (newCandidates.isEmpty() && newAlreadyFound.isEmpty() && potentialClique.size() > maxClique.size()) {
                maxClique = new ArrayList<>(potentialClique);
                System.out.println("Max Clique: " + maxClique);
            } else {
                expand(potentialClique, newCandidates, newAlreadyFound);
            }
            potentialClique.removeLast();
            candidatesCopy.removeFirst();
            alreadyFound.add(candidate);
        }
    }

    static class Graph {
        private List<List<Integer>> adjList;
        private int numVertices;

        public Graph(int numVertices) {
            this.numVertices = numVertices;
            adjList = new ArrayList<>();
            for (int i = 0; i < numVertices; i++) {
                adjList.add(new ArrayList<>());
            }
        }

        public void addEdge(int u, int v) {
            adjList.get(u).add(v);
            adjList.get(v).add(u);
        }

        public List<Integer> getNeighbours(int v) {
            return adjList.get(v);
        }

        public int getNumVertices() {
            return numVertices;
        }
    }
}
