import java.util.*;


public class Piece {
    private int x;
    private int y;
    private boolean isKing = false;
    public boolean isWhite;

    public Piece(int x, int y, boolean isWhite) {
        this.x = x;
        this.y = y;
		this.isWhite = isWhite;
    }

    public int[] getCoordinates() {
        int[] coordinates = new int[2];
        coordinates[0] = this.x;
        coordinates[1] = this.y;
        return coordinates;
    }
    
    public String getString() {
        String pieceSymbol;

        if(isWhite)
			pieceSymbol = "W";
        else
			pieceSymbol = "B";

        if(isKing)
            if (isWhite)
                pieceSymbol = "Q ";
            else
                pieceSymbol = "K ";
        else
			pieceSymbol += " ";

        return pieceSymbol;
    }

    private void setKing() {
        isKing = true;
    }
    

    public void checkIfShouldBeKing(Board board) {
        if(isWhite && this.y == board.size - 1 || 
            !isWhite && this.y == 0)
            this.setKing();
    }


    public void moveTo(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Move[] getAllPossibleMoves(Board board) {
        ArrayList<Move> moves = new ArrayList<Move>();

        int startingY, yIncrement;
        if (isWhite) {
            startingY = this.y + 1; 
            yIncrement = -2;
        } else {
            startingY = this.y - 1;
            yIncrement = 2;
        }

        int rowsToCheck = 1;
        if (this.isKing)
            rowsToCheck = 2;
  
        for (int x = this.x - 1; x <= this.x + 1; x += 2) {
            int y = startingY - yIncrement;
            for (int i = 0; i < rowsToCheck; i++) {
                y += yIncrement;

                if (board.isOverEdge(x, y))
                    continue;
                if (board.getValueAt(x, y) == null) {
                    moves.add(new Move(this.x, this.y, x, y, null, false)); 
                }
            }
        }

        Move[] possibleJumps = this.getAllPossibleJumps(board, null);
        if(possibleJumps != null)
            moves.addAll(Arrays.asList(possibleJumps));

        if(!moves.isEmpty()) {
            moves.trimToSize();
            return moves.toArray(new Move[1]);
        } else {
            return null;
		}
    }


    private Move[] getAllPossibleJumps(Board board, Move precedingMove) {

        ArrayList<Move> moves = new ArrayList<Move>();

        int startingY, yIncrement;
        if(isWhite) {
            startingY = this.y + 2;
            yIncrement = -4;
        } else {
            startingY = this.y - 2;
            yIncrement = 4;
        }
        

        int rowsToCheck = 1;
        if(this.isKing)
            rowsToCheck = 2;
              
        for(int x = this.x - 2; x <= this.x + 2; x += 4) {
            int y = startingY - yIncrement;
            for(int i = 0; i < rowsToCheck; i++) {

                y += yIncrement;

                if(board.isOverEdge(x, y))
                    continue;

                if(precedingMove != null &&
                    x == precedingMove.getStartingPosition()[0] && 
                    y == precedingMove.getStartingPosition()[1])
                    continue;

                Piece betweenPiece = board.getValueAt( (this.x + x)/2 , (this.y + y)/2 );
                if(betweenPiece != null && betweenPiece.isWhite != this.isWhite && board.getValueAt(x, y) == null) {
                    Move jumpingMove = new Move(this.x, this.y, x, y, precedingMove, true);
                    moves.add(jumpingMove);
                    Piece imaginaryPiece = new Piece(x, y, this.isWhite);
               		if(this.isKing) imaginaryPiece.setKing();
                    Move[] subsequentMoves = imaginaryPiece.getAllPossibleJumps(board, jumpingMove);
                    if(subsequentMoves != null)
                        moves.addAll(Arrays.asList(subsequentMoves));
                }
            }
        }

        if(!moves.isEmpty()) {
            moves.trimToSize();
            return moves.toArray(new Move[1]);
        } else {
            return null;
		}
    }
}
