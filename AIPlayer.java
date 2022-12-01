import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Random;

public class AIPlayer extends Player {

    boolean isWhite;

    public AIPlayer(boolean isWhite) {
        this.isWhite = isWhite;
    }


    public Board getMove(Board board) {

        HashMap<Piece, Move[]> possibleChoices = new HashMap<Piece, Move[]>();

        for (int x = 0; x < board.size; x++) {
            for (int y = 0; y < board.size; y++) {
                Piece piece = board.getValueAt(x, y);
                if (piece != null && piece.isWhite == this.isWhite) {
                    Move[] possibleMoves = piece.getAllPossibleMoves(board);

                    if (possibleMoves != null)
                        possibleChoices.put(piece, possibleMoves);
                }
            }
        }

        Piece furthestBackwardPiece = possibleChoices.keySet().toArray(new Piece[1])[0];
        Piece furthestForwardPiece = possibleChoices.keySet().toArray(new Piece[1])[0];

        HashMap<Move, Piece> bestMovesPerPiece = new HashMap<Move, Piece>();
        for (Piece piece : possibleChoices.keySet()) {
            int thisPieceY = piece.getCoordinates()[1];
            if (thisPieceY > furthestForwardPiece.getCoordinates()[1]) {
                if (isWhite)
                    furthestForwardPiece = piece;
                else
                    furthestBackwardPiece = piece;
            } else if (thisPieceY < furthestBackwardPiece.getCoordinates()[1]) {
                if (isWhite)
                    furthestBackwardPiece = piece;
                else
                    furthestForwardPiece = piece;
            }

            Move[] possibleMoves = possibleChoices.get(piece);
            Move maxJumpMove = possibleMoves[0];
            int maxJumpMoveLength = 0;
            for (int i = 0; i < possibleMoves.length; i++) {
                Piece[] jumpedPieces = possibleMoves[i].getJumpedPieces(board);
                if (jumpedPieces != null) {
                    int jumpLength = jumpedPieces.length;
                    if (jumpLength >= maxJumpMoveLength) {
                        maxJumpMoveLength = jumpLength;
                        maxJumpMove = possibleMoves[i];
                    }
                }
            }

            bestMovesPerPiece.put(maxJumpMove, piece);
        }

        Move absoluteBestMove = bestMovesPerPiece.keySet().toArray(new Move[1])[0];
        int absoluteBestMoveJumpLength = 0;
        for (Move move : bestMovesPerPiece.keySet()) {
            Piece[] jumpedPieces = move.getJumpedPieces(board);

            if(jumpedPieces != null) {
                int thisBestMoveJumpLength = jumpedPieces.length;

                if(thisBestMoveJumpLength >= absoluteBestMoveJumpLength) {
                    absoluteBestMoveJumpLength = thisBestMoveJumpLength;
                    absoluteBestMove = move;
                }
            }
        }

        if(absoluteBestMoveJumpLength > 0) {
            board.applyMoveToBoard(absoluteBestMove, bestMovesPerPiece.get(absoluteBestMove));
        } else {
            int randomNum = new Random().nextInt(2);
            if(randomNum == 0) {
                board.applyMoveToBoard(getKeyfromVal(bestMovesPerPiece, furthestBackwardPiece), furthestBackwardPiece);
            } else {
                board.applyMoveToBoard(getKeyfromVal(bestMovesPerPiece, furthestForwardPiece), furthestForwardPiece);
            }
        }

        return board;
    }

    private <T, E> T getKeyfromVal(HashMap<T, E> map, E value) {
        for(Entry<T, E> entry : map.entrySet()) {
            if(Objects.equals(value, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }
}
