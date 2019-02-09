package edu.jyo.practice.tictactoe.domain;

public enum BoardCellState {
    BLANK("blank", "-", 0),
    X("X", "X", 2),
    O("O", "O", 1);

    private final String notation;
    private final String description;
    private final int boardValue;

    BoardCellState(String description, String notation, int boardValue) {
        this.description = description;
        this.notation = notation;
        this.boardValue = boardValue;
    }

    @Override
    public String toString() {
        return notation;
    }
}
