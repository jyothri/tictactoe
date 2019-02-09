package edu.jyo.practice.tictactoe.main;

import edu.jyo.practice.tictactoe.ai.AIPlayer;
import edu.jyo.practice.tictactoe.domain.Board;
import edu.jyo.practice.tictactoe.domain.BoardCellState;
import edu.jyo.practice.tictactoe.domain.GameConstants;
import edu.jyo.practice.tictactoe.domain.GameState;
import edu.jyo.practice.tictactoe.domain.TurnCoOrdinator;
import edu.jyo.practice.tictactoe.exception.InvalidMarkingException;
import edu.jyo.practice.tictactoe.ui.CellClickHandler;
import edu.jyo.practice.tictactoe.ui.UiFacade;
import edu.jyo.practice.tictactoe.ui.UiFacadeImpl;

import java.util.function.Function;
import java.util.logging.Logger;

public class UiGameEngine implements GameEngine {

    private static final Logger logger = Logger.getLogger(UiGameEngine.class.getName());

    private final Board board;
    private final AIPlayer aiPlayer;

    private final TurnCoOrdinator turnCoOrdinator;

    public UiGameEngine(Board board, AIPlayer aiPlayer) {
        this.board = board;
        this.aiPlayer = aiPlayer;
        turnCoOrdinator = new TurnCoOrdinator(aiPlayer);
    }

    @Override
    public void play() {
        logger.info("Launching UI application");
        UiFacade facade = new UiFacadeImpl();
        Function<UiFacade, CellClickHandler> uiFacadeCellClickHandlerFunction = getUiCellClickHandlerFunction();

        Thread aiThread = new Thread(() -> {
            while (board.getState() == GameState.PLAYING) {
                if (!turnCoOrdinator.canDoAiEvent()) {
                    facade.setMessage("Waiting for your turn..");
                    continue;
                }
                if (board.getState() != GameState.PLAYING) {
                    break;
                }
                try {
                    int[] cell = aiPlayer.suggestMove();
                    int row = cell[0];
                    int col = cell[1];
                    logger.info(String.format("AI move on row=%d and col=%d", row, col));
                    board.addToken(row, col, BoardCellState.O);
                    facade.clearMessage();
                    facade.markCell(row, col, BoardCellState.O.toString());
                    turnCoOrdinator.completeAiEvent();
                } catch (InvalidMarkingException ime) {
                    facade.setMessage(ime.getMessage());
                    turnCoOrdinator.retryAiEvent();
                }
            }
            logger.info("Game over");
            facade.clearMessage();
            facade.setStatus(board.getState().toString());
        });
        aiThread.setDaemon(true);
        aiThread.start();

        facade.setup(GameConstants.dimension,
                BoardCellState.BLANK.toString(),
                uiFacadeCellClickHandlerFunction.apply(facade));
    }

    private Function<UiFacade, CellClickHandler> getUiCellClickHandlerFunction() {
        return facade -> (CellClickHandler) (row, col) -> {
            logger.info(String.format("Clicked on row=%d and col=%d", row, col));
            if (!turnCoOrdinator.canDoUserEvent()) {
                facade.setMessage("Not your turn. Please wait");
                return;
            }
            try {
                board.addToken(row, col, BoardCellState.X);
                facade.clearMessage();
                facade.markCell(row, col, BoardCellState.X.toString());
                turnCoOrdinator.completeUserEvent();
            } catch (InvalidMarkingException ime) {
                facade.setMessage(ime.getMessage());
                turnCoOrdinator.retryUserEvent();
            }
        };
    }

}
