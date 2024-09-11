package Adversarial;

import Pawntastic.Board;

public class AlphaBeta extends State {
    int alpha;
    int beta;

    public AlphaBeta(Board pawnBoard){
        super(pawnBoard);
        alpha = Integer.MIN_VALUE;
        beta = Integer.MAX_VALUE;
    }

    public AlphaBeta(Board pawnBoard, int alpha, int beta) {
        super(pawnBoard);
        this.alpha = alpha;
        this.beta = beta;
    }
}