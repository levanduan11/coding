package com.coding.dsa.backtracking;

import java.util.Arrays;

public class NQueen {
    static boolean isSafe(int[][] board, int row, int col, int n) {
        int i, j;
        for (i = 0; i < col; i++) {
            if (board[row][i] == 1) return false;
        }
        for (i = row, j = col; i >= 0 && j >= 0; i--, j--) {
            if (board[i][j] == 1) return false;
        }
        for (i = row, j = col; j >= 0 && i < n; i++, j--) {
            if (board[i][j] == 1) return false;
        }
        return true;
    }
    static void print(int[][]grid){
        for(int[]g:grid){
            System.out.println(Arrays.toString(g));
        }
    }
    static boolean solveNQUtil(int[][] board,int col,int n){
        if (col == n){
            print(board);
            System.out.println();
            return false;
        }
        for (int i = 0; i < n; i++) {
            if (isSafe(board,i,col,n)){
                board[i][col] =  1;
                if (solveNQUtil(board,col+1,n))return true;
                board[i][col] = 0;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        int n=4;
        int[][]board=new int[4][4];
        solveNQUtil(board,0,n);

    }
}
