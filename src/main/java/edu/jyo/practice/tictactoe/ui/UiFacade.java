package edu.jyo.practice.tictactoe.ui;

public interface UiFacade {

    void setup(int dimension, String defaultCellDisplay, CellClickHandler cellClickHandler);

    void setMessage(String message);

    void clearMessage();

    void setStatus(String status);

    void markCell(int row, int column, String text);

}
