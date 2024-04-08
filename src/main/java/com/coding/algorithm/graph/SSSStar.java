package com.coding.algorithm.graph;

import java.util.*;

public class SSSStar {
    public static int search(Node root) {
        PriorityQueue<State> open = new PriorityQueue<>();
        Map<Node, Integer> bestValues = new HashMap<>();

        open.add(new State(root, Integer.MAX_VALUE));
        bestValues.put(root, Integer.MAX_VALUE);

        while (!open.isEmpty()) {
            State current = open.poll();
            Node currentNode = current.node;
            int currentMinimax = current.minimaxValue;

            if (currentNode.children.isEmpty()) {
                updateParentBestValue(currentNode, currentMinimax, bestValues, open);
            } else {
                for (Node child : currentNode.children) {
                    if (!bestValues.containsKey(child) || currentMinimax > bestValues.get(child)) {
                        bestValues.put(child, currentMinimax);
                        open.add(new State(child, currentMinimax));
                    }
                }
            }
        }
        return bestValues.get(root);
    }

    private static void updateParentBestValue(Node currentNode, int currentMinimax, Map<Node, Integer> bestValues, PriorityQueue<State> open) {
        Node parent = currentNode.parent;
        if (parent == null)
            return;
        boolean isBetter = false;
        if (!bestValues.containsKey(parent) || currentMinimax > bestValues.get(parent)) {
            bestValues.put(parent, currentMinimax);
            isBetter = true;
        }
        if (isBetter) {
            open.add(new State(parent, currentMinimax));
        }
    }

    static class State implements Comparable<State> {
        Node node;
        int minimaxValue;

        public State(Node node, int minimaxValue) {
            this.node = node;
            this.minimaxValue = minimaxValue;
        }

        @Override
        public int compareTo(State o) {
            return Integer.compare(this.minimaxValue, o.minimaxValue);
        }

        @Override
        public boolean equals(Object obj) {
            return (this == obj)
                    || (obj instanceof State that && this.node.equals(that.node));
        }

        @Override
        public int hashCode() {
            return this.node.hashCode();
        }

        @Override
        public String toString() {
            return "State{" +
                    "node=" + node +
                    ", minimaxValue=" + minimaxValue +
                    '}';
        }
    }

    static class Node {
        int id;
        int value;
        Node parent;
        List<Node> children;

        public Node(int id) {
            this.id = id;
            this.children = new ArrayList<>();
        }

        public void addChild(Node child) {
            this.children.add(child);
            this.parent = this;
        }

        @Override
        public boolean equals(Object obj) {
            return (this == obj)
                    || (obj instanceof Node that && this.id == that.id);
        }

        @Override
        public int hashCode() {
            return Integer.hashCode(this.id);
        }

        @Override
        public String toString() {
            return "Node{" +
                    "id=" + id +
                    ", value=" + value +
                    '}';
        }

    }
}
