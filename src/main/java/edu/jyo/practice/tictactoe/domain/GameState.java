package edu.jyo.practice.tictactoe.domain;

public enum GameState {
    PLAYING("playing"),
    X_WON("X won"),
    O_WON("O won"),
    DRAW("Draw");

    private final String description;

    GameState(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}
