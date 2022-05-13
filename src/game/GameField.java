package game;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.BorderFactory;

import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Font;

public class GameField extends JPanel {
    private final Board board;
    private final Scores scores;

    private final JLabel status, points, highScore;

    private boolean writtenToFile;

    public GameField(JLabel status, JLabel points, JLabel highScore) {
        board = new Board(true);
        scores = new Scores();

        this.status = status;
        this.points = points;
        this.highScore = highScore;

        writtenToFile = false;

        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setFocusable(true);

        scores.setScores(board.getPoints());
        points.setText("Points: " + scores.getScore());
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (!board.isGameOver()) {
                    int key = e.getKeyCode();
                    if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
                        board.left();
                    } else if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W) {
                        board.up();
                    } else if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) {
                        board.down();
                    } else if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
                        board.right();
                    }
                    repaint();
                    board.autoSave();
                    scores.setScores(board.getPoints());
                    points.setText("Points: " + scores.getScore());
                    highScore.setText("HighScore: " + scores.getHighScore());
                }

                if (board.isGameOver() && !writtenToFile) {
                    writeGameDataToFiles();
                    status.setText("Game Over!");
                }
            }
        });
    }

    public void writeGameDataToFiles() {
        scores.recordScore();
        scores.writeToFile();
        writtenToFile = true;
    }

    public boolean gameOver() {
        return board.isGameOver();
    }

    public void reset()  {
        // saves the points when resetting without finishing the game
        if (!writtenToFile && scores.getScore() != 0) {
            writeGameDataToFiles();
        }
        writtenToFile = false;
        scores.reset();
        status.setText("Running...");
        points.setText("Points: " + scores.getScore());
        highScore.setText("HighScore: " + scores.getHighScore());
        board.reset();
        board.autoSave();
        repaint();
        requestFocusInWindow();
    }

    public void save() {
        board.save(Constants.MANUAL_SAVED_GAME_FILE);
    }

    public void load() {
        if (!writtenToFile && scores.getScore() != 0) {
            writeGameDataToFiles();
        }
        board.load(Constants.MANUAL_SAVED_GAME_FILE);
        writtenToFile = false;
        scores.setScores(board.getPoints());
        status.setText("Running...");
        points.setText("Points: " + scores.getScore());
        highScore.setText("HighScore: " + scores.getHighScore());
        board.autoSave();
        repaint();
        requestFocusInWindow();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawLine(Constants.FIRST_LINE, 0, Constants.FIRST_LINE, Constants.FIELD_HEIGHT);
        g.drawLine(Constants.MIDDLE_LINE, 0, Constants.MIDDLE_LINE, Constants.FIELD_HEIGHT);
        g.drawLine(Constants.LAST_LINE, 0, Constants.LAST_LINE, Constants.FIELD_HEIGHT);
        g.drawLine(0, Constants.FIRST_LINE, Constants.FIELD_WIDTH, Constants.FIRST_LINE);
        g.drawLine(0, Constants.MIDDLE_LINE, Constants.FIELD_WIDTH, Constants.MIDDLE_LINE);
        g.drawLine(0, Constants.LAST_LINE, Constants.FIELD_WIDTH, Constants.LAST_LINE);

        g.setFont(new Font(null, Font.PLAIN, 20));
        for (int y = 0; y < Constants.BOARD_SIZE; y++) {
            for (int x = 0; x < Constants.BOARD_SIZE; x++) {
                int currNum = board.getCell(x, y);
                if (currNum != 0) {
                    g.drawString(String.valueOf(currNum), 25 + 100 * x, 55 + 100 * y);
                }
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(Constants.FIELD_WIDTH, Constants.FIELD_HEIGHT);
    }
}
