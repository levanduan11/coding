package com.coding.algorithm.graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TrustRank {
    static class TrustPage{
        String title;
        double trustScore;
        List<TrustPage>incomingLinks;

        public TrustPage(String title) {
            this.title = title;
            this.trustScore = 0.0;
            this.incomingLinks = new ArrayList<>();
        }
    }

    public static void main(String[] args) {
        Set<TrustPage>pages = createPages();
        int k =10;
        for (int i = 0; i < k; i++) {
            updateTrustScores(pages);
        }
        displayTrustScores(pages);
    }
    private static Set<TrustPage>createPages(){
        final var page1 =new TrustPage("yes");
        final var page2 =new TrustPage("yes");
        final var page3 =new TrustPage("no");
        page2.incomingLinks.add(page1);
        page3.incomingLinks.add(page1);
        page1.incomingLinks.add(page2);
        return new HashSet<>(List.of(page1, page2, page3));
    }
    private static void updateTrustScores(Set<TrustPage>pages){
        double decayFactor = 0.15;
        Set<TrustPage>trustSeedPages = new HashSet<>();
        for (TrustPage page : pages) {
            if (isTrustedSeed(page)){
                trustSeedPages.addAll(pages);
                page.trustScore = 1.0;
            }
        }
        for (TrustPage page : pages) {
            if (!trustSeedPages.contains(page)){
                double incomingTrust = 0.0;
                for (TrustPage incomingLink : page.incomingLinks) {
                    incomingTrust += incomingLink.trustScore / incomingLink.incomingLinks.size();
                }
                page.trustScore = decayFactor + (1.0 - decayFactor) * incomingTrust;
            }
        }
    }
    private static boolean isTrustedSeed(TrustPage page){
        return "yes".equals(page.title);
    }
    private static void displayTrustScores(Set<TrustPage>pages){
        for (TrustPage page : pages) {
            System.out.println("Trust score for page: "+page.trustScore);
        }
    }
}
