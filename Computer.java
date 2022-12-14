import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;

public class Computer extends Player {
    boolean isWhite;

    /**
     * Constructer
     * @param isWhite colour of "player"
     */
    public Computer(boolean isWhite) {
        this.isWhite = isWhite;
    }

    /**
     * Gets move of "AI"
     * @param board board to apply move
     * @return Returns board which has been modified due to AI move
     */
    public Board getMove(Board board) {

        // HashMap of possible pieces and their moves
        HashMap<Piece, Move[]> possibleChoices = new HashMap<Piece, Move[]>();

        possibleChoices = getChoice(board, possibleChoices);

        // Keep track of furthest back and forward piece to alternate between
        Piece furthestBackwardPiece = possibleChoices.keySet().toArray(new Piece[1])[0];
        Piece furthestForwardPiece = possibleChoices.keySet().toArray(new Piece[1])[0];

        HashMap<Move, Piece> bestMovesPerPiece = new HashMap<Move, Piece>();
        bestMovesPerPiece = getBestMoves(possibleChoices, furthestBackwardPiece, furthestForwardPiece, board, bestMovesPerPiece);
        Move absoluteBestMove = bestMovesPerPiece.keySet().toArray(new Move[1])[0];
        int absoluteBestMoveJumpLength = 0;
        // Go through all best moves for piece and get best
        for (Move move : bestMovesPerPiece.keySet()) {
            // Get length of piece's best move's jump
            Piece[] jumpedPieces = move.getJumpedPieces(board);

            if (jumpedPieces != null) {
                int thisBestMoveJumpLength = jumpedPieces.length;
                // If move is better than previous then apply else keep same
                if (thisBestMoveJumpLength >= absoluteBestMoveJumpLength) {
                    absoluteBestMoveJumpLength = thisBestMoveJumpLength;
                    absoluteBestMove = move;
                }
            }
        }

        // If jump to apply, apply it
        if(absoluteBestMoveJumpLength > 0) {
            board.applyMoveToBoard(absoluteBestMove, bestMovesPerPiece.get(absoluteBestMove));
        // Otherwise, choose 50/50 chance (which is random) to apply furthest back or furthest forward piece 
        } else {
            int randomNum = new Random().nextInt(2);
            if(randomNum == 0) {
                board.applyMoveToBoard(getKeyByValue(bestMovesPerPiece, furthestBackwardPiece), furthestBackwardPiece);
            } else {
                board.applyMoveToBoard(getKeyByValue(bestMovesPerPiece, furthestForwardPiece), furthestForwardPiece);
            }
        }
        return board;
    }

    /**
     * Get best moves for pieces in map
     * @param possibleChoices all possible moves for each piece
     * @param furthestBackwardPiece 
     * @param furthestForwardPiece
     * @param board board to work with 
     * @param bestMovesPerPiece
     * @return Returns HashMap of bestMoves for each piece
     */
    public HashMap<Move, Piece> getBestMoves(HashMap<Piece, Move[]> possibleChoices, Piece furthestBackwardPiece, 
    Piece furthestForwardPiece, Board board, HashMap<Move, Piece> bestMovesPerPiece) {
        // Iterate over collected possible choices and check for best choice
        for(Piece piece : possibleChoices.keySet()) {
            // Check if furthest back or forward
            int thisPieceY = piece.getCoordinates()[1];

            Piece[] furthPieceArr = checkChoice(thisPieceY, furthestBackwardPiece, furthestForwardPiece, furthestForwardPiece);
            furthestBackwardPiece = furthPieceArr[0];
            furthestForwardPiece = furthPieceArr[1];

            Move[] possibleMoves = possibleChoices.get(piece);
            Move maxJumpMove = possibleMoves[0];
            int maxJumpMoveLength = 0;
            // Iterate through possible moves and keep track of jumped numbers
            for (int i = 0; i < possibleMoves.length; i++) {
                Piece[] jumpedPieces = possibleMoves[i].getJumpedPieces(board);
                // Get jump length by num of pieces jumped, ignore if not jump move
                if (jumpedPieces != null) {
                    int jumpLength = jumpedPieces.length;
                    if (jumpLength >= maxJumpMoveLength) {
                        maxJumpMoveLength = jumpLength;
                        maxJumpMove = possibleMoves[i];
                    }
                }
            }
            // Add best move to Map for piece
            bestMovesPerPiece.put(maxJumpMove, piece);
        }
        return bestMovesPerPiece;
    }


    /**
     * Helper method to check if piece is furthest back or forward
     * @param thisPieceY
     * @param furthestBackwardPiece
     * @param furthestForwardPiece
     * @param piece
     * @return Returns array of pieces
     */
    public Piece[] checkChoice(int thisPieceY, Piece furthestBackwardPiece, Piece furthestForwardPiece, Piece piece) {
        if(thisPieceY > furthestForwardPiece.getCoordinates()[1]) {
            if(isWhite)
                furthestForwardPiece = piece;
            else
                furthestBackwardPiece = piece;
        } else if(thisPieceY < furthestBackwardPiece.getCoordinates()[1]) {
            if(isWhite)
                furthestBackwardPiece = piece;
            else
                furthestForwardPiece = piece;
        }
        Piece[] pieceArr = {furthestBackwardPiece, furthestForwardPiece};
        return pieceArr;
    }

    /**
     * 
     * @param board
     * @param possibleChoices
     * @return Returns hashmap of all possible choices
     */
    public HashMap<Piece, Move[]> getChoice(Board board, HashMap<Piece, Move[]> possibleChoices) {
        // Loop over all pieces on board until you find piece that works
        for (int x = 0; x < board.size; x++) {
            for (int y = 0; y < board.size; y++) {
                Piece piece = board.getValueAt(x, y);
                // If piece is same colour as our piece
                if (piece != null && piece.isWhite == this.isWhite) {
                    // Get the moves of piece
                    Move[] possibleMoves = piece.getAllPossibleMoves(board);
                    // Put move with piece in HashMap if there is at least one
                    // If not, nothing is done
                    if (possibleMoves != null)
                        possibleChoices.put(piece, possibleMoves);
                }
            }
        }
        return possibleChoices;
    }


    /**
     * Returns Move found in HashMap given Piece
     * @param map Map to search
     * @param value Piece to search for
     * @return Returns Move found in map, returns null if not found
     */
    private <K,V> K getKeyByValue(HashMap<K, V> map, V value) {
        // Go through map entry
        for(Entry<K, V> entry : map.entrySet()) {
            // Checks if val in map is same as val to search
            if(value.equals(entry.getValue())) {
                // If so return key
                return entry.getKey();
            }
        }
        return null;
    }
}
