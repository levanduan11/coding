package dsa.tree;

import java.util.HashMap;
import java.util.Map;

public class Trie {
    private static class Node {
        char ch;
        int count = 0;
        boolean isWordEnding = false;
        Map<Character, Node> children = new HashMap<>();

        public Node(char ch) {
            this.ch = ch;
        }

        public void addChild(Node node, char c) {
            children.put(c, node);
        }
    }

    private final char rootCharacter = '\0';
    private Node root = new Node(rootCharacter);

    public boolean insert(String key, int numInserts) {
        if (key == null) throw new IllegalArgumentException();
        if (numInserts < 0) throw new IllegalArgumentException();
        Node node = root;
        boolean create_new_node = false;
        boolean is_prefix = false;

        for (int i = 0; i < key.length(); i++) {
            char ch = key.charAt(i);
            Node nextNode = node.children.get(ch);
            if (nextNode == null) {
                nextNode = new Node(ch);
                node.addChild(nextNode, ch);
                create_new_node = true;
            } else {
                if (nextNode.isWordEnding) is_prefix = true;
            }
            node = nextNode;
            node.count += numInserts;
        }
        if (node != root) node.isWordEnding = true;
        return is_prefix || !create_new_node;
    }

    public boolean insert(String key) {
        return insert(key, 1);
    }

    public boolean delete(String key, int numDeletions) {
        if (!contains(key)) return false;
        if (numDeletions <= 0) throw new IllegalArgumentException();
        Node node = root;
        for (int i = 0; i < key.length(); i++) {
            char ch = key.charAt(i);
            Node curNode = node.children.get(ch);
            curNode.count -= numDeletions;
            if (curNode.count <= 0) {
                node.children.remove(ch);
                curNode.children = null;
                curNode = null;
                return true;
            }
            node = curNode;
        }
        return true;
    }

    public boolean delete(String key) {
        return delete(key, 1);
    }

    public boolean contains(String key) {
        return count(key) != 0;
    }

    public int count(String key) {
        if (key == null) throw new IllegalArgumentException();
        Node node = root;
        for (int i = 0; i < key.length(); i++) {
            char ch = key.charAt(i);
            if (node == null) return 0;
            node = node.children.get(ch);
        }
        return node != null ? node.count : 0;
    }

    private void clear(Node node) {
        if (node == null) return;
        for (Character ch : node.children.keySet()) {
            Node nextNode = node.children.get(ch);
            clear(nextNode);
            nextNode = null;
        }
    }

    private void clear() {
        root.children = null;
        root = new Node(rootCharacter);
    }

    public static void main(String[] args) {
        String[] keys = {"the", "a", "there", "answer", "any",
                "by", "bye", "their"};
        var trie = new Trie();
        for (String s : keys){
            trie.insert(s);
        }
        System.out.println(trie);
    }
}
