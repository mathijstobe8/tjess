package streamingfire.tjess;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.Scanner;

public class GameManager {

    private TjessBoard playingBoard;
    public JPanel playerBlack;
    public JPanel playerWhite;

    public GameManager(String p1, String p2){
        playingBoard = new TjessBoard(this);
        TjessGUI GUI = new TjessGUI(playingBoard);

        GUI.createBoardSquares(playingBoard);
        GUI.add(playingBoard);
        GUI.setBoardPieces(playingBoard);
        playingBoard.addBoardParentToAllChildren();

        JPanel informationPanel = new JPanel();
        JPanel hitPiecesPanel = new JPanel();
        informationPanel.setLayout(new BorderLayout());
        informationPanel.setBorder(new LineBorder(Color.BLACK));
        hitPiecesPanel.setLayout(new BorderLayout());

        playerBlack = new JPanel() {
            @Override
            public Dimension getPreferredSize(){
                return new Dimension(100, 100);
            }
        };
        //playerBlack.setBorder(new LineBorder(Color.BLACK));

        playerWhite = new JPanel() {
            @Override
            public Dimension getPreferredSize(){
                return new Dimension(100, 100);
            }
        };
        //playerWhite.setBorder(new LineBorder(Color.BLACK));

        //playerBlack.add(new JLabel("White"));
        //playerWhite.add(new JLabel("Black"));

        informationPanel.add(new JLabel("Game Information"), BorderLayout.NORTH);

        hitPiecesPanel.add(playerBlack, BorderLayout.EAST);
        hitPiecesPanel.add(playerWhite, BorderLayout.WEST);

        informationPanel.add(hitPiecesPanel, BorderLayout.NORTH);

        playingBoard.setBounds(0, 0, 400, 400);
        informationPanel.setBounds(425, 0, 200, 400);
        GUI.setSize(new Dimension(800, 450));

        GUI.add(informationPanel);

    }

    public void AddPieceToSlainListGUI(Piece piece){
        JLabel pic = new JLabel();
        pic.setBounds(new Rectangle(20, 20));
        Image dimg = piece.getImage().getScaledInstance(pic.getWidth(), pic.getHeight(), Image.SCALE_SMOOTH);
        pic.setIcon(new ImageIcon(dimg));

        if (piece.getColor() == Player.Color.BLACK){
            playerBlack.add(pic);
        } else {
            playerWhite.add(pic);
        }
    }

    public static void main(String[] args){

        String name_player1;
        String name_player2;

        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the first player's name: ");
        name_player1 = sc.next();
        System.out.print("Enter the second player's name: ");
        name_player2 = sc.next();

        System.out.println("Game starting...");

        GameManager game = new GameManager(name_player1, name_player2);


    }

}
