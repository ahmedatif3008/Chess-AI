package Adversarial;
import Pawntastic.Board;
import Pawntastic.Piece;

public class HeuristicMinimaxAB {

    public static Action heuristicMinimaxAB(AlphaBeta gameState, int depth) {
        Piece currentPlayer = gameState.pawnBoard.getCurrentPlayer();
        return maxValueAB(gameState, currentPlayer, gameState.alpha, gameState.beta, depth);
    }

    public static int heuristic(AlphaBeta gameState, Piece player){
        int currentPlayerPieceTravel = gameState.pawnBoard.countPieceTravel(gameState.pawnBoard.getCurrentPlayer());
        int opponentPlayerPieceTravel = gameState.pawnBoard.countPieceTravel(gameState.pawnBoard.getOpponent(gameState.pawnBoard.getCurrentPlayer()));

        if(player == gameState.pawnBoard.getCurrentPlayer()) {
            if (gameState.pawnBoard.checkWin() == gameState.pawnBoard.getCurrentPlayer()){
                currentPlayerPieceTravel *= 2;
            }
            return currentPlayerPieceTravel;
        }
        else {
            if (gameState.pawnBoard.checkWin() == gameState.pawnBoard.getOpponent(gameState.pawnBoard.getCurrentPlayer())){
                opponentPlayerPieceTravel *= 2;
            }
            return opponentPlayerPieceTravel;
        }
    }

    public static Integer utilityHAB(AlphaBeta gameState, Piece player, int depth) {
        if (gameState.pawnBoard.checkWin() == player) {
            return 1;
        }
        else if (gameState.pawnBoard.checkWin() != gameState.pawnBoard.getPieceEmpty()) {
            return -1;
        }
        if (depth == 0) {
            return heuristic(gameState, player);
        }
        return 0;
    }

    public static boolean gameTerminal(AlphaBeta gameState) {
        return gameState.pawnBoard.checkWin() != gameState.pawnBoard.getPieceEmpty() || gameState.pawnBoard.checkDraw();
    }

    public static Action maxValueAB(AlphaBeta gameState, Piece player, int alpha, int beta, int depthLimit) {
        if (gameTerminal(gameState) || depthLimit == 0) {
            return new Action(null, utilityHAB(gameState, player, depthLimit));
        }

        int utility = Integer.MIN_VALUE;
        String bestMove = null;

        for (String move : gameState.pawnBoard.getLegalMoves()) {
            Board newboard = new Board(gameState.pawnBoard.deepCopy());
            newboard.playGame(newboard.getCurrentPlayer(), move);

            AlphaBeta newGameState = new AlphaBeta(newboard, alpha, beta);
            newGameState.stateAction.set_first(move);
            Action tuple = minValueAB(newGameState, player, alpha, beta, depthLimit - 1);

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

    public static Action minValueAB(AlphaBeta gameState, Piece player, int alpha, int beta, int depthLimit) {
        if (gameTerminal(gameState) || depthLimit == 0) {
            return new Action(null, heuristic(gameState, player));
        }

        int utility = Integer.MAX_VALUE;
        String bestMove = null;

        for (String move : gameState.pawnBoard.getLegalMoves()) {
            Board newboard = new Board(gameState.pawnBoard.deepCopy());
            newboard.playGame(newboard.getCurrentPlayer(), move);

            AlphaBeta newGameState = new AlphaBeta(newboard, alpha, beta);
            newGameState.stateAction.set_first(move);
            Action tuple = maxValueAB(newGameState, player, alpha, beta, depthLimit - 1);

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
