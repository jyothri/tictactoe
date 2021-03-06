package edu.jyo.practice.tictactoe.main;

import edu.jyo.practice.tictactoe.ai.AIPlayer;
import edu.jyo.practice.tictactoe.ai.AiPlayerV2;
import edu.jyo.practice.tictactoe.domain.Board;
import edu.jyo.practice.tictactoe.domain.GameConstants;

public class Main {
    public static void main(String[] args) {
        System.setProperty("java.util.logging.SimpleFormatter.format",
                "%1$tF %1$tT %4$s %2$s %5$s%6$s%n");
        Board board = new Board(GameConstants.dimension);
        AIPlayer aiPlayer = new AiPlayerV2(board);

        if (args == null || args.length == 0) {
            GameEngine uiPlayer = new UiGameEngine(board, aiPlayer);
            uiPlayer.play();
            return;
        }

        if (args[0].equals("console")) {
            GameEngine consolePlay = new ConsoleGameEngine(board, aiPlayer);
            consolePlay.play();
        }
    }
}
