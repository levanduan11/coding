package com.coding.hackkerank;

import java.util.*;

class MatrixPower {
    private final long[][] matrix;
    private final Map<Integer, long[][]> powers;

    MatrixPower(long[][] matrix) {
        this.matrix = matrix;
        this.powers = new HashMap<>();
    }

    long[][] power(int p) {
        if (p == 1) return matrix;
        long[][] result = powers.get(p);
        if (result != null)
            return result;
        result = power(p / 2);
        powers.put(p / 2 * 2, result = multiply(result, result));
        if (p % 2 > 0)
            powers.put(p, result = multiply(result, power(p % 2)));
        return result;
    }

    private long[][] multiply(long[][] a, long[][] b) {
        long[][] m = new long[a.length][a.length];
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a.length; j++) {
                long sum = 0;
                for (int k = 0; k < a.length; k++) {
                    sum = (sum + a[i][k] * b[k][j]) % 1000000007L;
                }
                m[i][j] = sum;
            }
        }
        return m;
    }
}

class Regex {
    static char e = '\0';
    final Node root;
    final Node[] nodes;

    public Regex(String regex) {
        Parser parser = new Parser(regex);
        parser.parse();
        SubsetConstruction subet = new SubsetConstruction(parser.expr.start, parser.expr.end);
        this.nodes = subet.dfa();
        this.root = nodes[0];
    }

    static class SubsetConstruction {
        private final Node nfaEnd;
        private final Queue<State> queue;
        private final Map<Set<Node>, Node> dfaMap;
        private final Node dfaRoot;

        SubsetConstruction(Node dfaRoot, Node nfaEnd) {
            this.nfaEnd = nfaEnd;
            this.queue = new ArrayDeque<>();
            this.dfaMap = new HashMap<>();
            this.dfaRoot = dfaRoot;
        }

        Node[] dfa() {
            while (!queue.isEmpty()) {
                State state = queue.poll();
                for (char c : new char[]{'a', 'b'}) {
                    Set<Node> nfaNext = eClosure(next(state.nfa, c));
                    if (nfaNext.isEmpty())
                        continue;
                    Node dfaNext = dfaMap.get(nfaNext);
                    if (dfaNext == null)
                        dfaNext = addState(nfaNext).dfa;
                    state.dfa.edges.add(new Edge(c, dfaNext));
                }
            }
            return getNodes();
        }

        private Node[] getNodes() {
            ArrayList<Node> nodes = new ArrayList<>();
            nodes.add(dfaRoot);
            for (Node node : dfaMap.values()) {
                if (node != dfaRoot)
                    nodes.add(node);
            }
            return nodes.toArray(Node[]::new);
        }

        private State addState(Set<Node> nfa) {
            State state = new State(nfa);
            state.dfa.isFinal = nfa.contains(nfaEnd);
            dfaMap.put(state.nfa, state.dfa);
            queue.add(state);
            return state;
        }

        private Set<Node> eClosure(Node node) {
            return eClosure(Collections.singleton(node));
        }

        private static Set<Node> eClosure(Collection<Node> nodes) {
            Set<Node> closure = new HashSet<>();
            Stack<Node> stack = new Stack<>();
            stack.addAll(nodes);
            while (!stack.isEmpty()) {
                Node node = stack.pop();
                if (closure.add(node)) {
                    stack.addAll(next(node, e));
                }
            }
            return closure;
        }

        private static Collection<Node> next(Collection<Node> nodes, char c) {
            Collection<Node> list = new ArrayList<>();
            for (Node node : nodes) {
                list.addAll(next(node, c));
            }
            return list;
        }

        private static Collection<Node> next(Node node, char c) {
            Collection<Node> list = new ArrayList<>();
            for (Edge edge : node.edges) {
                if (edge.value == c)
                    list.add(edge.node);
            }
            return list;
        }

        static class State {
            final Set<Node> nfa;
            final Node dfa = new Node();

            State(Set<Node> nfa) {
                this.nfa = nfa;
            }
        }
    }

    static class Parser {
        private final String regex;
        private int pos;
        Expression expr;

        Parser(String regex) {
            this.regex = regex;
            this.pos = 0;
        }

        Node parse() {
            return (expr = sequence()).start;
        }

        private static class Expression {
            Node start = new Node();
            Node end = start;
        }

        private Expression sequence() {
            Expression expression = new Expression();
            for (; ; ) {
                char c = take();
                switch (c) {
                    case 'a':
                    case 'b':
                        literal(expression, c);
                        break;
                    case '(':
                        expr = parenthesis(expression);
                        break;
                    case '|':
                        expression = pipe(expression);
                        break;
                    case '*':
                        expression = star(expression);
                        break;
                    default:
                        putback();
                        return expression;
                }
            }
        }

        private void literal(Expression expr, char c) {
            expr.end.edges.add(new Edge(c, expr.end = new Node()));
        }

        private Expression parenthesis(Expression expr) {
            Expression nested = sequence();
            if (take() != ')')
                throw new IllegalStateException();
            if (expr.start == expr.end)
                return nested;
            expr.end.edges.add(new Edge(e, nested.start));
            expr.end = nested.end;
            return expr;
        }

        private Expression pipe(Expression first) {
            Expression second = sequence();
            Expression expr = new Expression();
            expr.start.edges.add(new Edge(e, first.start));
            expr.start.edges.add(new Edge(e, second.start));
            first.end.edges.add(new Edge(e, expr.end = new Node()));
            second.end.edges.add(new Edge(e, expr.end));
            return expr;
        }

        private Expression star(Expression inner) {
            Expression expr = new Expression();
            expr.start.edges.add(new Edge(e, inner.start));
            inner.end.edges.add(new Edge(e, inner.start));
            inner.end.edges.add(new Edge(e, expr.end = new Node()));
            expr.start.edges.add(new Edge(e, expr.end));
            return expr;
        }

        private char take() {
            return skipWs() ? regex.charAt(pos++) : e;
        }

        private void putback() {
            if (pos >= 0)
                --pos;
        }

        public int getPos() {
            return pos;
        }

        private boolean skipWs() {
            while (pos < regex.length() && Character.isWhitespace(regex.charAt(pos)))
                pos++;
            return pos < regex.length();
        }
    }

    static class Node {
        final ArrayList<Edge> edges = new ArrayList<Edge>();
        boolean isFinal;
    }

    static class Edge {
        final char value;
        final Node node;

        Edge(char value, Node node) {
            this.value = value;
            this.node = node;
        }
    }
}

class RegexCounter {
    private final long[][] adjacencyMatrix;
    private final ArrayList<Integer> finalStates;

    public RegexCounter(Regex regex) {
        Map<Regex.Node, Integer> indexes = new HashMap<>();
        this.adjacencyMatrix = new long[regex.nodes.length][regex.nodes.length];
        this.finalStates = new ArrayList<>();
        for (Regex.Node node : regex.nodes) {
            int index = getIndex(indexes, node);
            for (Regex.Edge edge : node.edges) {
                int next = getIndex(indexes, edge.node);
                adjacencyMatrix[index][next] = 1;
            }
        }
    }

    private int getIndex(Map<Regex.Node, Integer> indexes, Regex.Node node) {
        Integer index = indexes.get(node);
        if (index == null) {
            indexes.put(node, index = indexes.size());
            if (node.isFinal)
                finalStates.add(index);
        }
        return index;
    }
    long count(int len){
        long[][]m = new MatrixPower(adjacencyMatrix).power(len);
        long count = 0;
        for (Integer finalState : finalStates) {
            count = (count + m[0][finalState]) % 1000000007L;
        }
        return count;
    }
}

public class CountStrings {
    public static int countStrings(String r, int l) {
        return 0;
    }

    public static void main(String[] args) {

    }
}
