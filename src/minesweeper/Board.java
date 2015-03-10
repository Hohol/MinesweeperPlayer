package minesweeper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Board {
    public static final int EMPTY = 0;
    public static final int UNKNOWN = -1;
    public static final int FLAG = -2;

    public static final char EMPTY_CHAR = '.';
    public static final char UNKNOWN_CHAR = '?';
    public static final char FLAG_CHAR = 'x';

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
                } else if (ch == FLAG_CHAR) {
                    val = FLAG;
                } else {
                    val = Integer.parseInt("" + ch);
                }
                b[i][j] = val;
            }
        }
    }

    public Board(int bombCnt, int height, int width) {
        this.bombCnt = bombCnt;
        b = new int[height][width];
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int[] line : b) {
            for (int val : line) {
                if (val == UNKNOWN) {
                    sb.append(UNKNOWN_CHAR);
                } else if (val == FLAG) {
                    sb.append(FLAG_CHAR);
                } else if (val == EMPTY) {
                    sb.append(EMPTY_CHAR);
                } else {
                    sb.append(val);
                }
            }
            sb.append("\n");
        }
        return sb.toString();
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

    public int get(Position cell) {
        return get(cell.row, cell.col);
    }

    public int getBombCnt() {
        return bombCnt;
    }

    public List<Position> getNeighbours(int row, int col) {
        return getNeighbours(row, col, v -> true);
    }

    public List<Position> getFlagNeighbours(int row, int col) {
        return getNeighbours(row, col, v -> v == FLAG);
    }

    public List<Position> getUnknownNeighbours(int row, int col) {
        return getNeighbours(row, col, v -> v == UNKNOWN);
    }

    public List<Position> getNumberNeighbours(int row, int col) {
        return getNeighbours(row, col, v -> v > 0);
    }

    private List<Position> getNeighbours(int row, int col, Function<Integer, Boolean> filter) {
        List<Position> r = new ArrayList<>();
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) {
                    continue;
                }
                int toX = row + dx;
                int toY = col + dy;
                if (toX >= 0 && toX < getHeight() && toY >= 0 && toY < getWidth() && filter.apply(b[toX][toY])) {
                    r.add(new Position(toX, toY));
                }
            }
        }
        return r;
    }

    public List<Position> getNeighbours(Position pos) {
        return getNeighbours(pos.row, pos.col);
    }

    public void set(int row, int col, int val) {
        b[row][col] = val;
    }

    public List<Position> getFlagNeighbours(Position pos) {
        return getFlagNeighbours(pos.row, pos.col);
    }

    public List<Position> getUnknownNeighbours(Position pos) {
        return getUnknownNeighbours(pos.row, pos.col);
    }

    public List<Position> getNumberNeighbours(Position pos) {
        return getNumberNeighbours(pos.row, pos.col);
    }
}