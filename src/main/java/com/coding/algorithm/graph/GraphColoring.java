package com.coding.algorithm.graph;

import com.coding.InputReader;
import com.coding.OutputWriter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class GraphColoring {
    private int vertices;
    private List<List<Integer>> adjacencyList;

    public GraphColoring(int vertices) {
        this.vertices = vertices;
        this.adjacencyList = new ArrayList<>();
        for (int i = 0; i < vertices; i++) {
            this.adjacencyList.add(new ArrayList<>());
        }
    }

    public void addEdge(int u, int v) {
        adjacencyList.get(u).add(v);
        adjacencyList.get(v).add(u);
    }

    public String greedyColoring() {
        int[] result = new int[vertices];
        Arrays.fill(result, -1);
        result[0] = 0;
        boolean[] availableColors = new boolean[vertices];
        Arrays.fill(availableColors, true);
        var sb = new StringBuilder();
        for (int u = 1; u < vertices; u++) {
            for (int neighbor : adjacencyList.get(u)) {
                if (result[neighbor] != -1)
                    availableColors[result[neighbor]] = false;
            }
            int color;
            for (color = 0; color < vertices; color++) {
                if (availableColors[color])
                    break;
            }
            result[u] = color;
            Arrays.fill(availableColors, true);
            for (int i = 0; i < vertices; i++) {
                sb.append("Vertex ").append(i).append(" ----> Color ").append(result[i]);
                System.out.println("Vertex " + i + " ----> Color " + result[i]);
                sb.append(System.lineSeparator());
            }
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        var o = new GraphColoring(4);
        InputReader.inputAsStrings("graph-color.txt").forEach(s -> {
            String[] strings = s.trim().split(",");
            int u = Integer.parseInt(strings[0]);
            int v = Integer.parseInt(strings[1]);
            o.addEdge(u, v);
        });
        String out=o.greedyColoring();
        OutputWriter.write(out,"graph-color.txt");
    }
}
