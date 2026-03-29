/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.fourgame;

/**
 * Board – holds the whole game state and implements the rules.
 * <p>
 * Public methods used by FourGame:
 * <ul>
 *   <li>processMove(row,col)</li>
 *   <li>isGameOver()</li>
 *   <li>getValue(r,c) / getOwner(r,c)</li>
 *   <li>nextPlayer()</li>
 *   <li>getWinner()</li>
 * </ul>
 */
public class Board {
    private final int[][] values;   
    private final int[][] owners;   
    private final int[] scores = new int[2];
    private int currentPlayer = 0; 
    private final int n;

    /** Create a fresh board of size n x n. */
    public Board(int size) {
        n = size;
        values = new int[n][n];
        owners = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                owners[i][j] = -1;
            }
        }
    }

    /** Increment a cell (if <4) and handle scoring/ownership. */
    private void inc(int x, int y) {
        if (values[x][y] < 4) {
            values[x][y]++;
            if (values[x][y] == 4) {
                owners[x][y] = currentPlayer;
                scores[currentPlayer]++;
            }
        }
    }

    /** Process a move: chosen cell + orthogonal neighbours. */
    public void processMove(int x, int y) {
        inc(x, y);
        if (x > 0) inc(x - 1, y);
        if (x < n - 1) inc(x + 1, y);
        if (y > 0) inc(x, y - 1);
        if (y < n - 1) inc(x, y + 1);
    }

    /** True when every cell == 4. */
    public boolean isGameOver() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (values[i][j] < 4) return false;
            }
        }
        return true;
    }

    public int getValue(int x, int y) { return values[x][y]; }
    public int getOwner(int x, int y) { return owners[x][y]; }

    public void nextPlayer() { currentPlayer = 1 - currentPlayer; }
    public int getCurrentPlayer() { return currentPlayer; }
    public int getScore(int player) { return scores[player]; }

    /** 0 = red wins, 1 = blue wins, -1 = tie. */
    public int getWinner() {
        if (scores[0] > scores[1]) return 0;
        if (scores[1] > scores[0]) return 1;
        return -1;
    }
}