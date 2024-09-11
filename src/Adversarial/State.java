package Adversarial;


import Pawntastic.Board;

public class State {
    Board pawnBoard;
    Action stateAction;

    public State(Board pawnBoard) {
        this.pawnBoard = pawnBoard;
        this.stateAction = new Action(null, null);
    }

    public Action getMoveTuple() {
        return stateAction;
    }
}
