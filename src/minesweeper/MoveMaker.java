package minesweeper;

import java.awt.*;
import java.awt.event.InputEvent;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
            click(Collections.singletonList(InputEvent.BUTTON1_DOWN_MASK));
        } else if (move.moveType == MARK_BOMB) {
            System.out.println("flag");
            click(Collections.singletonList(InputEvent.BUTTON3_DOWN_MASK));
        } else {
            System.out.println("both");
            click(Arrays.asList(InputEvent.BUTTON1_DOWN_MASK, InputEvent.BUTTON3_DOWN_MASK));
        }
    }

    private void click(List<Integer> buttons) {
        buttons.forEach(robot::mousePress);
        try {
            Thread.sleep(40);
        } catch (InterruptedException e) {
            throw new RuntimeException();
        }
        buttons.forEach(robot::mouseRelease);
    }
}
