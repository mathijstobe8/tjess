package streamingfire.tjess;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

public class TjessGUI extends JFrame implements ActionListener, MouseListener {

    JButton button;
    TjessBoard board;

    public TjessGUI(TjessBoard board){
        super("Tjess Game");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(new Dimension(600, 600));

        this.board = board;

        createBoardSquares(board);
        add(board);
        addMouseListener(this);
        updateBoardPieces(board);

        // TEMP
        button = new JButton("Press me to remove something");
        button.setBounds(450, 50, 150, 100);
        button.addActionListener(this);

        add(button);

        setLayout(null);
        setVisible(true);
    }

    /**
     * Update the display with the NEW board, after a turn was done. (or first turn)
     * @param board, which is the current state of the board
     */
    public void updateBoardPieces(TjessBoard board){
        for(int file = 1; file <= 8; file++){
            for(int rank = 1; rank <= 8; rank++) {
                // COMPARE CURRENT BOARD ON SWING TO BOARD PARAMETER
                // use board.removePieceFromSwing(file,rank) to remove at position
                // use board.addPieceToSwing(file,rank) to add at position
                // use board.removePieceFromBoard(file,rank) or (Piece piece) to remove piece from phy
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

    /**
     * Make a move on the GUI.
     */
    public void makePhysicalMove(){

    }

    public static void main(String[] args){
        TjessBoard board = new TjessBoard();
        TjessGUI GUI = new TjessGUI(board);
    }

    /*
    EVENT LISTENERS
     */

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println(e.getX());
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
