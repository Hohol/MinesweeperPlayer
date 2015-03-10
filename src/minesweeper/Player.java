package minesweeper;

public class Player {
    public void play() {
        GameStateReader gameStateReader = new GameStateReader();
        Board board = gameStateReader.readGameState();
        System.out.println(board);
    }
}
