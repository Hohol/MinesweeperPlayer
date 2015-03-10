package minesweeper;

import java.awt.*;
import java.awt.event.InputEvent;

import static minesweeper.GameStateReader.*;
import static minesweeper.Move.MoveType.*;

public class MoveMaker {
    private final Robot robot;

    public MoveMaker() {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException();
        }
    }

    public void makeMove(Move move) {
        robot.mouseMove(X_SHIFT + move.col * CELL_SIZE + CELL_SIZE / 2, Y_SHIFT + move.row * CELL_SIZE + CELL_SIZE / 2);
        if (move.moveType == OPEN) {
            System.out.println("open");
            click(InputEvent.BUTTON1_DOWN_MASK);
        } else {
            System.out.println("flag");
            click(InputEvent.BUTTON3_DOWN_MASK);
        }
    }

    private void click(int button) {
        robot.mousePress(button);
        try {
            Thread.sleep(40);
        } catch (InterruptedException e) {
            throw new RuntimeException();
        }
        robot.mouseRelease(button);
    }
}
