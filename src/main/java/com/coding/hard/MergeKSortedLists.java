package com.coding.hard;


import java.util.PriorityQueue;

 class ListNode {
    int val;
    ListNode next;

    ListNode() {
    }

    ListNode(int val) {
        this.val = val;
    }

    ListNode(int val, ListNode next) {
        this.val = val;
        this.next = next;
    }
}

class Solution2 {

    public static ListNode mergeTow(ListNode a, ListNode b) {
        ListNode current1 = a;
        ListNode current2 = b;
        ListNode head = null;
        ListNode tail = head;
        while (current1 != null || current2 != null) {
            if (current1 == null) {
                if (head == null) {
                    head = current2;
                } else {
                    tail.next = current2;
                }
                break;
            } else if (current2 == null) {
                if (head == null) {
                    head = current1;
                } else {
                    tail.next = current1;
                }
                break;
            } else if (current1.val < current2.val) {
                ListNode newNode = new ListNode(current1.val);
                if (head == null) {
                    head = newNode;
                    tail = head;
                } else {
                    tail.next = newNode;
                    tail = newNode;
                }
                current1 = current1.next;
            } else {
                ListNode newNode = new ListNode(current2.val);
                if (head == null) {
                    head = newNode;
                    tail = head;
                } else {
                    tail.next = newNode;
                    tail = newNode;
                }
                current2 = current2.next;
            }
        }
        return head;
    }

    public static ListNode mergeKLists(ListNode[] lists) {
        if (lists.length == 0) return null;
        if (lists.length == 1) return lists[0];
        ListNode res=null;
        for(ListNode node:lists){
            res=mergeTow(res,node);
        }
        return res;
    }

    public static void main(String[] args) {
        ListNode node3 = new ListNode(3);
        ListNode node2 = new ListNode(2, node3);
        ListNode node1 = new ListNode(1, node2);

        ListNode node7 = new ListNode(7);
        ListNode node6 = new ListNode(6, node7);
        ListNode node5 = new ListNode(5, node6);
        ListNode node4 = new ListNode(4, node5);

        ListNode res = mergeTow(node1, node4);
        ListNode[]listNodes={node1,node4};
        ListNode v=mergeKLists(listNodes);
        System.out.println(v);
        System.out.println(res);

    }
}

public class MergeKSortedLists {
    public ListNode mergeKLists(ListNode[] lists) {
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        for (ListNode node : lists) {
            for (ListNode current = node; current != null; current = current.next) {
                pq.add(current.val);
            }
        }
        ListNode head = null;
        ListNode tail = head;
        while (!pq.isEmpty()) {
            ListNode newNode = new ListNode(pq.poll());
            if (head == null) {
                head = newNode;
                tail = head;
            } else {
                tail.next = newNode;
                tail = newNode;
            }
        }
        return head;
    }

    public static void main(String[] args) {

    }
}
