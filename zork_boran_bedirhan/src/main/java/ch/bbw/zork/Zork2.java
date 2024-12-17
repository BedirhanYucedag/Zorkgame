package ch.bbw.zork;

import javax.swing.SwingUtilities;

public class Zork2 {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Game gameGUI = new Game();
            gameGUI.setVisible(true);
        });
    }
}
