package com.coding.hard;

import java.util.*;

public class WordBreakII {
    Map<String, List<String>> map = new HashMap<>();
    private Trie trie = new Trie();
    public List<String> wordBreak(String s, List<String> wordDict) {
        if (map.containsKey(s))
            return map.get(s);
        if (s.isEmpty())
            return Arrays.asList("");
        List<String> ans = new ArrayList<>();
        for (String each : wordDict) {
            if (each.length() > s.length() || !s.substring(0, each.length()).equals(each))
                continue;
            List<String> remainderList = wordBreak(s.substring(each.length()), wordDict);
            for (String rem : remainderList) {
                ans.add(each + (rem.isEmpty() ? "" : " ") + rem);
            }
        }
        map.put(s, ans);
        return ans;
    }

    private List<String> wordBreak2(String s, List<String> wordDict) {
        Set<String> set = new HashSet<>(wordDict);
        List<String> res = new ArrayList<>();
        backtrack(s, 0, new StringBuilder(), set, res);
        return res;
    }

    private List<String> wordBreak3(String s, List<String> wordDict) {
        if (s.isEmpty())
            return new ArrayList<>();
        Set<String> dict = new HashSet<>(wordDict);

        Map<Integer, List<String>> map = new HashMap<>();
        List<String> ans = new ArrayList<>();
        List<String> start = new LinkedList<>();
        start.add("");
        map.put(0, start);
        for (int i = 1; i < s.length() + 1; i++) {
            List<String> ilist = new ArrayList<>();
            for (int j = 0; j < i; j++) {
                String sub = s.substring(j, i);
                if (dict.contains(sub)) {
                    List<String> allJs = map.get(j);
                    for (String jString : allJs) {
                        ilist.add((jString.isEmpty() ? "" : jString + " ") + sub);
                    }
                }
            }
            map.put(i, ilist);
        }
        return map.get(s.length());
    }
    private List<String>wordBreak4(String s,List<String>wordDict){
        wordDict.forEach(trie::insert);
        List<List<String>>res = dfs(s);
        return res.stream().map(l -> String.join(" ",l)).toList();
    }
    private List<List<String>>dfs(String s){
        List<List<String>>res = new ArrayList<>();
        if ("".equals(s)){
            res.add(new ArrayList<>());
            return res;
        }
        for (int i = 1; i <= s.length(); i++) {
            if (trie.search(s.substring(0,i))){
                List<List<String>> dfsList = dfs(s.substring(i));
                for (List<String> v : dfsList) {
                    v.add(0,s.substring(0,i));
                    res.add(v);
                }
            }
        }
        return res;
    }
    private void backtrack(String s, int start, StringBuilder sb, Set<String> set, List<String> res) {
        if (start == s.length()) {
            if (sb.length() > 0) {
                res.add(sb.toString());
                return;
            }
        }
        for (int i = start; i < s.length(); i++) {
            String cur = s.substring(start, i + 1);
            if (set.contains(cur)) {
                int size = sb.length();
                if (sb.length() > 0) {
                    sb.append(" ");
                }
                sb.append(cur);
                backtrack(s, i + 1, sb, set, res);
                sb.setLength(size);
            }
        }
    }

    public static void main(String[] args) {
        String s = "catsanddog";
        List<String> dict = List.of("cat", "cats", "and", "sand", "dog");
        var o = new WordBreakII();
        var res = o.wordBreak(s, dict);
        var res2 = o.wordBreak2(s, dict);
        var res3 = o.wordBreak3(s, dict);
        var res4 = o.wordBreak4(s, dict);
        System.out.println(res);
        System.out.println(res2);
        System.out.println(res3);
        System.out.println(res4);
    }
}
class Trie {
    Trie[] children = new Trie[256];
    boolean isEnd;

    void insert(String word) {
        Trie root = this;
        for (char c : word.toCharArray()) {
            if (root.children[c] == null)
                root.children[c] = new Trie();
            root = root.children[c];
        }
        root.isEnd = true;
    }

    boolean search(String key) {
        Trie root = this;
        for (char c : key.toCharArray()) {
            if (root.children[c] == null)
                return false;
            root = root.children[c];
        }
        return root.isEnd;
    }
}