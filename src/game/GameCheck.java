package src.game;

import java.util.HashSet;
import java.util.Queue;
import java.util.ArrayDeque;

import src.Board;

public class GameCheck {
    private static Board board;
    private static int dim;

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
    private static int check(int row, int col, Queue<Integer> queue, HashSet<Integer> visited,
            int player) {
        if (row < 0 || row >= dim || col < 0 || col >= dim)
            return 0;

        int idx = row * dim + col;

        if (visited.contains(idx))
            return 0;

        if (board.get(idx) == player) {
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
    private static int bfs(Queue<Integer> queue, HashSet<Integer> visited, int player) {
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
    public static int checkWin(Board b, int d) {
        board = b;
        dim = d;

        // handle 1x1 board corner case
        if (dim == 1)
            return 1;

        HashSet<Integer> visited = new HashSet<>();
        Queue<Integer> queue = new ArrayDeque<>();

        int i;
        // add top row pieces for player 1
        for (i = 0; i < dim; i++) {
            if (board.get(i) == 1) {
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
            if (board.get(i) == 2) {
                queue.add(i);
                visited.add(i);
            }
        }
        if (bfs(queue, visited, 2) == 2)
            return 2;

        return 0;
    }
}
