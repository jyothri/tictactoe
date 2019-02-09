package edu.jyo.practice.tictactoe.domain;

import edu.jyo.practice.tictactoe.ai.AIPlayer;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class TurnCoOrdinator {
    private final Semaphore userTurn;
    private final Semaphore aiTurn;

    public TurnCoOrdinator(AIPlayer aiPlayer) {
        this.userTurn = new Semaphore(1);
        this.aiTurn = new Semaphore(0);
    }


    public boolean canDoUserEvent() {
        try {
            if (userTurn.tryAcquire(1, 1, TimeUnit.SECONDS)) {
                return true;
            }
        } catch (InterruptedException e) {
            return false;
        }
        return false;
    }

    public void completeUserEvent() {
        aiTurn.release();
    }

    public boolean canDoAiEvent() {
        try {
            if (aiTurn.tryAcquire(1, 1, TimeUnit.HOURS)) {
                return true;
            }
        } catch (InterruptedException e) {
            return false;
        }
        return false;
    }

    public void completeAiEvent() {
        userTurn.release();
    }

    public void retryUserEvent() {
        userTurn.release();
    }

    public void retryAiEvent() {
        aiTurn.release();
    }
}
