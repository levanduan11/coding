package com.coding.algorithm.graph.pagerank;

import java.util.Properties;

public class PageRankProps {
    public final String graphFileSeparator;
    public final double standardErrorThreshold;
    public final double dampingFactor;
    public PageRankProps(Properties props){
        this.graphFileSeparator = props.getProperty("se_k");
        this.standardErrorThreshold = Double.parseDouble(props.getProperty("th_k"));
        this.dampingFactor = Double.parseDouble(props.getProperty("d_k"));
    }

    @Override
    public String toString() {
        return "PageRankProps{" +
                "graphFileSeparator='" + graphFileSeparator + '\'' +
                ", standardErrorThreshold=" + standardErrorThreshold +
                ", dampingFactor=" + dampingFactor +
                '}';
    }
}
