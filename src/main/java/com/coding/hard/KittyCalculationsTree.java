package hard;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class KittyCalculationsTree {

    int n, q;
    List<int[]> edges;
    Map<Integer, List<Integer>> sets;
    Map<Integer, List<Integer>> graph;
    PrintStream out;

    public KittyCalculationsTree() {
        out = System.out;
        readInput();
        buildGraph(this.edges);
        solve();
    }

    void solve() {
        for (Map.Entry<Integer, List<Integer>> entry : sets.entrySet()) {
            List<Integer> edge = entry.getValue();
            List<int[]> list = findEdges(edge);
            long res = 0;
            for (int[] ints : list) {
                int start = ints[0];
                int end = ints[1];
                int distance = dfs(graph, start, end, graph.size());
                res += ((long) start * end * distance);
            }
            res = (long) (res % (Math.pow(10, 9) + 7));
            out.println(res);
        }
    }

    List<int[]> findEdges(List<Integer> set) {
        int n = set.size();
        List<int[]> res = new ArrayList<>();
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                int u = set.get(i);
                int v = set.get(j);
                res.add(new int[]{u, v});
            }
        }
        return res;
    }

    void readInput() {
        edges = new ArrayList<>();
        sets = new LinkedHashMap<>();
        Path path = Paths.get("input.txt");
        try (Stream<String> stream = Files.lines(path)) {
            List<String> strings = stream.collect(Collectors.toList());
            String[] first = strings.get(0).split(" ");
            this.n = Integer.parseInt(first[0]);
            this.q = Integer.parseInt(first[1]);

            List<String> edges = strings.subList(1, q * 2 + 1);
            this.edges = edges.stream()
                    .map(s -> {
                        String[] arr = s.split(" ");
                        int[] ints = new int[arr.length];
                        int i = 0;
                        for (String s1 : arr) {
                            ints[i++] = Integer.parseInt(s1);
                        }
                        return ints;
                    }).collect(Collectors.toList());
            List<String> remain = strings.subList(q * 2 + 1, strings.size());
            for (int i = 1; i < remain.size(); i += 2) {
                int n = Integer.parseInt(remain.get(i - 1));
                List<Integer> arr = parserToIntArr(remain.get(i).split(" "));
                sets.put(n, new ArrayList<>(arr));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    List<Integer> parserToIntArr(String[] strings) {
        if (strings == null || strings.length == 0) return new ArrayList<>();
        List<Integer> res = new ArrayList<>(strings.length);
        for (String string : strings) {
            res.add(Integer.parseInt(string));
        }
        return res;
    }

    int dfs(Map<Integer, List<Integer>> graph, int start, int end, int n) {
        int count = 0;
        boolean[] visited = new boolean[n];
        Deque<Integer> stack = new ArrayDeque<>();

        stack.push(start);
        visited[start] = true;

        while (!stack.isEmpty()) {
            int node = stack.pop();
            count++;
            List<Integer> edges = graph.get(node);
            if (edges != null) {
                for (Integer e : edges) {
                    if (!visited[e]) {
                        visited[e] = true;
                        stack.push(e);
                        if (e == end) return count;
                    }
                }
            }
        }
        return count - 1;
    }

    void addEdge(Map<Integer, List<Integer>> graph, int from, int to) {
        List<Integer> fromEdges = graph.getOrDefault(from, new ArrayList<>());
        fromEdges.add(to);
        List<Integer> toEdges = graph.getOrDefault(to, new ArrayList<>());
        toEdges.add(from);
        graph.put(from, fromEdges);
        graph.put(to, toEdges);
    }

    void buildGraph(List<int[]> edges) {
        this.graph = new LinkedHashMap<>();
        for (int[] edge : edges) {
            int from = edge[0];
            int to = edge[1];
            addEdge(graph, from, to);
        }
    }

    public static void main(String[] args) throws IOException {
        new KittyCalculationsTree();
    }
}

class C {
    static ArrayList<ArrayList<Integer>> edges = new ArrayList<>();
    static int[] nodeDepth;
    static int[][] nodeLinks;
    static boolean[] nodeOccupied;
    static long[] nodeSum;
    static long totalResult;
    static final long MODMAX = 1000000007;

    public static void main(String[] args) {
        Reader scan = new Reader();

        int N = scan.readInt();
        int Q = scan.readInt();

        for (int i = 0; i <= N; i++) {
            edges.add(new ArrayList<>());
        }
        edges.get(1).add(0);
        for (int i = 0; i < N - 1; i++) {
            int a = scan.readInt();
            int b = scan.readInt();
            edges.get(a).add(b);
            edges.get(b).add(a);
        }
        // Process entire tree, calculate depth and parentage
        processTree(N);
        StringBuilder sb = new StringBuilder(Q * 10);
        // Read and process all sets
        for (int q = 0; q < Q; q++) {
            int K = scan.readInt();
            if (K <= 1) {
                scan.readInt();
                sb.append('0');
                sb.append('\n');
                continue;
            }
            nodeOccupied = new boolean[N + 1];
            nodeSum = new long[N + 1];

            long kSum = 0;
            int maxDepth = 0;

            ArrayList<Integer> fullNodeList = new ArrayList<>(K + 1);

            // build (k) sets from input
            for (int k = 0; k < K; k++) {
                int node = scan.readInt();
                int depth = nodeDepth[node];
                if (depth > maxDepth) maxDepth = depth;
                kSum += node;
                nodeOccupied[node] = true;
                nodeSum[node] = node;
                fullNodeList.add(node);
            }
            // Compute part of the result
            long totalWithoutLCA = computeFullDepthResult(fullNodeList, K, kSum);

            totalResult = 0;
            computeSubtreeResult(fullNodeList, 0, maxDepth, 1);
            long totalOnlyLCA = totalResult;

            // Combine both parts of the result
            long result = combineResults(totalWithoutLCA, totalOnlyLCA);
            sb.append(result);
            sb.append('\n');

        }
        System.out.println(sb);

    }

    static final void computeSubtreeResult(ArrayList<Integer> nodesInSubtree, int topDepth, int bottomDepth, int rootNode) {
        // If only 1 node in this subtree, move it to the root and return.
        int subtreeSize = nodesInSubtree.size();
        if (subtreeSize == 1) {
            nodeOccupied[rootNode] = true;
            nodeSum[rootNode] = nodeSum[nodesInSubtree.get(0)];
            return;
        }
        // If more than 1 node exists and depth indicates only 2 levels of nodes, then merge and return.
        int height = bottomDepth - topDepth;
        if (height == 1) {
            // Merge all the child nodes
            int onChild = 0;
            if (!nodeOccupied[rootNode]) {
                // move first child to this node
                int childNode = nodesInSubtree.get(0);
                nodeOccupied[rootNode] = true;
                nodeSum[rootNode] = nodeSum[childNode];
                onChild++;
            }
            while (onChild < subtreeSize) {
                // Merge to parent
                int childNode = nodesInSubtree.get(onChild);
                if (childNode != rootNode) {
                    // Compute piecewise to keep range of values within sizeof(long).
                    totalResult = modSumMultiply(totalResult, nodeSum[rootNode], nodeSum[childNode], topDepth);

                    // Merge this node into its parent. This is an LCA node.
                    nodeSum[rootNode] = modSum(nodeSum[rootNode], nodeSum[childNode]);
                }
                onChild++;
            }
            return;
        }
        // More than 2 nodes exist and are not in merge distance,so subdivide and recurse.
        int midDepth = topDepth + height / 2;
        HashMap<Integer, ArrayList<Integer>> parentList = new HashMap<>();
        ArrayList<Integer> nodesTooHigh = new ArrayList<>();
        // Map every subtree node to its half-depth parent.
        for (int node : nodesInSubtree) {
            if (nodeDepth[node] < midDepth) {
                nodesTooHigh.add(node);
                continue;
            }
            int parentNode = getParentAtDepth(node, midDepth);
            ArrayList<Integer> list = parentList.computeIfAbsent(parentNode, k -> new ArrayList<>()); // Is parent already in list ?
            list.add(node);
        }

        // Recurse th bottom half of the subtree (Process each parent node).
        for (int pnode : parentList.keySet()) {
            ArrayList<Integer> eachChild = parentList.get(pnode);
            computeSubtreeResult(eachChild, midDepth, bottomDepth, pnode);
            nodesTooHigh.add(pnode);
        }
        // Recurse the top half of the subtree
        computeSubtreeResult(nodesTooHigh, topDepth, midDepth, rootNode);
    }

    static final long computeFullDepthResult(ArrayList<Integer> nodeList, int nodeCount, long sumOfAllNodeValues) {
        // Compute total using the depth of each node (and not account for their least common ancestor)
        long total = 0;
        for (int k = 0; k < nodeCount; k++) {
            int node = nodeList.get(k);
            int depth = nodeDepth[node];

            // Sum each node's contribution
            total = modSumMultiply(total, depth, node, (sumOfAllNodeValues - node));
        }
        return total;
    }

    static final long combineResults(long totalEverything, long totalLCOnly) {
        long temp = 2 * totalLCOnly;
        if (temp >= MODMAX) temp %= MODMAX;
        temp = totalEverything - temp;
        if (temp < 0) temp += MODMAX;
        return temp;
    }

    static final long modSum(long accumulator, long term1) {
        accumulator += term1;
        if (accumulator >= MODMAX) accumulator %= MODMAX;
        return accumulator;
    }

    static final long modSumMultiply(long accumulator, long term1, long term2, long term3) {
        // Computes:  accumulator += term1+term2+term3;
        long temp = term1 * term2;
        if (temp >= MODMAX) temp %= MODMAX;
        temp = temp * term3;
        if (temp >= MODMAX) temp %= MODMAX;
        accumulator += temp;
        if (accumulator >= MODMAX) accumulator %= MODMAX;
        return accumulator;
    }

    static final int getParentAtDepth(int node, int targetDepth) {
        int onDepth = nodeDepth[node];
        while (onDepth > targetDepth) {
            int diff = onDepth - targetDepth;
            int diff2 = Integer.highestOneBit(diff);
            int path = Integer.numberOfTrailingZeros(diff2);
            node = nodeLinks[node][path]; // traverse towards desired parent node
            onDepth -= diff2;
        }
        return node;
    }

    static void processTree(int nodeCount) {
        int maxSize = nodeCount + 1;

        // Pre-process tree structure
        nodeDepth = new int[maxSize]; // depth of each node
        nodeLinks = new int[maxSize][18];

        ArrayDeque<Integer> queue = new ArrayDeque<>(maxSize);
        boolean[] isParent = new boolean[maxSize];

        queue.addLast(1);
        isParent[0] = true;
        isParent[1] = true;
        int[] nodePath = new int[maxSize];

        while (!queue.isEmpty()) {
            int onNode = queue.removeLast().intValue();
            int depth = nodeDepth[onNode];
            nodePath[depth] = onNode;

            int[] links = nodeLinks[onNode];
            int powerValue = 1;
            int linkNum = 0;
            while (true) {
                int index = depth - powerValue;
                if (index < 0) break;
                links[linkNum++] = nodePath[index];
                powerValue <<= 1;
            }
            for (int childNode : edges.get(onNode)) {
                if (!isParent[childNode]) {
                    isParent[childNode] = true;
                    nodeDepth[childNode] = depth + 1;
                    queue.addLast(childNode);
                }
            }
        }
    }

    static class Reader {
        final private int BUFFER_SIZE = 1 << 16;
        private DataInputStream din;
        private byte[] buffer;
        private int bufferPointer, bytesRead;

        public Reader() {
            din = new DataInputStream(System.in);
            buffer = new byte[BUFFER_SIZE];
            bufferPointer = bytesRead = 0;
        }

        public String readLine() {
            byte[] buf = new byte[64]; // line length
            int cnt = 0, c;
            while ((c = read()) != -1) {
                if (c == '\n') break;
                buf[cnt++] = (byte) c;
            }
            return new String(buf, 0, cnt);
        }

        public int readInt() {
            int ret = 0;
            byte c = read();
            while (c <= ' ') {
                c = read();
            }
            boolean neg = (c == '-');
            if (neg)
                c = read();
            do {
                ret = ret * 10 + c - '0';
            } while ((c = read()) >= '0' && c < '9');
            if (neg)
                return -ret;
            return ret;
        }

        private byte read() {
            if (bufferPointer == bytesRead)
                fillBuffer();
            return buffer[bufferPointer++];
        }

        private void fillBuffer() {
            try {
                bytesRead = din.read(buffer, bufferPointer = 0, BUFFER_SIZE);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (bytesRead == -1) {
                buffer[0] = -1;
            }
        }
    }


}