package hard;

import java.util.*;

public class WordLadder {
    public int ladderLength(String beginWord, String endWord, List<String> wordList) {
        int ans = 0;
        List<List<String>> res = new ArrayList<>();
        Set<String> dict = new HashSet<>(wordList);
        if (!dict.contains(endWord))
            return ans;
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
                return res.stream()
                        .mapToInt(List::size)
                        .reduce(Integer::min)
                        .orElse(ans);
            }
        }
        return ans;
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
                String newStr = new String(chars);
                if (dict.contains(newStr))
                    nexts.add(newStr);
            }
            chars[i] = temp;
        }
        return nexts;
    }

    int biDFS(Set<String> begin, Set<String> end, Set<String> word, int res) {
        if (begin.isEmpty())
            return 0;
        if (begin.size() > end.size())
            return biDFS(end, begin, word, res);
        for (String w : begin) {
            word.remove(w);
        }
        Set<String> next = new HashSet<>();
        for (String curr : begin) {
            char[] ch = curr.toCharArray();
            for (int i = 0; i < ch.length; i++) {
                char c = ch[i];
                for (char j = 'a'; j <= 'z'; j++) {
                    ch[i] = j;
                    String neighbor = new String(ch);
                    if (word.contains(neighbor)) {
                        if (end.contains(neighbor))
                            return res + 1;
                        next.add(neighbor);
                    }
                    ch[i] = c;
                }
            }
        }
        return biDFS(next, end, word, res + 1);
    }

    public int ladderLen(String beginWord, String endWord, List<String> wordList) {
        Set<String> begin = new HashSet<>();
        Set<String> end = new HashSet<>();
        Set<String> word = new HashSet<>(wordList);
        if (!word.contains(endWord))
            return 0;
        begin.add(beginWord);
        end.add(endWord);
        return biDFS(begin, end, word, 1);
    }

    public static void main(String[] args) {
        var o = new WordLadder();
        String b = "hit";
        String e = "cog";
        var wl = List.of("hot", "dot", "dog", "lot", "log", "cog");
        var res = o.ladderLength(b, e, wl);
        var res2 = o.ladderLen(b, e, wl);
        System.out.println(res);
        System.out.println(res2);
    }
}
