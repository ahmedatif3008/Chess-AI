package Adversarial;
import Pawntastic.Board;
import Pawntastic.Piece;

public class MinimaxAB {

    public static Action miniMaxSearchAB(AlphaBeta gameState) {
        Piece currentPlayer = gameState.pawnBoard.getCurrentPlayer();
        return maxValueAB(gameState, currentPlayer, gameState.alpha, gameState.beta);
    }

    public static Integer utilityAB(AlphaBeta gameState, Piece player) {
        if (gameState.pawnBoard.checkWin() == player) {
            return 1;
        }
        else if (gameState.pawnBoard.checkWin() != gameState.pawnBoard.getPieceEmpty()) {
            return -1;
        }
        return 0;
    }

    public static boolean gameTerminal(AlphaBeta gameState) {
        return gameState.pawnBoard.checkWin() != gameState.pawnBoard.getPieceEmpty() || gameState.pawnBoard.checkDraw();
    }

    public static Action maxValueAB(AlphaBeta gameState, Piece player, int alpha, int beta) {
        if (gameTerminal(gameState)) {
            return new Action(null, utilityAB(gameState, player));
        }
        int utility = Integer.MIN_VALUE;
        String bestMove = null;

        for (String move : gameState.pawnBoard.getLegalMoves()) {
            Board newboard = new Board(gameState.pawnBoard.deepCopy());
            newboard.playGame(newboard.getCurrentPlayer(), move);

            AlphaBeta newGameState = new AlphaBeta(newboard, alpha, beta);
            newGameState.stateAction.set_first(move);

            Action tuple = minValueAB(newGameState, player, alpha, beta);

            if (tuple.get_second() > utility) {
                utility = tuple.get_second();
                bestMove = move;
            }

            alpha = Math.max(alpha, utility);

            if (tuple.get_second() == 1){
                break;
            }
        }

        return new Action(bestMove, utility);
    }

    public static Action minValueAB(AlphaBeta gameState, Piece player, int alpha, int beta) {
        if (gameTerminal(gameState)) {
            return new Action(null, utilityAB(gameState, player));
        }

        int utility = Integer.MAX_VALUE;
        String bestMove = null;

        for (String move : gameState.pawnBoard.getLegalMoves()) {
            Board newboard = new Board(gameState.pawnBoard.deepCopy());
            newboard.playGame(newboard.getCurrentPlayer(), move);

            AlphaBeta newGameState = new AlphaBeta(newboard,alpha,beta);
            newGameState.stateAction.set_first(move);

            Action tuple = maxValueAB(newGameState, player, alpha, beta);

            if (tuple.get_second() < utility) {
                utility = tuple.get_second();
                bestMove = move;
            }

            beta = Math.min(beta, utility);

            if (tuple.get_second() == -1){
                break;
            }
        }
        return new Action(bestMove, utility);
    }

}