public class Board {
    public Piece[][] board;
    public int size;

    /**
     * Makes the board.
     * 
     * @param size Represents the size of the board.
     */
    public Board(int size) {
        this.board = new Piece[size][size];
        this.size = size;
        setupBoard();
    }

    /**
     * Makes the board.
     * 
     * @param board Represents the board.
     */
    public Board(Board board) {
        this.board = board.board;
        this.size = board.size;
    }

    /**
     * Setup the board with pieces
     */
    public void setupBoard() {
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                if (y < 3 && isCheckerboardSpace(x, y)) {
                    this.board[y][x] = new Piece(x, y, true);
                } else if (y >= size - 3 && isCheckerboardSpace(x, y)) {
                    this.board[y][x] = new Piece(x, y, false);
                }
            }
        }
    }

    /**
     * Applies move of piece to the board.
     * 
     * @param move  Represents the move.
     * @param piece Represents the piece being moved.
     */
    public void applyMoveToBoard(Move move, Piece piece) {
        int[] moveStartingPos = piece.getCoordinates();
        int[] moveEndingPos = move.getEndingPosition();

        Piece[] jumpedPieces = move.getJumpedPieces(this);
        if (jumpedPieces != null) {
            for (int i = 0; i < jumpedPieces.length; i++) {
                if (jumpedPieces[i] != null) {
                    this.setValueAt(jumpedPieces[i].getCoordinates()[0], jumpedPieces[i].getCoordinates()[1], null);
                }
            }
        }

        this.setValueAt(moveStartingPos[0], moveStartingPos[1], null);
        piece.moveTo(moveEndingPos[0], moveEndingPos[1]);
        piece.checkIfShouldBeKing(this);
        this.setValueAt(moveEndingPos[0], moveEndingPos[1], piece);
    }

    /**
     * Displays the board.
     * 
     * @param board         Represents the board.
     * @param possibleMoves Represents the possible moves.
     */
    public static void displayBoard(Board board, Move[] possibleMoves) {
        // Clear screen
        RunCheckers.clearScreen();

        // Include hidden top row for coords
        for (int y = -1; y < board.size; y++) {
            // Include hidden left col for coords
            for (int x = -1; x < board.size; x++) {
                if (y == -1) {
                    // Skip hidden col
                    if (x != -1)
                        System.out.print("-" + (char) (x + 65) + "- ");
                    else
                        System.out.print("     ");
                } else if (x == -1) {
                    if (y != -1) // Skip hidden row
                        System.out.print("-" + (y + 1) + "- ");
                } else {
                    // Get piece here
                    Piece thisPiece = board.getValueAt(x, y);

                    if (possibleMoves != null) {
                        boolean moveFound = false;

                        moveFound = BoolMove(moveFound, possibleMoves, x, y);
                        if (moveFound)
                            continue;
                    }
                    PrintSquares(thisPiece, board, x, y);
                }
            }
            System.out.println();
        }
    }

    /**
     * Prints spaces, pieces, and the checkerboard spaces on the board.
     * 
     * @param thisPiece Represents the selected piece.
     * @param board     Represents the board.
     * @param x         Represents the columns of the board.
     * @param y         Represents the rows of the board.
     */
    public static void PrintSquares(Piece thisPiece, Board board, int x, int y) {
        if (thisPiece != null)
            System.out.print("| " + thisPiece.getString());
        else if (board.isCheckerboardSpace(x, y))
            System.out.print("| . ");
        else
            System.out.print("|   ");
    }

    /**
     * Places numbers on the board to represent the possible moves of a selected
     * piece.
     * 
     * @param moveFound     Represents if it is a possible move from the selected
     *                      piece.
     * @param possibleMoves Represents the possible moves.
     * @param x             Represents the columns of the board.
     * @param y             Represents the rows of the board.
     * @return
     */
    public static boolean BoolMove(Boolean moveFound, Move[] possibleMoves, int x, int y) {
        for (int i = 0; i < possibleMoves.length; i++) {
            int[] move = possibleMoves[i].getEndingPosition();
            if (move[0] == x && move[1] == y) {
                System.out.print("| " + Integer.toString(i + 1) + " ");
                moveFound = true;
            }
        }
        return moveFound;
    }

    /**
     * Puts the piece in the place of the coordinates. Can be null if no piece is
     * present which results in an empty space.
     * 
     * @param x     Represents the x value of the piece.
     * @param y     Represents the y value of the piece.
     * @param piece Represents the piece to put in at the coordinates.
     */
    private void setValueAt(int x, int y, Piece piece) {
        this.board[y][x] = piece;
    }

    /**
     * Gets the value of the space at the coordinates.
     * 
     * @param x Represents the x value of the piece.
     * @param y Represents the y value of the piece.
     * @return The value of the space at the coordinates.
     */
    public Piece getValueAt(int x, int y) {
        return this.board[y][x];
    }

    /**
     * 
     * @param position  
     * @return
     */
    public Piece getValueAt(int position) {
        int[] coords = getCoordinatesFromPosition(position);
        return this.getValueAt(coords[0], coords[1]);
    }

    /**
     * 
     * @param position
     * @return 
     */
    public int[] getCoordinatesFromPosition(int position) {
        int[] coords = new int[2];
        coords[0] = position % this.size;
        coords[1] = position / this.size;
        return coords;
    }

    /**
     * 
     * 
     * @param x Represents the columns of the board.
     * @param y Represents the rows of the board.
     * @return
     */
    public int getPositionFromCoordinates(int x, int y) {
        return this.size * y + x;
    }

    /**
     * Checks if it is a checkerboard space.
     * 
     * @param x Represents the columns of the board.
     * @param y Represents the rows of the board.
     * @return
     */
    public boolean isCheckerboardSpace(int x, int y) {
        return x % 2 == y % 2;
    }

    /**
     * 
     * @param x Represents the columns of the board.
     * @param y Represents the rows of the board.
     * @return
     */
    public boolean isOverEdge(int x, int y) {
        return (x < 0 || x >= this.size || y < 0 || y >= this.size);
    }

    /**
     * 
     * @param position
     * @return
     */
    public boolean isOverEdge(int position) {
        int[] coords = getCoordinatesFromPosition(position);
        return this.isOverEdge(coords[0], coords[1]);
    }

    /**
     * Gets the board.
     * 
     * @return the board.
     */
    public Piece[][] getBoard() {
        return board;
    }

    /**
     * Sets the board
     * 
     * @param board Represent the board.
     */
    public void setBoard(Piece[][] board) {
        this.board = board;
    }

    /**
     * Gets the size of the board.
     * 
     * @return the size of the board.
     */
    public int getSize() {
        return size;
    }

    /**
     *  Sets the size of the board.
     * 
     * @param size Represents the size of the board.
     */
    public void setSize(int size) {
        this.size = size;
    }
}
