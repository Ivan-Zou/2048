package game;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Board {
    private final boolean autoSave;
    private int[][] board;
    private int points;

    public Board(boolean autoSave) {
        this.autoSave = autoSave;
        board = new int[Constants.BOARD_SIZE][Constants.BOARD_SIZE];
        if (autoSave) {
            load(Constants.AUTO_SAVED_GAME_FILE);
            if (isGameOver()) {
                reset();
            }
        } else {
            reset();
        }
    }

    public void reset() {
        board = new int[Constants.BOARD_SIZE][Constants.BOARD_SIZE];
        generate();
        generate();
        points = 0;
    }

    private void generate() {
        boolean notGenerated = true;
        int numToGenerate = (getRandomInteger() <= Constants.NUMBER_PROBABILITY ? 2 : 4);
        while (notGenerated && !isFull()) {
            for (int y = 0; y < board.length; y++) {
                for (int x = 0; x < board.length; x++) {
                    if (getCell(x, y) == 0) {
                        int random = getRandomInteger();
                        if (random <= Constants.GENERATION_PROBABILITY) {
                            board[y][x] = numToGenerate;
                            notGenerated = false;
                            break;
                        }
                    }
                }
                if (!notGenerated) {
                    break;
                }
            }
        }
    }

    private int getRandomInteger() {
        return (int) ((Math.random() * 100) + 1);
    }

    private boolean isFull() {
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board.length; x++) {
                if (getCell(x, y) == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public int getCell(int x, int y) {
        return board[y][x];
    }

    public void load(String file) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            points = Integer.parseInt(reader.readLine());
            for (int y = 0; y < board.length; y++) {
                for (int x = 0; x < board.length; x++) {
                    board[y][x] = Integer.parseInt(reader.readLine());
                }
            }
            reader.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (NumberFormatException ex) {
            reset();
        }
    }

    public boolean isGameOver() {
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board.length; x++) {
                int currCell = getCell(x, y);
                if (currCell == 0) {
                    return false;
                }
                // Left Cell
                if (cellInBound(board, x - 1, y)) {
                    if (currCell == getCell(x - 1, y)) {
                        return false;
                    }
                }
                // Top Cell
                if (cellInBound(board, x, y - 1)) {
                    if (currCell == getCell(x, y - 1)) {
                        return false;
                    }
                }
                // Bottom Cell
                if (cellInBound(board, x, y + 1)) {
                    if (currCell == getCell(x, y + 1)) {
                        return false;
                    }
                }
                // Right Cell
                if (cellInBound(board, x + 1, y)) {
                    if (currCell == getCell(x + 1, y)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean cellInBound(int[][] board, int x, int y) {
        return y >= 0 && y < board.length && x >= 0 && x < board[0].length;
    }

    public int getPoints() {
        return points;
    }

    public void up() {
        boolean cellsMoved = false;
        for (int y = 1; y < board.length; y++) {
            for (int x = 0; x < board.length; x++) {
                int currNum = board[y][x];
                int aboveNum = board[y - 1][x];
                if (currNum != 0) {
                    if (currNum == aboveNum) {
                        cellsMoved = true;
                        points += (2 * currNum);
                        board[y - 1][x] = 2 * currNum;
                        board[y][x] = 0;
                    } else if (aboveNum == 0) {
                        int i = y - 1;
                        cellsMoved = true;
                        while (aboveNum == 0 && i >= 0) {
                            swap(currNum, x, i + 1, aboveNum, x, i);
                            i--;
                            if (cellInBound(board, x, i)) {
                                aboveNum = board[i][x];
                            }
                        }
                        if (currNum == aboveNum) {
                            points += (2 * currNum);
                            board[i][x] = 2 * currNum;
                            board[i + 1][x] = 0;
                        }
                    }
                }
            }
        }
        if (cellsMoved) {
            generate();
        }
    }

    private void swap(int num1, int x1, int y1, int num2, int x2, int y2) {
        board[y1][x1] = num2;
        board[y2][x2] = num1;
    }

    public void down() {
        boolean cellsMoved = false;
        for (int y = board.length - 2; y >= 0; y--) {
            for (int x = 0; x < board.length; x++) {
                int currNum = board[y][x];
                int belowNum = board[y + 1][x];
                if (currNum != 0) {
                    if (currNum == belowNum) {
                        cellsMoved = true;
                        points += (2 * currNum);
                        board[y + 1][x] = 2 * currNum;
                        board[y][x] = 0;
                    } else if (belowNum == 0) {
                        int i = y + 1;
                        cellsMoved = true;
                        while (belowNum == 0 && i < board.length) {
                            swap(currNum, x, i - 1, belowNum, x, i);
                            i++;
                            if (cellInBound(board, x, i)) {
                                belowNum = board[i][x];
                            }
                        }
                        if (currNum == belowNum) {
                            points += (2 * currNum);
                            board[i][x] = 2 * currNum;
                            board[i - 1][x] = 0;
                        }
                    }
                }
            }
        }
        if (cellsMoved) {
            generate();
        }
    }

    public void left() {
        boolean cellsMoved = false;
        for (int y = 0; y < board.length; y++) {
            for (int x = 1; x < board.length; x++) {
                int currNum = board[y][x];
                int leftNum = board[y][x - 1];
                if (currNum != 0) {
                    if (currNum == leftNum) {
                        cellsMoved = true;
                        points += (2 * currNum);
                        board[y][x - 1] = 2 * currNum;
                        board[y][x] = 0;
                    } else if (leftNum == 0) {
                        int i = x - 1;
                        cellsMoved = true;
                        while (leftNum == 0 && i >= 0) {
                            swap(currNum, i + 1, y, leftNum, i, y);
                            i--;
                            if (cellInBound(board, i, y)) {
                                leftNum = board[y][i];
                            }
                        }
                        if (currNum == leftNum) {
                            points += (2 * currNum);
                            board[y][i] = 2 * currNum;
                            board[y][i + 1] = 0;
                        }
                    }
                }
            }
        }
        if (cellsMoved) {
            generate();
        }
    }

    public void right() {
        boolean cellsMoved = false;
        for (int y = 0; y < board.length; y++) {
            for (int x = board.length - 2; x >= 0; x--) {
                int currNum = board[y][x];
                int rightNum = board[y][x + 1];
                if (currNum != 0) {
                    if (currNum == rightNum) {
                        cellsMoved = true;
                        points += (2 * currNum);
                        board[y][x + 1] = 2 * currNum;
                        board[y][x] = 0;
                    } else if (rightNum == 0) {
                        int i = x + 1;
                        cellsMoved = true;
                        while (rightNum == 0 && i < board.length) {
                            swap(currNum, i - 1, y, rightNum, i, y);
                            i++;
                            if (cellInBound(board, i, y)) {
                                rightNum = board[y][i];
                            }
                        }
                        if (currNum == rightNum) {
                            points += (2 * currNum);
                            board[y][i] = 2 * currNum;
                            board[y][i - 1] = 0;
                        }
                    }
                }
            }
        }
        if (cellsMoved) {
            generate();
        }
    }

    public void autoSave() {
        if (autoSave) {
            save(Constants.AUTO_SAVED_GAME_FILE);
        }
    }

    public void save(String file) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file, false));
            writer.write(String.valueOf(points));
            writer.newLine();
            for (int[] row : board) {
                for (int num : row) {
                    writer.write(String.valueOf(num));
                    writer.newLine();
                }
            }
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
