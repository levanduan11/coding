package com.coding.algorithm.graph.sudoku;

public class SudokuPuzzle {
    protected String[][] board;
    protected boolean[][] mutable;
    private final int ROWS;
    private final int COLUMNS;
    private final int BOX_WIDTH;
    private final int BOX_HEIGHT;
    private final String[] VALID_VALUES;

    public SudokuPuzzle(int rows, int columns, int boxWidth, int boxHeight, String[] validValues) {
        this.ROWS = rows;
        this.COLUMNS = columns;
        this.BOX_WIDTH = boxWidth;
        this.BOX_HEIGHT = boxHeight;
        this.VALID_VALUES = validValues;
        this.board = new String[ROWS][COLUMNS];
        this.mutable = new boolean[ROWS][COLUMNS];
        initializeBoard();
        initializeMutableSlots();
    }

    public SudokuPuzzle(SudokuPuzzle puzzle) {
        this.ROWS = puzzle.ROWS;
        this.COLUMNS = puzzle.COLUMNS;
        this.BOX_WIDTH = puzzle.BOX_WIDTH;
        this.BOX_HEIGHT = puzzle.BOX_HEIGHT;
        this.VALID_VALUES = puzzle.VALID_VALUES;
        this.board = new String[ROWS][COLUMNS];
        this.mutable = new boolean[ROWS][COLUMNS];
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                this.board[i][j] = puzzle.board[i][j];
                this.mutable[i][j] = puzzle.mutable[i][j];
            }
        }
    }

    public void makeMove(int row, int column, String value, boolean isMutable) {
        if (this.isValidValue(value) && this.isValidMove(row, column, value) && this.isSlotMutable(row, column)) {
            this.board[row][column] = value;
            this.mutable[row][column] = isMutable;
        }
    }

    public boolean isValidMove(int row, int column, String value) {
        if (this.inRange(row, column)) {
            return !this.numInCol(column, value) && !this.numInRow(row, value) && !this.numInBox(row, column, value);
        }
        return false;
    }

    public boolean numInCol(int col, String value) {
        if (col <= this.COLUMNS) {
            for (int i = 0; i < this.ROWS; i++) {
                if (this.board[i][col].equals(value))
                    return true;
            }
        }
        return false;
    }

    public boolean numInRow(int row, String value) {
        if (row <= this.ROWS) {
            for (int i = 0; i < this.COLUMNS; i++) {
                if (this.board[row][i].equals(value))
                    return true;
            }
        }
        return false;
    }

    public boolean numInBox(int row, int col, String value) {
        if (this.inRange(row, col)) {
            int boxRow = row / this.BOX_HEIGHT;
            int boxCol = col / this.BOX_WIDTH;
            int startingRow = boxRow * this.BOX_HEIGHT;
            int startingCol = boxCol * this.BOX_WIDTH;
            for (int i = startingRow; i < startingRow + this.BOX_HEIGHT; i++) {
                for (int j = startingCol; j < startingCol + this.BOX_WIDTH; j++) {
                    if (this.board[i][j].equals(value))
                        return true;
                }
            }
        }
        return false;
    }

    public boolean isSlotAvailable(int row, int column) {
        return (this.inRange(row, column) && this.board[row][column].isEmpty()) && this.isSlotMutable(row, column);
    }

    public boolean isSlotMutable(int row, int col) {
        return this.mutable[row][col];
    }

    public String getValue(int row, int col) {
        if (this.inRange(row, col)) {
            return this.board[row][col];
        } else {
            return "";
        }
    }

    private boolean isValidValue(String value) {
        for (String validValue : this.VALID_VALUES) {
            if (validValue.equals(value)) {
                return true;
            }
        }
        return false;
    }

    public boolean inRange(int row, int col) {
        return row >= 0 && row < this.ROWS && col >= 0 && col < this.COLUMNS;
    }

    public boolean boardFull() {
        for (int i = 0; i < this.ROWS; i++) {
            for (int j = 0; j < this.COLUMNS; j++) {
                if (this.board[i][j].isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    public void makeSlotEmpty(int row, int col) {
        if (this.inRange(row, col) && this.isSlotMutable(row, col)) {
            this.board[row][col] = "";
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.ROWS; i++) {
            for (int j = 0; j < this.COLUMNS; j++) {
                sb.append(this.board[i][j]);
                if (j < this.COLUMNS - 1) {
                    sb.append(" ");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private void initializeBoard() {
        for (int i = 0; i < this.ROWS; i++) {
            for (int j = 0; j < this.COLUMNS; j++) {
                this.board[i][j] = "";
            }
        }
    }

    private void initializeMutableSlots() {
        for (int i = 0; i < this.ROWS; i++) {
            for (int j = 0; j < this.COLUMNS; j++) {
                this.mutable[i][j] = true;
            }
        }
    }

    public String[][] getBoard() {
        return board;
    }

    public int getBoxHeight() {
        return BOX_HEIGHT;
    }

    public int getBoxWidth() {
        return BOX_WIDTH;
    }

    public int getColumns() {
        return COLUMNS;
    }

    public boolean[][] getMutable() {
        return mutable;
    }

    public int getRows() {
        return ROWS;
    }

    public String[] getValidValues() {
        return VALID_VALUES;
    }
}
