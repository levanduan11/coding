package com.coding.hard;

import java.util.Arrays;

class Solution3 {
    private final static int SIZE = 9;
    private final static int BLOCK_SIZE = 3;
    private final static char EMPTY_CELL = '.';
    // private final static int allBits = 0x1ff;
    private final static int allBits = 0b111111111;
    private final static int[] bitFlags = new int[]{
            0x1,
            0x1 << 1,
            0x1 << 2,
            0x1 << 3,
            0x1 << 4,
            0x1 << 5,
            0x1 << 6,
            0x1 << 7,
            0x1 << 8,
    };
    private char[][] board;
    private int[] rows;
    private int[] cols;
    private int[] blocks;
    private int totalCount = 0;

    public void solveSudoku(char[][] board) {
        if (board == null || board.length != SIZE && board[0].length != SIZE) throw new IllegalArgumentException();
        this.board = board;
        initialize();
        initialUpdate();
        bruteForce();
    }

    private boolean bruteForce() {
        if (totalCount >= SIZE * SIZE) return true;
        int row = -1;
        int col = -1;

        int min = SIZE + 1;
        for (int r = 0; r < SIZE && min > 1; r++) {
            for (int c = 0; c < SIZE; c++) {
                if (board[r][c] != EMPTY_CELL) continue;
                int candidateCounts = getCandidateCount(r, c);
                if (candidateCounts < min) {
                    min = candidateCounts;
                    row = r;
                    col = c;
                }
                if (min == 1) break;
            }
        }
        if (min < 1) return false;
        int candidates = getCandidates(row, col);

        for (int i = 0; i < SIZE && candidates != 0; i++, candidates >>= 1) {
            if (candidates % 2 == 0) continue;
            mark(i, row, col);
            board[row][col] = toChar(i);
            if (bruteForce()) return true;
            board[row][col] = EMPTY_CELL;
            unmark(i, row, col);
        }
        return false;
    }

    private void unmark(int value, int row, int col) {
        int bldId = row / BLOCK_SIZE * BLOCK_SIZE + col / BLOCK_SIZE;
        int flag = ~bitFlags[value];
        rows[row] &= flag;
        cols[col] &= flag;
        blocks[bldId] &= flag;
        --totalCount;
    }

    private char toChar(int i) {
        return (char) (i + '1');
    }

    private void mark(int value, int row, int col) {
        int bldId = row / BLOCK_SIZE * BLOCK_SIZE + col / BLOCK_SIZE;
        int flag = bitFlags[value];
        rows[row] |= flag;
        cols[col] |= flag;
        blocks[bldId] |= flag;
        ++totalCount;
    }

    private int getCandidates(int row, int col) {
        int blkId = row / BLOCK_SIZE * BLOCK_SIZE + col / BLOCK_SIZE;
        int v = (rows[row] | cols[col] | blocks[blkId]);
        int r = ~(rows[row] | cols[col] | blocks[blkId]);
        int re = r & allBits;
        return ~(rows[row] | cols[col] | blocks[blkId]) & allBits;
    }

    private int getCandidateCount(int r, int c) {
        int candidates = getCandidates(r, c);
        int count = 0;
        for (int i = 0; candidates != 0; i++, candidates = candidates & (candidates - 1)) count++;
        return count;
    }

    private void initialUpdate() {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                if (board[r][c] == EMPTY_CELL) continue;
                int value = fromChar(board[r][c]);
                mark(value, r, c);
            }
        }
    }

    private int fromChar(char c) {
        return c - '1';
    }

    private void initialize() {
        rows = new int[SIZE];
        cols = new int[SIZE];
        blocks = new int[SIZE];
        totalCount = 0;
    }

    public static void main(String[] args) {
        var sol = new Solution3();
        char[][] grid = {
                {'5', '3', '.', '.', '7', '.', '.', '.', '.'},
                {'6', '.', '.', '1', '9', '5', '.', '.', '.'},
                {'.', '9', '8', '.', '.', '.', '.', '6', '.'},
                {'8', '.', '.', '.', '6', '.', '.', '.', '3'},
                {'4', '.', '.', '8', '.', '3', '.', '.', '1'},
                {'7', '.', '.', '.', '2', '.', '.', '.', '6'},
                {'.', '6', '.', '.', '.', '.', '2', '8', '.'},
                {'.', '.', '.', '4', '1', '9', '.', '.', '5'},
                {'.', '.', '.', '.', '8', '.', '.', '7', '9'}};
//        sol.solveSudoku(grid);
//        Arrays.stream(bitFlags).reduce((left, right) -> {
//            left|=right;
//            return left;
//        }).ifPresent(System.out::println);


        int flag = 1 << 2;
        System.out.println(Integer.toBinaryString(flag));
        int i = 8;
        int mergeI = i | flag;
        System.out.println(Integer.toBinaryString(mergeI));
        int unFlag = ~flag;
        System.out.println(Integer.toBinaryString(unFlag));
        int undoI = mergeI & unFlag;
        System.out.println(Integer.toBinaryString(undoI));

        System.out.println(Integer.toBinaryString((1 << 7) - 1));
        System.out.println("----");
        System.out.println(Integer.toBinaryString(5));
        System.out.println(Integer.toBinaryString(4));


    }
}

public class SudokuSolver {

    static int N = 9;

    public static void solveSudoku(char[][] board) {
        int row = 0, col = 0;
        solveSudoku(board, row, col);
    }

    private static boolean solveSudoku(char[][] grid, int row, int col) {
        if (row == N - 1 && col == N) return true;
        if (col == N) {
            row++;
            col = 0;
        }
        if (grid[row][col] != '.') return solveSudoku(grid, row, col + 1);
        for (char ch = '1'; ch <= '9'; ch++) {
            if (isValid(grid, row, col, ch)) {
                grid[row][col] = ch;
                if (solveSudoku(grid, row, col + 1)) return true;
            }
            grid[row][col] = '.';
        }
        return false;
    }

    private static boolean isValid(char[][] grid, int row, int col, char ch) {
        for (int i = 0; i < N; i++) {
            if (grid[row][i] == ch || grid[i][col] == ch) return false;
        }
        int startRow = row - row % 3, startCol = col - col % 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (grid[i + startRow][j + startCol] == ch) return false;
            }
        }
        return true;
    }

    static void print(char[][] grid) {
        for (char[] chars : grid) {
            System.out.println(Arrays.toString(chars));
        }
    }

    public static void main(String[] args) {
        char[][] grid = {
                {'5', '3', '.', '.', '7', '.', '.', '.', '.'},
                {'6', '.', '.', '1', '9', '5', '.', '.', '.'},
                {'.', '9', '8', '.', '.', '.', '.', '6', '.'},
                {'8', '.', '.', '.', '6', '.', '.', '.', '3'},
                {'4', '.', '.', '8', '.', '3', '.', '.', '1'},
                {'7', '.', '.', '.', '2', '.', '.', '.', '6'},
                {'.', '6', '.', '.', '.', '.', '2', '8', '.'},
                {'.', '.', '.', '4', '1', '9', '.', '.', '5'},
                {'.', '.', '.', '.', '8', '.', '.', '7', '9'}};

        int a = 10;
        int b = 5;
        int s = a | b;
        System.out.println(~7);
        System.out.println(7 ^ 9);
    }
}
