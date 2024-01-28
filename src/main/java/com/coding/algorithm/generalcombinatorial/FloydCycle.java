package com.coding.algorithm.generalcombinatorial;

public class FloydCycle {
    public static void main(String[] args) {
        insert(10);
        insert(20);
        insert(30);
        insert(40);
        insert(50);

        Node temp = head;
        while (temp.next != null)
            temp = temp.next;
        temp.next = head;
        if (detectLoop())
            System.out.println("true");
        else
            System.out.println("false");
    }

    static class Node {
        int data;
        Node next;

        Node(int data) {
            this.data = data;
            next = null;
        }
    }

    static Node head = null;

    static void insert(int value) {
        Node newNode = new Node(value);
        if (head == null)
            head = newNode;
        else {
            newNode.next = head;
            head = newNode;
        }
    }

    static boolean detectLoop() {
        Node slowPointer = head, fastPointer = head;
        while (slowPointer != null
                && fastPointer != null
                && fastPointer.next != null) {
            slowPointer = slowPointer.next;
            fastPointer = fastPointer.next.next;
            if (slowPointer == fastPointer)
                return true;
        }
        return true;
    }
}
