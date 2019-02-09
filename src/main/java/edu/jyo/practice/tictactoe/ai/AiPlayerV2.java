package edu.jyo.practice.tictactoe.ai;

import edu.jyo.practice.tictactoe.domain.Board;
import edu.jyo.practice.tictactoe.domain.BoardCellState;
import edu.jyo.practice.tictactoe.domain.GameState;
import edu.jyo.practice.tictactoe.exception.NoMoreMovesException;

import static edu.jyo.practice.tictactoe.domain.BoardCellState.BLANK;

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
    public int[] suggestMove() {
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
                return markInLine(i, true);
            }

            if (count == -2) {
                canLoseRow = true;
                row = i;
            }

            count = getLineCount(i, false);
            if (count == 2) {
                return markInLine(i, false);
            }

            if (count == -2) {
                canLoseCol = true;
                col = i;
            }
        }

        int forwardDiagCount = getDiagonalCount(true);
        if (forwardDiagCount == dim - 1) {
            return markInDiag(true);
        }

        int backwardDiagCount = getDiagonalCount(false);
        if (backwardDiagCount == dim - 1) {
            return markInDiag(false);
        }

        if (Math.abs(forwardDiagCount) == dim - 1) {
            return markInDiag(true);
        }

        if (Math.abs(backwardDiagCount) == dim - 1) {
            return markInDiag(false);
        }

        if (canLoseRow) {
            return markInLine(row, true);
        }

        if (canLoseCol) {
            return markInLine(col, false);
        }

        int center = dim / 2;
        if (matrix[center][center] == BLANK) {
            return new int[]{center, center};
        }

        return new AiPlayerV1(board).suggestMove();
    }

    private int[] markInDiag(boolean isForward) {
        for (int j = 0; j < dim; j++) {
            int offset = isForward ? j : dim - 1 - j;
            if (matrix[j][offset] == BLANK) {
                return new int[]{j, offset};
            }
        }
        throw new IllegalStateException("Should never happen");
    }

    private int[] markInLine(int i, boolean isRow) {
        for (int j = 0; j < dim; j++) {
            BoardCellState cell = isRow ? matrix[i][j] : matrix[j][i];
            if (cell == BLANK) {
                if (isRow) {
                    return new int[]{i, j};
                } else {
                    return new int[]{j, i};
                }
            }
        }
        throw new IllegalStateException("Should never happen");
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
