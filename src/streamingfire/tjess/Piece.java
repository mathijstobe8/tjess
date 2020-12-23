package streamingfire.tjess;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 A chess piece. Can either be a Rook, Knight, Bishop, King, Queen or Pawn.
 @author Mathijs Tobé
 */
public class Piece extends JPanel {

    private Pieces piece;
    private Player.Color color;
    private BufferedImage image;

    public Piece(Pieces piece, Player.Color color){
        this.piece = piece;
        this.color = color;
        System.out.println(returnImagePath());

        try {
            image = ImageIO.read(new File("img/" + returnImagePath()));
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public Pieces getPiece() {
        return piece;
    }

    public BufferedImage getImage() { return image; }

    private String returnImagePath(){
        String prefix, suffix;

        prefix = color.toString().equals("BLACK") ? "DARK" : "LIGHT";
        suffix = piece.toString();


        return (prefix + "_" + suffix + ".png");
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
