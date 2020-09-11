package src.agents;

import src.Board;

public interface Agent {
    /**
     * Return agent's next move on the given board state
     * 
     * @param board The Board object the agent will make a move on
     * @return The index of the move
     */
    public int getMove(Board board);

    /**
     * Return this agent's type
     * 
     * @return String corresponding to this agent's type
     */
    public String type();
}