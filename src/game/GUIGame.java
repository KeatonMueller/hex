package src.game;

import src.gui.GUI;

public class GUIGame {
    private GUI gui;

    public GUIGame() {
        gui = new GUI();

        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                gui.setVisible(true);
            }
        });
    }
}
