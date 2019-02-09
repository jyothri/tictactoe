package edu.jyo.practice.tictactoe.ai;

import edu.jyo.practice.tictactoe.domain.Board;
import edu.jyo.practice.tictactoe.domain.BoardCellState;
import edu.jyo.practice.tictactoe.domain.GameState;
import edu.jyo.practice.tictactoe.exception.InvalidMarkingException;
import edu.jyo.practice.tictactoe.exception.NoMoreMovesException;

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
    public int[] suggestMove() {
        if (board.getState() != GameState.PLAYING) {
            throw new NoMoreMovesException("AI Cannot make a move. We are not playing anymore");
        }

        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                if (matrix[i][j] == BoardCellState.BLANK) {
                    return new int[]{i, j};
                }
            }
        }

        throw new InvalidMarkingException("No more Blank cells");
    }
}
