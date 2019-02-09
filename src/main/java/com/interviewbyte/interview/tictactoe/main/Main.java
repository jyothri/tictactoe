package com.interviewbyte.interview.tictactoe.main;

import com.interviewbyte.interview.tictactoe.ai.AIPlayer;
import com.interviewbyte.interview.tictactoe.ai.AiPlayerV2;
import com.interviewbyte.interview.tictactoe.domain.Board;
import com.interviewbyte.interview.tictactoe.domain.GameConstants;

public class Main {
    public static void main(String[] args) {
        System.setProperty("java.util.logging.SimpleFormatter.format",
                "%1$tF %1$tT %4$s %2$s %5$s%6$s%n");
        Board board = new Board(GameConstants.dimension);
        AIPlayer aiPlayer = new AiPlayerV2(board);

        GamePlayer consolePlay = new ConsolePlayer(board, aiPlayer);
        consolePlay.play();
    }
}
