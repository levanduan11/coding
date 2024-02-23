package com.coding.algorithm.graph.pagerank;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PageRank {
    private static final Logger log = LoggerFactory.getLogger(PageRank.class);
    private PageRankProps pageRankProps;
    protected Graph graph;
    protected long start_time = 0L;
    protected long elapsed_time = 0L;

    public PageRank(PageRankProps pageRankProps) {
        this.pageRankProps = pageRankProps;
    }

    public void initTime() {
        start_time = System.currentTimeMillis();
    }

    public void markTime(final String label) {
        elapsed_time = System.currentTimeMillis() - start_time;
        log.info("ELAPSED: {} {}", elapsed_time, label);
    }

    public Graph graph() {
        return graph;
    }

    public Node[] run(String graphFileDir) {
        log.info("Loading graph...");
        graph = Graph.loadGraph(graphFileDir, pageRankProps.graphFileSeparator, pageRankProps.dampingFactor, pageRankProps.standardErrorThreshold);
        log.info("Graph loaded successfully");
        initTime();
        final int max_results = graph.size();
        log.info("Running PageRank...");
        graph.runPageRank();
        graph.sortResults(max_results);
        log.info("PageRank complete for this graph");
        if (log.isTraceEnabled()) {
            graph.printNodeList();
        }
        markTime("basic_pagerank");
        log.info("Graph size: {}", graph.size());
        return graph.node_list;
    }
}
