package src;

import java.util.Scanner;
import java.util.HashSet;
import java.util.Queue;
import java.util.ArrayDeque;

import src.agents.Agent;
import src.agents.random.RandomAgent;

public class Game {
    private Board b;
    private Scanner scan;
    private int dim;
    private Agent opponent;

    /**
     * Print game information and begin startup process
     */
    public Game() {
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
    }

    /**
     * Prompt user for board dimensions and initialize Board
     */
    private void initBoard() {
        System.out.print("Enter board dimension (default=11): ");
        try {
            // minimum dimension = 1
            dim = Math.max(1, Integer.parseInt(scan.nextLine()));
        } catch (Exception e) {
            dim = 11;
        }
        b = new Board(dim);
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
            if (col < 0 || col >= dim || row < 0 || row >= dim || !b.isValid(row * dim + col)) {
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
    public int getMove(int turn) {
        if (turn == 0 || opponent == null) {
            return prompt(turn);
        }
        return opponent.getMove(b, dim);
    }

    /**
     * Run the game loop, prompting user for input and displaying the board
     */
    public void run() {
        int move, winner, turn = 0;
        while (true) {
            b.show();

            move = getMove(turn);
            if (move == -1)
                return;
            b.move(move, turn);

            winner = checkWin();
            if (winner != 0) {
                b.show();
                System.out.println("Player " + winner + " won!");
                break;
            }
            turn = 1 - turn;
        }

        System.out.println("Play again? (yes | no)");
        if (scan.nextLine().equalsIgnoreCase("yes")) {
            initBoard();
            chooseOpponent();
            run();
        }
    }

    /**
     * Utility function for BFS. Validates and checks given row and col for player's piece
     * 
     * @param row     The row number to check
     * @param col     The column number to check
     * @param queue   The queue used for BFS
     * @param visited Visited set to avoid loops
     * @param player  Player who we're checking (1 | 2)
     * @return 1 if the player has reached their goal, 0 otherwise
     */
    public int check(int row, int col, Queue<Integer> queue, HashSet<Integer> visited, int player) {
        if (row < 0 || row >= dim || col < 0 || col >= dim)
            return 0;

        int idx = row * dim + col;

        if (visited.contains(idx))
            return 0;

        if (b.get(idx) == player) {
            if ((player == 1 && row == dim - 1) || (player == 2 && col == dim - 1)) {
                return 1;
            }
            visited.add(idx);
            queue.add(idx);
        }
        return 0;
    }

    /**
     * Run BFS to check for a win
     * 
     * @param queue   Queue used for BFS
     * @param visited Visited set to avoid loops
     * @param player  Player we're checking a win for (1 | 2)
     * @return Winning player (1 | 2) or 0 if no winner
     */
    public int bfs(Queue<Integer> queue, HashSet<Integer> visited, int player) {
        int idx, row, col, win = 0;
        while (!queue.isEmpty()) {
            idx = queue.poll();
            row = idx / dim;
            col = idx % dim;

            win |= check(row - 1, col, queue, visited, player);
            win |= check(row + 1, col, queue, visited, player);
            win |= check(row, col - 1, queue, visited, player);
            win |= check(row, col + 1, queue, visited, player);
            win |= check(row - 1, col + 1, queue, visited, player);
            win |= check(row + 1, col - 1, queue, visited, player);

            if (win == 1) {
                return player;
            }
        }
        return 0;
    }

    /**
     * Check if either Player 1 or Player 2 has won the game. Does so by running BFS to see if
     * opposite sides are linked
     * 
     * @return Winning player (1 | 2) or 0 if no winner
     */
    public int checkWin() {
        // handle 1x1 board corner case
        if (dim == 1)
            return 1;

        HashSet<Integer> visited = new HashSet<>();
        Queue<Integer> queue = new ArrayDeque<>();

        int i;
        // add top row pieces for player 1
        for (i = 0; i < dim; i++) {
            if (b.get(i) == 1) {
                queue.add(i);
                visited.add(i);
            }
        }
        if (bfs(queue, visited, 1) == 1)
            return 1;

        visited.clear();
        queue.clear();

        // add left column pieces for player 2
        for (i = 0; i < dim * dim; i += dim) {
            if (b.get(i) == 2) {
                queue.add(i);
                visited.add(i);
            }
        }
        if (bfs(queue, visited, 2) == 2)
            return 2;

        return 0;
    }

    /**
     * Perform any cleanup necessary when exiting game
     */
    public void cleanup() {
        scan.close();
    }
}
