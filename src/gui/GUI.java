package src.gui;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import src.agents.Agent;
import src.Board;

public class GUI extends JFrame {
    private static final long serialVersionUID = 1L;
    private static final int FRAME_X = 400, FRAME_Y = 100;
    private static final int FRAME_WIDTH = 1000, FRAME_HEIGHT = 700;
    private static final String FRAME_TITLE = "Hex";

    private Canvas canvas;
    private Agent opponent;
    private int dim;

    public GUI() {
        super();
        getDim();
        getOpponent();
        canvas = new Canvas(FRAME_WIDTH, FRAME_HEIGHT, new Board(dim), dim, opponent);
        add(canvas);
        setTitle(FRAME_TITLE);
        setBounds(FRAME_X, FRAME_Y, FRAME_WIDTH, FRAME_HEIGHT);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // listen for frame resizes
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent componentEvent) {
                canvas.newSize(getWidth(), getHeight());
            }
        });
    }

    /**
     * Display dialog for user to select an opponent
     */
    private void getOpponent() {
        OpponentSelect selector = new OpponentSelect();
        JOptionPane.showMessageDialog(null, selector, "Choose your opponent",
                JOptionPane.PLAIN_MESSAGE);
        opponent = selector.getOpponent();
    }

    /**
     * Display input dialog for user to enter dimension
     */
    private void getDim() {
        String dimString = JOptionPane.showInputDialog(null, "Enter board dimension (default=11):",
                "Setup", JOptionPane.PLAIN_MESSAGE);

        if (dimString.matches("\\d+")) {
            dim = Math.max(1, Integer.parseInt(dimString));
        } else {
            dim = 11;
        }
    }
}
