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
    private int[][] matrix;
    private int dim;

    public Board(int dim) {
        this.dim = dim;
        this.matrix = new int[dim][dim];
    }

    public void printBoard() {
        for (int i = 0; i < dim; i++) {
            System.out.print("# ");
        }
        System.out.println();
        for (int i = 0; i < dim; i++) {
            String row = Arrays.stream(matrix[i]).boxed().map(cell -> GameConstants.mapping[cell]).collect(Collectors.joining("|"));
            System.out.println(row);
        }

        System.out.println();
        System.out.println();
    }

    public void addToken(int x, int y, String token) {
        if (matrix[x - 1][y - 1] != GameConstants.BLANK) {
            throw new InvalidMarkingException("Index Already marked. Cannot perform again");
        }
        switch (token) {
            case "X":
                matrix[x - 1][y - 1] = GameConstants.X;
                break;
            case "O":
                matrix[x - 1][y - 1] = GameConstants.O;
                break;
            default:
                throw new InvalidMarkingException("Invalid Player. Should be either X or O");
        }
    }

    public boolean isFull() {
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                if (matrix[i][i] != GameConstants.BLANK) {
                    return true;
                }
            }
        }
        return false;
    }

    public int[][] getMatrix() {
        return matrix;
    }

    public void setMatrix(int[][] matrix) {
        this.matrix = matrix;
    }

    public int getDim() {
        return dim;
    }

    public void setDim(int dim) {
        this.dim = dim;
    }
}
