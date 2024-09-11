package Pawntastic;
import java.util.*;


public class Board implements GameBoard {
    int boardSize;

    Piece playerWhite;
    Piece playerBlack;
    Piece pieceEmpty;
    Piece currentPlayer;

    char userColor;

    ArrayList<String> positionWhite;
    ArrayList<String> positionBlack;
    ArrayList<String> legalMoves;

    Piece[][] board;

    public Board(int n, char userColor) {
        if (n < 4) {
            return;
        }

        this.userColor = userColor;

        this.boardSize = n;
        this.board = new Piece[n][n];

        this.playerWhite = Piece.WHITE;
        this.playerBlack = Piece.BLACK;

        this.pieceEmpty = Piece.EMPTY;
        this.currentPlayer = playerWhite;

        this.positionBlack = new ArrayList<>();
        this.positionWhite = new ArrayList<>();

        this.legalMoves = new ArrayList<>();

        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                if (row == 1) {
                    board[row][col] = playerBlack;
                    positionBlack.add(intConverter(col,row));

                } else if (row == n-2) {
                    board[row][col] = playerWhite;
                    positionWhite.add(intConverter(col,row));
                }
                else {
                    board[row][col] = pieceEmpty;
                }
            }
        }
        setLegalMoves(getWhitePlayer());
    }


    public Board(Board newBoard) {
        this.boardSize = newBoard.boardSize;
        this.board = newBoard.board;
        this.playerWhite = newBoard.playerWhite;
        this.playerBlack = newBoard.playerBlack;
        this.pieceEmpty = newBoard.pieceEmpty;
        this.currentPlayer = newBoard.currentPlayer;
        this.positionBlack = newBoard.positionBlack;
        this.positionWhite = newBoard.positionWhite;
        this.legalMoves = newBoard.legalMoves;

        newBoard.setLegalMoves(getCurrentPlayer());
    }

    public Board deepCopy() {

        // Create a new Board instance
        Board copy = new Board(this.boardSize, userColor);

        // Copy primitive data types
        copy.playerWhite = this.playerWhite;
        copy.playerBlack = this.playerBlack;
        copy.pieceEmpty = this.pieceEmpty;
        copy.currentPlayer = this.currentPlayer;

        // Clone ArrayLists
        copy.positionWhite = new ArrayList<>(this.positionWhite);
        copy.positionBlack = new ArrayList<>(this.positionBlack);
        copy.legalMoves = new ArrayList<>(this.legalMoves);

        // Clone the 2D array
        copy.board = new Piece[this.boardSize][this.boardSize];
        for (int row = 0; row < this.boardSize; row++) {
            System.arraycopy(this.board[row], 0, copy.board[row], 0, this.boardSize);
        }
        return copy;
    }

    // Accessors & Mutators
    @Override
    public Piece getBlackPlayer() {
        return playerBlack;
    }

    @Override
    public Piece getWhitePlayer() {
        return playerWhite;
    }

    public void setCurrentPlayer(Piece Player) {
        currentPlayer = Player;
    }

    public Piece getCurrentPlayer() {
        return currentPlayer;
    }

    public Piece getPieceEmpty() {
        return pieceEmpty;
    }

    public ArrayList<String> getLegalMoves() {
        return legalMoves;
    }

    // Move Functions
    @Override
    public boolean isMoveLegal(Piece player, int rf, int cf, int rt, int ct) {
        int n = boardSize;

        // Out of Bounds: Positions
        if ((rf >= n || rf < 0) || (rt >= n || rt < 0) || (cf >= n || cf < 0) || (ct >= n || ct < 0)) {
            return false;
        }

        if (isPlayerMovingOwnPiece(player, rf, cf)) {
            int direction = (player == playerBlack) ? 1 : -1;
            Piece opponent = (player == playerBlack) ? playerWhite : playerBlack;
            return isValidPawnMove(rf, cf, rt, ct, direction, opponent);
        }

        return false;
    }

    @Override
    public boolean makeLegalMove(Piece player, int rf, int cf, int rt, int ct) {

        if (isMoveLegal(player, rf, cf, rt, ct)) {
            makeMove(rf,cf,rt,ct);

            if (player == playerBlack) {
                positionBlack.remove(intConverter(cf,rf));
                positionBlack.add(intConverter(ct, rt));
                setCurrentPlayer(playerWhite);
                setLegalMoves(currentPlayer);
            }

            else {
                positionWhite.remove(intConverter(cf,rf));
                positionWhite.add(intConverter(ct, rt));
                setCurrentPlayer(playerBlack);
                setLegalMoves(currentPlayer);
            }
            return true;
        }
        else {
            System.out.println("Invalid Move!");
            return false;
        }
    }

    @Override
    public boolean makeMove(int rf, int cf, int rt, int ct) {
        Piece currentPlayer = board[rf][cf];
        board[rt][ct] = currentPlayer;
        board[rf][cf] = pieceEmpty;
        return board[rt][ct] == currentPlayer && board[rf][cf] == pieceEmpty;
    }

    private boolean isPlayerMovingOwnPiece(Piece player, int rf, int cf) {
        return player == board[rf][cf];
    }

    private boolean isValidPawnMove(int rf, int cf, int rt, int ct, int direction, Piece opponent) {
        int moveDistance = Math.abs(rt - rf);
        int safetyNet = rt - rf;

        if (safetyNet < 0 && board[rf][cf] == Piece.BLACK){
            System.out.println("Invalid Move! Trying to move black back!");
            return false;
        }
        if (safetyNet > 0 && board[rf][cf] == Piece.WHITE){
            System.out.println("Invalid Move! Trying to move white back!");
            return false;
        }

        // Move Forward
        if ((moveDistance == 1 || moveDistance == 2) && cf == ct) {
            if (moveDistance == 1 && board[rt][ct] == pieceEmpty) {
                return true;
            }
            else if (moveDistance == 2 && direction == 1 && rf == 1 && board[rt][ct] == pieceEmpty && board[rf + (direction)][ct] == pieceEmpty) {
                return true;
            }
            else return (moveDistance == 2 && direction == -1 && rf == boardSize - 2 && board[rt][ct] == pieceEmpty && board[rf + (direction)][ct] == pieceEmpty);
        }
        else if (moveDistance == 1 && (ct == cf - 1 || ct == cf + 1)) {
            return (board[rt][ct] == opponent);
        }

        System.out.println("Invalid Move! ");
        return false;
    }

    @Override
    public void setLegalMoves(Piece player) {
        legalMoves.clear();

        int n = (player == playerWhite) ? -1: 1;
        ArrayList<String> posPlayer = (player == playerWhite) ? positionWhite: positionBlack;

        for (String fromSym: posPlayer) {
            int[] positionFrom = symbolConverterFrom(fromSym);
            ArrayList<int[]> positionsTo = new ArrayList<>();

            int[] move1 = {positionFrom[0]+(n), positionFrom[1]};
            int[] move2 = {positionFrom[0]+(2*n), positionFrom[1]};
            int[] move3 = {positionFrom[0]+n, positionFrom[1]-1};
            int[] move4 = {positionFrom[0]+n, positionFrom[1]+1};

            positionsTo.add(move1);
            positionsTo.add(move2);
            positionsTo.add(move3);
            positionsTo.add(move4);

            for (int[] toMoves: positionsTo) {
                if (isMoveLegal(player, positionFrom[0], positionFrom[1], toMoves[0], toMoves[1])) {
                    String fromToMove = intConverter(positionFrom[1], positionFrom[0]) + "-" + intConverter(toMoves[1], toMoves[0]);
                    legalMoves.add(fromToMove);
                }}
        }
    }

    // Random Legal Move Generator
    @Override
    public String randomMover(Piece player) {
        if (!legalMoves.isEmpty()) {
            Random rand = new Random();
            return legalMoves.get(rand.nextInt(legalMoves.size()));
        }
        else {
            return "None";
        }
    }

    public boolean checkDraw() {
        return legalMoves.isEmpty();
    }

    @Override
    public Piece checkWin() {
        for (int i = 0; i < boardSize; i++){
            if (board[0][i] == playerWhite){
                return playerWhite;
            }
            if (board[boardSize -1][i] == playerBlack){
                return playerBlack;
            }
        }
        return pieceEmpty;
    }

    public boolean isGameOver() {
        for (int i = 0; i < boardSize; i++){
            if (board[0][i] == playerWhite){
                return true;
            }
            if (board[boardSize -1][i] == playerBlack){
                return true;
            }
        }
        return false;
    }

    @Override
    public void gameDisplay() {
        System.out.println();
        Piece [][] gameBoard = this.board;

        for (int counterC = 0; counterC < boardSize; counterC++) {
            char ch = (char) ('a' + counterC);
            System.out.print("  "+ ch);
        }

        System.out.println();
        int counterR = boardSize;

        for (Piece[] pieces : gameBoard) {
            System.out.print(counterR + " ");

            for (Piece piece : pieces) {
                if (piece == Piece.BLACK) {
                    System.out.print("[B]");
                }
                else if (piece == Piece.WHITE) {
                    System.out.print("[W]");
                }
                else {
                    System.out.print("[ ]");
                }
            }

            System.out.print(" " + counterR);
            counterR--;
            System.out.println();
        }

        for (int counterC = 0; counterC < boardSize; counterC++) {
            char ch = (char) ('a' + counterC);
            System.out.print("  "+ ch);
        }
    }

    public int[] symbolConverter(String input){

        String[] moves = input.split("-");

        try{
            int cf = moves[0].charAt(0) - '0';
            int rf = moves[0].charAt(1) - '0';

            int ct = moves[1].charAt(0) - '0';
            int rt = moves[1].charAt(1) - '0';


            cf = cf - 49;
            rf = boardSize - rf;

            ct = ct - 49;
            rt = boardSize - rt;

            return new int[]{rf, cf, rt, ct};
        }
        catch (Exception e){
            System.out.println("Wrong input format");
        }

        return null;
    }
    private String intConverter(int cf, int rf){
        char fromFileChar = (char) ('a' + cf);
        char fromRankChar = (char) ('1' + (boardSize - rf - 1));

        return "" + fromFileChar +  fromRankChar;
    }
    private int[] symbolConverterFrom(String input) {

        int cf = input.charAt(0) - '0';
        int rf = input.charAt(1) - '0';

        cf = cf - 49;
        rf = boardSize - rf;

        return new int[]{rf, cf};
    }

    public int countPieces(Piece player) {
        if (player == playerWhite) {
            if (positionWhite.isEmpty()) {
                return 0;
            }
            else return positionWhite.size();
        }
        else {
            if (positionBlack.isEmpty()) {
                return 0;
            }
            else return positionBlack.size();
        }
    }

    public int countPieceTravel(Piece player){
        int pieceTravel = 0;

        if(player == playerWhite){
            for(String s: positionWhite){
                int[] rowCol = symbolConverterFrom(s);
                pieceTravel += (int) Math.pow((boardSize - rowCol[0] - 1), 3);
            }
        }

        if(player == playerBlack){
            for(String s: positionBlack){
                int[] rowCol = symbolConverterFrom(s);
                pieceTravel += (int) Math.pow(rowCol[0], 3);
            }
        }
        return pieceTravel;
    }

    public Piece getOpponent(Piece player) {
        return (currentPlayer == playerWhite) ? playerBlack: playerWhite;
    }

    @Override
    public void playGame(Piece player, String input){

        int[] moves = symbolConverter(input);
        int rf = moves[0]; int cf = moves[1];
        int rt = moves[2]; int ct = moves[3];

        if (player == playerWhite){
            makeLegalMove(playerWhite, rf, cf, rt, ct);
        }

        else {
            makeLegalMove(playerBlack, rf, cf, rt, ct);
        }

    }

    public boolean isUserMove(){
        return ((currentPlayer == Piece.WHITE && userColor == 'w') || (currentPlayer == Piece.BLACK && userColor == 'b'));
    }

    public Piece getUser() {
        if (userColor == 'w') {
            return Piece.WHITE;
        }
        if (userColor == 'b') {
            return Piece.BLACK;
        }
        return null;
    }

    public Piece getAI(){
        if(userColor == 'w'){
            return Piece.BLACK;
        }
        if(userColor == 'b'){
            return Piece.WHITE;
        }
        return null;
    }

    public String playerToString(Piece player) {
        if (player == playerWhite) return "White Player";
        else if (player == playerBlack) return "Black Player";
        else return "Empty Piece";
    }
}

