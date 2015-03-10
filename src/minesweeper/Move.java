package minesweeper;

public class Move {
    private final int row;
    private final int col;
    private final MoveType moveType;

    public Move(int row, int col, MoveType moveType) {
        this.row = row;
        this.col = col;
        this.moveType = moveType;
    }

    public Move(Cell cell, MoveType moveType) {
        this(cell.row, cell.col, moveType);
    }

    public static enum MoveType {
        MARK_BOMB, OPEN
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Move move = (Move) o;

        if (col != move.col) return false;
        if (row != move.row) return false;
        if (moveType != move.moveType) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = row;
        result = 31 * result + col;
        result = 31 * result + moveType.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Move{" +
                "row=" + row +
                ", col=" + col +
                ", moveType=" + moveType +
                '}';
    }
}