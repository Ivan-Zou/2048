package game;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.BorderFactory;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Run2048 implements Runnable {
    public void run() {
        final JFrame frame = new JFrame("2048");
        frame.setLocation(Constants.WINDOW_LOCATION_X, Constants.WINDOW_LOCATION_Y);

        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.NORTH);

        // Points labels
        final JLabel pointLabel = new JLabel("Points: " + 0);
        pointLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        status_panel.add(pointLabel);

        Scores scores = new Scores();
        final JLabel highScoreLabel = new JLabel("HighScore: " + scores.getHighScore());
        highScoreLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        status_panel.add(highScoreLabel);

        // Status label
        final JLabel statusLabel = new JLabel("Running...");
        statusLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        status_panel.add(statusLabel);

        // Creates the Game
        final GameField field = new GameField(statusLabel, pointLabel, highScoreLabel);
        frame.add(field, BorderLayout.CENTER);

        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.SOUTH);

        // Reset button
        final JButton reset = new JButton("New Game");
        reset.setFocusable(false);
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                field.reset();
            }
        });
        control_panel.add(reset);

        // Save Game button
        final JButton save = new JButton("Save Game");
        save.setFocusable(false);
        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (field.gameOver()) JOptionPane.showMessageDialog(null, "Invalid Save: Game Over");
                else field.save();
            }
        });
        control_panel.add(save);

        // Load Game button
        final JButton load = new JButton("Load Game");
        load.setFocusable(false);
        load.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                field.load();
            }
        });
        control_panel.add(load);

        frame.pack();
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
