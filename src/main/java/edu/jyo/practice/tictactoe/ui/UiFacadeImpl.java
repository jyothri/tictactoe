package edu.jyo.practice.tictactoe.ui;

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

import java.util.logging.Logger;

public class UiFacadeImpl extends Application implements UiFacade {
    private static final Logger logger = Logger.getLogger(UiFacadeImpl.class.getName());
    private static int dimension;
    private static String defaultCellDisplay;
    private static CellClickHandler cellClickHandler;
    private static TextField statusText;
    private static TextArea messageArea;
    private static Stage window;
    private static Button reset;
    private static Button[][] boardCells;
    private static GridPane gridPane;
    private static EventHandler<ActionEvent> resetHandler;

    @Override
    public void setup(int dimension, String defaultCellDisplay, CellClickHandler cellClickHandler) {
        this.dimension = dimension;
        this.defaultCellDisplay = defaultCellDisplay;
        this.cellClickHandler = cellClickHandler;
        this.resetHandler = null;
        launch(null);
    }

    @Override
    public void setMessage(String message) {
        Platform.runLater(() -> messageArea.setText(message));
    }

    @Override
    public void clearMessage() {
        Platform.runLater(() -> messageArea.clear());
    }

    @Override
    public void setStatus(String status) {
        Platform.runLater(() -> statusText.setText(status));
    }

    @Override
    public void markCell(int row, int column, String text) {
        Platform.runLater(() -> boardCells[row][column].setText(text));
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        logger.fine("Setting up primaryStage");
        window = primaryStage;
        window.setTitle("Tic tac toe");

        VBox mainPanel = new VBox(25);

        messageArea = new TextArea();
        messageArea.setPromptText("messages will enter here");
        messageArea.setEditable(false);
        mainPanel.getChildren().add(messageArea);

        logger.fine("Setting up board");
        gridPane = new GridPane();
        gridPane.setPadding(new Insets(5, 5, 5, 5));
        boardCells = new Button[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                boardCells[i][j] = new Button();
                setupCell(boardCells[i][j], i, j);
                gridPane.getChildren().add(boardCells[i][j]);
            }
        }
        mainPanel.getChildren().add(gridPane);

        HBox hBox = new HBox();
        statusText = new TextField();
        statusText.setEditable(false);
        reset = new Button();
        reset.setText("reset");
        reset.setOnAction(resetHandler);
        hBox.getChildren().addAll(statusText, reset);
        mainPanel.getChildren().add(hBox);

        Scene scene = new Scene(mainPanel, 300, 250);
        window.setScene(scene);
        window.show();
        window.setOnCloseRequest(e -> close(e));
    }

    private void setupCell(Button button, final int i, final int j) {
        button.setText(defaultCellDisplay);
        button.setOnAction((event) -> cellClickHandler.handleEvent(i, j));
        GridPane.setConstraints(button, i, j);
    }

    private void close(WindowEvent event) {
        event.consume();
        window.close();
    }
}
