package com.coding.hard;

import java.util.*;

public class WordLadderII {
    public List<List<String>> findLadders(String beginWord, String endWord, List<String> wordList) {
        List<List<String>> res = new ArrayList<>();
        Set<String> dict = new HashSet<>(wordList);
        if (!dict.contains(endWord))
            return res;
        Queue<String> queue = new LinkedList<>();
        queue.offer(beginWord);
        dict.remove(beginWord);
        Map<String, List<String>> graph = new LinkedHashMap<>();
        boolean isOver = false;
        while (!queue.isEmpty()) {
            int size = queue.size();
            Set<String> nextLevelSet = new HashSet<>();
            while (size-- > 0) {
                String cur = queue.poll();
                List<String> nexts = getNeighbors(cur, dict);
                for (String next : nexts) {
                    if (next.equals(endWord))
                        isOver = true;
                    if (nextLevelSet.add(next)) {
                        queue.offer(next);
                        graph.put(next, new ArrayList<>());
                    }
                    graph.get(next).add(cur);
                }
            }
            dict.removeAll(nextLevelSet);
            if (isOver) {
                List<String> path = new LinkedList<>();
                path.add(endWord);
                dfs(res, path, endWord, beginWord, graph);
                return res;
            }
        }
        return res;
    }

    private void dfs(List<List<String>> res, List<String> path, String cur, String beginWord, Map<String, List<String>> graph) {
        if (cur.equals(beginWord)) {
            res.add(new ArrayList<>(path));
            return;
        }
        for (String next : graph.get(cur)) {
            path.add(0, next);
            dfs(res, path, next, beginWord, graph);
            path.remove(0);
        }
    }

    private List<String> getNeighbors(String cur, Set<String> dict) {
        List<String> nexts = new ArrayList<>();
        char[] chars = cur.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char temp = chars[i];
            for (char c = 'a'; c <= 'z'; c++) {
                if (chars[i] == c)
                    continue;
                chars[i] = c;
                String newtStr = new String(chars);
                if (dict.contains(newtStr))
                    nexts.add(newtStr);
            }
            chars[i] = temp;
        }
        return nexts;
    }

    List<List<String>> ans = new ArrayList<>();
    Map<String, Integer> wordToId = new LinkedHashMap<>();
    Map<Integer, String> idToWord = new LinkedHashMap<>();
    Map<Integer, List<Integer>> path = new LinkedHashMap<>();
    Deque<String> list = new LinkedList<>();
    int[] ne, e, h;
    boolean[] vis;
    int len, idx, start, end;

    void add(int u, int v) {
        e[++len] = v;
        ne[len] = h[u];
        h[u] = len;
    }

    void dfs(int u, int level) {
        if (u == start) {
            ans.add(new ArrayList<>(list));
            return;
        }
        List<Integer> p = path.get(u);
        if (p == null) return;
        for (int v : p) {
            if (level % 2 == 1) list.addFirst(idToWord.get(v));
            dfs(v, level + 1);
            if (level % 2 == 1) list.pollFirst();
        }
    }

    void addEdge(String word) {
        int u = idx;
        char[] arr = word.toCharArray();
        wordToId.put(word, idx);
        idToWord.put(idx++, word);
        for (int i = 0; i < arr.length; i++) {
            char t = arr[i];
            arr[i] = '*';
            String vstr = new String(arr);
            if (!wordToId.containsKey(vstr)) {
                wordToId.put(vstr, idx);
                idToWord.put(idx++, vstr);
            }
            int v = wordToId.get(vstr);
            add(u, v);
            add(v, u);
            arr[i] = t;
        }
    }

    public List<List<String>> findLadders2(String beginWord, String endWord, List<String> wordList) {
        int n = wordList.size();
        ne = new int[20 * n];
        e = new int[20 * n];
        h = new int[20 * n];
        vis = new boolean[10 * n];
        if (!wordList.contains(beginWord)) wordList.add(beginWord);
        if (!wordList.contains(endWord)) return ans;
        for (String s : wordList) {
            addEdge(s);
        }
        Queue<Integer> q = new LinkedList<>();
        start = wordToId.get(beginWord);
        end = wordToId.get(endWord);
        q.add(start);
        while (!q.isEmpty()) {
            int u = q.poll();
            if (u == end) break;
            if (vis[u]) continue;
            vis[u] = true;
            for (int j = h[u]; j != 0; j = ne[j]) {
                int v = e[j];
                if (vis[v]) continue;
                if (!path.containsKey(v)) path.put(v, new ArrayList<>());
                path.get(v).add(u);
                q.add(v);
            }
        }
        list.add(endWord);
        dfs(end, 0);
        return ans;
    }

    public static void main(String[] args) {
        String beginWord = "hit";
        String endWord = "cog";
        List<String> wordList = new ArrayList<>(List.of("hot", "dot", "dog", "lot", "log", "cog"));
        var o = new WordLadderII();
        var res = o.findLadders(beginWord, endWord, wordList);
        var res2 = o.findLadders2(beginWord, endWord, wordList);
        System.out.println(res);
        System.out.println(res2);
    }
}
