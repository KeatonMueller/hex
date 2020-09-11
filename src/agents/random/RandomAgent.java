package src.agents.random;

import src.Board;
import src.agents.Agent;

public class RandomAgent implements Agent {
    public int getMove(Board b) {
        int move;
        do {
            move = (int) (Math.random() * b.dim * b.dim);
        } while (!b.isValid(move));
        return move;
    }

    public String type() {
        return "random";
    }
}
