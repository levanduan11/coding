package com.coding.hard;

import java.util.*;

public class TheSkylineProblem {
    static class Edge {
        int x;
        int height;
        boolean isStart;

        public Edge(int x, int height, boolean isStart) {
            this.x = x;
            this.height = height;
            this.isStart = isStart;
        }
    }

    public List<List<Integer>> getSkyline(int[][] buildings) {
        List<List<Integer>> res = new ArrayList<>();
        if (buildings.length == 0)
            return res;
        List<Edge> edges = new ArrayList<>();
        for (int[] each : buildings) {
            edges.add(new Edge(each[0], each[2], true));
            edges.add(new Edge(each[1], each[2], false));
        }
        edges.sort((a, b) -> {
            if (a.x != b.x)
                return Integer.compare(a.x, b.x);
            if (a.isStart && b.isStart)
                return Integer.compare(b.height, a.height);
            if (!a.isStart && !b.isStart)
                return Integer.compare(a.height, b.height);
            return a.isStart ? -1 : 1;
        });
        PriorityQueue<Integer> heightHeap = new PriorityQueue<>(Collections.reverseOrder());
        for (Edge edge : edges) {
            if (edge.isStart) {
                if (heightHeap.isEmpty() || edge.height > heightHeap.peek())
                    res.add(List.of(edge.x, edge.height));
                heightHeap.add(edge.height);
            } else {
                heightHeap.remove(edge.height);
                if (heightHeap.isEmpty())
                    res.add(List.of(edge.x, 0));
                else if (edge.height > heightHeap.peek())
                    res.add(List.of(edge.x, heightHeap.peek()));
            }
        }
        return res;
    }

    public List<List<Integer>> getSkyline2(int[][] buildings) {
        List<List<Integer>> res = new ArrayList<>();
        PriorityQueue<int[]> queue = new PriorityQueue<>((a, b) -> b[2] - a[2]);
        int next = 0;
        int[] point = null;
        while (point != null || next < buildings.length) {
            if (point == null) {
                point = buildings[next];
                res.add(List.of(point[0], point[2]));
            } else if (next < buildings.length && buildings[next][0] <= point[1]) {
                if (buildings[next][2] > point[2]) {
                    if (buildings[next][0] == point[0])
                        res.remove(res.size() - 1);
                    if (buildings[next][1] <= point[1])
                        queue.add(point);
                    point = buildings[next];
                    res.add(List.of(point[0], point[2]));
                } else if (buildings[next][1] > point[1])
                    queue.add(buildings[next]);
                next++;
            } else {
                int[] lower = queue.poll();
                while (lower != null && lower[1] <= point[1])
                    lower = queue.poll();
                if (lower == null)
                    res.add(List.of(point[1], 0));
                else if (lower[2] < point[2])
                    res.add(List.of(point[1], lower[2]));
                point = lower;
            }
        }
        return res;
    }

    public static void main(String[] args) {
        var o = new TheSkylineProblem();
        SolutionMergeSort mergeSort = new SolutionMergeSort();
        int[][] input = {{2, 9, 10}, {3, 7, 15}, {5, 12, 12}, {15, 20, 10}, {19, 24, 8}};
        var res = o.getSkyline(input);
        var res3 = o.getSkyline2(input);
        List<int[]> res2 = mergeSort.getSkyline(input);
        System.out.println(res);
        System.out.println(res3);
        System.out.println("==================");
        res2.stream().map(Arrays::toString).forEach(System.out::println);
    }
}

class SolutionMergeSort {
    public List<int[]> getSkyline(int[][] buildings) {
        if (buildings.length == 0)
            return new ArrayList<>();
        return getSkyline(buildings, 0, buildings.length - 1);
    }

    private LinkedList<int[]> getSkyline(int[][] buildings, int lo, int hi) {
        if (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            LinkedList<int[]> skyline1 = getSkyline(buildings, lo, mid);
            LinkedList<int[]> skyline2 = getSkyline(buildings, mid + 1, hi);
            return mergeSkylines(skyline1, skyline2);
        } else {
            LinkedList<int[]> skyline = new LinkedList<>();
            skyline.add(new int[]{buildings[lo][0], buildings[lo][2]});
            skyline.add(new int[]{buildings[lo][1], 0});
            return skyline;
        }
    }

    private LinkedList<int[]> mergeSkylines(LinkedList<int[]> skyline1, LinkedList<int[]> skyline2) {
        LinkedList<int[]> skyline = new LinkedList<>();
        int height1 = 0, height2 = 0;
        while (!skyline1.isEmpty() && !skyline2.isEmpty()) {
            int index = 0, height = 0;
            if (skyline1.getFirst()[0] < skyline2.getFirst()[0]) {
                index = skyline1.getFirst()[0];
                height1 = skyline1.getFirst()[1];
                height = Math.max(height1, height2);
                skyline1.removeFirst();
            } else if (skyline1.getFirst()[0] > skyline2.getFirst()[0]) {
                index = skyline2.getFirst()[0];
                height2 = skyline2.getFirst()[1];
                height = Math.max(height1, height2);
                skyline2.removeFirst();
            } else {
                index = skyline1.getFirst()[0];
                height1 = skyline1.getFirst()[1];
                height2 = skyline2.getFirst()[1];
                height = Math.max(height1, height2);
                skyline1.removeFirst();
                skyline2.removeFirst();
            }
            if (skyline.isEmpty() || height != skyline.getLast()[1]) {
                skyline.add(new int[]{index, height});
            }
        }
        skyline.addAll(skyline1);
        skyline.addAll(skyline2);
        return skyline;
    }
}