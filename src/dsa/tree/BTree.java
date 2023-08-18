package dsa.tree;

public class BTree<Key extends Comparable<Key>, Value> {
    // max children per B-tree node = M-1
    // (must be even and greater than 2)
    private static final int M = 4;
    private Node root;
    private int height;
    private int n;

    private static final class Node {
        private int m; // number of children
        private Entry[] children = new Entry[M]; // the array of children

        public Node(int k) {
            m = k;
        }
    }

    // internal nodes: only use key and next
    // external nodes: only use key and value
    private static class Entry {
        private Comparable key;
        private Object val;
        private Node next;

        public Entry(Comparable key, Object val, Node next) {
            this.key = key;
            this.val = val;
            this.next = next;
        }
    }

    public BTree() {
        root = new Node(0);
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    private int size() {
        return n;
    }

    public int height() {
        return height;
    }

    public Value get(Key key) {
        if (key == null) throw new IllegalArgumentException();
        return search(root, key, height);
    }

    @SuppressWarnings("unchecked")
    private Value search(Node x, Key key, int height) {
        Entry[] children = x.children;

        // external node
        if (height == 0) {
            for (int i = 0; i < x.m; i++) {
                if (eq(key, children[i].key)) {
                    return (Value) children[i].val;
                }
            }
        }
        // internal node
        else {
            for (int i = 0; i < x.m; i++) {
                if (i + 1 == x.m || less(key, children[i + 1].key)) {
                    return search(children[i].next, key, height - 1);
                }
            }
        }
        return null;
    }

    public void put(Key key, Value val) {
        if (key == null) throw new IllegalArgumentException();
        Node u = insert(root, key, val, height);
        n++;
        if (u == null) return;

        // need to split root
        Node t = new Node(2);
        t.children[0] = new Entry(root.children[0].key, null, root);
        t.children[1] = new Entry(u.children[0].key, null, u);
        root = t;
        height++;
    }

    private Node insert(Node h, Key key, Value val, int height) {
        int i;
        Entry t = new Entry(key, val, null);

        // external node
        if (height == 0) {
            for (i = 0; i < h.m; i++) {
                if (less(key, h.children[i].key)) break;
            }
        }

        // internal node
        else {
            for (i = 0; i < h.m; i++) {
                if ((i + 1 == h.m) || less(key, h.children[i + 1].key)) {
                    Node u = insert(h.children[i++].next, key, val, height - 1);
                    if (u == null) return null;
                    t.key = u.children[0].key;
                    t.val = null;
                    t.next = u;
                    break;
                }
            }
        }

        for (int j = h.m; j > i; j--) {
            h.children[j] = h.children[j - 1];
        }
        h.children[i] = t;
        h.m++;
        if (h.m < M) return null;
        else return split(h);
    }

    private Node split(Node h) {
        Node t = new Node(M / 2);
        h.m = M / 2;
        for (int i = 0; i < M / 2; i++) {
            t.children[i] = h.children[M / 2 + i];
        }
        return t;
    }

    private boolean less(Comparable k1, Comparable k2) {
        return k1.compareTo(k2) < 0;
    }

    private boolean eq(Comparable k1, Comparable k2) {
        return k1.compareTo(k2) == 0;
    }

    public static void main(String[] args) {
        var bTree=new BTree<Integer,Integer>();
        bTree.put(10,10);
        bTree.put(20,20);
        bTree.put(30,30);
        bTree.put(40,40);
        bTree.put(50,50);
        System.out.println(bTree);
    }

}
