import javax.swing.*;

public class Open2048 {
    public static void main(String[] args) {
        Runnable game = new game.Run2048();
        SwingUtilities.invokeLater(game);
    }
}
