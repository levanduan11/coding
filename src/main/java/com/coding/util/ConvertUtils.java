package com.coding.util;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public final class ConvertUtils {
    private ConvertUtils() {
    }

    public static List<List<String>> stringTo2dArray(String s) {
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
    public static Object stackParseNestedArray(String input) {
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
}
