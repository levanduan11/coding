package com.coding.algorithm.graph;

import java.util.HashSet;
import java.util.Set;

public class HITS {
    static class Page {
        double auth;
        double hub;
        Set<Page> incomingNeighbors;
        Set<Page> outgoingNeighbors;

        public Page() {
            this.auth = 1.0;
            this.hub = 1.0;
            this.incomingNeighbors = new HashSet<>();
            this.outgoingNeighbors = new HashSet<>();
        }
    }

    private static Set<Page> createPages() {
        var page1 = new Page();
        var page2 = new Page();
        var page3 = new Page();
        var page4 = new Page();

        Set<Page> pages = new HashSet<>() {{
            add(page1);
            add(page2);
            add(page3);
            add(page4);
        }};


        page2.incomingNeighbors.add(page1);
        page3.incomingNeighbors.add(page1);
        page4.incomingNeighbors.add(page2);
        page4.incomingNeighbors.add(page3);
        page1.outgoingNeighbors.add(page2);
        page1.outgoingNeighbors.add(page3);
        page2.outgoingNeighbors.add(page4);
        page3.outgoingNeighbors.add(page4);

        return pages;
    }

    private static void updateAuthScores(Set<Page> pages) {
        double norm = 0.0;
        for (Page page : pages) {
            page.auth = 0.0;
            for (Page q : page.incomingNeighbors) {
                page.auth += q.hub;
            }
            norm += Math.pow(page.auth, 2);
        }
        norm = Math.sqrt(norm);
        for (Page page : pages) {
            page.auth /= norm;
        }
    }

    private static void updateHubScores(Set<Page> pages) {
        double norm = 0.0;
        for (Page page : pages) {
            page.hub = 0.0;
            for (Page r : page.outgoingNeighbors) {
                page.hub += r.auth;
            }
            norm += Math.pow(page.hub, 2);
        }
        norm = Math.sqrt(norm);
        for (Page page : pages) {
            page.hub /= norm;
        }
    }

    private static void displayScores(Set<Page> pages) {
        for (Page page : pages) {
            System.out.println("Page: Auth=" + page.auth + ", Hub=" + page.hub);
        }
    }

    public static void main(String[] args) {
        Set<Page> pages = createPages();
        int k = 10;
        for (int step = 1; step <= k; step++) {
            updateAuthScores(pages);
            updateHubScores(pages);
        }
        displayScores(pages);
    }
}
