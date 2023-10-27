package com.coding.hastable.hard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AddEdgesMakeDegrees {
    public static boolean isPossible(int n, List<List<Integer>> edges) {
        ArrayList<Integer>[] a = new ArrayList[n + 1];
        for (int i = 0; i <= n; i++) {
            a[i] = new ArrayList<>();
        }
        for (List<Integer> list : edges) {
            a[list.get(0)].add(list.get(1));
            a[list.get(1)].add(list.get(0));
        }
        int j = 0;
        ArrayList<Integer> oddList = new ArrayList<>();
        for (ArrayList<Integer> temp : a) {
            if (temp.size() % 2 == 1) {
                oddList.add(j);
                j++;
            }
        }
        if (oddList.size() == 0) {
            return true;
        } else if (oddList.size() == 2) {
            int[] t = new int[n + 1];

            int a1 = oddList.get(0);
            int b1 = oddList.get(1);

            for (int x : a[a1]) {
                t[x]++;
            }
            for (int x : a[b1]) {
                t[x]++;
            }
            for (int i = 1; i <= n; i++) {
                if (t[i] == 0) {
                    return true;
                }
            }
            return false;
        }
        else if (oddList.size()==4){
            int[]t=new int[n+1];

            int a1=oddList.get(0);
            int b1=oddList.get(1);
            int c1=oddList.get(2);
            int d1=oddList.get(3);

            if (!a[a1].contains(b1)&&!a[c1].contains(d1))
                return true;
            if (!a[a1].contains(c1)&&!a[b1].contains(d1))
                return true;
            if (!a[a1].contains(d1)&&!a[b1].contains(c1))
                return true;
        }
        return false;
    }

    private static boolean checkIfLegal(List<List<Integer>> graph, int x, int y) {
        for (int val : graph.get(x)) if (val == y) return false;
        return true;
    }

    public static void main(String[] args) {
        int n = 5;
        List<List<Integer>> edges = List.of(
                List.of(1,2),
                List.of(2,3),
                List.of(3,4),
                List.of(4,2),
                List.of(1,4),
                List.of(2,5)
        );
        System.out.println(isPossible(n, edges));
    }
}

class Solution {
    public boolean isPossible(int n, List<List<Integer>> edges) {
        // number of edges of each node
        int[] noe = new int[n + 1];

        // make a graph
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i <= n; i++) graph.add(new ArrayList<>());
        for (List<Integer> c : edges) {
            int x = c.get(0);
            int y = c.get(1);
            graph.get(x).add(y);
            graph.get(y).add(x);
            noe[x]++;
            noe[y]++;
        }

        // nodes with number of odd edges will be in the list
        List<Integer> list = new ArrayList<>();

        for (int i = 0; i < noe.length; i++) {
            if (noe[i] % 2 == 1) {
                list.add(i);
            }
        }

        int odd = list.size();

        //no odds
        if (odd == 0) return true;
        else if (odd > 4 || odd == 1 || odd == 3)
            return false; // we can't get ans with these conditions because edges will connect 2 nodes
        else if (odd == 4) { // if odd is 4 then we have to try out every possible combination
            int node1 = list.get(0);
            int node2 = list.get(1);
            int node3 = list.get(2);
            int node4 = list.get(3);
            if (checkIfLegal(graph, node1, node2) && checkIfLegal(graph, node3, node4)) return true;
            if (checkIfLegal(graph, node1, node3) && checkIfLegal(graph, node2, node4)) return true;
            if (checkIfLegal(graph, node1, node4) && checkIfLegal(graph, node2, node3)) return true;
        } else if (odd == 2) { // if odd is 2 there are 2 conditions either join the 2 nodes with odd edges or join one node with even edge with 2 nodes with odd edge
            int x = list.get(0);
            int y = list.get(1);
            if (checkIfLegal(graph, y, x)) return true;
            for (int i = 1; i < noe.length; i++) {
                if (i == x || i == y) continue;
                if (checkIfLegal(graph, i, x) && checkIfLegal(graph, i, y)) return true;
            }
        }
        return false;
    }

    // will check if there is not a repeated edge or self loop
    public boolean checkIfLegal(List<List<Integer>> graph, int x, int y) {
        for (int val : graph.get(x)) if (val == y) return false;
        return true;
    }

}