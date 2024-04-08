package com.coding.algorithm.graph.sudoku;

public enum SudokuPuzzleType {
    SIXBYSIX(6, 6, 3, 2, new String[]{"1", "2", "3", "4", "5", "6"}, "6 by 6 game"),
    NINEBYNINE(9, 9, 3, 3, new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9"}, "9 by 9 game"),
    TWELVEBYTWELVE(12, 12, 4, 3, new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C"}, "12 by 12 game"),
    SIXTEENBYSIXTEEN(16, 16, 4, 4, new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G"}, "16 by 16 game");
    private final int rows;
    private final int columns;
    private final int boxWidth;
    private final int boxHeight;
    private final String[] validValues;
    private final String desc;

    SudokuPuzzleType(int rows, int columns, int boxWidth, int boxHeight, String[] validValues, String desc) {
        this.rows = rows;
        this.columns = columns;
        this.boxWidth = boxWidth;
        this.boxHeight = boxHeight;
        this.validValues = validValues;
        this.desc = desc;
    }

    public int getBoxHeight() {
        return boxHeight;
    }

    public int getBoxWidth() {
        return boxWidth;
    }

    public int getColumns() {
        return columns;
    }

    public String getDesc() {
        return desc;
    }

    public int getRows() {
        return rows;
    }

    public String[] getValidValues() {
        return validValues;
    }
}
