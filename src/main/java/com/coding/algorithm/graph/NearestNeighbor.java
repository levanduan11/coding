package com.coding.algorithm.graph;

public class NearestNeighbor {
    public static int findShortestPath(int[][] distance) {
        int n = distance.length;
        boolean[] visited = new boolean[n];
        int totalDistance = 0;
        int citiesVisited = 1;
        int currentCity = 0;
        visited[currentCity] = true;
        while (citiesVisited < n) {
            int nearestCity = -1;
            int shortestDistance = Integer.MAX_VALUE;
            for (int i = 0; i < n; i++) {
                if (!visited[i] && distance[currentCity][i] < shortestDistance) {
                    nearestCity = i;
                    shortestDistance = distance[currentCity][i];
                }
            }
            visited[nearestCity] = true;
            totalDistance += shortestDistance;
            currentCity = nearestCity;
            citiesVisited++;
        }
        return totalDistance;
    }

}
