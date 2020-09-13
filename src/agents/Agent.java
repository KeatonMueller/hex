package src.agents;

import src.Board;

public interface Agent {
    /**
     * Return agent's next move on the given board state
     * 
     * @param board The Board object the agent will make a move on
     * @param dim   The dimension of the board
     * @return The index of the move
     */
    public int getMove(Board board, int dim);
}
