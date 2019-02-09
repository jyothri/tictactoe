package com.interviewbyte.interview.tictactoe.ai;

import com.interviewbyte.interview.tictactoe.domain.Board;
import com.interviewbyte.interview.tictactoe.domain.BoardCellState;
import com.interviewbyte.interview.tictactoe.domain.GameState;
import com.interviewbyte.interview.tictactoe.exception.NoMoreMovesException;

import static com.interviewbyte.interview.tictactoe.domain.BoardCellState.BLANK;

/**
 * Makes a move. Mark the first possible occurrence.
 */
public class AiPlayerV2 implements AIPlayer {
    final Board board;
    final int dim;
    final BoardCellState[][] matrix;

    public AiPlayerV2(Board board) {
        this.board = board;
        this.dim = board.getDim();
        this.matrix = board.getMatrix();
    }

    /**
     * Makes move based on the rules in priority
     * 1. If can win, make that move
     * 2. If can lose, make move to block that
     * 3. Delegate to v1 AI
     */
    @Override
    public void makeMove() {
        if (board.getState() != GameState.PLAYING) {
            throw new NoMoreMovesException("AI Cannot make a move. We are not playing anymore");
        }

        boolean canLoseRow = false;
        boolean canLoseCol = false;
        int row = 0;
        int col = 0;

        for (int i = 0; i < dim; i++) {
            int count = getLineCount(i, true);
            if (count == 2) {
                markInLine(i, true);
                return;
            }

            if (count == -2) {
                canLoseRow = true;
                row = i;
            }

            count = getLineCount(i, false);
            if (count == 2) {
                markInLine(i, false);
                return;
            }

            if (count == -2) {
                canLoseCol = true;
                col = i;
            }
        }

        int forwardDiagCount = getDiagonalCount(true);
        if (forwardDiagCount == dim - 1) {
            markInDiag(true);
            return;
        }

        int backwardDiagCount = getDiagonalCount(false);
        if (backwardDiagCount == dim - 1) {
            markInDiag(false);
            return;
        }

        if (Math.abs(forwardDiagCount) == dim - 1) {
            markInDiag(true);
            return;
        }

        if (Math.abs(backwardDiagCount) == dim - 1) {
            markInDiag(false);
            return;
        }

        if (canLoseRow) {
            markInLine(row, true);
            return;
        }

        if (canLoseCol) {
            markInLine(col, false);
            return;
        }

        int center = dim / 2;
        if (matrix[center][center] == BLANK) {
            board.addToken(center, center, "O");
            return;
        }

        new AiPlayerV1(board).makeMove();
    }

    private void markInDiag(boolean isForward) {
        for (int j = 0; j < dim; j++) {
            int offset = isForward ? j : dim - 1 - j;
            if (matrix[j][offset] == BLANK) {
                board.addToken(j, offset, "O");
                break;
            }
        }
    }

    private void markInLine(int i, boolean isRow) {
        for (int j = 0; j < dim; j++) {
            BoardCellState cell = isRow ? matrix[i][j] : matrix[j][i];
            if (cell == BLANK) {
                if (isRow) {
                    board.addToken(i, j, "O");
                } else {
                    board.addToken(j, i, "O");
                }

                break;
            }
        }
    }

    private int getLineCount(int line, boolean isRow) {
        int count = 0;
        for (int i = 0; i < dim; i++) {
            count += getCount(isRow ? matrix[line][i] : matrix[i][line]);
        }
        return count;
    }

    private int getDiagonalCount(boolean isForward) {
        int count = 0;
        for (int i = 0; i < dim; i++) {
            count += getCount(isForward ? matrix[i][i] : matrix[i][dim - 1 - i]);
        }
        return count;
    }

    private int getCount(BoardCellState i) {
        switch (i) {
            case BLANK:
                return 0;
            case O:
                return 1;
            case X:
                return -1;
        }
        return 0;
    }
}