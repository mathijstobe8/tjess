package streamingfire.tjess;

/**
 A chess piece. Can either be a Rook, Knight, Bishop, King, Queen or Pawn.
 @author Mathijs Tobé
 */
public class Piece {

    private Pieces piece;
    private Player.Color color;

    public Piece(Pieces piece, Player.Color color){
        this.piece = piece;
        this.color = color;
    }

    public Pieces getPiece() {
        return piece;
    }

    public Player.Color getColor() {
        return color;
    }

    @Override
    public String toString(){
        return piece.toString();
    }

    /**
     * All the available pieces for the chess game.
     */
    public enum Pieces {
        ROOK,
        KNIGHT,
        BISHOP,
        KING,
        QUEEN,
        PAWN
    }
}
