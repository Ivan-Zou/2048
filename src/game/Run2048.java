package game;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.BorderFactory;

import java.awt.BorderLayout;

public class Run2048 implements Runnable {
    public void run() {
        final JFrame frame = new JFrame("2048");

        final JPanel statusPanel = new JPanel();
        frame.add(statusPanel, BorderLayout.NORTH);

        // Points labels
        final JLabel pointLabel = new JLabel("Points: " + 0);
        pointLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        statusPanel.add(pointLabel);

        final Scores scores = new Scores();
        final JLabel highScoreLabel = new JLabel("High Score: " + scores.getHighScore());
        highScoreLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        statusPanel.add(highScoreLabel);

        // Status label
        final JLabel statusLabel = new JLabel("Running...");
        statusLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        statusPanel.add(statusLabel);

        // Creates the Game
        final GameMechanics field = new GameMechanics(statusLabel, pointLabel, highScoreLabel);
        frame.add(field, BorderLayout.CENTER);

        final JPanel controlPanel = new JPanel();
        frame.add(controlPanel, BorderLayout.SOUTH);

        // Reset button
        final JButton reset = new JButton("New Game");
        reset.setFocusable(false);
        reset.addActionListener(actionEvent -> field.reset());
        controlPanel.add(reset);

        // Save Game button
        final JButton save = new JButton("Save Game");
        save.setFocusable(false);
        save.addActionListener(actionEvent -> {
            if (field.gameOver()) {
                JOptionPane.showMessageDialog(null, "Invalid Save: Game Over");
            } else {
                field.save();
            }
        });
        controlPanel.add(save);

        // Load Game button
        final JButton load = new JButton("Load Game");
        load.setFocusable(false);
        load.addActionListener(actionEvent -> field.load());
        controlPanel.add(load);

        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation(Constants.WINDOW_LOCATION_X, Constants.WINDOW_LOCATION_Y);
    }
}
