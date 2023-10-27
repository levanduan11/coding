package com.coding.heap;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static java.lang.Math.min;
import static java.lang.Math.max;

public class MinIndexedDHeap<T extends Comparable<T>> {

    private int sz;
    private final int N;
    private final int D;
    private final int[] child, parent;
    private final int[] pm;
    private final int[] im;
    private final Object[] values;

    public MinIndexedDHeap(int degree, int maxSize) {
        if (maxSize <= 0) throw new IllegalArgumentException("max size <=0");
        D = max(2, degree);
        N = max(D + 1, maxSize);

        im = new int[N];
        pm = new int[N];
        child = new int[N];
        parent = new int[N];
        values = new Object[N];

        for (int i = 0; i < N; i++) {
            parent[i] = (i - 1) / D;
            child[i] = i * D + 1;
            pm[i] = im[i] = -1;
        }
    }

    public int size() {
        return sz;
    }

    public boolean isEmpty() {
        return sz == 0;
    }

    public boolean contains(int ki) {
        keyInBoundsOrThrow(ki);
        return pm[ki] != -1;
    }

    public int peekMinKeyIndex() {
        isNotEmptyOrThrow();
        return im[0];
    }

    public int pollMinKeyIndex() {
        int minKi = peekMinKeyIndex();
        delete(minKi);
        return minKi;
    }

    @SuppressWarnings("unchecked")
    public T peekMinValue() {
        isNotEmptyOrThrow();
        return (T) values[im[0]];
    }

    public T pollMinValue() {
        T minValue = peekMinValue();
        delete(peekMinKeyIndex());
        return minValue;
    }

    public void insert(int ki, T value) {
        if (contains(ki)) throw new IllegalArgumentException("index already exists; received: " + ki);
        valueNotNullOrThrow(value);
        pm[ki] = sz;
        im[sz] = ki;
        values[ki] = value;
        swim(sz++);
    }

    @SuppressWarnings("unchecked")
    public T valueOf(int ki) {
        keyExistsOrThrow(ki);
        return (T) values[ki];
    }

    @SuppressWarnings("unchecked")
    public T delete(int ki) {
        keyExistsOrThrow(ki);
        final int i = pm[ki];
        swap(i, --sz);
        sink(i);
        swim(i);
        T value = (T) values[ki];
        values[ki] = null;
        pm[ki] = -1;
        im[sz] = -1;
        return value;
    }

    @SuppressWarnings("unchecked")
    public T update(int ki, T value) {
        keyExistsAndValueNotNullOrThrow(ki, value);
        final int i = pm[ki];
        T oldValue = (T) values[ki];
        values[ki] = value;
        sink(i);
        swim(i);
        return oldValue;
    }

    public void decrease(int ki, T value) {
        keyExistsAndValueNotNullOrThrow(ki, value);
        if (less(value, values[ki])) {
            values[ki] = value;
            swim(pm[ki]);
        }
    }

    public void increase(int ki, T value) {
        keyExistsAndValueNotNullOrThrow(ki, value);
        if (less(values[ki], value)) {
            values[ki] = value;
            sink(pm[ki]);
        }
    }

    private void sink(int i) {
        for (int j = minChild(i); j != -1; ) {
            swap(i, j);
            i = j;
            j = minChild(i);
        }
    }
    public void swim(int i){
        while (less(i,parent[i])){
            swap(i,parent[i]);
            i = parent[i];
        }
    }
    private int minChild(int i){
        int index = -1,from = child[i], to = min(sz,from + D);
        for (int j = from; j <to ; j++) {
            if (less(j,i)){
                index=i=j;
            }
        }
        return index;
    }

    private void swap(int i,int j){
        pm[im[j]]=i;
        pm[im[i]]=j;
        int tmp = im[i];
        im[i] = im[j];
        im[j]=tmp;
    }
    @SuppressWarnings("unchecked")
    private boolean less(int i, int j){
        return ((Comparable<? super T>)values[im[i]]).compareTo((T)values[im[j]])<0;
    }
    @SuppressWarnings("unchecked")
    private boolean less(Object obj1, Object obj2){
        return ((Comparable<? super T>)obj1).compareTo((T)obj2)<0;
    }

    @Override
    public String toString() {
        List<Integer>list=new ArrayList<>(sz);
        for (int i = 0; i < sz; i++) {
            list.add(im[i]);
        }
        return list.toString();
    }
    private void isNotEmptyOrThrow(){
        if (isEmpty())throw new NoSuchElementException();
    }
    private void keyExistsAndValueNotNullOrThrow(int ki, Object value){
        keyExistsOrThrow(ki);
        valueNotNullOrThrow(value);
    }
    private void keyExistsOrThrow(int ki){
        if (!contains(ki)) throw new NoSuchElementException();
    }
    private void valueNotNullOrThrow(Object value){
        if (value == null) throw new IllegalArgumentException();
    }
    private void keyInBoundsOrThrow(int ki){
        if (ki<0 || ki>=N){
            throw new IllegalArgumentException();
        }
    }
    public boolean isMinHeap(){
        return isMinHeap(0);
    }
    public boolean isMinHeap(int i){
        int from = child[i], to=min(sz,from+D);
        for (int j = from; j <to ; j++) {
            if (!less(i,j))return false;
            if (!isMinHeap(j))return false;
        }
        return isMinHeap(0);
    }
}
