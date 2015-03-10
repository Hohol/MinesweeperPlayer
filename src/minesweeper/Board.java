package minesweeper;

import java.util.ArrayList;
import java.util.List;

public class Board {
    public static final int EMPTY = 0;
    public static final int UNKNOWN = -1;
    public static final int BOMB = -2;

    public static final char EMPTY_CHAR = '.';
    public static final char UNKNOWN_CHAR = '?';
    public static final char BOMB_CHAR = 'x';

    private final int[][] b;
    private final int bombCnt;

    public Board(int bombCnt, String s) {
        this.bombCnt = bombCnt;
        String[] a = s.split("\n");
        b = new int[a.length][a[0].length()];
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[i].length(); j++) {
                char ch = a[i].charAt(j);
                int val;
                if (ch == EMPTY_CHAR) {
                    val = EMPTY;
                } else if (ch == UNKNOWN_CHAR) {
                    val = UNKNOWN;
                } else if (ch == BOMB_CHAR) {
                    val = BOMB;
                } else {
                    val = Integer.parseInt("" + ch);
                }
                b[i][j] = val;
            }
        }
    }

    public int getHeight() {
        return b.length;
    }

    public int getWidth() {
        return b[0].length;
    }

    public int get(int row, int col) {
        return b[row][col];
    }

    public int get(Cell cell) {
        return get(cell.row, cell.col);
    }

    public int getBombCnt() {
        return bombCnt;
    }

    public List<Cell> getNeighbours(int row, int col) {
        List<Cell> r = new ArrayList<>();
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) {
                    continue;
                }
                int toX = row + dx;
                int toY = col + dy;
                if (toX >= 0 && toX < getHeight() && toY >= 0 && toY < getWidth()) {
                    r.add(new Cell(toX, toY));
                }
            }
        }
        return r;
    }

    public List<Cell> getNeighbours(Cell cell) {
        return getNeighbours(cell.row, cell.col);
    }
}