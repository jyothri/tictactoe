package com.interviewbyte.interview.tictactoe.ui;

import com.interviewbyte.interview.tictactoe.domain.Board;
import com.interviewbyte.interview.tictactoe.domain.BoardCellState;
import com.interviewbyte.interview.tictactoe.domain.GameConstants;
import com.interviewbyte.interview.tictactoe.exception.InvalidMarkingException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.Arrays;
import java.util.logging.Logger;

public class JfxApplication extends Application {
    private static final Logger logger = Logger.getLogger(JfxApplication.class.getName());

    TextField statusText;
    TextArea messageArea;
    Stage window;
    private EventHandler<ActionEvent> sendButtonEventHandler;

    public static void main(String[] args) {
        System.setProperty("java.util.logging.SimpleFormatter.format",
                "%1$tF %1$tT %4$s %2$s %5$s%6$s%n");
        logger.info("Launching JavaFX client");

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Board board = new Board(GameConstants.dimension);

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
                boardCells[i][j].setText(board.getMatrix()[i][j].toString());
                boardCells[i][j].setOnAction(new CellClickHandler(board, i, j));
                GridPane.setConstraints(boardCells[i][j], i, j);
            }
        }

        Arrays.stream(boardCells)
                .forEach(row -> Arrays.stream(row)
                        .forEach(cell -> gridPane.getChildren().add(cell)));

        mainPanel.getChildren().add(gridPane);

        statusText = new TextField();
        statusText.setEditable(false);
        mainPanel.getChildren().add(statusText);

        Scene scene = new Scene(mainPanel, 300, 250);
        window.setScene(scene);
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

        public CellClickHandler(Board board, int row, int col) {
            this.board = board;
            this.row = row;
            this.col = col;
        }

        @Override
        public void handle(ActionEvent event) {
            try {
                board.addToken(row, col, BoardCellState.O);
                messageArea.clear();
                statusText.setText(board.getState().toString());
            } catch (InvalidMarkingException ime) {
                messageArea.setText(ime.getMessage());
            }
        }
    }
}
