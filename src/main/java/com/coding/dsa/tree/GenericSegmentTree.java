package com.coding.dsa.tree;

import java.util.Objects;
import java.util.function.BinaryOperator;

public class GenericSegmentTree {
    public enum SegmentCombinationFn {
        SUM,
        MIN,
        MAX,
        GCD,
        PRODUCT
    }

    public enum RangeUpdateFn {
        ASSIGN,
        ADDITION,
        MULTIPLICATION
    }

    private int n;
    private Long[] t;
    private Long[] lazy;
    private BinaryOperator<Long> combinationFn;

    private interface Ruf {
        Long apply(Long base, long tl, long tr, Long delta);
    }

    private Ruf ruf;
    private Ruf lruf;

    private Long safeSum(Long a, Long b) {
        if (a == null)
            a = 0L;
        if (b == null)
            b = 0L;
        return a + b;
    }

    private Long safeMul(Long a, Long b) {
        if (a == null)
            a = 1L;
        if (b == null)
            b = 1L;
        return a * b;
    }

    private Long safeMin(Long a, Long b) {
        if (a == null)
            return b;
        if (b == null)
            return a;
        return Math.min(a, b);
    }

    private Long safeMax(Long a, Long b) {
        if (a == null)
            return b;
        if (b == null)
            return a;
        return Math.max(a, b);
    }

    private BinaryOperator<Long> sumCombinationFn = this::safeSum;
    private BinaryOperator<Long> minCombinationFn = this::safeMin;
    private BinaryOperator<Long> maxCombinationFn = this::safeMax;
    private BinaryOperator<Long> productCombinationFn = this::safeMul;
    private BinaryOperator<Long> gcdCombinationFn =
            (a, b) -> {
                if (a == null)
                    return b;
                if (b == null)
                    return a;
                long gcd = a;
                while (b != 0) {
                    gcd = b;
                    b = a % b;
                    a = gcd;
                }
                return Math.abs(gcd);
            };
    private Ruf minQuerySumUpdate = (b, tl, tr, d) -> safeSum(b, d);
    private Ruf lminQuerySumUpdate = (b, tl, tr, d) -> safeSum(b, d);
    private Ruf minQueryMulUpdate = (b, tl, tr, d) -> safeMul(b, d);
    private Ruf lminQueryMulUpdate = (b, tl, tr, d) -> safeMul(b, d);
    private Ruf minQueryAssignUpdate = (b, tl, tr, d) -> d;
    private Ruf lminQueryAssignUpdate = (b, tl, tr, d) -> d;
    private Ruf maxQuerySumUpdate = (b, tl, tr, d) -> safeSum(b, d);
    private Ruf lmaxQuerySumUpdate = (b, tl, tr, d) -> safeSum(b, d);
    private Ruf maxQueryMulUpdate = (b, tl, tr, d) -> safeMul(b, d);
    private Ruf lmaxQueryMulUpdate = (b, tl, tr, d) -> safeMul(b, d);
    private Ruf maxQueryAssignUpdate = (b, tl, tr, d) -> d;
    private Ruf lmaxQueryAssignUpdate = (b, tl, tr, d) -> d;
    private Ruf sumQuerySumUpdate = (b, tl, tr, d) -> b + (tr - tl + 1) * d;
    private Ruf lsumQuerySumUpdate = (b, tl, tr, d) -> safeSum(b, d);
    private Ruf sumQueryMulUpdate = (b, tl, tr, d) -> safeMul(b,d);
    private Ruf lsumQueryMulUpdate = (b, tl, tr, d) -> safeMul(b, d);
    private Ruf sumQueryAssignUpdate = (b, tl, tr, d) -> (tr - tl + 1) * d;
    private Ruf lsumQueryAssignUpdate = (b, tl, tr, d) -> d;
    private Ruf gcdQuerySumUpdate = (b, tl, tr, d) -> null;
    private Ruf lgcdQuerySumUpdate = (b, tl, tr, d) -> null;
    private Ruf gcdQueryMulUpdate = (b, tl, tr, d) -> safeMul(b, d);
    private Ruf lgcdQueryMulUpdate = (b, tl, tr, d) -> safeMul(b, d);
    private Ruf gcdQueryAssignUpdate = (b, tl, tr, d) -> d;
    private Ruf lgcdQueryAssignUpdate = (b, tl, tr, d) -> d;
    private Ruf productQuerySumUpdate = (b, tl, tr, d) -> b + (long) (b + Math.pow(d, (tr - tl + 1)));
    private Ruf lproductQuerySumUpdate = (b, tl, tr, d) -> safeSum(b, d);
    private Ruf productQueryMulUpdate = (b, tl, tr, d) -> b * (long) (b + Math.pow(d, (tr - tl + 1)));
    private Ruf lproductQueryMulUpdate = (b, tl, tr, d) -> safeSum(b, d);
    private Ruf productQueryAssignUpdate = (b, tl, tr, d) -> d;
    private Ruf lproductQueryAssignUpdate = (b, tl, tr, d) -> d;

    public GenericSegmentTree(
            long[] values,
            SegmentCombinationFn segmentCombinationFn,
            RangeUpdateFn rangeUpdateFn) {
        Objects.requireNonNull(values);
        Objects.requireNonNull(segmentCombinationFn);
        Objects.requireNonNull(rangeUpdateFn);

        n = values.length;
        int N = 4 * n;
        t = new Long[N];
        lazy = new Long[N];
        buildSegmentTree(0, 0, n - 1, values);
        switch (segmentCombinationFn) {
            case SUM -> {
                combinationFn = sumCombinationFn;
                switch (rangeUpdateFn){
                    case ADDITION -> {
                        ruf = sumQuerySumUpdate;
                        lruf = lsumQuerySumUpdate;
                    }
                    case ASSIGN -> {
                        ruf = sumQueryAssignUpdate;
                        lruf = lsumQueryAssignUpdate;
                    }
                    case MULTIPLICATION -> {
                        ruf = sumQueryMulUpdate;
                        lruf = lsumQueryMulUpdate;
                    }
                }
            }
            case MIN -> {
                // TODO
            }
        }
    }

    private void buildSegmentTree(int i, int tl, int tr, long[] values) {
        if (tl == tr) {
            t[i] = values[tl];
        } else {
            int tm = (tl + tr) / 2;
            buildSegmentTree(2 * i + 1, tl, tm, values);
            buildSegmentTree(2 * i + 2, tm + 1, tr, values);
            t[i] = combinationFn.apply(t[2 * i + 1], t[2 * i + 2]);
        }
    }

    public Long rangeQuery1(int l, int r) {
        return rangeQuery1(0, 0, n - 1, l, r);
    }

    private Long rangeQuery1(int i, int tl, int tr, int l, int r) {
        if (l > r)
            return null;
        propagate1(i, tl, tr);
        if (tl == l && tr == r)
            return t[i];
        int tm = (tl + tr) / 2;
        Long leftQuery = rangeQuery1(2 * i + 1, tl, tm, l, Math.min(tm, r));
        Long rightQuery = rangeQuery1(2 * i + 2, tm + 1, tr, Math.max(l, tm + 1), r);
        return combinationFn.apply(leftQuery, rightQuery);
    }

    private void propagate1(int i, int tl, int tr) {
        if (lazy[i] == null)
            return;
        t[i] = ruf.apply(t[i], tl, tr, lazy[i]);
        propagateLazy1(i, tl, tr, lazy[i]);
        lazy[i] = null;
    }

    private void propagateLazy1(int i, int tl, int tr, long delta) {
        if (tl == tr)
            return;
        lazy[2 * i + 1] = lruf.apply(lazy[2 * i + 1], tl, tr, delta);
        lazy[2 * i + 2] = lruf.apply(lazy[2 * i + 2], tl, tr, delta);
    }

    public void rangeUpdate1(int l, int r, long x) {
        rangeUpdate1(0, 0, n - 1, l, r, x);
    }

    private void rangeUpdate1(int i, int tl, int tr, int l, int r, long x) {
        propagate1(i, tl, tr);
        if (l > r)
            return;
        if (tl == l && tr == r) {
            t[i] = ruf.apply(t[i], tl, tr, x);
            propagateLazy1(i, tl, tr, x);
        } else {
            int tm = (tl + tr) / 2;
            rangeUpdate1(2 * i + 1, tl, tm, l, Math.min(tm, r), x);
            rangeUpdate1(2 * i + 2, tm + 1, tr, Math.max(l, tm + 1), r, x);
            t[i] = combinationFn.apply(t[2 * i + 1], t[2 * i + 2]);
        }
    }
}
