package com.coding.hard;

import java.util.*;

public class WordSearchII {
    static class TrieNode {
        public TrieNode[] children;
        public String word;

        public TrieNode() {
            children = new TrieNode[26];
            word = null;
            for (int i = 0; i < 26; i++) {
                children[i] = null;
            }
        }
    }

    static class Trie {
        TrieNode root;

        public Trie() {
            root = new TrieNode();
        }

        public void insert(String word) {
            char[] array = word.toCharArray();
            TrieNode curr = root;
            for (char c : array) {
                if (curr.children[c - 'a'] == null)
                    curr.children[c - 'a'] = new TrieNode();
                curr = curr.children[c - 'a'];
            }
            curr.word = word;
        }
    }

    public List<String> findWords(char[][] board, String[] words) {
        Trie trie = new Trie();
        List<String> res = new ArrayList<>();
        for (String word : words) {
            trie.insert(word);
        }
        int rows = board.length;
        if (rows == 0)
            return res;
        int cols = board[0].length;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                dfs(board, i, j, trie.root, res);
            }
        }
        return res;
    }

    void dfs(char[][] grid, int r, int c, TrieNode node, List<String> res) {
        if (r < 0 || r >= grid.length || c < 0 || c >= grid[0].length)
            return;
        char cur = grid[r][c];
        if (cur == '$' || node.children[cur - 'a'] == null)
            return;
        node = node.children[cur - 'a'];
        if (node.word != null) {
            res.add(node.word);
            node.word = null;
        }
        char tmp = grid[r][c];
        grid[r][c] = '$';
        dfs(grid, r - 1, c, node, res);
        dfs(grid, r + 1, c, node, res);
        dfs(grid, r, c - 1, node, res);
        dfs(grid, r, c + 1, node, res);
        grid[r][c] = tmp;
    }

    public static void main(String[] args) {
        var o = new WordSearchII();
        char[][] board = {{'o', 'a', 'a', 'n'}, {'e', 't', 'a', 'e'}, {'i', 'h', 'k', 'r'}, {'i', 'f', 'l', 'v'}};
        String[] words = {"oath", "pea", "eat", "rain"};
        var res = o.findWords(board, words);
        var so = new SolutionWordSearchII();
        var res2 = so.findWords(board, words);
        System.out.println(res);
        System.out.println(res2);
    }
}

class SolutionWordSearchII {
    static int[][] directs = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

    public List<String> findWords(char[][] board, String[] words) {
        Three three = new Three(board);
        Trie trie = new Trie();
        for (String word : words) {
            if (three.check(word))
                trie.insert(word);
        }
        List<String> ans = new ArrayList<>();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                dfs(board, trie, i, j, ans);
            }
        }
        return ans;
    }

    public void dfs(char[][] board, Trie now, int i, int j, List<String> ans) {
        if (i < 0 || j < 0 || i >= board.length || j >= board[0].length)
            return;
        if (!now.children.containsKey(board[i][j]))
            return;
        char ch = board[i][j];
        Trie nxt = now.children.get(ch);
        if (!nxt.word.isEmpty()) {
            ans.add(nxt.word);
            nxt.word = "";
        }
        if (!nxt.children.isEmpty()) {
            board[i][j] = '#';
            for (int[] direct : directs) {
                dfs(board, nxt, i + direct[0], j + direct[1], ans);
            }
            board[i][j] = ch;
        }
        if (nxt.children.isEmpty())
            now.children.remove(ch);
    }

    static class Trie {
        String word;
        Map<Character, Trie> children;

        public Trie() {
            word = "";
            this.children = new HashMap<>();
        }

        public void insert(String word) {
            Trie cur = this;
            for (char c : word.toCharArray()) {
                cur.children.putIfAbsent(c, new Trie());
                cur = cur.children.get(c);
            }
            cur.word = word;
        }
    }

    static class Three {
        static int[][] directs = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        Set<String> dict;

        public Three(char[][] board) {
            dict = new HashSet<>();
            char[] choose = new char[3];
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[0].length; j++) {
                    dfs(board, i, j, 0, choose);
                }
            }
        }

        private void dfs(char[][] board, int i, int j, int now, char[] choose) {
            if (i < 0 || j < 0 || i >= board.length || j >= board[0].length)
                return;
            if (board[i][j] == '#')
                return;
            char ch = board[i][j];
            choose[now] = ch;
            if (now == choose.length - 1)
                dict.add(String.valueOf(choose));
            else {
                board[i][j] = '#';
                for (int[] direct : directs) {
                    dfs(board, i + direct[0], j + direct[1], now + 1, choose);
                }
                board[i][j] = ch;
            }
        }

        static int[] checkPoint = {10, 3, 8, 5};

        public boolean check(String word) {
            int n = word.length();
            for (int p : checkPoint) {
                if (n >= p && !dict.contains(word.substring(p - 3, p)))
                    return false;
            }
            return true;
        }
    }
}