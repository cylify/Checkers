public abstract class Player {
	/**
	 * 
	 * @param board The board to apply to 
	 * @return Returns board, modified by player
	 */
	public abstract Board getMove(Board board);
}