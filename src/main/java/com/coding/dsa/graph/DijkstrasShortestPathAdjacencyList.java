package dsa.graph;

import java.util.*;
import java.util.stream.Stream;

public class DijkstrasShortestPathAdjacencyList {
    private static final double EPS = 1e-6;

    public static class Edge {
        double cost;
        int from, to;

        public Edge(int from, int to, double cost) {
            this.cost = cost;
            this.from = from;
            this.to = to;
        }
    }

    public static class Node {
        int id;
        double value;

        public Node(int id, double value) {
            this.id = id;
            this.value = value;
        }
    }

    private int n;
    private double[] dist;
    private Integer[] prev;
    private List<List<Edge>> graph;
    private Comparator<Node> comparator = (Node node1, Node node2) -> {
        if (Math.abs(node1.value - node2.value) < EPS) return 0;
        return (node1.value - node2.value) > 0 ? +1 : -1;
    };

    public DijkstrasShortestPathAdjacencyList(int n) {
        this.n = n;
        createEmptyGraph();
    }

    public DijkstrasShortestPathAdjacencyList(int n, Comparator<Node> comparator) {
        this(n);
        Objects.requireNonNull(comparator);
        this.comparator = comparator;
    }

    private void createEmptyGraph() {
        graph = new ArrayList<>(n);
        for (int i = 0; i < n; i++) graph.add(new ArrayList<>());
    }

    public List<List<Edge>> getGraph() {
        return graph;
    }

    public void addEdge(int from, int to, int cost) {
        graph.get(from).add(new Edge(from, to, cost));

    }

    public List<Integer> reconstructPath(int start, int end) {
        if (end < 0 || end >= n) throw new IllegalArgumentException();
        if (start < 0 || start >= n) throw new IllegalArgumentException();
        double dist = dijkstra(start, end);
        List<Integer> path = new ArrayList<>();
        if (dist == Double.POSITIVE_INFINITY) return path;
        for (Integer at = end; at != null; at = prev[at]) path.add(at);
        Collections.reverse(path);
        return path;
    }

    private double dijkstra(int start, int end) {
        dist = new double[n];
        Arrays.fill(dist, Double.POSITIVE_INFINITY);
        dist[start] = 0;
        PriorityQueue<Node> pq = new PriorityQueue<>(2 * n, comparator);
        pq.offer(new Node(start, 0));
        boolean[] visited = new boolean[n];
        prev = new Integer[n];
        while (!pq.isEmpty()) {
            Node node = pq.poll();
            visited[node.id] = true;

            if (dist[node.id] < node.value) continue;
            List<Edge> edges = graph.get(node.id);
            for (Edge edge : edges) {
                if (visited[edge.to]) continue;
                double newDist = dist[edge.from] + edge.cost;
                if (newDist < dist[edge.to]) {
                    prev[edge.to] = edge.from;
                    dist[edge.to] = newDist;
                    pq.offer(new Node(edge.to, dist[edge.to]));
                }
            }
            if (node.id == end) return dist[end];
        }
        return Double.POSITIVE_INFINITY;
    }

    public static void main(String[] args) {
       var sb = new StringBuilder();
       List<Person> list = Stream.of("nguyen van a","nguyen van b","nguyen van c")
              .map(Person::new)
              .peek(System.out::println)
              .toList();
    }
}
record Person(String name){}
