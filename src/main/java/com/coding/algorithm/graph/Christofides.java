package com.coding.algorithm.graph;

import java.util.*;

public class Christofides {
    static class Bag<I> implements Iterable<I> {
        private int N;
        private Node<I> first;

        static class Node<I> {
            private I item;
            private Node<I> next;
        }

        public Bag() {
            first = null;
            N = 0;
        }

        public boolean isEmpty() {
            return first == null;
        }

        public int size() {
            return N;
        }

        public void add(I item) {
            Node<I> oldFirst = first;
            first = new Node<I>();
            first.item = item;
            first.next = oldFirst;
            N++;
        }

        public int remove(I item) {
            if (first == null)
                return 1;
            if (first.item.equals(item)) {
                first = first.next;
                return 0;
            }
            Node<I> prev = first;
            while (prev.next != null && !prev.next.item.equals(item))
                prev = prev.next;
            if (prev.next == null)
                return 1;
            prev.next = prev.next.next;
            N--;
            return 0;
        }

        @SuppressWarnings("NullableProblems")
        @Override
        public Iterator<I> iterator() {
            return new ListIterator<>(first);
        }

        private static class ListIterator<I> implements Iterator<I> {
            private Node<I> current;

            public ListIterator(Node<I> first) {
                current = first;
            }

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public I next() {
                if (!hasNext())
                    throw new NoSuchElementException("no more elements");
                I item = current.item;
                current = current.next;
                return item;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("remove not supported");
            }
        }
    }

    static class Edge<T> implements Comparable<Edge<T>> {
        private T u;
        private T v;
        private double w;
        private boolean eularVisited;

        public Edge(T u, T v, double w) {
            this.u = u;
            this.v = v;
            this.w = w;
        }

        public T from() {
            return u;
        }

        public T to() {
            return v;
        }

        public double weight() {
            return w;
        }

        public void setW(double w) {
            this.w = w;
        }

        @Override
        public int compareTo(Edge<T> o) {
            return Double.compare(this.w, o.w);
        }

        public T other(T vertex) {
            if (vertex == u)
                return v;
            else if (vertex == v)
                return u;
            else
                throw new IllegalArgumentException("vertex not in edge");
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((u == null) ? 0 : u.hashCode());
            result = prime * result + ((v == null) ? 0 : v.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof Edge<?> that
                    && this.u.equals(that.u)
                    && this.v.equals(that.v);
        }

        public boolean isEularVisited() {
            return eularVisited;
        }

        public void setEularVisited(boolean eularVisited) {
            this.eularVisited = eularVisited;
        }
    }

    static class Vertex<T> implements Comparable<Vertex<T>> {
        @Override
        public int compareTo(Vertex<T> o) {
            return Double.compare(this.obj, o.obj);
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + obj;
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof Vertex<?> that
                    && this.obj == that.obj;
        }

        private int obj;
        private boolean visited;
        private Vertex<T> mate;
        private Vertex<T> parent;
        private Character type;
        private double label;
        private Vertex<T> root;
        private boolean real = true;

        public Vertex<T> getRoot() {
            return root;
        }

        public void setRoot(Vertex<T> root) {
            this.root = root;
        }

        private Bag<Edge<Vertex<T>>> adj;

        public Vertex(int value) {
            this.obj = value;
            adj = new Bag<>();
        }

        public int getValue() {
            return obj;
        }

        public boolean isVisited() {
            return visited;
        }

        public void setVisited(boolean visited) {
            this.visited = visited;
        }

        public Vertex<T> getMate() {
            return mate;
        }

        public void setMate(Vertex<T> mate) {
            this.mate = mate;
        }

        public Vertex<T> getParent() {
            return parent;
        }

        public void setParent(Vertex<T> parent) {
            this.parent = parent;
        }

        public Character getType() {
            return type;
        }

        public double getLabel() {
            return label;
        }

        public void setLabel(double label) {
            this.label = label;
        }

        public void setType(Character type) {
            this.type = type;
        }

        public Bag<Edge<Vertex<T>>> adjacencyList() {
            return adj;
        }

        public boolean isReal() {
            return real;
        }

        public void setReal(boolean real) {
            this.real = real;
        }

        public int getObj() {
            return obj;
        }

        public void setObj(int obj) {
            this.obj = obj;
        }

        public Bag<Edge<Vertex<T>>> getAdj() {
            return adj;
        }

        public void setAdj(Bag<Edge<Vertex<T>>> adj) {
            this.adj = adj;
        }
    }

    static class UF {
        private int[] id;
        private byte[] rank;
        private int count;

        public UF(int n) {
            if (n < 0)
                throw new IllegalArgumentException();
            count = n;
            id = new int[n];
            rank = new byte[n];
            for (int i = 0; i < n; i++) {
                id[i] = i;
                rank[i] = 0;
            }
        }

        public int find(int p) {
            if (p < 0 || p >= id.length)
                throw new IndexOutOfBoundsException("p is out of bounds");
            while (p != id[p]) {
                id[p] = id[id[p]];
                p = id[p];
            }
            return p;
        }

        public int count() {
            return count;
        }

        public boolean connected(int p, int q) {
            return find(p) == find(q);
        }

        public void union(int p, int q) {
            int i = find(p);
            int j = find(q);
            if (i == j)
                return;
            if (rank[i] < rank[j])
                id[i] = j;
            else if (rank[i] > rank[j])
                id[j] = i;
            else {
                id[j] = i;
                rank[i]++;
            }
            count--;
        }
    }

    static class Queue<I> implements Iterable<I> {
        private int N;
        private Node<I> first;
        private Node<I> last;

        static class Node<I> {
            private I item;
            private Node<I> next;
        }

        public Queue() {
            first = null;
            last = null;
            N = 0;
        }

        public boolean isEmpty() {
            return first == null;
        }

        public int size() {
            return N;
        }

        public I peek() {
            if (isEmpty())
                throw new NoSuchElementException("queue is empty");
            return first.item;
        }

        public void enqueue(I item) {
            Node<I> oldLast = last;
            last = new Node<>();
            last.next = null;
            last.item = item;
            if (isEmpty())
                first = last;
            else
                oldLast.next = last;
            N++;
        }

        public I dequeue() {
            if (isEmpty())
                throw new NoSuchElementException("queue is empty");
            I item = first.item;
            first = first.next;
            N--;
            if (isEmpty())
                last = null;
            return item;
        }

        @Override
        public String toString() {
            StringBuilder s = new StringBuilder();
            s.append('[');
            int c = N;
            for (I item : this) {
                s.append(item);
                if (--c > 0) {
                    s.append(", ");
                }
            }
            s.append(']');
            return s.toString();
        }

        @Override
        public Iterator<I> iterator() {
            return new ListIterator<>(first);
        }

        private static class ListIterator<I> implements Iterator<I> {
            private Node<I> current;

            public ListIterator(Node<I> first) {
                current = first;
            }

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public I next() {
                if (!hasNext())
                    throw new NoSuchElementException("no more elements");
                I item = current.item;
                current = current.next;
                return item;
            }
        }
    }

    static class MinPQ<K> implements Iterable<K> {
        private K[] pq;
        private int N;
        private Comparator<K> comparator;

        @SuppressWarnings("unchecked")
        public MinPQ(int capacity) {
            pq = (K[]) new Object[capacity + 1];
            N = 0;
        }

        public MinPQ() {
            this(1);
        }

        @SuppressWarnings("unchecked")
        public MinPQ(int capacity, Comparator<K> comparator) {
            this.comparator = comparator;
            pq = (K[]) new Object[capacity + 1];
            N = 0;
        }

        public MinPQ(Comparator<K> comparator) {
            this(1, comparator);
        }

        @SuppressWarnings("unchecked")
        public MinPQ(K[] keys) {
            N = keys.length;
            pq = (K[]) new Object[N + 1];
            for (int i = 0; i < N; i++) {
                pq[i + 1] = keys[i];
            }
            for (int k = N / 2; k >= 1; k--) {
                sink(k);
            }
            assert isMinHeap();
        }

        public boolean isEmpty() {
            return N == 0;
        }

        public int size() {
            return N;
        }

        public K min() {
            if (isEmpty())
                throw new NoSuchElementException("Priority queue underflow");
            return pq[1];
        }

        @SuppressWarnings("unchecked")
        private void resize(int capacity) {
            assert capacity > N;
            K[] temp = (K[]) new Object[capacity];
            for (int i = 1; i <= N; i++) {
                temp[i] = pq[i];
            }
            pq = temp;
        }

        public void insert(K x) {
            if (N == pq.length - 1)
                resize(2 * pq.length);
            pq[++N] = x;
            swim(N);
            assert isMinHeap();
        }

        public K delMin() {
            if (isEmpty())
                throw new NoSuchElementException();
            exch(1, N);
            K min = pq[N--];
            sink(1);
            pq[N + 1] = null;
            if (N > 0 && N == (pq.length - 1) / 4)
                resize(pq.length / 2);
            assert isMinHeap();
            return min;
        }

        private void swim(int k) {
            while (k > 1 && greater(k / 2, k)) {
                exch(k, k / 2);
                k = k / 2;
            }
        }

        private void sink(int k) {
            while (2 * k <= N) {
                int j = 2 * k;
                if (j < N && greater(j, j + 1))
                    j++;
                if (!greater(k, j))
                    break;
                exch(k, j);
                k = j;
            }
        }

        @SuppressWarnings("unchecked")
        private boolean greater(int i, int j) {
            if (comparator == null)
                return ((Comparable<K>) pq[i]).compareTo(pq[j]) > 0;
            else
                return comparator.compare(pq[i], pq[j]) > 0;
        }

        private void exch(int i, int j) {
            K tmp = pq[i];
            pq[i] = pq[j];
            pq[j] = tmp;
        }

        private boolean isMinHeap() {
            return isMinHeap(1);
        }

        private boolean isMinHeap(int k) {
            if (k > N)
                return true;
            int l = 2 * k;
            int r = l + 1;
            if (l <= N && greater(k, l))
                return false;
            if (r <= N && greater(k, r))
                return false;
            return isMinHeap(l) && isMinHeap(r);
        }

        @Override
        public Iterator<K> iterator() {
            return null;
        }

        private class HeapIterator implements Iterator<K> {
            private MinPQ<K> copy;

            public HeapIterator() {
                if (MinPQ.this.comparator == null)
                    copy = new MinPQ<>(MinPQ.this.size());
                else
                    copy = new MinPQ<>(MinPQ.this.size(), MinPQ.this.comparator);
                for (int i = 1; i <= N; i++) {
                    copy.insert(pq[i]);
                }
            }

            @Override
            public boolean hasNext() {
                return !copy.isEmpty();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("remove not supported");
            }

            @Override
            public K next() {
                if (!hasNext())
                    throw new NoSuchElementException();
                return copy.delMin();
            }
        }
    }

    static class Graph<T> {
        private final int INFINITY = Integer.MAX_VALUE;
        private final int V;
        private int E;
        private Vertex<T>[] vertices;
        private double[][] adjMatrix;

        @SuppressWarnings("unchecked")
        public Graph(int V, Integer[] values) {
            this.V = V;
            this.vertices = new Vertex[V + 1];
            this.adjMatrix = new double[V + 1][V + 1];
            for (int i = 1, j = 0; i < V + 1; i++, j++) {
                this.vertices[i] = new Vertex<>(values[j]);
                for (int k = 1; k < V + 1; k++) {
                    this.adjMatrix[i][k] = INFINITY;
                }
            }
        }

        public void addEdge(int u, int v, double w) {
            Vertex<T> from = this.vertices[u];
            Vertex<T> to = this.vertices[v];
            from.adjacencyList().add(new Edge<>(from, to, w));
            to.adjacencyList().add(new Edge<>(to, from, w));
            adjMatrix[u][v] = w;
            adjMatrix[v][u] = w;
        }

        public Vertex<T> addVertex(Vertex<T> newVertex, int index) {
            this.vertices[index] = newVertex;
            return newVertex;
        }

        public int removeVertex(Vertex<Integer> remVertex) {
            int i = 1;
            for (; i < vertices.length; i++) {
                Vertex<T> vertex = vertices[i];
                if (vertex != null && vertex.getValue() == remVertex.getValue()) {
                    this.vertices[i] = null;
                    break;
                }
            }
            return i;
        }

        public int removeEdge(Edge<Vertex<T>> removingEdge) {
            int f = 0;
            for (int i = 1; i < vertices.length; i++) {
                Vertex<T> u = vertices[i];
                if (u != null && u.getValue() == removingEdge.to().getValue()) {
                    f = u.adjacencyList().remove(new Edge<>(removingEdge.to(), removingEdge.from(), removingEdge.weight()));
                }
            }
            return f;
        }

        public int noOfVertices() {
            return V;
        }

        public Vertex<T>[] vertices() {
            return vertices;
        }

        public int noOfEdges() {
            return E;
        }

        public boolean edgePresent(int u, int v) {
            Vertex<T> from = vertices[u];
            Vertex<T> to = vertices[v];
            for (Edge<Vertex<T>> edge : from.adjacencyList()) {
                if (to.equals(edge.to()))
                    return true;
            }
            return false;
        }

        public Iterable<Edge<Vertex<T>>> edges() {
            Bag<Edge<Vertex<T>>> list = new Bag<>();
            for (int v = 1; v < vertices.length; v++) {
                Vertex<T> vertex = vertices[v];
                int selfLoops = 0;
                for (Edge<Vertex<T>> e : vertex.adjacencyList()) {
                    if ((e.other(vertex).getValue() > vertex.getValue()))
                        list.add(e);
                    else if ((e.other(vertex).getValue() == vertex.getValue())) {
                        if (selfLoops % 2 == 0)
                            selfLoops++;
                    }
                }
            }
            return list;
        }
    }

    private static double weight;
    private static Queue<Edge> mst = new Queue<>();
    private static double[][] distTo;
    private static Edge[][] edgeTo;
    private static int mszie;
    private static int tVertices;
    private static ArrayList<Vertex<Integer>> shortestMatchedPath = new ArrayList<>();
    private static Vertex[][] next;

    public static void main(String[] args) {
        int tEdges = 0;
        Scanner console = new Scanner(System.in);
        Scanner lineTokenizer2;
        Graph<Integer>G;
        Scanner lineTokenizer = new Scanner(console.nextLine());
        tVertices = lineTokenizer.nextInt();
        tEdges = lineTokenizer.nextInt();
        Integer[] values = new Integer[tVertices];
        for (int i = 0; i < tVertices; i++) {
            values[i] = i + 1;
        }
        G = new Graph<>(tVertices, values);
        lineTokenizer.close();
        while (console.hasNextLine()){
            lineTokenizer2 = new Scanner(console.nextLine());
            if (lineTokenizer2.hasNextInt())
                G.addEdge(lineTokenizer2.nextInt(),lineTokenizer2.nextInt(),lineTokenizer2.nextInt());
            else
                break;
            lineTokenizer2.close();
        }
        long start = System.currentTimeMillis();
        Kuskal(G);
        Graph<Integer>T = new Graph<>(values.length,values);
        for (Edge<Vertex<Integer>> edge : mst) {
            T.addEdge(edge.from().getValue(),edge.to().getValue(),edge.weight());
        }
    }

    private static void eularTour(Stack<Vertex<Integer>> eularStack) {
        for (Edge<Vertex<Integer>> edge : eularStack.lastElement().adjacencyList()) {
            if (!edge.isEularVisited()) {
                edge.setEularVisited(true);
                for (Edge<Vertex<Integer>> otherEdge : edge.to().adjacencyList()) {
                    if (otherEdge.to().equals(edge.from()) && !otherEdge.isEularVisited()) {
                        otherEdge.setEularVisited(true);
                        break;
                    }
                }
                eularStack.push(edge.to());
                eularTour(eularStack);
            }
        }
    }

    private static List<Vertex<Integer>> foydWarshalPath(Vertex<Integer> u, Vertex<Integer> v) {
        if (next[u.getValue()][v.getValue()] == null)
            return new ArrayList<>();
        shortestMatchedPath = new ArrayList<>();
        shortestMatchedPath.add(u);
        while (!u.equals(v)) {
            u = next[u.getValue()][v.getValue()];
            shortestMatchedPath.add(u);
        }
        return shortestMatchedPath;
    }

    private static List<Edge<Vertex<Integer>>> findMinWeightGraph(Graph<Integer> graph, Integer[] values) {
        char augmentPathFoundFlag = '0';
        boolean fistblosomcheck = false;
        List<Edge<Vertex<Integer>>> matchedEdges = null;
        reduceLabel(graph);

        int counter = 0;
        Map<Vertex<Integer>, List<Vertex<Integer>>> shrinkMap = new HashMap<>();
        while (true) {
            Graph<Integer> Z = new Graph<>(values.length, values);
            for (Edge<Vertex<Integer>> edge : graph.edges()) {
                if (edge.to().getLabel() + edge.from().getLabel() == edge.weight())
                    Z.addEdge(edge.from().getValue(), edge.to().getValue(), edge.weight());
            }
            for (int i = 1; i < Z.vertices().length; i++) {
                Z.vertices()[i].setLabel(graph.vertices()[i].getLabel());
            }
            for (int i = 1; i < Z.vertices().length; i++) {
                for (Edge<Vertex<Integer>> edge : Z.vertices()[i].adjacencyList()) {
                    Vertex<Integer> v = edge.to();
                    if (Z.vertices()[i].getMate() == null && v.getMate() == null) {
                        Z.vertices()[i].setMate(v);
                        v.setMate(Z.vertices()[i]);
                        mszie++;
                    }
                }
            }
            Queue<Vertex<Integer>> outerQueue = new Queue<>();
            for (Vertex<Integer> u : Z.vertices()) {
                if (u != null) {
                    if (u.getMate() == null)
                        outerQueue.enqueue(u);
                }
            }
            List<Vertex<Integer>> blosomList = null;
            boolean blosomFlag = false;
            while (!outerQueue.isEmpty()) {
                Vertex<Integer> u = outerQueue.dequeue();
                u.setVisited(true);
                for (Edge<Vertex<Integer>> edge : u.adjacencyList()) {
                    Vertex<Integer> v = edge.to();
                    if (v.getMate().equals(u))
                        continue;
                    if (!v.isVisited()) {
                        if (v.getMate() == null && v.getType() == 'i') {
                            System.out.println("Augment Path Found");
                            augmentPathFoundFlag = '1';
                            processAugPath(v);
                            break;
                        } else {
                            v.setType('i');
                            v.setParent(u);
                            v.setRoot(u.getRoot() == null ? u : u.getRoot());
                            Vertex<Integer> x = v.getMate();
                            x.setType('o');
                            x.setParent(v);
                            x.setVisited(true);
                            outerQueue.enqueue(x);
                        }
                    } else {
                        if (v.getType() == 'i')
                            continue;
                        else if (v.getType() == 'o') {
                            if (v.getRoot() != u.getRoot()) {
                                System.out.println("Augment Path Found outside tree");
                                augmentPathFoundFlag = '2';
                                v.setMate(u);
                                u.setMate(v);
                                processAugPath(u.getParent());
                                processAugPath(v.getParent());
                                break;
                            } else {
                                if (v.getLabel() == 0) {
                                    System.out.println("Augment Path Found inside tree");
                                    augmentPathFoundFlag = '3';
                                    break;
                                } else {
                                    blosomList = new ArrayList<>();
                                    blosomList.add(v);
                                    createBlossomList(u, v, blosomList);
                                    blosomFlag = true;
                                    if (shrinkMap.isEmpty())
                                        fistblosomcheck = true;
                                    break;
                                }
                            }
                        }
                    }
                }
                if (blosomFlag || augmentPathFoundFlag != '0')
                    break;
            }
            if (blosomFlag) {
                List<Edge<Vertex<Integer>>> edgeList = new ArrayList<>();
                for (Edge<Vertex<Integer>> edge : graph.edges()) {
                    if (blosomList.contains(edge.from()) && !blosomList.contains(edge.to()))
                        edgeList.add(edge);
                }
                int index = 0;
                for (Vertex<Integer> cycleEdge : blosomList) {
                    index = Z.removeVertex(cycleEdge);
                    index = graph.removeVertex(cycleEdge);
                }
                Vertex<Integer> newVertex = new Vertex<>(tVertices + (++counter));
                newVertex.setLabel(0);
                newVertex.setType('o');
                newVertex.setReal(false);
                newVertex.setRoot(blosomList.get(blosomList.size() - 1));
                Z.addVertex(newVertex, index);
                graph.addVertex(newVertex, index);
                for (Edge<Vertex<Integer>> removingEdge : edgeList) {
                    if (blosomList.get(blosomList.size() - 1).getValue() == removingEdge.from().getValue()) {
                        Z.addEdge(index, removingEdge.to().getValue(), removingEdge.weight());
                        graph.addEdge(index, removingEdge.to().getValue(), removingEdge.weight());
                    }
                    graph.removeEdge(removingEdge);
                    Z.removeEdge(removingEdge);
                }
                shrinkMap.put(newVertex, blosomList);
            }
            if (blosomFlag || outerQueue.isEmpty()) {
                double delta = Double.MAX_VALUE;
                for (Edge<Vertex<Integer>> edge : Z.edges()) {
                    if ((edge.from().getType() == 'o') && (!edge.to().getRoot().equals(edge.from().getRoot()))) {
                        double slack = edge.from().getLabel() + edge.to().getLabel() - edge.weight();
                        if (delta < slack)
                            delta = slack;
                    }
                }
                if (delta != Double.MAX_VALUE) {
                    for (int i = 0; i < Z.vertices().length; i++) {
                        Vertex<Integer> u = Z.vertices()[i];
                        if (u != null) {
                            if (u.getType() == 'o' && u.isReal())
                                u.setLabel(u.getLabel() + delta);
                            else if (u.getType() == 'o' && !u.isReal()) {
                                if (fistblosomcheck) {
                                    u.setLabel(u.getLabel() - 2 * delta);
                                    fistblosomcheck = true;
                                }
                            } else if (u.getType() == 'i' && u.isReal()) {
                                u.setLabel(u.getLabel() - delta);
                            } else {
                                if (fistblosomcheck) {
                                    u.setLabel(u.getLabel() + 2 * delta);
                                    fistblosomcheck = false;
                                }
                            }
                        }
                    }
                }
            }
            if (augmentPathFoundFlag != '0') {
                boolean perfectCheck = false;
                for (Vertex<Integer> u : Z.vertices()) {
                    if (u != null) {
                        if (u.getMate() == null) {
                            perfectCheck = true;
                            break;
                        }
                    }
                }
                if (!perfectCheck) {
                    System.out.println("Perfect Matching");
                }
                if (!shrinkMap.isEmpty()) {
                    Vertex<Integer> blossomVertex = null;
                    for (int i = 1; i < graph.vertices().length; i++) {
                        if (shrinkMap.containsKey(graph.vertices()[i])) {
                            blossomVertex = graph.vertices()[i];
                            break;
                        }
                    }
                    List<Vertex<Integer>> blossomCycle = shrinkMap.get(blossomVertex);
                    if (blossomVertex.getType() == 'i') {
                        System.out.println("Perfect Matching");
                    } else if (blossomVertex.getType() == 'o') {
                        for (int i = 0; i < blossomCycle.size(); i++) {
                            blossomCycle.get(i).setMate(null);
                        }
                        for (int i = 0; i < blossomCycle.size(); i++) {
                            for (Edge<Vertex<Integer>> edge : blossomCycle.get(i).adjacencyList()) {
                                if (blossomCycle.contains(edge.from()) && blossomCycle.contains(edge.to())) {
                                    Vertex<Integer> v = edge.to();
                                    if (blossomCycle.get(i).getMate() == null && v.getMate() == null
                                            && blossomCycle.get(i).getLabel() != 0 && v.getLabel() != 0) {
                                        blossomCycle.get(i).setMate(v);
                                        v.setMate(blossomCycle.get(i));
                                    }
                                }
                            }
                        }
                    }
                    for (Edge<Vertex<Integer>> edge : blossomVertex.adjacencyList()) {
                        graph.removeEdge(edge);
                    }
                    graph.removeVertex(blossomVertex);
                    for (int i = 0; i < blossomCycle.size(); i++) {
                        for (Edge<Vertex<Integer>> edge : blossomCycle.get(i).adjacencyList()) {
                            if (!blossomCycle.contains(edge.to())) {
                                graph.vertices()[edge.to().getValue()].adjacencyList().add(new Edge<>(edge.to(), edge.from(), edge.weight()));
                            }
                        }
                    }
                    shrinkMap.remove(blossomVertex);
                } else {
                    double totalWeight = 0;
                    matchedEdges = new ArrayList<>();
                    for (int i = 1; i < graph.vertices().length; i++) {
                        Vertex<Integer> u = graph.vertices()[i];
                        for (Edge<Vertex<Integer>> edge : u.adjacencyList()) {
                            Vertex<Integer> v = edge.to();
                            if ((u.getMate().equals(v) && (!matchedEdges.contains(edge)))) {
                                totalWeight += edge.weight();
                                matchedEdges.add(edge);
                            }
                        }
                    }
                    for (Edge<Vertex<Integer>> edge : matchedEdges) {
                        System.out.println(edge.from().getValue() + " " + edge.to().getValue() + " " + edge.weight());
                    }
                    break;
                }
            }
        }
        return matchedEdges;
    }

    private static void createBlossomList(Vertex<Integer> u, Vertex<Integer> v, List<Vertex<Integer>> blosomList) {
        Vertex<Integer> x = v.getParent();
        if (x != u) {
            blosomList.add(x);
            createBlossomList(u, x, blosomList);
        } else {
            blosomList.add(x);
        }
    }

    private static void reduceLabel(Graph<Integer> graph) {
        double[] labels = new double[graph.vertices().length];
        for (int i = 1; i < graph.vertices().length; i++) {
            Vertex<Integer> u = graph.vertices()[i];
            if (u != null) {
                for (Edge<Vertex<Integer>> edge : u.adjacencyList()) {
                    Vertex<Integer> v = edge.to();
                    if (edge.weight() - v.getLabel() > 0) {
                        if (labels[i] == 0)
                            labels[i] = edge.weight() - v.getLabel();
                        else if (labels[i] > edge.weight() - v.getLabel())
                            labels[i] = edge.weight() - v.getLabel();
                    }
                }
            }
        }
        for (int i = 1; i < graph.vertices().length; i++) {
            Vertex<Integer> u = graph.vertices()[i];
            if (u != null)
                u.setLabel(labels[i]);
        }
    }

    private static void processAugPath(Vertex<Integer> vertex) {
        Vertex<Integer> p = vertex.getParent();
        Vertex<Integer> x = p.getParent();
        vertex.setMate(p);
        p.setMate(vertex);
        while (x != null) {
            Vertex<Integer> newMate = x.getParent();
            Vertex<Integer> y = newMate.getParent();
            x.setMate(newMate);
            newMate.setMate(x);
            x = y;
        }
        mszie++;
    }

    private static void dfs(Vertex<Integer> vertex) {
        if (!vertex.isVisited()) {
            vertex.setVisited(true);
            for (Edge<Vertex<Integer>> edge : vertex.adjacencyList()) {
                Vertex<Integer> v = edge.to();
                if (vertex.getType() == 'o')
                    v.setType('i');
                else
                    v.setType('o');
                dfs(v);
            }
        }
    }

    private static void floydWarshall(Graph<Integer> graph) {
        int V = graph.vertices().length;
        distTo = new double[V][V];
        edgeTo = new Edge[V][V];
        next = new Vertex[V][V];
        for (int v = 1; v < V; v++) {
            for (int w = 1; w < V; w++) {
                distTo[v][w] = Double.POSITIVE_INFINITY;
            }
        }
        for (int v = 1; v < graph.vertices().length; v++) {
            for (Edge<Vertex<Integer>> e : graph.vertices()[v].adjacencyList()) {
                distTo[e.from().getValue()][e.to().getValue()] = e.weight();
                edgeTo[e.from().getValue()][e.to().getValue()] = e;
                next[e.from().getValue()][e.to().getValue()] = e.to();
            }
            if (distTo[v][v] > 0.0) {
                distTo[v][v] = 0.0;
                edgeTo[v][v] = null;
                next[v][v] = null;
            }
        }
        for (int i = 1; i < V; i++) {
            for (int v = 1; v < V; v++) {
                if (edgeTo[v][i] == null)
                    continue;
                for (int w = 0; w < V; w++) {
                    if (distTo[v][w] > distTo[v][i] + distTo[i][w]) {
                        distTo[v][w] = distTo[v][i] + distTo[i][w];
                        edgeTo[v][w] = edgeTo[i][w];
                        next[v][w] = next[v][i];
                    }
                }
                if (distTo[v][v] < 0.0) {
                    System.out.println("Negative Cycle");
                    break;
                }
            }
        }
        System.out.println("Floyd Warshall done");
    }

    private static void Kuskal(Graph<Integer> graph) {
        MinPQ<Edge<Vertex<Integer>>> pq = new MinPQ<>();
        for (Edge<Vertex<Integer>> edge : graph.edges()) {
            pq.insert(edge);
        }
        UF uf = new UF(graph.noOfVertices() + 1);
        while (!pq.isEmpty() && mst.size() < graph.noOfVertices() - 1) {
            Edge<Vertex<Integer>> e = pq.delMin();
            Vertex<Integer> ver1 = e.to();
            Vertex<Integer> ver2 = e.other(ver1);
            if (!uf.connected(ver1.getValue(), ver2.getValue())) {
                uf.union(ver1.getValue(), ver2.getValue());
                mst.enqueue(e);
                weight += e.weight();
            }
        }
    }

}
