package streamingfire.tjess;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
The board of the game.
 @author Mathijs Tobé
 */
public class TjessBoard extends JLayeredPane {

    private char[] files = new char[] { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
    private TjessSquare[][] starting_board = new TjessSquare[8][8];
    private TjessSquare[][] squaresCurrentBoard = new TjessSquare[8][8];

    public TjessBoard(){
        setBounds(0, 0, 400, 400);
        setStartingBoard();

        squaresCurrentBoard = starting_board;
    }

    /*
    Board functionality, so only in memory, board will be updated later.
     */

    /**
     * Get the current board state of the game.
     * @return the current board with squares
     */
    public TjessSquare[][] getBoard(){
        return squaresCurrentBoard;
    }

    public void setStartingBoard(){
        for(int file = 1; file <= 8; file++){
            for(int rank = 1; rank <= 8; rank++){
                Piece piece = null;
                TjessSquare tjessSquare = null;
                TjessSquare.Location location = new TjessSquare.Location(files[file-1], rank);
                if (rank == 2){
                    piece = new Piece(Piece.Pieces.PAWN, Player.Color.WHITE);
                } else if (rank == 1 || rank == 8){
                    Player.Color color = Player.Color.WHITE;
                    if (rank == 8) { color = Player.Color.BLACK; }

                    if (file == 1 || file == 8) {
                        piece = new Piece(Piece.Pieces.ROOK, color);
                    } else if (file == 2 || file == 7) {
                        piece = new Piece(Piece.Pieces.KNIGHT, color);
                    } else if (file == 3 || file == 6) {
                        piece = new Piece(Piece.Pieces.BISHOP, color);
                    } else if (file == 4) {
                        piece = new Piece(Piece.Pieces.QUEEN, color);
                    } else {
                        piece = new Piece(Piece.Pieces.KING, color);
                    }

                } else if (rank == 7){
                    piece = new Piece(Piece.Pieces.PAWN, Player.Color.BLACK);
                }
                tjessSquare = new TjessSquare(location, piece);
                starting_board[file-1][rank-1] = tjessSquare;

/*                if (piece != null) {
                    System.out.println("RANK: " + rank + ", FILE: " + file + ", PIECE: " + piece.toString() + ", COLOR: " + tjessSquare.getCurrentPiece().getColor().toString());
                } else {
                    System.out.println("RANK: " + rank + ", FILE: " + file + ", PIECE: null");

                }*/
            }
        }
    }

    /**
     * Remove a piece from the board. Is used in making moves, but also when a piece is slain.
     * @param file as X
     * @param rank as Y
     */
    public void removePieceFromBoard(int file, int rank){

    }

    /**
     * Remove a piece from the board. Is used with a TjessSquare, and get the piece from this square.
     * @param square to get the piece on that square.
     */
    public void removePieceFromBoard(TjessSquare square){

    }

    public void addPieceToBoard(int file, int rank, Piece piece){

    }

    /**
     * Moves a piece on the board. Updates board afterwards.
     * @param from, from square, get the piece on this square. Update it.
     * @param to, to square, set the piece on this square. Update it.
     * @requires from: to have a piece on it.
     */
    public void movePiece(TjessSquare from, TjessSquare to){

    }

    private boolean isValidMove(){
        return false;
    }

    /*
     * JAVA SWING FUNCTIONALITY
     */
    private static final int RECT_X = 50;
    private static final int RECT_Y = RECT_X;
    private static final int RECT_WIDTH = 50;
    private static final int RECT_HEIGHT = RECT_WIDTH;

    private List<TjessSquare> squares = new ArrayList<TjessSquare>();

    /**
     * Adding all the squares (rectangles) to the GUI. One time process.
     * @param file as square X
     * @param rank as square Y
     */
    public void addSquareToSwing(int file, int rank){
        TjessSquare rect = squaresCurrentBoard[file-1][rank-1];
        rect.setBounds(RECT_X * (file-1), RECT_Y * (8-rank), RECT_WIDTH, RECT_HEIGHT);

        squares.add(rect);
    }

    /**
     * If there is a piece at this location, remove it from swing.
     * @param file as X of the square.
     * @param rank as Y of the square
     */
    public void removePieceFromSwing(int file, int rank){
        //System.out.println("Found: " + squaresCurrentBoard[file-1][rank-1].getReferenceName());
        for (Component c : getComponentsInLayer(0)){
            //System.out.println("Component name: " + c.getName() + ", location: " + c.getBounds());
            if (c.getName().equals(squaresCurrentBoard[file-1][rank-1].getReferenceName())){
                remove(c);
                revalidate();
                repaint();
                //System.out.println("Removed.");
            }
        }
    }

    /**
     * Add a piece to the layered board.
     * @param file as X
     * @param rank as Y
     * @requires a piece on a file or rank not to be NULL.
     */
    public void addPieceToSwing(int file, int rank){
        BufferedImage img = squaresCurrentBoard[file-1][rank-1].getCurrentPiece().getImage();
        JLabel pic = new JLabel(new ImageIcon(img));
        pic.setName(squaresCurrentBoard[file-1][rank-1].getReferenceName());
        pic.setBounds(RECT_X * (file-1), RECT_Y * (8-rank), RECT_WIDTH, RECT_HEIGHT);

        add(pic, 0);
    }

    @Override
    public Dimension getPreferredSize(){
        return new Dimension(RECT_WIDTH + 2 * RECT_X, RECT_HEIGHT + 2 * RECT_Y);
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        for (TjessSquare rect : squares){
            g2.draw(rect);
            g2.setColor(rect.getSquareColor() == TjessSquare.SquareColor.DARK ? Color.DARK_GRAY : Color.WHITE);
            g2.fill(rect);
        }
    }
}