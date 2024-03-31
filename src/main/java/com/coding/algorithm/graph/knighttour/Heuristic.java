package com.coding.algorithm.graph.knighttour;

import javax.swing.*;
import java.awt.*;

public class Heuristic extends JFrame {
    public Heuristic(){
        super("Optimized Heuristic");
        Container contents = getContentPane();
        contents.setLayout(new GridLayout(8,8));
        Board chessBoard = new Board();
        for (int row = 0; row < Board.BOARD_SIZE; row++) {
            for (int column = 0; column < Board.BOARD_SIZE; column++) {
                contents.add(chessBoard.getTile(row, column));
            }
        }
        setSize(700,700);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        for (int row = 0; row < Board.BOARD_SIZE; row++) {
            for (int column = 0; column < Board.BOARD_SIZE; column++) {
                chessBoard.runTour(row, column,true);
                chessBoard.resetBoard();
            }
        }
    }

    public static void main(String[] args) {
        new Heuristic();
    }
}
