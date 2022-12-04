import java.util.Scanner;

public class RunCheckers {

    public static final int SIZE = 8;
    private static Scanner in = new Scanner(System.in);
    private static boolean isPlayer1 = true;
    private static boolean endGameNow = false;
    
    public static void main(String[] args) {
        // Create board
        Board board = new Board(SIZE);

        // Define abstract classes
        Player player1;
        Player player2;

        // Print menu for user
        PrintMenu();

        if(AskIfTwoPlayer()) {
            player1 = new ActualPlayer(true);
            player2 = new ActualPlayer(false);
        } else {         
            player1 = new ActualPlayer(true);
            player2 = new Computer(false);
        }

        clearScreen();
        
        while(!endGame(board)) {          
            if(isPlayer1) {
                board = player1.getMove(board);
            } else {
                board = player2.getMove(board);
            }
            // Switch player
            isPlayer1 = !isPlayer1;
        }
    }

    /**
     * Print menu
     */
    public static void PrintMenu() {
        // Clear the screen before printing
        clearScreen();
        // Print instructions and info
        System.out.println("---------Welcome to checkers!---------\n");
        System.out.println("This program allows you to play checkers!\n");
        System.out.println("You are able to have both a PvE and PvP experience\n");
        System.out.println("You are able to play locally with a friend or with an AI\n");
        System.out.println("Enter 'exit' to exit at any point (or 0 when moving a piece).\n");
        System.out.println("\nPlease enter [1] if you would like to play locally!");
        System.out.println("\nPlease enter [2] if you would like to play with an AI!\n");
    }


    /**
     * Asks for what gamemode
     * @return Return true if user wants two player else play with AI
     */
    private static boolean AskIfTwoPlayer() {
        // Keep asking till valid response is received    
        while(true) {
            String r = in.nextLine();
            // Check what mode they want to play or exit
            switch (r.trim()) {
                case "1":
                    return true;
                case "2":
                    return false;
                case "exit":
                    endGameNow();
                    return true;
            }
        }
    }

    /**
     * Checks if game is done or in stalement
     * @param board Board to check whether game is completed or in stalemate
     * @return Returns true if want to end game else ends when either user or the AI wins
     */
    private static boolean endGame(Board board) {
        // Safety so if you want to end, it ends immediately
        if(endGameNow) {
            return true;
        } else {
            // For each movable piece
            int movableWhiteNum = 0;
            int movableBlackNum = 0;
            for(int pos = 0; pos < board.size * board.size; pos++) {
                Piece pieceHere = board.getValueAt(pos);
                if(pieceHere != null) {
                    Move[] movesHere = pieceHere.getAllPossibleMoves(board);
                    if(movesHere != null && movesHere.length > 0) {
                        if(pieceHere.isWhite)
                            movableWhiteNum++;
                        else if(!pieceHere.isWhite)
                            movableBlackNum++;
                    }
                }
            }
            

            if(movableWhiteNum + movableBlackNum == 0)
                System.out.println("The game was a stalemate.");
            else if(movableWhiteNum == 0)
                System.out.println("Congratulations, Black, you have won the game!");
            else if(movableBlackNum == 0)
                System.out.println("Congratulations, White, you have won the game!");
            else
                return false;

            return true;
        }
    }
    
    /**
     * Ends game
     */
    public static void endGameNow() {
        endGameNow = true;
    }
    
    /**
     * Clears terminal
     */
    public static void clearScreen() {
        System.out.print("\033[2J\033[1;1H");
    }
}
