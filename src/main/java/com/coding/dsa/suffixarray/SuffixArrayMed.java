package com.coding.dsa.suffixarray;

import java.util.Arrays;

public class SuffixArrayMed extends SuffixArray {
    static class SuffixRankTuple implements Comparable<SuffixRankTuple> {
        int firstHalf, secondHalf, originalIndex;

        @Override
        public int compareTo(SuffixRankTuple other) {
            int cmp = Integer.compare(firstHalf, other.firstHalf);
            if (cmp == 0)
                return Integer.compare(secondHalf, other.secondHalf);
            return cmp;
        }

        @Override
        public String toString() {
            return originalIndex + "->(" + firstHalf + "," + secondHalf + ")";
        }
    }

    public SuffixArrayMed(String text) {
        super(toIntArray(text));
    }

    public SuffixArrayMed(int[] text) {
        super(text);
    }

    @Override
    protected void construct() {
        sa = new int[N];
        int[][] suffixRanks = new int[2][N];
        SuffixRankTuple[] ranks = new SuffixRankTuple[N];
        for (int i = 0; i < N; i++) {
            suffixRanks[0][i] = T[i];
            ranks[i] = new SuffixRankTuple();
        }
        for (int pos = 1; pos < N; pos *= 2) {
            for (int i = 0; i < N; i++) {
                SuffixRankTuple suffixRank = ranks[i];
                suffixRank.firstHalf = suffixRanks[0][i];
                suffixRank.secondHalf = i + pos < N ? suffixRanks[0][i + pos] : -1;
                suffixRank.originalIndex = i;
            }
            Arrays.sort(ranks);
            int newRank = 0;
            suffixRanks[1][ranks[0].originalIndex] = 0;
            for (int i = 1; i < N; i++) {
                SuffixRankTuple lastSuffixRank = ranks[i - 1];
                SuffixRankTuple currSuffixRank = ranks[i];
                if (currSuffixRank.firstHalf != lastSuffixRank.firstHalf
                        || currSuffixRank.secondHalf != lastSuffixRank.secondHalf) {
                    newRank++;
                }
                suffixRanks[1][currSuffixRank.originalIndex] = newRank;
            }
            suffixRanks[0] = suffixRanks[1];
            if (newRank == N - 1)
                break;
        }
        for (int i = 0; i < N; i++) {
            sa[i] = ranks[i].originalIndex;
            ranks[i] = null;
        }
        suffixRanks[0] = suffixRanks[1] = null;
        suffixRanks = null;
        ranks = null;
    }

    public static void main(String[] args) {
        SuffixArrayMed sa = new SuffixArrayMed("ABBABAABAA");
        int[] sa1 = sa.getSa();
        System.out.println(Arrays.toString(sa1));
    }
}
