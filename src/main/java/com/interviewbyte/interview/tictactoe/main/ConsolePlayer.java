package com.interviewbyte.interview.tictactoe.main;

import com.interviewbyte.interview.tictactoe.ai.AIPlayer;
import com.interviewbyte.interview.tictactoe.domain.Board;
import com.interviewbyte.interview.tictactoe.domain.BoardCellState;
import com.interviewbyte.interview.tictactoe.domain.GameState;
import com.interviewbyte.interview.tictactoe.exception.InvalidMarkingException;

import java.util.InputMismatchException;
import java.util.Scanner;

public class ConsolePlayer implements GamePlayer {

    private final Board board;
    private final AIPlayer aiPlayer;

    public ConsolePlayer(Board board, AIPlayer aiPlayer) {
        this.board = board;
        this.aiPlayer = aiPlayer;
    }

    @Override
    public void play() {
        try (Scanner in = new Scanner(System.in)) {
            while (true) {          //Game loop to alternate between players

                System.out.println("Your move. Enter row col");
                while (true) {      // User loop till correct move is made
                    try {
                        int row = in.nextInt();
                        int col = in.nextInt();
                        board.addToken(row - 1, col - 1, BoardCellState.X);
                        board.printBoard();
                        break;
                    } catch (InvalidMarkingException | InputMismatchException | ArrayIndexOutOfBoundsException ime) {
                        System.out.println(ime.getMessage() + " Try again.");
                    }
                }
                if (!hasMoreMoves(board)) {
                    break;
                }

                System.out.println("System move.");
                int[] move = aiPlayer.suggestMove();
                board.addToken(move[0], move[1], BoardCellState.O);
                board.printBoard();
                if (!hasMoreMoves(board)) {
                    break;
                }
            }
        }
    }

    private boolean hasMoreMoves(Board board) {
        if (board.getState() != GameState.PLAYING) {
            System.out.println("Gamer Over. " + board.getState());
            return false;
        }
        return true;
    }
}
