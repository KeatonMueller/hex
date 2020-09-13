package src.agents.random;

import src.Board;
import src.agents.Agent;

public class RandomAgent implements Agent {
    public int getMove(Board b, int dim) {
        int move;
        do {
            move = (int) (Math.random() * dim * dim);
        } while (!b.isValid(move));
        return move;
    }
}
