import game.Run2048;

import javax.swing.SwingUtilities;

public class Open2048 {
    public static void main(String[] args) {
        Runnable game = new Run2048();
        SwingUtilities.invokeLater(game);
    }
}
