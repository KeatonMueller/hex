package src.game;

import java.util.Scanner;

import src.Board;
import src.agents.Agent;
import src.agents.random.RandomAgent;

public class TextGame {
    private Scanner scan;
    Board board;
    Agent opponent;
    int dim;

    /**
     * Print game information and begin startup process
     */
    public TextGame() {
        super();
        System.out.println();
        System.out.println("Welcome to Hex!");
        System.out.println("Player 1 is X, and is trying to connect the top to the bottom");
        System.out.println("Player 2 is O, and is trying to connect the left to the right");
        System.out.println();
        System.out.println("Enter moves in the format <row><col>");
        System.out.println("\tExample: b3");
        System.out.println();

        scan = new Scanner(System.in);
        initBoard();
        chooseOpponent();
        run();
    }

    /**
     * Prompt user for board dimensions and initialize Board
     */
    private void initBoard() {
        System.out.print("Enter board dimension (default=11): ");
        String dimString = scan.nextLine();
        if (dimString.matches("\\d+")) {
            dim = Math.max(1, Integer.parseInt(dimString));
        } else {
            dim = 11;
        }
        board = new Board(dim);
    }

    /**
     * Prompt user to pick their opponent
     */
    private void chooseOpponent() {
        System.out.print("Opponent? (human | random): ");
        String opp = scan.nextLine();
        switch (opp.trim().toLowerCase()) {
            case "random":
                opponent = new RandomAgent();
                break;
            case "human":
            default:
                opponent = null;
                break;
        }
    }

    /**
     * Continually prompts user for input until valid input is provided. Typing "quit" stops the
     * game
     * 
     * @param turn Turn indicator (0 | 1)
     * @return The index of the move
     */
    private int prompt(int turn) {
        System.out.println("Player " + (turn + 1) + "'s turn (type \"quit\" to exit game)");
        String move;
        int row, col;
        while (true) {
            System.out.print("Enter move: ");
            if ((move = scan.nextLine()).equals(""))
                continue;

            if (move.equalsIgnoreCase("quit"))
                return -1;

            try {
                row = move.charAt(0) - 'a';
                col = Integer.parseInt(move.substring(1)) - 1;
            } catch (Exception e) {
                row = -1;
                col = -1;
            }
            if (col < 0 || col >= dim || row < 0 || row >= dim || !board.isValid(row * dim + col)) {
                System.out.println("Invalid input");
                continue;
            }
            return row * dim + col;
        }
    }


    /**
     * Get the current move. Prompts user for input if it's their turn, or gets move from the
     * current opponent
     * 
     * @param turn Turn indicator (0 | 1)
     * @return The index of the next move
     */
    private int getMove(int turn) {
        if (turn == 0 || opponent == null) {
            return prompt(turn);
        }
        return opponent.getMove(board, dim);
    }

    /**
     * Run the game loop, prompting user for input and displaying the board
     */
    private void run() {
        int move, winner;
        while (true) {
            board.show();

            move = getMove(board.getTurn());
            if (move == -1) {
                scan.close();
                return;
            }
            board.move(move);

            winner = GameCheck.checkWin(board, dim);
            if (winner != 0) {
                board.show();
                System.out.println("Player " + winner + " won!");
                break;
            }
        }

        System.out.println("Play again? (yes | no)");
        if (scan.nextLine().equalsIgnoreCase("yes")) {
            initBoard();
            chooseOpponent();
            run();
        }
        scan.close();
    }
}
