public class Board {
    public Piece[][] board;
    public int size;

    /**
     * Constructer
     * @param size Size of board
     */
    public Board(int size) {
        this.board = new Piece[size][size];
        this.size = size;
        setupBoard();
    }

    /**
     * 
     * @param board
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
     * Apply move of piece to board
     * @param move get move
     * @param piece what piece is moved
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


    public static void displayBoard(Board board, Move[] possibleMoves) {
        RunCheckers.clearScreen();

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
                    else if(board.isCheckerboardSpace(x, y))
                        System.out.print("| . ");
                    else
                        System.out.print("|   ");
                }
            }
            System.out.println();
        }
    }

    /**
     * 
     * @param x
     * @param y
     * @param piece
     */
    private void setValueAt(int x, int y, Piece piece) {
        this.board[y][x] = piece;
    }


    /**
     * 
     * @param x
     * @param y
     * @return
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
     * @param x
     * @param y
     * @return
     */
    public int getPositionFromCoordinates(int x, int y) {
        return this.size * y + x;
    }

    /**
     * 
     * @param x
     * @param y
     * @return
     */
    public boolean isCheckerboardSpace(int x, int y) {
        return x % 2 == y % 2;
    }

    /**
     * 
     * @param x
     * @param y
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
}
