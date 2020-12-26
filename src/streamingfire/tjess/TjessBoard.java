package streamingfire.tjess;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
The board of the game.
 @author Mathijs Tobé
 */
public class TjessBoard extends JLayeredPane {

    public String boardName;

    private char[] files = new char[] { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
    private TjessSquare[][] starting_board = new TjessSquare[8][8];
    private TjessSquare[][] squaresCurrentBoard = new TjessSquare[8][8];

    public TjessBoard(){
        setBounds(0, 0, 400, 400);
        setStartingBoard();
        boardName = "TjessBoard1";

        squaresCurrentBoard = starting_board;

        displayBoardToCMD();
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

    /**
     * To be able for the children (Listeners) to call methods for this board.
     */
    public void addBoardParentToAllChildren(){
        for(int file = 1; file <= 8; file++) {
            for (int rank = 1; rank <= 8; rank++) {
                squaresCurrentBoard[file-1][rank-1].setParent(this);
            }
        }
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

    private void displayBoardToCMD(){
        for(int file = 1; file <= 8; file++){
            for(int rank = 1; rank <= 8; rank++) {
                System.out.println(getBoard()[file-1][rank-1].toString());
            }
        }
    }

    /**
     * Moves a piece on the board. Updates board afterwards. Also checks if valid etc.
     * @param from, from square, get the piece on this square. Update it.
     * @param to, to square, set the piece on this square. Update it.
     * @requires from: to have a piece on it.
     */
    public void movePiece(TjessSquare from, TjessSquare to){
        System.out.println("Attempting to move piece: " + from.getCurrentPiece().toString() + " from "
                + from.getLocFileRank().toString() + " to " + to.getLocFileRank().toString());

        MoveInformation info = getMoveInformation(from, to);
        String fromRefName = info.from.getReferenceName();
        String toRefName = info.to.getReferenceName();

        System.out.println("FRom ref name: " + fromRefName);
        System.out.println("To ref name: " + toRefName);


        if (info.isValidMove()){
            // move the piece actually
            // SET the to square to be the piece from the 'from' square.
            getBoard()[info.to.getLocFileRank().getFileAsInt()-1][info.to.getLocFileRank().getRank()-1].changeState(info.from.getCurrentPiece());
            // SET the piece of the old 'from' square to null.
            getBoard()[info.from.getLocFileRank().getFileAsInt()-1][info.from.getLocFileRank().getRank()-1].changeState(null);

            if(!info.getHitPiece()){
                updateBoardToSwing(info.from, info.to, fromRefName);
            } else {
                updateBoardToSwing(info.from, info.to, fromRefName, toRefName);
            }
        } else {
            // wasn;t a valid move.
            System.out.println("This move wasn't a valid move. ");
        }

    }

    // VARIABLES FOR MOVING
    private TjessSquare storedSquare = null;

    /**
     * Add the square to the selected squares.
     * @param square, the square that was passed on
     * @return true if the square is currently selected to be moved, returns false if a square was already selected.
     */
    public boolean squarePressed(TjessSquare square){
        if (square.getCurrentPiece() == null){
            // There is currently no piece on this square.
            if (storedSquare != null){
                // There is already an item in the select squares list
                // Which means that this is a square to MOVE TO
                movePiece(storedSquare, square);
                storedSquare = null;
                return true;
            } else {
                // Player pressed a square first. Player should press piece first.
                // Nothing will be stored. storeSquare = null.
                return false;
            }
        } else {
            // There is a piece on this square.
            if (storedSquare != null){
                // Piece is attempting to replace another piece (attack). Is this valid?
                movePiece(storedSquare, square);
                storedSquare = null;
                return true;
            } else {
                // Player is about to do something with this square.
                storedSquare = square;
                return true;
            }
        }
    }

    /**
     *
     * @param from
     * @param to
     * @return
     * @requires from to have a piece on it.
     */
    private MoveInformation getMoveInformation(TjessSquare from, TjessSquare to){
        boolean isValidMove = false;
        boolean hitPieceOnMove = false;

        ArrayList<TjessSquare> possibleSquares = getPossibleSquaresToMoveTo(from);

        for (TjessSquare s : possibleSquares){
            System.out.println("Possible square: " + s.toString());
            if (s==to) {
                isValidMove = true;
                if((from.getLocFileRank().getFileAsInt() != to.getLocFileRank().getFileAsInt()) ||
                        from.getLocFileRank().getRank() != to.getLocFileRank().getRank()){
                    hitPieceOnMove = true;
                }
                break;
            }
        }

        return new MoveInformation(isValidMove, hitPieceOnMove, from, to);
    }

    /**
     * Also takes into account taken squares.
     * @param fromSquare
     * @return
     */
    private ArrayList<TjessSquare> getPossibleSquaresToMoveTo(TjessSquare fromSquare){
        ArrayList<TjessSquare> possibleSquares = new ArrayList<TjessSquare>();

        int from_x = fromSquare.getLocFileRank().getFileAsInt() - 1;
        int from_y = fromSquare.getLocFileRank().getRank() - 1;

        Piece piece = fromSquare.getCurrentPiece();
        Player.Color PIECE_COLOR = fromSquare.getCurrentPiece().getColor();

        switch(piece.getPiece()){
            case KING:
                // KING CAN MOVE LEFT, RIGHT, DOWN, UP, DIAGONAL. 1 STEP
                // COLOR DOESNT MATTER HERE FOR SIDES, ONLY FOR ATTACKS
                // CHECK FOR BORDERS AND PIECES

                // TO DO: KING CAN BE IN CHECK.
                possibleSquares.addAll(getKingSquares(from_x, from_y, PIECE_COLOR));
                break;
            case PAWN:
                // PAWN CAN EITHER MOVE 1 FORWARD OR 2 FORWARD. NOT BACKWARDS
                // HENCE COLOR IS IMPORTANT!
                possibleSquares.addAll(getPawnSquares(from_x, from_y, PIECE_COLOR));
                break;
            case ROOK:
                // ROOK CAN MOVE UP, DOWN, LEFT, RIGHT. UNLIMITED STEPS.
                possibleSquares.addAll(getStraightLineSquares(from_x, from_y, PIECE_COLOR));
                break;
            case QUEEN:
                // QUEEN CAN MOVE DIAGONALLY, UP DOWN LEFT RIGHT. UNLIMITED STEPS.
                possibleSquares.addAll(getStraightLineSquares(from_x, from_y, PIECE_COLOR));
                possibleSquares.addAll(getDiagonalSquares(from_x, from_y, PIECE_COLOR));
                break;
            case BISHOP:
                // BISHOP CAN MOVE DIAGONALLY IN ANY DIRECTION. UNLIMITED STEPS
                possibleSquares.addAll(getDiagonalSquares(from_x, from_y, PIECE_COLOR));
                break;
            case KNIGHT:
                // KNIGHT CAN MOVE 2 STEPS 1 LEFT. 2 STEPS 1 RIGHT. IN ANY DIRECTION.
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + piece.getPiece());
        }

        return possibleSquares;
    }

    private ArrayList<TjessSquare> getStraightLineSquares(int from_x, int from_y, Player.Color PIECE_COLOR){
        ArrayList<TjessSquare> possibleSquares = new ArrayList<>();

        // LEFT
        if (from_x != 0){ // if 0, then there is no squares to go to. (for left)
            for (int i = 0; i < from_x; i++){
                TjessSquare square = getBoard()[from_x - i - 1][from_y];

                // the moment there is a piece on this square, check if its same team.
                if (!square.isEmpty()){
                    if (square.getCurrentPiece().getColor() != PIECE_COLOR){
                        possibleSquares.add(square);
                    }
                    break; // don't check others. ( can't teleport behind piece )
                }

                possibleSquares.add(square);
            }
        }

        // RIGHT
        if (from_x != 7){
            for (int i = 0; i < 7 - from_x; i++){
                TjessSquare square = getBoard()[from_x + 1 + i][from_y];

                // the moment there is a piece on this square, check if its same team.
                if (!square.isEmpty()){
                    if (square.getCurrentPiece().getColor() != PIECE_COLOR){
                        possibleSquares.add(square);
                    }
                    break; // don't check others.
                }

                possibleSquares.add(square);
            }
        }

        // UP
        if (from_y != 7){
            for (int i = 0; i < 7 - from_y; i++){
                TjessSquare square = getBoard()[from_x][from_y + i + 1];

                // the moment there is a piece on this square, check if its same team.
                if (!square.isEmpty()){
                    if (square.getCurrentPiece().getColor() != PIECE_COLOR){
                        possibleSquares.add(square);
                    }
                    break; // don't check others.
                }

                possibleSquares.add(square);
            }
        }

        // DOWN
        if (from_y != 0){
            for (int i = 0; i < from_y; i++){
                TjessSquare square = getBoard()[from_x][from_y - i - 1];

                // the moment there is a piece on this square, check if its same team.
                if (!square.isEmpty()){
                    if (square.getCurrentPiece().getColor() != PIECE_COLOR){
                        possibleSquares.add(square);
                    }
                    break; // don't check others.
                }

                possibleSquares.add(square);
            }
        }
        return possibleSquares;
    }

    private ArrayList<TjessSquare> getDiagonalSquares(int from_x, int from_y, Player.Color PIECE_COLOR){

        ArrayList<TjessSquare> possibleSquares = new ArrayList<>();

        int valX = -1;
        int valY = -1;

        // LEFT UP
        if (from_x != 0 && from_y != 7){
            for (int i = 0; i < 8; i++){
                valX = from_x - i - 1;
                valY = from_y + i + 1;

                if(valX < 0 || valY > 7) break;

                TjessSquare square = getBoard()[valX][valY];

                // the moment there is a piece on this square, check if its same team.
                if (!square.isEmpty()){
                    if (square.getCurrentPiece().getColor() != PIECE_COLOR){
                        possibleSquares.add(square);
                    }
                    break; // don't check others.
                }

                possibleSquares.add(square);
            }
        }

        // RIGHT DOWN
        if (from_x != 7 && from_y != 0){
            for (int i = 0; i < 8; i++){
                valX = from_x + i + 1;
                valY = from_y - i - 1;

                if(valX > 7 || valY < 0) break;

                TjessSquare square = getBoard()[valX][valY];

                // the moment there is a piece on this square, check if its same team.
                if (!square.isEmpty()){
                    if (square.getCurrentPiece().getColor() != PIECE_COLOR){
                        possibleSquares.add(square);
                    }
                    break; // don't check others.
                }

                possibleSquares.add(square);
            }
        }

        // LEFT DOWN
        if (from_x != 0 && from_y != 0){
            for (int i = 0; i < 8; i++){
                valX = from_x - i - 1;
                valY = from_y - i - 1;

                if(valX < 0 || valY < 0) break;

                TjessSquare square = getBoard()[valX][valY];

                // the moment there is a piece on this square, check if its same team.
                if (!square.isEmpty()){
                    if (square.getCurrentPiece().getColor() != PIECE_COLOR){
                        possibleSquares.add(square);
                    }
                    break; // don't check others.
                }

                possibleSquares.add(square);
            }
        }

        // RIGHT UP
        if (from_x != 7 && from_y != 7){
            for (int i = 0; i < 8; i++){
                valX = from_x + i + 1;
                valY = from_y + i + 1;

                if(valX > 7 || valY > 7) break;

                TjessSquare square = getBoard()[valX][valY];

                // the moment there is a piece on this square, check if its same team.
                if (!square.isEmpty()){
                    if (square.getCurrentPiece().getColor() != PIECE_COLOR){
                        possibleSquares.add(square);
                    }
                    break; // don't check others.
                }

                possibleSquares.add(square);
            }
        }

        return possibleSquares;
    }

    private ArrayList<TjessSquare> getPawnSquares(int from_x, int from_y, Player.Color PIECE_COLOR){
        ArrayList<TjessSquare> possibleSquares = new ArrayList<>();

        if(PIECE_COLOR == Player.Color.WHITE){
            TjessSquare frontOfPawn, leftDiag, rightDiag;

            /* Can return error when pawn is at the 8th rank. But won't matter since this piece will be replaced anyway.*/
            frontOfPawn = getBoard()[from_x][from_y + 1];

            // for slaying other pieces
            if (from_x > 0 && from_y < 7){
                leftDiag = getBoard()[from_x - 1][from_y + 1];

                // only add it when there is a piece here.
                if (!leftDiag.isEmpty() && leftDiag.getCurrentPiece().getColor() != Player.Color.WHITE){
                    possibleSquares.add(leftDiag);
                }
            }

            if (from_x < 7 && from_y < 7){
                rightDiag = getBoard()[from_x + 1][from_y + 1];

                // only add it when there is a piece here

                if (!rightDiag.isEmpty() && rightDiag.getCurrentPiece().getColor() != Player.Color.WHITE) {
                    possibleSquares.add(rightDiag);
                }
            }

            if (from_y == 1){
                // PAWN IS AT THE SECOND RANK, SO CAN MOVE 2 FORWARD
                if (frontOfPawn.isEmpty()){
                    possibleSquares.add(frontOfPawn);
                    TjessSquare frontOfFrontOfPawn = getBoard()[from_x][from_y + 2];
                    if (frontOfFrontOfPawn.isEmpty()){
                        possibleSquares.add(frontOfFrontOfPawn);
                    }
                }
            } else {
                if (frontOfPawn.isEmpty()){
                    possibleSquares.add(frontOfPawn);
                }
            }
        } else {
            TjessSquare frontOfPawn, leftDiag, rightDiag;
            frontOfPawn = getBoard()[from_x][from_y - 1];

            // for slaying other pieces
            if (from_x < 7){
                leftDiag = getBoard()[from_x + 1][from_y - 1];

                // only add it when there is a piece here.
                if (!leftDiag.isEmpty()  && leftDiag.getCurrentPiece().getColor() != Player.Color.BLACK){
                    possibleSquares.add(leftDiag);
                }
            }

            if (from_x > 0){
                rightDiag = getBoard()[from_x - 1][from_y - 1];

                // only add it when there is a piece here

                if (!rightDiag.isEmpty() && rightDiag.getCurrentPiece().getColor() != Player.Color.BLACK) {
                    possibleSquares.add(rightDiag);
                }
            }

            if (from_y == 6){
                // PAWN IS AT THE SECOND RANK, SO CAN MOVE 2 FORWARD
                if (frontOfPawn.isEmpty()){
                    possibleSquares.add(frontOfPawn);
                    TjessSquare frontOfFrontOfPawn = getBoard()[from_x][from_y - 2];
                    if (frontOfFrontOfPawn.isEmpty()){
                        possibleSquares.add(frontOfFrontOfPawn);
                    }
                }
            } else {
                if (frontOfPawn.isEmpty()){
                    possibleSquares.add(frontOfPawn);
                }
            }
        }

        return possibleSquares;
    }

    private ArrayList<TjessSquare> getKingSquares(int from_x, int from_y, Player.Color PIECE_COLOR){

        ArrayList<TjessSquare> possibleSquares = new ArrayList<>();

        ArrayList<TjessSquare> king_possibilities = new ArrayList<>();

        if (from_y != 7) king_possibilities.add(getBoard()[from_x][from_y + 1]);
        if (from_y != 0) king_possibilities.add(getBoard()[from_x][from_y - 1]);
        if (from_x != 7) king_possibilities.add(getBoard()[from_x + 1][from_y]);
        if (from_x != 0) king_possibilities.add(getBoard()[from_x - 1][from_y]);
        if (from_y != 7 && from_x != 0) king_possibilities.add(getBoard()[from_x - 1][from_y + 1]);
        if (from_y != 7 && from_x != 7) king_possibilities.add(getBoard()[from_x + 1][from_y + 1]);
        if (from_y != 0 && from_x != 0) king_possibilities.add(getBoard()[from_x - 1][from_y - 1]);
        if (from_y != 0 && from_x != 7) king_possibilities.add(getBoard()[from_x + 1][from_y - 1]);

        for (TjessSquare s : king_possibilities){
            if (s.isEmpty() || (!s.isEmpty() && s.getCurrentPiece().getColor() != PIECE_COLOR)) {
                possibleSquares.add(s);
            }
        }

        return possibleSquares;
    }

    private ArrayList<TjessSquare> getKnightSquares(int from_x, int from_y, Player.Color PIECE_COLOR){

        ArrayList<TjessSquare> possibleSquares = new ArrayList<>();

        return possibleSquares;
    }

    /**
     * A class which will have information about the move the player did.
     */
    private static class MoveInformation {
        private boolean isValidMove;
        private boolean hitPiece; // did it hit the piece to go to this square?
        private TjessSquare from;
        private TjessSquare to;

        public MoveInformation(boolean isValidMove, boolean hitPiece, TjessSquare from, TjessSquare to){
            setValidMove(isValidMove);
            setHitPiece(hitPiece);
            setFromSquare(from);
            setToSquare(to);
        }

        public void setHitPiece(boolean val){
            this.hitPiece = val;
        }

        public void setFromSquare(TjessSquare val){
            this.from = val;
        }

        public void setToSquare(TjessSquare val){
            this.to = val;
        }

        public void setValidMove(boolean val){
            this.isValidMove = val;
        }

        public boolean getHitPiece(){
            return this.hitPiece;
        }

        public TjessSquare getFromSquare(){
            return this.from;
        }

        public TjessSquare getToSquare(){
            return this.to;
        }

        public boolean isValidMove(){
            return this.isValidMove;
        }
    }

    /* ------------------------------------------------------------------------------------------------------------- */

    /*
     * JAVA SWING FUNCTIONALITY
     */

    private static final int RECT_X = 50;
    private static final int RECT_Y = RECT_X;
    private static final int RECT_WIDTH = 50;
    private static final int RECT_HEIGHT = RECT_WIDTH;

    /**
     * Adding all the squares (rectangles) to the GUI. One time process.
     * @param file as square X
     * @param rank as square Y
     */
    public void addSquareToSwing(int file, int rank){
        TjessSquare square = squaresCurrentBoard[file-1][rank-1];
        square.setBounds(RECT_X * (file-1), RECT_Y * (8-rank), RECT_WIDTH, RECT_HEIGHT);
        square.setBackground(square.getSquareColor() == TjessSquare.SquareColor.DARK ? Color.DARK_GRAY : Color.WHITE);

        add(square, 0);
    }

    public void removePieceFromSwing(String refName){
        for (Component c : getComponentsInLayer(MODAL_LAYER)){
            if (c.getName().equals(refName)) {
                remove(c);
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
        BufferedImage img = getBoard()[file-1][rank-1].getCurrentPiece().getImage();
        JLabel pic = new JLabel(new ImageIcon(img));
        pic.setName(getBoard()[file-1][rank-1].getReferenceName());
        pic.setBounds(RECT_X * (file-1), RECT_Y * (8-rank), RECT_WIDTH, RECT_HEIGHT);

        add(pic, MODAL_LAYER);
    }

    public void updateBoardToSwing(TjessSquare fromSquare, TjessSquare toSquare, String refName){

        // only do affected squares (from and to)
        // fromSquare WILL BE EMPTY NOW
        // toSquare WILL HAVE THE NEW PIECE ON IT

        removePieceFromSwing(refName);
        addPieceToSwing(toSquare.getLocFileRank().getFileAsInt(), toSquare.getLocFileRank().getRank());
        revalidate();
        repaint();
    }

    public void updateBoardToSwing(TjessSquare fromSquare, TjessSquare toSquare, String refNameFrom, String refNameTo){

        // only do affected squares (from and to)
        // fromSquare WILL BE EMPTY NOW
        // toSquare WILL HAVE THE NEW PIECE ON IT
        // also remove the piece of the toSquare first.
        removePieceFromSwing(refNameFrom);
        removePieceFromSwing(refNameTo);
        addPieceToSwing(toSquare.getLocFileRank().getFileAsInt(), toSquare.getLocFileRank().getRank());
        revalidate();
        repaint();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(RECT_WIDTH + 2 * RECT_X, RECT_HEIGHT + 2 * RECT_Y);
    }
}