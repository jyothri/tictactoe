package com.interviewbyte.interview.tictactoe.main;

import com.interviewbyte.interview.tictactoe.domain.Board;
import com.interviewbyte.interview.tictactoe.domain.GameConstants;

public class Main {
    public static void main(String[] args) {
        Board board = new Board(GameConstants.dimension);
        board.printBoard();
        board.addToken(1, 2, "X");
        board.printBoard();
    }
}
