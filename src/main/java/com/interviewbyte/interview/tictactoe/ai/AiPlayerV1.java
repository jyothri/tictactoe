package com.interviewbyte.interview.tictactoe.ai;

import com.interviewbyte.interview.tictactoe.domain.Board;
import com.interviewbyte.interview.tictactoe.domain.BoardCellState;
import com.interviewbyte.interview.tictactoe.domain.GameState;
import com.interviewbyte.interview.tictactoe.exception.NoMoreMovesException;

/**
 * Makes a move. Mark the first possible occurrence.
 */
public class AiPlayerV1 implements AIPlayer {
    private final Board board;
    private final int dim;
    private final BoardCellState[][] matrix;

    public AiPlayerV1(Board board) {
        this.board = board;
        this.dim = board.getDim();
        this.matrix = board.getMatrix();
    }

    /**
     * A very simple move maker
     * Scans cells from left to right, top to bottom
     * If finds a blank cell, will mark it and return
     */
    @Override
    public void makeMove() {
        if (board.getState() != GameState.PLAYING) {
            throw new NoMoreMovesException("AI Cannot make a move. We are not playing anymore");
        }

        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                if (matrix[i][j] == BoardCellState.BLANK) {
                    board.addToken(i, j, BoardCellState.O);
                    return;
                }
            }
        }
    }
}
