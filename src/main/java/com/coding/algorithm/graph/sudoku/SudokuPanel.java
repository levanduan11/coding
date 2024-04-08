package com.coding.algorithm.graph.sudoku;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;

public class SudokuPanel extends JPanel {
    private SudokuPuzzle puzzle;
    private int currentSelectedRow;
    private int currentSelectedCol;
    private int usedWidth;
    private int usedHeight;
    private int fontSize;

    public SudokuPanel() {
        this.setPreferredSize(new Dimension(540, 540));
        this.addMouseListener(new SudokuPanelMouseAdapter());
        this.puzzle = new SudokuGenerator().generateRandomSudoku(SudokuPuzzleType.NINEBYNINE);
        currentSelectedRow = -1;
        currentSelectedCol = -1;
        usedHeight = 0;
        usedWidth = 0;
        fontSize = 27;
    }

    public SudokuPanel(SudokuPuzzle puzzle) {
        this.setPreferredSize(new Dimension(540, 540));
        this.addMouseListener(new SudokuPanelMouseAdapter());
        this.puzzle = puzzle;
        currentSelectedRow = -1;
        currentSelectedCol = -1;
        usedHeight = 0;
        usedWidth = 0;
        fontSize = 27;
    }

    public void newSudokuPuzzle(SudokuPuzzle puzzle) {
        this.puzzle = puzzle;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(new Color(1.0f, 1.0f, 1.0f));

        int slotWidth = this.getWidth() / puzzle.getColumns();
        int slotHeight = this.getHeight() / puzzle.getRows();

        usedWidth = (this.getWidth() / puzzle.getColumns()) * puzzle.getColumns();
        usedHeight = (this.getHeight() / puzzle.getRows()) * puzzle.getRows();

        g2d.fillRect(0, 0, usedWidth, usedHeight);
        g2d.setColor(new Color(0.0f, 0.0f, 0.0f));
        for (int x = 0; x <= usedWidth; x += slotWidth) {
            if ((x / slotWidth) % puzzle.getBoxWidth() == 0) {
                g2d.setStroke(new BasicStroke(3));
                g2d.drawLine(x, 0, x, usedHeight);
            } else {
                g2d.setStroke(new BasicStroke(1));
                g2d.drawLine(x, 0, x, usedHeight);
            }
        }
        for (int y = 0; y <= usedHeight; y += slotHeight) {
            if ((y / slotHeight) % puzzle.getBoxHeight() == 0) {
                g2d.setStroke(new BasicStroke(3));
                g2d.drawLine(0, y, usedWidth, y);
            } else {
                g2d.setStroke(new BasicStroke(1));
                g2d.drawLine(0, y, usedWidth, y);
            }
        }
        Font f = new Font("Times New Roman", Font.BOLD, fontSize);
        g2d.setFont(f);
        FontRenderContext frc = g2d.getFontRenderContext();
        for (int row = 0; row < puzzle.getRows(); row++) {
            for (int col = 0; col < puzzle.getColumns(); col++) {
                if (!puzzle.isSlotAvailable(row, col)) {
                    int textWidth = (int) f.getStringBounds(puzzle.getValue(row, col), frc).getWidth();
                    int textHeight = (int) f.getStringBounds(puzzle.getValue(row, col), frc).getHeight();
                    g2d.drawString(puzzle.getValue(row, col), (col * slotWidth) + ((slotWidth / 2) - (textWidth / 2)), (row * slotHeight) + ((slotHeight / 2) + (slotWidth / 2)));
                }
            }
        }
        if (currentSelectedRow != -1 && currentSelectedCol != -1) {
            g2d.setColor(new Color(0.0f, 0.0f, 1.0f, 0.3f));
            g2d.fillRect(currentSelectedCol * slotWidth, currentSelectedRow * slotHeight, slotWidth, slotHeight);
        }
    }

    public void messageFromNumActionListener(String buttonValue) {
        if (currentSelectedCol != -1 && currentSelectedRow != -1) {
            puzzle.makeMove(currentSelectedRow, currentSelectedCol, buttonValue, true);
            repaint();
        }
    }

    public class NumActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            messageFromNumActionListener(((JButton) e.getSource()).getText());
        }
    }

    private class SudokuPanelMouseAdapter extends MouseInputAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                int slotWidth = usedWidth / puzzle.getColumns();
                int slotHeight = usedHeight / puzzle.getRows();
                currentSelectedRow = e.getY() / slotHeight;
                currentSelectedCol = e.getX() / slotWidth;
                repaint();
            }
        }
    }
}
