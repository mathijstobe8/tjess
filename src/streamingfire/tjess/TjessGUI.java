package streamingfire.tjess;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class TjessGUI extends JFrame {

    public TjessGUI(TjessBoard board){
        super("Tjess Game");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(new Dimension(500, 500));

        createBoardSquares(board);

        getContentPane().add(board);

        updateBoardPieces(board);

        setVisible(true);
    }

    /**
     * Update the display with the NEW board, after a turn was done.
     * @param board, which is the current state of the board
     */
    public void updateBoardPieces(TjessBoard board){
        for(int file = 1; file <= 8; file++){
            for(int rank = 1; rank <= 8; rank++) {
                if (board.getBoard()[file-1][rank-1].getCurrentPiece() != null){
                    board.addPieceToSwing(file, rank);
                }
            }
        }


    }

    /**
     * Display the board for the first time, create the elements. etc.
     * @param board, the parameter which is the current state of the board
     * @requires a board to be passed on.
     */
    public void createBoardSquares(TjessBoard board){
        for(int file = 1; file <= 8; file++){
            for(int rank = 1; rank <= 8; rank++) {
                board.addSquareToSwing(file, rank);
            }
        }

    }

    public static void main(String[] args){
        TjessBoard board = new TjessBoard();
        TjessGUI GUI = new TjessGUI(board);
    }
}
