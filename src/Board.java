package src;

public class Board {
	private int[] board;
	private int dim;
	private int turn;

	public Board(int _dim) {
		dim = _dim;
		board = new int[dim * dim];
		turn = 0;
	}

	/**
	 * Reset board to initial state
	 */
	public void reset() {
		board = new int[dim * dim];
		turn = 0;
	}

	/**
	 * Determine if given board position is a valid move
	 * 
	 * @param idx The position of the move
	 * @return true if move is valid, else false
	 */
	public boolean isValid(int idx) {
		return board[idx] == 0;
	}

	/**
	 * Makes a move on the board
	 * 
	 * @param idx  The position of the move
	 * @param turn Turn indicator (0 | 1)
	 */
	public void move(int idx) {
		board[idx] = turn + 1;
		turn = 1 - turn;
	}

	/**
	 * Get the value of the board at the given index. Caller must validate index
	 * 
	 * @param idx The index of the board to access
	 * @return The value of the board at the given index
	 */
	public int get(int idx) {
		return board[idx];
	}

	/**
	 * Get the current turn indicator
	 * 
	 * @return 0 if player 1's turn, 1 if player 2's turn
	 */
	public int getTurn() {
		return turn;
	}

	/**
	 * Returns the printable char for board value
	 * 
	 * @param val The board value
	 * @return The corresponding char to print
	 */
	private char getChar(int val) {
		switch (val) {
			case 0:
				return '.';
			case 1:
				return 'X';
			case 2:
				return 'O';
		}
		return '?';
	}

	/**
	 * Prints board state to console
	 */
	public void show() {
		int idx, i, row, col;

		// print column labels
		System.out.print("  ");
		for (i = 0; i < dim; i++) {
			System.out.print((i + 1) + " ");
			if (i < 8)
				System.out.print(' ');
		}
		System.out.println();

		for (row = 0; row < dim; row++) {
			// print leading whitespace & row label
			for (i = 0; i < row; i++)
				System.out.print(' ');
			System.out.print((char) (row + 'a'));
			System.out.print("\\");

			// print row, excluding final column
			for (col = 0; col < dim - 1; col++) {
				idx = row * dim + col;
				System.out.print(getChar(board[idx]) + "  ");
			}

			// print last column value & row label
			System.out.print(getChar(board[(row + 1) * dim - 1]) + "\\" + (char) (row + 'a'));
			System.out.println();
		}

		// print bottom column labels
		for (i = 0; i < dim + 2; i++)
			System.out.print(' ');
		for (i = 0; i < dim; i++) {
			System.out.print((i + 1) + " ");
			if (i < 8)
				System.out.print(' ');
		}
		System.out.println();
	}
}
