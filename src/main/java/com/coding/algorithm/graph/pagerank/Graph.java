package com.coding.algorithm.graph.pagerank;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.TreeMap;

public class Graph extends TreeMap<String, Node> {
    private static final Logger log = LoggerFactory.getLogger(Graph.class);
    private double dampingFactor;
    private double standardErrorThreshold;
    private static final double INCLUSIVE_COEFF = 0.25;
    public SummaryStatistics dist_stats = new SummaryStatistics();
    public Node[] node_list;

    private Graph(double dampingFactor, double standardErrorThreshold) {
        this.dampingFactor = dampingFactor;
        this.standardErrorThreshold = standardErrorThreshold;
    }

    public static Graph loadGraph(String graphFileDir, String separator, double dampingFactor, double standardErrorThreshold) {
        Graph graph = new Graph(dampingFactor, standardErrorThreshold);
        try (var br = new BufferedReader(new FileReader(graphFileDir))) {
            String line = br.readLine();
            String[] data = line.split(separator);
            for (int i = 0; i < data.length; i++) {
                Node.buildNode(graph, String.valueOf(i), data[i]);
            }
            int nodeKey = 0;
            while ((line = br.readLine()) != null) {
                Node node = graph.get(String.valueOf(nodeKey));
                String[] edgeWeights = line.split(separator);
                for (int i = 0; i < edgeWeights.length; i++) {
                    double weight = Double.parseDouble(edgeWeights[i]);
                    if (weight != 0) {
                        Node otherNode = graph.get(String.valueOf(i));
                        node.connect(otherNode, weight);
                    }
                }
                nodeKey++;
            }
            if (log.isDebugEnabled()) {
                graph.printGraphValues();
            }
            return graph;
        } catch (IOException e) {
            return null;
        }
    }

    public void runPageRank() {
        final int max_iterations = this.size();
        node_list = new Node[this.size()];
        int j = 0;
        for (Node n1 : this.values()) {
            node_list[j++] = n1;
        }
        iterateGraph(max_iterations);
    }

    private void iterateGraph(final int max_iterations) {
        final double[] rank_list = new double[node_list.length];
        for (int k = 0; k < max_iterations; k++) {
            dist_stats.clear();
            for (int i = 0; i < node_list.length; i++) {
                final Node n1 = node_list[i];
                double rank = 0.0;
                for (Node n2 : n1.incomingEdges.keySet()) {
                    double numerator = n2.outgoingEdges.get(n1);
                    double denominator = 0.0;
                    for (Double d : n2.outgoingEdges.values()) {
                        denominator += d;
                    }
                    double normalizedWeight = numerator / denominator;
                    rank += normalizedWeight * n2.rank;
                }
                rank *= dampingFactor;
                rank += 1.0 - dampingFactor;
                rank_list[i] = rank;
                dist_stats.addValue(Math.abs(n1.rank - rank));
            }
            final double standard_error =
                    dist_stats.getStandardDeviation() / Math.sqrt(dist_stats.getN());
            log.info("iteration: " + k + " error: " + standard_error);
            for (int i = 0; i < node_list.length; i++) {
                node_list[i].rank = rank_list[i];
            }
            if (standard_error < standardErrorThreshold)
                break;
        }
    }

    public void sortResults(final long max_results) {
        Arrays.sort(node_list, (o1, o2) -> Double.compare(o2.rank, o1.rank));
        dist_stats.clear();
        for (int i = 0; i < node_list.length; i++) {
            final Node n1 = node_list[i];
            if (i <= max_results) {
                n1.marked = true;
                dist_stats.addValue(n1.rank);
            }
            log.trace("n: {} {} {}", n1.key, n1.rank, n1.marked);
            for (Node n2 : n1.outgoingEdges.keySet()) {
                log.trace(" - {}", n2.key);
            }
        }
    }

    public double getRankThreshold() {
        return dist_stats.getMean()
                + (dist_stats.getStandardDeviation() * INCLUSIVE_COEFF);
    }

    public void printGraphValues() {
        log.info("Graph size: {}", this.size());
        for (Node node : this.values()) {
            log.info(node.toString());
        }
    }

    public void printNodeList() {
        log.info("Graph size: {}", this.size());
        for (Node node : this.node_list) {
            log.info(node.toString());
        }
    }
}
