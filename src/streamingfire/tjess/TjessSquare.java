package streamingfire.tjess;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * A square on the chess board.
 */
public class TjessSquare extends JPanel implements MouseListener
{
    private final Location location;
    private final SquareColor squareColor;
    private Piece currentPiece = null;
    Color originalColor;

    private TjessBoard parentBoard;

    public TjessSquare(Location location) {
        this.location = location;
        squareColor = returnSquareColor();

        addMouseListener(this);
    }

    public TjessSquare(Location location, Piece piece){
        this(location);
        this.currentPiece = piece;
    }

    /**
     * Set the parent board of this square.
     * @param board with parent board.
     */
    public void setParent(TjessBoard board){
        this.parentBoard = board;
    }

    /**
     * Return the square color (Dark or White), based on the location of the square.
     * @return the square color based on the location of the square
     */
    private SquareColor returnSquareColor(){
        int rank = location.getRank();
        char file = location.getFile();
        if (file == 'a' || file == 'c' || file == 'e' || file == 'g') {
            return (rank % 2 == 1) ? SquareColor.DARK : SquareColor.WHITE;
        } else {
            return (rank % 2 == 0) ? SquareColor.DARK : SquareColor.WHITE;
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

    public boolean isEmpty(){
        return currentPiece == null;
    }

    /**
     * Change the current piece standing on this square.
     * @param piece, which is the piece which should be placed here.
     */
    public void changeState(Piece piece){
        this.currentPiece = piece;
    }

    public String getReferenceName(){
        return (getCurrentPiece() != null ? (getCurrentPiece().returnImagePath().replace(".png", "") + "_" + getLocFileRank().getFile() + getLocFileRank().getRank()): "Empty." + "_" + getLocFileRank().getFile() + getLocFileRank().getRank());
    }

    @Override
    public String toString(){
        return currentPiece == null ? ("Empty." + getLocFileRank().toString()) : (currentPiece.toString() + "." + getLocFileRank().toString());
    }

    public Location getLocFileRank(){
        return this.location;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        //System.out.println("You pressed the square with " + currentPiece + ", at location: FILE: " + getLocFileRank().getFile() + ", and RANK: " + getLocFileRank().getRank());
        //System.out.println("Current board: " + parentBoard.boardName);
        if (!parentBoard.squarePressed(this)) {
            System.out.println("Player pressed an empty square before selecting a piece.");
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }


    @Override
    public void mouseEntered(MouseEvent e) {
        originalColor = getBackground();
        setBackground(Color.lightGray);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        setBackground(originalColor);
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

        public int getFileAsInt(){
            return this.file - 'a' + 1;
        }

        public int getRank(){
            return this.rank;
        }

        @Override
        public String toString(){
            return file + "" + rank;
        }

    }


}
