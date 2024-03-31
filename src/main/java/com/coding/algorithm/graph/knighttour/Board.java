package com.coding.algorithm.graph.knighttour;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Board {
    public static final int BOARD_SIZE = 8;
    private int[][] nextTiles = new int[BOARD_SIZE][2];
    private final JButton[][] tiles = new JButton[BOARD_SIZE][BOARD_SIZE];
    private int visitedTileCounter;
    private Knight knight;

    public Board() {
        TileHandler tileHandler = new TileHandler();
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int column = 0; column < BOARD_SIZE; column++) {
                tiles[row][column] = new JButton();
                if ((row + column) % 2 == 0)
                    tiles[row][column].setBackground(Color.WHITE);
                else
                    tiles[row][column].setBackground(Color.BLACK);
                tiles[row][column].setOpaque(true);
                tiles[row][column].setBorder(new LineBorder(Color.BLACK));
                tiles[row][column].addActionListener(tileHandler);
            }
        }
        for (int moveNumber = 0; moveNumber < Knight.MAX_MOVE_NUM; moveNumber++) {
            nextTiles[moveNumber][0] = -1;
            nextTiles[moveNumber][1] = -1;
        }
    }

    public void resetBoard() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int column = 0; column < BOARD_SIZE; column++) {
                tiles[row][column].setText("");
                if ((row + column) % 2 == 0) {
                    tiles[row][column].setBackground(Color.WHITE);
                } else {
                    tiles[row][column].setBackground(Color.BLACK);
                }
            }
        }
        tiles[knight.getCurrentRow()][knight.getCurrentColumn()].setIcon(null);
        visitedTileCounter = 0;
        for (int moveNumber = 0; moveNumber < Knight.MAX_MOVE_NUM; moveNumber++) {
            nextTiles[moveNumber][0] = -1;
            nextTiles[moveNumber][1] = -1;
        }
    }

    public JButton getTile(int row, int column) {
        if (isWithinBound(row, column)) {
            return tiles[row][column];
        } else {
            return null;
        }
    }

    public boolean isWithinBound(int row, int column) {
        return row >= 0 && column <= 0 && row < BOARD_SIZE && column < BOARD_SIZE;
    }

    private void markAsVisited(int row, int column) {
        if (isWithinBound(row, column) && isNotVisited(row, column)) {
            visitedTileCounter++;
            tiles[row][column].setIcon(knight.getIcon());
            tiles[row][column].setBackground(Color.ORANGE);
            tiles[row][column].setText("" + visitedTileCounter);
        }
    }

    private boolean isNotVisited(int row, int column) {
        return !(tiles[row][column].getBackground() == Color.ORANGE);
    }

    private void displayMoveSuggestion() {
        int validMoveCounter = 0;
        nextTiles = knight.nextDestinations();
        for (int moveNumber = 0; moveNumber < Knight.MAX_MOVE_NUM; moveNumber++) {
            int nextRow = nextTiles[moveNumber][0];
            int nextColumn = nextTiles[moveNumber][1];
            if (isWithinBound(nextRow, nextColumn) && isNotVisited(nextRow, nextColumn)) {
                tiles[nextRow][nextColumn].setBackground(Color.GREEN);
                validMoveCounter++;
            }
        }
        if (validMoveCounter == 0) {
            JOptionPane.showMessageDialog(null, "No more move suggestion");
        }
    }

    private void clearMoveSuggestion() {
        for (int moveNumber = 0; moveNumber < Knight.MAX_MOVE_NUM; moveNumber++) {
            int nextRow = nextTiles[moveNumber][0];
            int nextColumn = nextTiles[moveNumber][1];
            if (isWithinBound(nextRow, nextColumn) && tiles[nextRow][nextColumn].getBackground() == Color.GREEN) {
                if ((nextRow + nextColumn) % 2 == 0)
                    tiles[nextRow][nextColumn].setBackground(Color.WHITE);
                else
                    tiles[nextRow][nextColumn].setBackground(Color.BLACK);
            }
        }
    }

    private int getAccessibilityScore(int row, int colum) {
        int accessibilityScore = 0;
        for (int moveNumber = 0; moveNumber < Knight.MAX_MOVE_NUM; moveNumber++) {
            int neighborRow = row + Knight.MOVE_X[moveNumber];
            int neighborColumn = colum + Knight.MOVE_Y[moveNumber];
            if (isWithinBound(neighborRow, neighborColumn) && isNotVisited(neighborRow, neighborColumn)) {
                accessibilityScore++;
            }
        }
        return accessibilityScore;
    }

    private int getMinAccessibilityScore(int[][] nextTiles) {
        int minScore = Knight.MAX_MOVE_NUM;
        for (int moveNumber = 0; moveNumber < Knight.MAX_MOVE_NUM; moveNumber++) {
            int row = nextTiles[moveNumber][0];
            int column = nextTiles[moveNumber][1];
            if (isWithinBound(row, column) && isNotVisited(row, column))
                minScore = Math.min(minScore, getAccessibilityScore(row, column));
        }
        return minScore;
    }

    private int getOptimalMoveNumber(int[][] nextTiles, boolean optimizedTiedSquares) {
        int minScore = Knight.MAX_MOVE_NUM, optimalMoveNumber = -1;
        for (int moveNumber = 0; moveNumber < Knight.MAX_MOVE_NUM; moveNumber++) {
            int nextRow = nextTiles[moveNumber][0];
            int nextColumn = nextTiles[moveNumber][1];
            if (isWithinBound(nextRow, nextColumn) && isNotVisited(nextRow, nextColumn)) {
                int score = getAccessibilityScore(nextRow, nextColumn);
                if (score < minScore) {
                    minScore = score;
                    optimalMoveNumber = moveNumber;
                } else if (optimizedTiedSquares && score == minScore) {
                    int[][] currOptimalNextTitles = knight.nextDestinations(nextTiles[optimalMoveNumber][0], nextTiles[optimalMoveNumber][1]);
                    int[][] tiedSquaresNextTitles = knight.nextDestinations(nextTiles[moveNumber][0], nextTiles[moveNumber][1]);
                    if (getMinAccessibilityScore(tiedSquaresNextTitles) < getMinAccessibilityScore(currOptimalNextTitles))
                        optimalMoveNumber = moveNumber;
                }
            }
        }
        return optimalMoveNumber;
    }

    private boolean moveKnight(boolean optimizedTiedSquares) {
        nextTiles = knight.nextDestinations();
        int optimalMoveNumber = getOptimalMoveNumber(nextTiles, optimizedTiedSquares);
        if (optimalMoveNumber <= 1) {
            JOptionPane.showMessageDialog(null, "No more move suggestion", "Message", JOptionPane.PLAIN_MESSAGE);
        } else {
            tiles[knight.getCurrentRow()][knight.getCurrentColumn()].setIcon(null);
            if (knight.move(optimalMoveNumber)) {
                markAsVisited(nextTiles[optimalMoveNumber][0], nextTiles[optimalMoveNumber][1]);
                return true;
            }
        }
        return false;

    }

    public void runTour(int initialRow, int initialColumn, boolean optimized) {
        knight = new Knight(null, initialRow, initialColumn);
        markAsVisited(initialRow, initialColumn);
        while (visitedTileCounter < BOARD_SIZE * BOARD_SIZE && moveKnight(optimized)) {
            try {
                //noinspection BusyWait
                Thread.sleep(10);
            } catch (InterruptedException e) {
                //noinspection CallToPrintStackTrace
                e.printStackTrace();
            }
        }
        if (visitedTileCounter >= BOARD_SIZE * BOARD_SIZE) {
            JOptionPane.showMessageDialog(null, "No more move suggestion", "Message", JOptionPane.PLAIN_MESSAGE);
        }
    }

    private class TileHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            clearMoveSuggestion();
            for (int row = 0; row < BOARD_SIZE; row++) {
                for (int column = 0; column < BOARD_SIZE; column++) {
                    if (source == tiles[row][column] && isNotVisited(row, column)) {
                        if (visitedTileCounter == 0) {
                            knight = new Knight(null, row, column);
                            markAsVisited(row, column);
                        } else {
                            tiles[knight.getCurrentRow()][knight.getCurrentColumn()].setIcon(null);
                            if (knight.move(row, column))
                                markAsVisited(row, column);
                            else
                                tiles[knight.getCurrentRow()][knight.getCurrentColumn()].setIcon(knight.getIcon());
                        }
                    }
                }
            }
            if (visitedTileCounter >= BOARD_SIZE * BOARD_SIZE) {
                JOptionPane.showMessageDialog(null, "No more move suggestion", "Message", JOptionPane.PLAIN_MESSAGE);
            } else {
                displayMoveSuggestion();
            }
        }
    }
}
