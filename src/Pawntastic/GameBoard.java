package Pawntastic;

public interface GameBoard {


    public void gameDisplay();
    public Piece getBlackPlayer();
    public Piece getWhitePlayer();
    public boolean isMoveLegal(Piece player, int rf, int cf, int rt, int ct);
    public boolean makeLegalMove(Piece player, int rf, int cf, int rt, int ct);

    public String randomMover(Piece player);
    public boolean makeMove(int rf, int cf, int rt, int ct);
    public int[] symbolConverter(String symbol);
    public Piece checkWin();
    public void playGame(Piece player, String input);
    public void setCurrentPlayer(Piece Player);
    public Piece getCurrentPlayer();

    public void setLegalMoves(Piece player);


}