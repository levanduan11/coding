package com.coding.algorithm.graph.knighttour;

import javax.swing.*;

public class Knight {
    public static final int MAX_MOVE_NUM = 8;
    public static final int[] MOVE_X = {2, 1, -1, -2, -2, -1, 1, 2};
    public static final int[] MOVE_Y = {1, 2, 2, 1, -1, -2, -2, -1};
    private ImageIcon icon;
    private int currentRow;
    private int currentColumn;

    public Knight(String imageFile, int currentRow, int currentColumn) {
        setIcon(imageFile);
        this.currentRow = currentRow;
        this.currentColumn = currentColumn;
    }

    public ImageIcon getIcon() {
        return icon;
    }

    public void setIcon(String imageFile) {
        this.icon = new ImageIcon(imageFile);
    }

    public boolean isValidMoveShape(int nextRow, int nextColumn) {
        int rowDiff = Math.abs(currentRow - nextRow);
        int columnDiff = Math.abs(currentColumn - nextColumn);
        return (rowDiff == 1 && columnDiff == 2) || (rowDiff == 2 && columnDiff == 1);
    }

    public int[][] nextDestinations() {
        int[][] nextTiles = new int[MAX_MOVE_NUM][2];
        for (int moveNumber = 0; moveNumber < MAX_MOVE_NUM; moveNumber++) {
            nextTiles[moveNumber][0] = currentRow + MOVE_X[moveNumber];
            nextTiles[moveNumber][1] = currentColumn + MOVE_Y[moveNumber];
        }
        return nextTiles;
    }

    public int[][] nextDestinations(int row, int column) {
        int[][] nextTiles = new int[MAX_MOVE_NUM][2];
        for (int moveNumber = 0; moveNumber < MAX_MOVE_NUM; moveNumber++) {
            nextTiles[moveNumber][0] = row + MOVE_X[moveNumber];
            nextTiles[moveNumber][1] = column + MOVE_Y[moveNumber];
        }
        return nextTiles;
    }

    public boolean move(int moveNumber) {
        if (moveNumber < MAX_MOVE_NUM) {
            currentRow += MOVE_X[moveNumber];
            currentColumn += MOVE_Y[moveNumber];
            return true;
        } else {
            return false;
        }
    }

    public boolean move(int nextRow, int nextColumn) {
        if (isValidMoveShape(nextRow, nextColumn)) {
            currentRow = nextRow;
            currentColumn = nextColumn;
            return true;
        } else {
            return false;
        }
    }

    public int getCurrentColumn() {
        return currentColumn;
    }

    public int getCurrentRow() {
        return currentRow;
    }
}
