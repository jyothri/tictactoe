package com.interviewbyte.interview.tictactoe.ui;

import com.interviewbyte.interview.tictactoe.ai.AIPlayer;
import com.interviewbyte.interview.tictactoe.ai.AiPlayerV2;
import com.interviewbyte.interview.tictactoe.domain.Board;
import com.interviewbyte.interview.tictactoe.domain.BoardCellState;
import com.interviewbyte.interview.tictactoe.domain.GameConstants;
import com.interviewbyte.interview.tictactoe.domain.GameState;
import com.interviewbyte.interview.tictactoe.exception.InvalidMarkingException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.Arrays;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class JfxApplication extends Application {
    private static final Logger logger = Logger.getLogger(JfxApplication.class.getName());

    private static final Semaphore userTurn = new Semaphore(1);
    private static final Semaphore aiTurn = new Semaphore(0);

    TextField statusText;
    TextArea messageArea;
    Stage window;
    AIPlayer aiPlayer;
    Button reset;

    public static void main(String[] args) {

        logger.info("Launching JavaFX client");
        launch(null);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Board board = new Board(GameConstants.dimension);
        aiPlayer = new AiPlayerV2(board);

        window = primaryStage;
        window.setTitle("Tic tac toe");

        VBox mainPanel = new VBox(25);

        messageArea = new TextArea();
        messageArea.setPromptText("messages will enter here");
        messageArea.setEditable(false);
        mainPanel.getChildren().add(messageArea);

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(5, 5, 5, 5));

        Button[][] boardCells = new Button[GameConstants.dimension][GameConstants.dimension];
        for (int i = 0; i < GameConstants.dimension; i++) {
            for (int j = 0; j < GameConstants.dimension; j++) {
                boardCells[i][j] = new Button();
                Button button = boardCells[i][j];
                button.setText(board.getMatrix()[i][j].toString());
                button.setOnAction(new CellClickHandler(button, board, i, j));
                GridPane.setConstraints(button, i, j);
            }
        }

        Arrays.stream(boardCells)
                .forEach(row -> Arrays.stream(row)
                        .forEach(cell -> gridPane.getChildren().add(cell)));

        mainPanel.getChildren().add(gridPane);

        HBox hBox = new HBox();
        statusText = new TextField();
        statusText.setEditable(false);
        reset = new Button();
        reset.setText("reset");
        hBox.getChildren().addAll(statusText, reset);

        mainPanel.getChildren().add(hBox);

        Scene scene = new Scene(mainPanel, 300, 250);
        window.setScene(scene);

        Thread aiPlayerThread = new Thread(new AiPlayerThread(board, aiPlayer, boardCells));
        aiPlayerThread.setDaemon(true);
        aiPlayerThread.start();

        window.show();

        window.setOnCloseRequest(e -> close(e));
    }

    private void close(WindowEvent event) {
        event.consume();
        window.close();
    }

    private class CellClickHandler implements EventHandler<ActionEvent> {
        private final Board board;
        private final int row;
        private final int col;
        private final Button button;

        public CellClickHandler(Button button, Board board, int row, int col) {
            this.board = board;
            this.button = button;
            this.row = row;
            this.col = col;
        }

        @Override
        public void handle(ActionEvent event) {
            try {
                if (userTurn.tryAcquire(1, 1, TimeUnit.SECONDS)) {
                    board.addToken(row, col, BoardCellState.X);
                    messageArea.clear();
                    statusText.setText(board.getState().toString());
                    button.setText(board.getMatrix()[row][col].toString());
                    aiTurn.release();
                }
            } catch (InvalidMarkingException ime) {
                messageArea.setText(ime.getMessage());
                userTurn.release();
            } catch (InterruptedException e) {
                messageArea.setText("Not your turn. Please wait");
            }
        }
    }

    private class AiPlayerThread implements Runnable {

        private Board board;

        private AIPlayer aiPlayer;

        private Button[][] uiCells;

        public AiPlayerThread(Board board, AIPlayer aiPlayer, Button[][] uiCells) {
            this.board = board;
            this.aiPlayer = aiPlayer;
            this.uiCells = uiCells;
        }

        @Override
        public void run() {
            try {
                while (board.getState() == GameState.PLAYING) {
                    if (aiTurn.tryAcquire(1, 3, TimeUnit.HOURS)) {
                        if (board.getState() != GameState.PLAYING) {
                            break;
                        }
                        int[] cell = aiPlayer.suggestMove();
                        int row = cell[0];
                        int col = cell[1];
                        board.addToken(row, col, BoardCellState.O);

                        // Avoid throwing IllegalStateException by running from a non-JavaFX thread.
                        Platform.runLater(
                                () -> {
                                    // Update UI here.
                                    messageArea.clear();
                                    statusText.setText(board.getState().toString());
                                    uiCells[row][col].setText(board.getMatrix()[row][col].toString());
                                }
                        );

                        userTurn.release();
                    }
                }
            } catch (InvalidMarkingException ime) {
                messageArea.setText("AI bug " + ime.getMessage());
            } catch (InterruptedException e) {
                messageArea.setText("Interrupted");
                Thread.interrupted();
            }
        }
    }
}
