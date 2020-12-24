package streamingfire.tjess;

import java.util.ArrayList;
import java.util.List;

public abstract class Player {

    private final String name;
    private final Color color;

    public Player(String name, Color color){
        this.name = name;
        this.color = color;
    }

    /**
     * @return the name of this Player
     */
    public String getName(){
        return this.name;
    }

    /**
     * @return the color of this player
     */
    public Color getColor(){
        return this.color;
    }

    public void makeMove(){

    }

    /**
     * An enumeration of piece/player colors.
     */
    public enum Color {
        BLACK,
        WHITE
    }

}
