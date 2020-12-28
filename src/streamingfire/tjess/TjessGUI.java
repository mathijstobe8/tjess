package streamingfire.tjess;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TjessGUI extends JFrame {

    JButton button;

    public TjessGUI(TjessBoard board){
        super("Tjess Game");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLayout(null);

        setVisible(true);
    }

    /**
     * Update the display with the first board.
     * @param board, which is the current state of the board
     */
    public void setBoardPieces(TjessBoard board){
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
}
