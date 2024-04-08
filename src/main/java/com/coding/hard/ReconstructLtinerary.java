package com.coding.hard;

import com.coding.InputReader;
import com.coding.OutputWriter;

import java.util.*;
import java.util.stream.Collectors;

public class ReconstructLtinerary {
    public static List<String> findItinerary2(List<List<String>> tickets) {
        return new AbstractList<>() {
            private LinkedList<String> path;

            {
                init();
            }

            private void init() {
                if (null != path && !path.isEmpty()) {
                    return;
                }
                Map<String, PriorityQueue<String>> graph = new HashMap<>();
                path = new LinkedList<>();
                for (List<String> ticket : tickets) {
                    final String source = ticket.getFirst();
                    final String destination = ticket.getLast();
                    graph.computeIfAbsent(source, k -> new PriorityQueue<>()).add(destination);
                }
                dfs2("JFK", graph, path);
            }

            private void dfs(String departure, Map<String, PriorityQueue<String>> graph, LinkedList<String> path) {
                PriorityQueue<String> current = graph.get(departure);
                while (null != current && !current.isEmpty()) {
                    dfs(current.poll(), graph, path);
                }
                path.addFirst(departure);
            }

            private void dfs2(String departure, Map<String, PriorityQueue<String>> graph, LinkedList<String> path) {
                Deque<String> stack = new ArrayDeque<>();
                stack.push(departure);
                Map<String, Iterator<String>> iteratorMap = new HashMap<>();
                for (Map.Entry<String, PriorityQueue<String>> entry : graph.entrySet()) {
                    iteratorMap.put(entry.getKey(), entry.getValue().iterator());
                }
                while (!stack.isEmpty()) {
                    String current = stack.peek();
                    Iterator<String> it = iteratorMap.get(current);
                    if (it != null && it.hasNext())
                        stack.push(it.next());
                    else {
                        stack.pop();
                        path.addFirst(current);
                    }
                }
            }

            @Override
            public String get(int index) {
                init();
                return path.get(index);
            }

            @Override
            public int size() {
                return path.size();
            }
        };
    }

    public static List<String> findItinerary(List<List<String>> tickets) {
        LinkedList<String> result = new LinkedList<>();
        if (tickets == null || tickets.isEmpty()) {
            return result;
        }
        Map<String, PriorityQueue<String>> graph = new HashMap<>();
        for (List<String> ticket : tickets) {
            String from = ticket.getFirst();
            String to = ticket.getLast();
            PriorityQueue<String> list = graph.getOrDefault(from, new PriorityQueue<>());
            list.add(to);
            graph.put(from, list);
        }
        dfs(graph, result, "JFK");
        return result;
    }

    private static void dfs(Map<String, PriorityQueue<String>> graph, LinkedList<String> ans, String curr) {
        PriorityQueue<String> neighbors = graph.get(curr);
        if (neighbors == null) {
            ans.addFirst(curr);
            return;
        }
        while (!neighbors.isEmpty()) {
            String neighbor = neighbors.poll();
            dfs(graph, ans, neighbor);
        }
        ans.addFirst(curr);
    }

    private static List<List<String>> stringToArray(String s) {
        List<List<String>> list = new ArrayList<>();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '[') {
                List<String> innerList = new ArrayList<>();
                StringBuilder sb = new StringBuilder();
                i++;
                while (i < s.length() && s.charAt(i) != ']') {
                    char ch = s.charAt(i);
                    if (ch == '"') {
                        i++;
                        continue;
                    }
                    if (s.charAt(i) == ',') {
                        innerList.add(sb.toString());
                        sb = new StringBuilder();
                    } else {
                        sb.append(s.charAt(i));
                    }
                    i++;
                }
                if (!sb.isEmpty()) {
                    innerList.add(sb.toString());
                }
                list.add(innerList);
            }
        }
        return list;
    }

    public static Object recursiveParseNestedArray(String input) {
        input = input.trim();
        if (input.startsWith("[") && input.endsWith("]")) {
            ArrayList<Object> list = new ArrayList<>();
            String innerContent = input.substring(1, input.length() - 1).trim();
            int startIndex = 0;
            int depth = 0;
            for (int i = 0; i < innerContent.length(); i++) {
                char c = innerContent.charAt(i);
                if (c == '[')
                    depth++;
                else if (c == ']')
                    depth--;
                else if (c == ',' && depth == 0) {
                    String part = innerContent.substring(startIndex, i).trim();
                    if (!part.isEmpty()) {
                        Object obj = recursiveParseNestedArray(part);
                        list.add(obj);
                    }
                    startIndex = i + 1;
                }
            }
            if (startIndex < innerContent.length()) {
                Object o = recursiveParseNestedArray(innerContent.substring(startIndex).trim());
                list.add(o);
            }
            return list;
        } else {
            return input.replace("\"", "");
        }
    }

    @SuppressWarnings("unchecked")
    private static Object stackParseNestedArray(String input) {
        Deque<Object> stack = new ArrayDeque<>();
        StringBuilder sb = new StringBuilder();
        boolean inString = false;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c == '\"') {
                inString = !inString;
                continue;
            }
            if (inString) {
                sb.append(c);
            } else {
                switch (c) {
                    case '[' -> stack.push(new ArrayList<>());
                    case ']' -> {
                        if (!sb.isEmpty()) {
                            assert stack.peek() != null;
                            ((ArrayList<Object>) stack.peek()).add(sb.toString());
                            sb = new StringBuilder();
                        }
                        if (stack.size() > 1) {
                            Object completed = stack.pop();
                            assert stack.peek() != null;
                            ((ArrayList<Object>) stack.peek()).add(completed);
                        }
                    }
                    case ',' -> {
                        if (!sb.isEmpty()) {
                            assert stack.peek() != null;
                            ((ArrayList<Object>) stack.peek()).add(sb.toString());
                            sb = new StringBuilder();
                        }
                    }
                    default -> sb.append(c);
                }
            }
        }
        return stack.pop();
    }

    public static void main(String[] args) {
        String out = InputReader.inputAsStrings("332.txt")
                .map(ReconstructLtinerary::stringToArray)
                .map(ReconstructLtinerary::findItinerary2)
                .map(Object::toString)
                .collect(Collectors.joining(System.lineSeparator()));
        OutputWriter.write(out, "332.txt");
    }
}
