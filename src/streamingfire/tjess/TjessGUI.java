package streamingfire.tjess;

import javax.swing.*;
import java.awt.image.BufferedImage;

public class TjessGUI extends JFrame {

    public TjessGUI(TjessBoard board){
        super("Tjess Game");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setSize(500, 500);

        createBoardSquares(board);

        getContentPane().add(board);

        setVisible(true);
    }

    /**
     * Update the display with the NEW board, after a turn was done.
     * @param board, which is the current state of the board
     */
    public void updateBoardPieces(TjessBoard board){
        for(int file = 1; file <= 8; file++){
            for(int rank = 1; rank <= 8; rank++) {
                TjessSquare square = board.getBoard()[file-1][rank-1];
                Piece piece = square.getCurrentPiece();

                if (piece != null){
                    // Draw it
                    BufferedImage img = piece.getImage();
                    JLabel pic = new JLabel(new ImageIcon(img));
                    pic.setBounds(0, 0, 100, 100);
                    getContentPane().add(pic);
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

        //TjessSquare[][] tjessSquares = board.getBoard();
        //for(int file = 1; file <= 8; file++){
        //    for(int rank = 1; rank <= 8; rank++) {
        //        System.out.println("File: " + file + ", Rank: " + rank + ", Item: " + tjessSquares[file-1][rank-1].toString());
        //   }
        //}

        GUI.updateBoardPieces(board);
    }
}
