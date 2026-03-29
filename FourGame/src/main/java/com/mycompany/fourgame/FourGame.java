/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.fourgame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * FourGame – the main window.
 * <p>
 * Handles:
 * <ul>
 *   <li>size selection (JComboBox)</li>
 *   <li>grid of JButton objects</li>
 *   <li>event routing to Board</li>
 *   <li>game-over dialog and automatic restart</li>
 * </ul>
 */
public class FourGame extends JFrame implements ActionListener {
    private Board board;
    private JButton[][] buttons;
    private JComboBox<String> sizeCombo;
    private JPanel center;
    private int n;                     

    /** Constructor – builds the window and starts a 3x3 game. */
    public FourGame() {
        setTitle("Four Game");
        setLayout(new BorderLayout());

        // ----- top panel (size selector) -----
        JPanel top = new JPanel();
        top.add(new JLabel("Board size: "));
        sizeCombo = new JComboBox<>(new String[]{"3x3", "5x5", "7x7"});
        sizeCombo.addActionListener(this);
        top.add(sizeCombo);
        add(top, BorderLayout.NORTH);

        startNewGame(3);           

        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /** Re-create the board and button grid for a new size. */
    private void startNewGame(int size) {
        n = size;
        board = new Board(n);

        if (center != null) remove(center);

        center = new JPanel(new GridLayout(n, n));
        buttons = new JButton[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                buttons[i][j] = new JButton("0");
                buttons[i][j].setBackground(Color.WHITE);
                buttons[i][j].addActionListener(this);
                center.add(buttons[i][j]);
            }
        }
        add(center, BorderLayout.CENTER);
        revalidate();
        repaint();
        pack();
    }

    /** Central action listener – combo box or grid button. */
    @Override
    public void actionPerformed(ActionEvent e) {
        // ----- size changed -----
        if (e.getSource() == sizeCombo) {
            String sel = (String) sizeCombo.getSelectedItem();
            int newSize = Integer.parseInt(sel.substring(0, 1));
            startNewGame(newSize);
            return;
        }

        // ----- grid button -----
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (e.getSource() == buttons[i][j]) {
                    processClick(i, j);
                    return;
                }
            }
        }
    }

    /** Process a player click, refresh UI, check for end. */
    private void processClick(int row, int col) {
        board.processMove(row, col);
        updateAllButtons();

        if (board.isGameOver()) {
            int winner = board.getWinner();
            String msg = (winner == 0) ? "Red wins!" :
                         (winner == 1) ? "Blue wins!" : "Tie!";
            JOptionPane.showMessageDialog(this, msg);
            startNewGame(n);               // same size, new game
        } else {
            board.nextPlayer();
        }
    }

    /** Refresh every button with current value + colour. */
    private void updateAllButtons() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                buttons[i][j].setText(String.valueOf(board.getValue(i, j)));
                int owner = board.getOwner(i, j);
                if (owner != -1) {
                    buttons[i][j].setBackground(owner == 0 ? Color.RED : Color.BLUE);
                } else {
                    buttons[i][j].setBackground(Color.WHITE);
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FourGame::new);
    }
}