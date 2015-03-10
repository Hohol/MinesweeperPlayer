package minesweeper;

public class Cell {
    public final int row;
    public final int col;
    public final int value;

    public Cell(int row, int col, int value) {
        this.row = row;
        this.col = col;
        this.value = value;
    }

    @Override
    public String toString() {
        return "Cell{" +
                "value=" + value +
                ", row=" + row +
                ", col=" + col +
                '}';
    }
}
