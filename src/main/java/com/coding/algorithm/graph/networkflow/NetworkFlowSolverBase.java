package com.coding.algorithm.graph.networkflow;

import java.util.ArrayList;
import java.util.List;

public abstract class NetworkFlowSolverBase {
    protected static final long INF = Long.MAX_VALUE / 2;

    public static class Edge {
        public int from, to;
        public Edge residual;
        public long flow, cost;
        public final long capacity, originalCost;

        public Edge(int from, int to, long capacity) {
            this(from, to, capacity, 0);
        }

        public Edge(int from, int to, long capacity, long cost) {
            this.from = from;
            this.to = to;
            this.capacity = capacity;
            this.originalCost = this.cost = cost;
        }

        public boolean isResidual() {
            return capacity == 0;
        }

        public long remainingCapacity() {
            return capacity - flow;
        }

        public void augment(long bottleNeck) {
            flow += bottleNeck;
            residual.flow -= bottleNeck;
        }

        public String toString(int s, int t) {
            String u = (from == s) ? "s" : String.valueOf(from);
            String v = (to == s) ? "t" : String.valueOf(to);
            return String.format(
                    "Edge %s -> %s | flow = %d | capacity = %d | is residual: %s",
                    u, v, flow, capacity, isResidual()
            );
        }
    }

    protected final int n, s, t;
    protected long maxFlow;
    protected long minCost;
    protected boolean[] minCut;
    protected List<Edge>[] graph;
    private int visitedToken = 1;
    private int[] visited;
    private boolean solved;

    public NetworkFlowSolverBase(int n, int s, int t) {
        this.n = n;
        this.s = s;
        this.t = t;
        initializedGraph();
        minCut = new boolean[n];
        visited = new int[n];
    }
    @SuppressWarnings("unchecked")
    private void initializedGraph(){
        graph = new List[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }
    }
    public void addEdge(int from,int to,long capacity){
        if (capacity<0)
            throw new IllegalArgumentException();
        Edge e1 = new Edge(from,to,capacity);
        Edge e2 = new Edge(to,from,0);
        e1.residual = e2;
        e2.residual = e1;
        graph[from].add(e1);
        graph[to].add(e2);
    }
    public void addEdge(int from,int to,long capacity,long cost){
        Edge e1 = new Edge(from,to,capacity,cost);
        Edge e2 = new Edge(to,from,0,-cost);
        e1.residual = e2;
        e2.residual = e1;
        graph[from].add(e1);
        graph[to].add(e2);
    }
    public void visit(int i){
        visited[i] = visitedToken;
    }
    public boolean visited(int i){
        return visited[i] == visitedToken;
    }
    public void markAllNodesAsUnvisited(){
        visitedToken++;
    }

    public List<Edge>[] graph() {
        execute();
        return graph;
    }

    public long maxFlow() {
        execute();
        return maxFlow;
    }

    public long minCost() {
        execute();
        return minCost;
    }

    public boolean[] minCut() {
        execute();
        return minCut;
    }
    private void execute(){
        if (solved)
            return;
        solved = true;
        solve();
    }
    public abstract void solve();
}
