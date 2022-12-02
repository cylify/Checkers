import java.util.*;

public class Move {
    int x1, y1, x2, y2;
    Move precedingMove;
    boolean isJump;
    
    /**
     * Constructer
     * @param x1 
     * @param y1
     * @param x2
     * @param y2
     * @param precedingMove
     * @param isJump
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
     * 
     * @return
     */
    public int[] getStartingPosition() {
        int[] position = new int[2];
        position[0] = x1;
        position[1] = y1;
        return position;
    }

    /**
     * 
     * @return
     */
    public int[] getEndingPosition() {
        int[] position = new int[2];
        position[0] = x2;
        position[1] = y2;
        return position;
    }
    
    /**
     * 
     * @param board
     * @return
     */
    public Piece[] getJumpedPieces(Board board) {
        if(isJump) {
            ArrayList<Piece> pieces = new ArrayList<Piece>();

            int pieceX = (x1 + x2)/2;
            int pieceY = (y1 + y2)/2;

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
