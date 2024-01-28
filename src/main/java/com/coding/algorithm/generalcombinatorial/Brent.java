package com.coding.algorithm.generalcombinatorial;

import java.util.function.Function;

public class Brent {
    public static double findRoot(
            Function<Double, Double> f, double a, double b, double tolerance, int maxIterations) {
        double fa = f.apply(a);
        double fb = f.apply(b);
        if (fa * fb > 0)
            throw new IllegalArgumentException();
        double c = a;
        double fc = fa;
        double m = a;
        double d = a;
        double s = 0;
        double fs;
        for (int i = 0; i < maxIterations; i++) {
            if (fa != fc && fb != fc) {
                s = a * fb * fc / ((fa - fb) * (fa - fc)) +
                        b * fa * fc / ((fb - fa) * (fb - fc)) +
                        c * fa * fb / ((fc - fa) * (fc - fb));
            } else {
                s = b - fb * (b - a) / (fb - fa);
            }
            double condition1 = (3 * a + b) / 4.0;
            boolean condition2 = (m - s) * (b - s) > 0;
            boolean condition3 = Math.abs(s - b) >= Math.abs(b - c) / 2.0;
            if (!(condition1 < s && s < b) || (condition2 && condition3))
                s = (a + b) / 2;
            fs = f.apply(s);
            d = m;
            m = b;
            if (fa * fs < 0) {
                b = s;
                fb = fs;
            } else {
                a = s;
                fa = fs;
            }
            if (Math.abs(fa) < Math.abs(fb)) {
                double tempA = a;
                a = b;
                b = c;
                c = tempA;
                double tempFa = fa;
                fa = fb;
                fb = fc;
                fc = tempFa;
            }
            if (Math.abs(b - a) < tolerance || fs == 0)
                break;
        }
        return s;
    }

    public static double fn(double x) {
        return Math.pow(x, 3) - x - 2;
    }

    public static void main(String[] args) {
        Node head = newNode(50);
        head.next = newNode(20);
        head.next.next = newNode(15);
        head.next.next.next = newNode(4);
        head.next.next.next.next = newNode(10);

        head.next.next.next.next.next = head.next.next;
        Node res = detectCycle(head);
        if (res == null)
            System.out.println("No loop");
        else
            System.out.println("Loop is present at " + res.data);
    }

    static Node detectCycle(Node head) {
        if (head == null)
            return null;
        Node first_pointer = head;
        Node second_pointer = head.next;
        int power = 1;
        int lenght = 1;
        while (second_pointer != null && second_pointer != first_pointer) {
            if (lenght == power) {
                power *= 2;
                lenght = 0;
                first_pointer = second_pointer;
            }
            second_pointer = second_pointer.next;
            ++lenght;
        }
        if (second_pointer == null)
            return null;
        first_pointer = second_pointer = head;
        while (lenght > 0) {
            second_pointer = second_pointer.next;
            --lenght;
        }
        while (second_pointer != first_pointer) {
            second_pointer = second_pointer.next;
            first_pointer = first_pointer.next;
        }
        return first_pointer;
    }

    static Node newNode(int key) {
        Node temp = new Node();
        temp.data = key;
        temp.next = null;
        return temp;
    }

    static class Node {
        int data;
        Node next;
    }
}
