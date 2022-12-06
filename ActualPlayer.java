import java.util.InputMismatchException;
import java.util.Scanner;

public class ActualPlayer extends Player {
    Scanner in = new Scanner(System.in);    
    boolean isWhite;
    
    /**
     * Constructor.
     * 
     * @param isWhite Represents if the player is white.
     */
    public ActualPlayer(boolean isWhite) {
		this.isWhite = isWhite;
    }

    /**
     * Gets move and applies move onto board.
     * 
     * @param board Represents the board.
     * @return the board.
     */
    public Board getMove(Board board) {
        Board.displayBoard(board, null);

        Move[] possibleMoves;
        // Keep asking till valid move is inputted
        while (true) {
            // Ask user for piece to play
            Piece pieceMoving = getPieceFromUser(board);

            // Check for quit
            if (pieceMoving == null)
                return board;

            // Find all possible moves of user's piece
            possibleMoves = pieceMoving.getAllPossibleMoves(board);

            // If no moves found say that there aren't any
            if(possibleMoves == null) System.out.println("That piece has no possible moves! Please choose another:");
            // Otherwise, show user all possible moves
            else {
                Board.displayBoard(board, possibleMoves);
                // Get move from user
                Move move = getMoveFromUser(possibleMoves);
                // Apply move to board if valid
                if(move != null) {
                    board.applyMoveToBoard(move, pieceMoving);
                    return board;
                }
            }
        } 
    }

    
    /**
     * Takes in user input and validates if the piece selected is valid.
     * 
     * @param board Represents the board.
     * @return the piece the user selected.
     * @throws IllegalArgumentException
     */
    private Piece getPieceFromUser(Board board) throws IllegalArgumentException {
        // Keep asking till valid input is inputted
        while (true) {
            String coords;

            System.out.println(getColour() + ", please select a piece by its coordinates (i.e. A3):");
            try {    
                coords = in.nextLine().toLowerCase();
                // Allow user to exit
                if(coords.equalsIgnoreCase("exit")) {
                    RunCheckers.killGame();
                    return null;
                // Check if valid coord input
                } else if (coords.length() < 2)
                    throw new IllegalArgumentException("Not valid input");  

                Piece userPiece = null;
                // Get piece
                userPiece = getPiece(coords, board, userPiece);

                // Check if piece is valid (if isn't null and is the player's colour)
                if(userPiece == null)
                    System.out.println("There is no piece there!\n");
                else if(userPiece.isWhite != this.isWhite)
                    System.out.println("That's not your piece!\n");
                else
                    return userPiece;  
            } catch(IllegalArgumentException e) {
               System.out.println("Please enter a coordinate on the board in the form '[letter][number]'.");
               continue;
            }
        }
    }

    /**
     * Gets the piece selected by the user.
     * 
     * @param coords Represents the coordinates.
     * @param board Represents the board.
     * @param userPiece Represents the piece selected by the user.
     * @return the piece the user selected.
     * @throws IllegalArgumentException
     */
    public Piece getPiece(String coords, Board board, Piece userPiece) throws IllegalArgumentException {
        // Assume that user inputted [letter][number] but flip if not true
        char letterChar = coords.charAt(0);
        char numberChar = coords.charAt(1);
        if (letterChar < 97) {
            letterChar = numberChar;
            numberChar = coords.charAt(0);
        }   

        // Get coords by shifting char to numeric val
        int x = letterChar - 97;
        int y = numberChar - 48 - 1;

        // Check if no out of bounds inputs
        if (board.isOverEdge(x, y))
            throw new IllegalArgumentException("Not valid input");

        // Get piece at location
        userPiece = board.getValueAt(x, y);

        return userPiece;
    }

    /**
     * Gets a move from the piece the user selected.
     * 
     * @param possibleMoves Represents the possible moves.
     * @return the move the user inputted.
     * @throws IllegalArgumentException
     * @throws InputMismatchException
     */
    private Move getMoveFromUser(Move[] possibleMoves) throws IllegalArgumentException, InputMismatchException {
        int moveNum;

        // Keep trying till valid input is reached
        while(true) {       
            System.out.println(getColour() + ", please select a move the its number (enter 0 to go back):");
            try {
                try {
                    moveNum = in.nextInt();
                    in.nextLine();

                    // Allow user to go back
                    if(moveNum == 0) {
                        return null;
                    // Check if valid input
                    } else if (moveNum > possibleMoves.length)
                        throw new IllegalArgumentException("Not valid input");                    
                    // Return move user entered once we get a valid entry
                    return possibleMoves[moveNum - 1];
                } catch (InputMismatchException e) {
                    System.out.println("Please enter a valid input type.");
                    in.nextLine();
                }
            } catch (IllegalArgumentException e) {
               System.out.println("Please enter one of the numbers on the board or 0 to exit.");
               in.nextLine();
            }
        }
    }
    
    /**
     * String of current player colour
     * @return Returns colour
     */
    private String getColour() {
        return isWhite ? "White" : "Black";
    }
}
