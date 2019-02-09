package com.interviewbyte.interview.tictactoe.domain;

import com.interviewbyte.interview.tictactoe.exception.InvalidMarkingException;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Constructs a new Board
 * Each cell is of type int
 * 0 -> No marking
 * 1 -> strike with O
 * 2 -> strike with X
 */
public class Board {
    private final BoardCellState[][] matrix;
    private final int dim;
    private GameState state;

    public Board(int dim) {
        this.dim = dim;
        this.matrix = new BoardCellState[dim][dim];
        for (BoardCellState[] row : matrix) {
            Arrays.fill(row, BoardCellState.BLANK);
        }
        state = GameState.PLAYING;
    }

    public void printBoard() {
        for (int i = 0; i < dim; i++) {
            System.out.print("# ");
        }
        System.out.println();
        for (int i = 0; i < dim; i++) {
            String row = Arrays.stream(matrix[i]).map(cell -> cell.toString()).collect(Collectors.joining("|"));
            System.out.println(row);
        }

        System.out.println();
        System.out.println();
    }

    public void addToken(int row, int column, BoardCellState token) {
        if (state != GameState.PLAYING) {
            throw new InvalidMarkingException(String.format("Not playing anymore " +
                    "Game state= %s", state));
        }

        if (row < 0 || column < 0 || row >= dim || column >= dim) {
            throw new InvalidMarkingException(String.format("row and col are out of " +
                    "bounds. max supported %d. Entered row=%d, col=%d", dim, row, column));
        }

        if (matrix[row][column] != BoardCellState.BLANK) {
            throw new InvalidMarkingException(String.format("Position [%d, %d] " +
                    "already marked. Cannot perform again", row, column));
        }
        switch (token) {
            case O:
            case X:
                break;
            default:
                throw new InvalidMarkingException("Invalid Player. Should be either X or O");
        }
        matrix[row][column] = token;
        determineWinner(row, column, token);
    }

    public GameState getState() {
        return state;
    }

    private void determineWinner(int row, int column, BoardCellState player) {
        if (state != GameState.PLAYING) {
            return;
        }
        GameState winner = player == BoardCellState.X ? GameState.X_WON : GameState.O_WON;

        boolean won = true;

        //check row
        won = true;
        for (int i = 0; i < dim; i++) {
            if (matrix[i][column] != player) {
                won = false;
                break;
            }
        }

        if (won) {
            this.state = winner;
            return;
        }

        //Process column
        won = true;
        for (int i = 0; i < dim; i++) {
            if (matrix[row][i] != player) {
                won = false;
                break;
            }
        }

        if (won) {
            this.state = winner;
            return;
        }

        //Process diagonals
        if (onDiagonal(row, column)) {
            won = true;
            for (int i = 0; i < dim; i++) {
                if (matrix[i][i] != player) {
                    won = false;
                    break;
                }
            }

            if (won) {
                this.state = winner;
                return;
            }

            won = true;
            for (int i = 0; i < dim; i++) {
                if (matrix[i][dim - i - 1] != player) {
                    won = false;
                    break;
                }
            }

            if (won) {
                this.state = winner;
                return;
            }
        }

        if (isFull()) {
            this.state = GameState.DRAW;
        }

    }

    private boolean onDiagonal(int row, int column) {
        // for dim=3 following are on diagnol -> 0,0 1,1 2,2  0,2  1,1  2,0
        // for dim=5 following are on diagnol -> 0,0, 1,1 2,2 3,3 4,4   0,4   1,3  2,3   3,0
        if (row == column || row + column == dim - 1) {
            return true;
        }
        return false;
    }

    public boolean isFull() {
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                if (matrix[i][j] == BoardCellState.BLANK) {
                    return false;
                }
            }
        }
        return true;
    }

    public BoardCellState[][] getMatrix() {
        return matrix;
    }

    public int getDim() {
        return dim;
    }

}
