package src.gui;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import src.agents.Agent;
import src.agents.random.RandomAgent;

public class OpponentSelect extends JPanel {
    private static final long serialVersionUID = 1L;
    private Agent opponent;

    public OpponentSelect() {
        JRadioButton human = new JRadioButton("Human");
        JRadioButton random = new JRadioButton("Random");

        human.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (human.isSelected()) {
                    opponent = null;
                    random.setSelected(false);
                } else {
                    opponent = null;
                }
            }
        });

        random.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (random.isSelected()) {
                    opponent = new RandomAgent();
                    human.setSelected(false);
                } else {
                    opponent = null;
                }
            }
        });

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(human);
        add(random);
    }

    /**
     * Return the selected opponent
     * 
     * @return The Agent that is the opponent (or null if opponent is human)
     */
    public Agent getOpponent() {
        return opponent;
    }
}
