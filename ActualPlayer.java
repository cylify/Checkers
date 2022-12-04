import java.util.Scanner;

public class ActualPlayer extends Player {
    Scanner in = new Scanner(System.in);    
    boolean isWhite;
    
    /**
     * Constructor
     * @param isWhite
     */
    public ActualPlayer(boolean isWhite) {
		this.isWhite = isWhite;
    }

    /**
     * @param board
     * @return Returns board
     */
    public Board getMove(Board board) {        
        Board.displayBoard(board, null);

        Move[] possibleMoves;
        while (true) {
            Piece pieceMoving = getPieceFromUser(board);

            if (pieceMoving == null)
                return board;

            possibleMoves = pieceMoving.getAllPossibleMoves(board);

            if(possibleMoves == null)
                System.out.println("That piece has no possible moves! Please choose another:");
            else {
                Board.displayBoard(board, possibleMoves);
                Move move = getMoveFromUser(possibleMoves);
                if(move != null) {
                    board.applyMoveToBoard(move, pieceMoving);
                    return board;
                }
            }
        } 
    }

    

    private Piece getPieceFromUser(Board board) throws IllegalArgumentException {
        while (true) {       
            String raw;

            System.out.println(getColour() + ", please select a piece by its coordinates (i.e. A3):");
            try {    
                raw = in.nextLine().toLowerCase();
                if(raw.equalsIgnoreCase("exit")) {
                    Main.endGameNow();
                    return null;
                } else if (raw.length() < 2)
                throw new IllegalArgumentException("Not valid input");  
                    
                char letterChar = raw.charAt(0);
                char numberChar = raw.charAt(1);
                if (letterChar < 97) {
                    letterChar = numberChar;
                    numberChar = raw.charAt(0);
                }   
                                
                int x = letterChar - 97;
                int y = numberChar - 48 - 1;

                if (board.isOverEdge(x, y))
                throw new IllegalArgumentException("Not valid input");               

                Piece userPiece = board.getValueAt(x, y);

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


    private Move getMoveFromUser(Move[] possibleMoves) throws IllegalArgumentException {
        int moveNum;

        while (true) {       
            System.out.println(getColour() + ", please select a move the its number (enter 0 to go back):");
            try {
                moveNum = in.nextInt();
                in.nextLine();

                if(moveNum == 0) {
                    return null;
                } else if (moveNum > possibleMoves.length)
                    throw new IllegalArgumentException("Not valid input");                    

                return possibleMoves[moveNum - 1];
            } catch (IllegalArgumentException e) {
               System.out.println("Please enter one of the numbers on the board or 0 to exit.");
               in.nextLine();
            }
        }
    }
    

    private String getColour() {
        return isWhite ? "White" : "Black";
    }
}
