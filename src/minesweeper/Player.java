package minesweeper;

public class Player {
    public void play() throws Exception {
        GameStateReader gameStateReader = new GameStateReader();
        BestMoveFinder bestMoveFinder = new BestMoveFinder();
        MoveMaker moveMaker = new MoveMaker();

        while (true) {
            Board board = gameStateReader.readGameState();
            System.out.println(board);
            if (board == null) {
                break;
            }
            Move move = bestMoveFinder.findBestMove(board);
            moveMaker.makeMove(move);
            //Thread.sleep(300);
        }
    }
}
