import java.util.Scanner;

public class ActualPlayer extends Player {
    Scanner in = new Scanner(System.in);    
    boolean isWhite;
    

    public ActualPlayer(boolean isWhite) {
		this.isWhite = isWhite;
    }


    public Board getMove(Board board) {        
        displayBoard(board, null);

        Move[] possibleMoves;
        while (true) {
            Piece pieceMoving = getPieceFromUser(board);

            if (pieceMoving == null)
                return board;

            possibleMoves = pieceMoving.getAllPossibleMoves(board);

            if(possibleMoves == null)
                System.out.println("That piece has no possible moves! Please choose another:");
            else {
                displayBoard(board, possibleMoves);
                Move move = getMoveFromUser(possibleMoves);
                if(move != null) {
                    board.applyMoveToBoard(move, pieceMoving);
                    return board;
                }
            }
        } 
    }

    private void displayBoard(Board board, Move[] possibleMoves) {
        Main.clearScreen();

        for(int y = -1; y < board.size; y++) {
            for(int x = -1; x < board.size; x++) {
                if(y == -1) {
                    if(x != -1)
                        System.out.print("-" + (char)(x + 65) + "- ");
                    else
                        System.out.print("     ");
                } else if(x == -1) {
                    if(y != -1)
                        System.out.print("-" + (y + 1) + "- ");
                } else {
                    Piece thisPiece = board.getValueAt(x, y);

                    if(possibleMoves != null) {
                        boolean moveFound = false;
                        
                        for(int i = 0; i < possibleMoves.length; i++) {
                            int[] move = possibleMoves[i].getEndingPosition();
                            if(move[0] == x && move[1] == y) {
                                System.out.print("| " + Integer.toString(i + 1) + " ");
                                moveFound = true;
                            }
                        }

                        if(moveFound)
                            continue;
                    }

                    if(thisPiece != null)
                        System.out.print("| " + thisPiece.getString());
                    else if (board.isCheckerboardSpace(x, y))
                        System.out.print("| . ");
                    else
                        System.out.print("|   ");
                }
            }
            System.out.println();
        }
    }
    

    private Piece getPieceFromUser(Board board) {
        while (true) {       
            String raw;

            System.out.println(getColour() + ", please select a piece by its coordinates (i.e. A3):");
            try {    
                raw = in.nextLine().toLowerCase();
                if(raw.equalsIgnoreCase("exit")) {
                    Main.endGameNow();
                    return null;
                } else if (raw.length() < 2)
                    throw new Exception();
                    
                char letterChar = raw.charAt(0);
                char numberChar = raw.charAt(1);
                if (letterChar < 97) {
                    letterChar = numberChar;
                    numberChar = raw.charAt(0);
                }   
                                
                int x = letterChar - 97;
                int y = numberChar - 48 - 1;

                if (board.isOverEdge(x, y))
                    throw new Exception();              

                Piece userPiece = board.getValueAt(x, y);

                if(userPiece == null)
                    System.out.println("There is no piece there!\n");
                else if(userPiece.isWhite != this.isWhite)
                    System.out.println("That's not your piece!\n");
                else
                    return userPiece;  
            } catch(Exception e) {
               System.out.println("Please enter a coordinate on the board in the form '[letter][number]'.");
               continue;
            }
        }
    }


    private Move getMoveFromUser(Move[] possibleMoves) {
        int moveNum;

        while (true) {       
            System.out.println(getColour() + ", please select a move the its number (enter 0 to go back):");
            try {
                moveNum = in.nextInt();
                in.nextLine();

                if (moveNum == 0) {
                    return null;
                } else if (moveNum > possibleMoves.length)
                    throw new Exception();                    

                return possibleMoves[moveNum - 1];
            } catch (Exception e) {
               System.out.println("Please enter one of the numbers on the board or 0 to exit.");
               in.nextLine();
            }
        }
    }
    

    private String getColour() {
        return isWhite ? "White" : "Black";
    }
}
