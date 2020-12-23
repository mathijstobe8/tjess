package streamingfire.tjess;

/**
The board of the game.
 @author Mathijs Tobé
 */
public class TjessBoard {

    private char[] files = new char[] { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
    private Square[][] starting_board = new Square[8][8];

    private Square[][] currentBoard = new Square[8][8];

    public TjessBoard(){
        if(setStartingBoard()){
            currentBoard = starting_board;
        }
    }

    public Square[][] getBoard(){
        return currentBoard;
    }

    public boolean setStartingBoard(){
        for(int file = 1; file <= 8; file++){
            for(int rank = 1; rank <= 8; rank++){
                Piece piece = null;
                Square square = null;
                Square.Location location = new Square.Location(files[file-1], rank);
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
                square = new Square(location, piece);
                starting_board[file-1][rank-1] = square;
            }
        }

        return true;
    }

    public static void main(String[] args){
        TjessBoard board = new TjessBoard();

        Square[][] squares = board.getBoard();

        for(int file = 1; file <= 8; file++){
            for(int rank = 1; rank <= 8; rank++) {
                System.out.println("File: " + file + ", Rank: " + rank + ", Item: " + squares[file-1][rank-1].toString());
            }
        }
    }
}