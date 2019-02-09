package edu.jyo.practice.tictactoe.exception;

public class InvalidMarkingException extends RuntimeException {

    public InvalidMarkingException() {

    }

    public InvalidMarkingException(String s) {
        super(s);
    }
}
