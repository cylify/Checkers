import java.util.*;

public class Piece {
    private int x;
    private int y;
    private boolean isKing = false;
    public boolean isWhite;

    /**
     * Constructer for Piece
     * @param x x coord of piece
     * @param y y coord of piece
     * @param isWhite if piece is black or white
     */
    public Piece(int x, int y, boolean isWhite) {
        this.x = x;
        this.y = y;
	this.isWhite = isWhite;
    }



    /**
     * Get coordinate of piece
     * @return Returns 2 element array of piece's coordinates
     */
    public int[] getCoordinates() {
        int[] coordinates = new int[2];
        coordinates[0] = this.x;
        coordinates[1] = this.y;
        return coordinates;
    }
    
    /**
     * Piece type to String
     * @return Returns String version of piece
     */
    public String getString() {
        String pieceSymbol;

        if(isWhite)
			pieceSymbol = "\u2617";
        else
			pieceSymbol = "\u2616";

        if(isKing)
            if (isWhite)
                pieceSymbol = "\u26CA ";
            else
                pieceSymbol = "\u26C9 ";
        else
			pieceSymbol += " ";

        return pieceSymbol;
    }

    /**
     * Sets piece to King
     */
    private void setKing() {
        isKing = true;
    }
    
    /**
     * Check if piece should be a king
     * @param board What board is being checked
     */
    public void checkIfShouldBeKing(Board board) {
        if(isWhite && this.y == board.size - 1 || 
            !isWhite && this.y == 0)
            this.setKing();
    }

    /**
     * Move piece on board
     * @param x x coord of move
     * @param y y coord of move
     */
    public void moveTo(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Get all possible moves given board
     * @param board board to check
     * @return Returns all possible moves of piece
     */
    public Move[] getAllPossibleMoves(Board board) {
        // Create list of all moves
        ArrayList<Move> moves = new ArrayList<Move>();

        // Change y endpoints depending on the King-ness and color/direction of movement
        int startingY, yIncrement;
        // If white move further down the board
        if (isWhite) {
            startingY = this.y + 1; 
            yIncrement = -2;
        // If black move further up the board
        } else {
            startingY = this.y - 1;
            yIncrement = 2;
        }

        int rowsToCheck = 1;
        if(this.isKing) rowsToCheck = 2;
  

        moves = addMoves(moves, board, yIncrement, startingY, rowsToCheck);

        // After checking all normal moves, look for and add all possible jumps
        Move[] possibleJumps = this.getAllPossibleJumps(board, null);
        if(possibleJumps != null)
            moves.addAll(Arrays.asList(possibleJumps));
        // If there are moves, return as normal array
        if(!moves.isEmpty()) {
            moves.trimToSize();
            return moves.toArray(new Move[1]);
        // Otherwise return null
        } else { return null; }
    }

    /**
     * Add possible non-jumping moves
     * @param moves Array of moves
     * @param board board to work with
     * @param yIncrement
     * @param startingY
     * @param rowsToCheck
     * @return Array of moves
     */
    public ArrayList<Move> addMoves(ArrayList<Move> moves, Board board, int yIncrement, int startingY, int rowsToCheck) {
        // Go through the 4 spaces where normal moves are possible
        for(int x = this.x - 1; x <= this.x + 1; x += 2) {
            // Go over rows
            int y = startingY - yIncrement;
            for(int i = 0; i < rowsToCheck; i++) { y += yIncrement; // Increment if needed
                // Check if board if going over edge 
                if(board.isOverEdge(x, y)) continue;
                // Add move if no piece
                if(board.getValueAt(x, y) == null) {
                    moves.add(new Move(this.x, this.y, x, y, null, false)); 
                }
            }
        }
        return moves;
    }

    /**
     * Finds all jumping moves from piece
     * Uses recursion to do this; for each move, a new "imaginary" piece will be created
     * @param board Board to work with
     * @param precedingMove 
     * @return
     */
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
            
        moves = getMoves(startingY, yIncrement, rowsToCheck, board, precedingMove, moves);
        
        // If there are moves, then shorten and return ArrayList as normal Array
        if(!moves.isEmpty()) {
            moves.trimToSize();
            return moves.toArray(new Move[1]);
        } else {
            // Otherwise return null to show no moves
            return null;
		}
    }

    /**
     * Helper method to get jumping moves
     * @param startingY
     * @param yIncrement
     * @param rowsToCheck
     * @param board
     * @param precedingMove
     * @param moves
     * @return Returns all moves that can be jumps
     */
    public ArrayList<Move> getMoves(int startingY, int yIncrement, int rowsToCheck, Board board, Move precedingMove, ArrayList<Move> moves) {
        // Iterate over 4 spaces where non-jumping moves are possible
        for(int x = this.x - 2; x <= this.x + 2; x += 4) {
            int y = startingY - yIncrement;
            for(int i = 0; i < rowsToCheck; i++) {
                // Increment y if needed (Will have no effect if only 1 iteration is run)
                y += yIncrement;
    
                // if going over board, skip this iteration
                if(board.isOverEdge(x, y))
                    continue;
    
                if(precedingMove != null && x == precedingMove.getStartingPosition()[0] && y == precedingMove.getStartingPosition()[1])
                    continue;
    
                // Check if different coloured piece is between average of our position and the starting point
                // Also check if there is no piece is planned landing space
                Piece betweenPiece = board.getValueAt((this.x + x)/2, (this.y + y)/2);
                if(betweenPiece != null && betweenPiece.isWhite != this.isWhite && board.getValueAt(x, y) == null) {
                    // If works then add jumping move there and note that is jump
                    Move jumpingMove = new Move(this.x, this.y, x, y, precedingMove, true);
                    // Add to list
                    moves.add(jumpingMove);
                    // Create imaginary piece to check for more jumps
                    Piece imaginaryPiece = new Piece(x, y, this.isWhite);
                    // If piece is King make imaginary piece King as well
                    if(this.isKing) imaginaryPiece.setKing();
    
                    // Find possible subsequent moves 
                    Move[] subsequentMoves = imaginaryPiece.getAllPossibleJumps(board, jumpingMove);
                    // If they exist then add to list
                    if(subsequentMoves != null)
                        moves.addAll(Arrays.asList(subsequentMoves));
                }
            }
        }
        return moves;
    }


    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isKing() {
        return isKing;
    }

    public boolean isWhite() {
        return isWhite;
    }

    public void setWhite(boolean isWhite) {
        this.isWhite = isWhite;
    }

}
