package minesweeper;

import org.omg.CORBA.UNKNOWN;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static minesweeper.Board.*;

public class GameStateReader {

    private static final int ONE_COLOR = new Color(0, 0, 255).getRGB();
    private static final int TWO_COLOR = new Color(0, 123, 0).getRGB();
    private static final int THREE_COLOR = new Color(255, 0, 0).getRGB();
    private static final int FOUR_COLOR = new Color(0, 0, 123).getRGB();

    private final Robot robot;

    public GameStateReader() {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
    }

    public Board readGameState() {
        int cellSize = 32;
        int height = 16;
        int width = 30;

        BufferedImage img = robot.createScreenCapture(new Rectangle(1960, 202, width * cellSize, height * cellSize));

        //int height = 9;
        //int width = 9;
        Board board = new Board(10, height, width);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int x = i * cellSize;
                int y = j * cellSize;
                int xx = x + 18;
                int yy = y + 8;
                int color = img.getRGB(xx, yy);
                int val;
                if (color == ONE_COLOR) {
                    val = 1;
                } else if (color == TWO_COLOR) {
                    val = 2;
                } else if (color == THREE_COLOR) {
                    val = 3;
                } else if (color == FOUR_COLOR) {
                    val = 4;
                } else if (color == Color.BLACK.getRGB()) { // BABAX
                    return null;
                } else if (img.getRGB(x + 20, y + 23) == Color.BLACK.getRGB()) {
                    val = FLAG;
                } else if (img.getRGB(x, y) == Color.WHITE.getRGB()) {
                    val = UNKNOWN;
                } else {
                    val = EMPTY;
                }
                board.set(j, i, val); //j, i !
            }
        }

        writeImage(img);
        return board;
    }

    private void writeImage(BufferedImage img) {
        try {
            ImageIO.write(img, "png", new File("img.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
