package streamingfire.tjess;

public class GameManager {

    private TjessBoard playingBoard;

    public GameManager(){
        playingBoard = new TjessBoard();
        TjessGUI GUI = new TjessGUI(playingBoard);

        GUI.createBoardSquares(playingBoard);
        GUI.add(playingBoard);
        GUI.updateBoardPieces(playingBoard);

        playingBoard.addBoardParentToAllChildren();
    }

    public static void main(String[] args){
        GameManager game = new GameManager();
    }

}
