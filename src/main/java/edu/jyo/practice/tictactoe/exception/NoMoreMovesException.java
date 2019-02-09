package edu.jyo.practice.tictactoe.exception;

public class NoMoreMovesException extends RuntimeException {
    public NoMoreMovesException() {

    }

    public NoMoreMovesException(String message) {
        super(message);
    }
}
