package Adversarial;
import Pawntastic.Board;
import Pawntastic.Piece;

public class Minimax {

    public static Action miniMaxSearch(State gameState) {
        Piece currentPlayer = gameState.pawnBoard.getCurrentPlayer();
        return maxValue(gameState, currentPlayer);
    }

    public static Integer utility(State gameState, Piece player) {
        if (gameState.pawnBoard.checkWin() == player) {
            return 1;
        }
        else if (gameState.pawnBoard.checkWin() != gameState.pawnBoard.getPieceEmpty()) {
            return -1;
        }
        return 0;
    }

    public static boolean gameTerminal(State gameState) {
        return gameState.pawnBoard.checkWin() != gameState.pawnBoard.getPieceEmpty() || gameState.pawnBoard.checkDraw();
    }

    public static Action maxValue(State gameState, Piece player) {
        if (gameTerminal(gameState)) {
            return new Action(null, utility(gameState, player));
        }

        int utility = Integer.MIN_VALUE;
        String bestMove = null;

        for (String move : gameState.pawnBoard.getLegalMoves()) {
            Board newboard = new Board(gameState.pawnBoard.deepCopy());
            newboard.playGame(newboard.getCurrentPlayer(), move);

            State newGameState = new State(newboard);
            newGameState.stateAction.set_first(move);

            Action tuple = minValue(newGameState, player);

            if (tuple.get_second() > utility) {
                utility = tuple.get_second();
                bestMove = move;
            }
        }

        return new Action(bestMove, utility);
    }

    public static Action minValue(State gameState, Piece player) {
        if (gameTerminal(gameState)) {
            return new Action(null, utility(gameState, player));
        }

        int utility = Integer.MAX_VALUE;
        String bestMove = null;

        for (String move : gameState.pawnBoard.getLegalMoves()) {
            Board newboard = new Board(gameState.pawnBoard.deepCopy());
            newboard.playGame(newboard.getCurrentPlayer(), move);

            State newGameState = new State(newboard);
            newGameState.stateAction.set_first(move);

            Action tuple = maxValue(newGameState, player);

            if (tuple.get_second() < utility) {
                utility = tuple.get_second();
                bestMove = move;
            }
        }
        return new Action(bestMove, utility);
    }
}