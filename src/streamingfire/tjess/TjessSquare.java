package streamingfire.tjess;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * A square on the chess board.
 */
public class TjessSquare extends Rectangle
{

    private final Location location;
    private final SquareColor squareColor;
    private static final int DIM = 100;
    private Piece currentPiece = null;

    public TjessSquare(Location location) {
        this.location = location;
        squareColor = returnSquareColor();


    }

    public TjessSquare(Location location, Piece piece){
        this(location);
        this.currentPiece = piece;
    }

    /**
     * Return the square color (Dark or White), based on the location of the square.
     * @return the square color based on the location of the square
     */
    private SquareColor returnSquareColor(){
        int rank = location.getRank();
        char file = location.getFile();
        if (file == 'a' || file == 'c' || file == 'e' || file == 'g') {
            return (rank % 2 == 0) ? SquareColor.DARK : SquareColor.WHITE;
        } else {
            return (rank % 2 == 1) ? SquareColor.DARK : SquareColor.WHITE;
        }
    }

    /**
     * Returns the color of this square.
     * @return the square color of this square
     */
    public SquareColor getSquareColor(){
        return squareColor;
    }

    /**
     * Returns the current piece standing on this square.
     * @return the current piece standing on this square, can be null.
     */
    public Piece getCurrentPiece(){
        return currentPiece;
    }

    /**
     * Change the current piece standing on this square.
     * @param piece, which is the piece which should be placed here.
     * @return if this action was valid.
     */
    public boolean changeState(Piece piece){
        return false;
    }

    public String getReferenceName(){
        return getCurrentPiece().returnImagePath().replace(".png", "") + "_" + getLocFileRank().getFile() + getLocFileRank().getRank();
    }

    @Override
    public String toString(){
        return currentPiece == null ? ("Empty.") : (currentPiece.toString() + ", " + currentPiece.getColor().toString());
    }

    public Location getLocFileRank(){
        return this.location;
    }

    public enum SquareColor {
        WHITE,
        DARK
    }

    @Deprecated
    public static class Location {
        private final char file; // X place on the board
        private final int rank; // Y place on the board

        public Location(char file, int rank){
            this.file = file;
            this.rank = rank;
        }

        public char getFile(){
            return this.file;
        }

        public int getRank(){
            return this.rank;
        }

    }


}
