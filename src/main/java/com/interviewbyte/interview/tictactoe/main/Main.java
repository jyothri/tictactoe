package com.interviewbyte.interview.tictactoe.main;

import com.interviewbyte.interview.tictactoe.ai.AIPlayer;
import com.interviewbyte.interview.tictactoe.ai.AiPlayerV2;
import com.interviewbyte.interview.tictactoe.domain.Board;
import com.interviewbyte.interview.tictactoe.domain.GameConstants;
import com.interviewbyte.interview.tictactoe.domain.GameState;
import com.interviewbyte.interview.tictactoe.exception.InvalidMarkingException;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Board board = new Board(GameConstants.dimension);
        AIPlayer aiPlayer = new AiPlayerV2(board);

        new Main().play(board, aiPlayer);
    }

    private void play(Board board, AIPlayer aiPlayer) {
        try (Scanner in = new Scanner(System.in)) {
            while (true) {
                System.out.println("Your move. Enter row col");
                boolean playerMove = true;
                while (playerMove) {
                    try {
                        int row = in.nextInt();
                        int col = in.nextInt();
                        board.addToken(row - 1, col - 1, "X");
                        board.printBoard();
                        playerMove = false;
                    } catch (InvalidMarkingException | InputMismatchException | ArrayIndexOutOfBoundsException ime) {
                        System.out.println(ime.getMessage() + " Try again.");
                    }
                }

                if (hasMoreMoves(board))
                    break;

                System.out.println("System move.");
                aiPlayer.makeMove();
                board.printBoard();

                if (hasMoreMoves(board))
                    break;
            }
        }
    }

    private boolean hasMoreMoves(Board board) {
        if (board.getState() != GameState.PLAYING) {
            System.out.println("Gamer Over. " + board.getState());
            return true;
        }
        return false;
    }
}
