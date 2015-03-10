package minesweeper;

import org.testng.annotations.Test;

import static minesweeper.Move.MoveType.MARK_BOMB;
import static minesweeper.Move.MoveType.OPEN;
import static org.testng.Assert.assertEquals;

@Test
public class BestMoveFinderTest {
    BestMoveFinder bestMoveFinder = new BestMoveFinder();

    @Test
    void test() {
        Board board = new Board(
                1,
                "1?"
        );
        check(board, new Move(0, 1, MARK_BOMB));
    }

    @Test
    void test3() {
        Board board = new Board(
                1,
                "?1x"
        );
        check(board, new Move(0, 0, OPEN));
    }

    @Test
    void test4() {
        Board board = new Board(
                1,
                "" +
                        "??\n" +
                        "??"
        );
        check(board, new Move(0, 0, OPEN));
    }

    @Test
    void test5() {
        Board board = new Board(
                2,

                "" +
                        "1??\n" +
                        "?.?\n" +
                        "???"
        );
        check(board, new Move(0, 2, OPEN));
    }

    //---------utils

    private void check(Board board, Move expected) {
        Move bestMove = bestMoveFinder.findBestMove(board);
        assertEquals(bestMove, expected);
    }
}