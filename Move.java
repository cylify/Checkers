import java.util.*;

public class Move {
    int x1, y1, x2, y2;
    Move precedingMove;
    boolean isJump;
    
    /**
     * Constructer
     * @param x1 Starting x position
     * @param y1 Starting y position
     * @param x2 Ending x position
     * @param y2 Ending y position
     * @param precedingMove Move preceding current one (can be null if first move) 
     * @param isJump Bool if move is a jump
     */
    public Move(int x1, int y1, int x2, int y2, Move precedingMove, boolean isJump) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.precedingMove = precedingMove;
        this.isJump = isJump;
    }

    /**
     * Gets move starting position
     * @return returns 2 part array of coords of moves starting position
     */
    public int[] getStartingPosition() {
        int[] position = new int[2];
        position[0] = x1;
        position[1] = y1;
        return position;
    }

    /**
     * Get moves ending position
     * @return returns 2 part array of moves ending position
     */
    public int[] getEndingPosition() {
        int[] position = new int[2];
        position[0] = x2;
        position[1] = y2;
        return position;
    }
    
    /**
     * Finds pieces that in this move
     * @param board Board to look for pieces
     * @return returns array of pieces that were jumped on
     */
    public Piece[] getJumpedPieces(Board board) {
        // If piece is jumped
        if(isJump) {
            // Create arraylist of pieces
            ArrayList<Piece> pieces = new ArrayList<Piece>();

            // The piece this move is jumping is in between the start and end of move
            int pieceX = (x1 + x2)/2;
            int pieceY = (y1 + y2)/2;

            // Add this most recent jump
            pieces.add(board.getValueAt(pieceX, pieceY));

            if(precedingMove != null) {
                pieces.addAll(Arrays.asList(precedingMove.getJumpedPieces(board))); 
			}

            pieces.trimToSize();
            return pieces.toArray(new Piece[1]);
        } else {
            return null;
		}
    }
}
