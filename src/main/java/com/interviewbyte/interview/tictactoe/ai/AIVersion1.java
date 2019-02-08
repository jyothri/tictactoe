package com.interviewbyte.interview.tictactoe.ai;

import com.interviewbyte.interview.tictactoe.domain.Board;
import com.interviewbyte.interview.tictactoe.domain.GameConstants;
import com.interviewbyte.interview.tictactoe.exception.NoMoreMovesException;

/**
 * Makes a move. Always mark with O
 */
public class AIVersion1 {
    final Board board;
    final int dim;
    final int[][] matrix;

    public AIVersion1(Board board) {
        this.board = board;
        this.dim = board.getDim();
        this.matrix = board.getMatrix();
    }

    public void makeMove() {
        if (board.isFull()) {
            throw new NoMoreMovesException("Board is Full. AI Cannot make a move");
        }
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                if (matrix[i][j] == GameConstants.BLANK) {
                    board.addToken(i + 1, j + 1, "O");
                }
            }
        }
    }
}
