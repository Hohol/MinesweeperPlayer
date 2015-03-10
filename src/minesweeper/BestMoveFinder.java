package minesweeper;

import java.util.List;
import java.util.stream.Collectors;

import static minesweeper.Board.*;
import static minesweeper.Move.MoveType.*;

public class BestMoveFinder {

    public Move findBestMove(Board board) {
        Move move = getSureMove(board);
        if (move == null) {
            move = getProbabilisticMove(board);
        }
        return move;
    }

    private Move getSureMove(Board board) {
        for (int i = 0; i < board.getHeight(); i++) {
            for (int j = 0; j < board.getWidth(); j++) {
                int unknownBombCnt = board.get(i, j);
                if (unknownBombCnt <= 0) {
                    continue;
                }
                List<Cell> neighbours = board.getNeighbours(i, j);
                int unknownNeighboursCnt = 0;
                for (Cell cell : neighbours) {
                    if (board.get(cell) == UNKNOWN) {
                        unknownNeighboursCnt++;
                    } else if (board.get(cell) == FLAG) {
                        unknownBombCnt--;
                    }
                }
                if (unknownNeighboursCnt == 0) {
                    continue;
                }

                Cell unknownCell = find(neighbours, board, UNKNOWN);
                if (unknownNeighboursCnt == unknownBombCnt) {
                    return new Move(unknownCell, MARK_BOMB);
                }
                if (unknownBombCnt == 0) {
                    return new Move(unknownCell, OPEN);
                }
            }
        }
        return null;
    }

    private Move getProbabilisticMove(Board board) {
        double minProbability = 1;
        Cell bestCell = null;
        for (int i = 0; i < board.getHeight(); i++) {
            for (int j = 0; j < board.getWidth(); j++) {
                if (board.get(i, j) == UNKNOWN) {
                    double p = getBombProbability(board, i, j);
                    if (p <= 0 || p >= 1) {
                        throw new RuntimeException("Incorrect probability! " + i + " " + j + " " + p);
                    }
                    if (p < minProbability) {
                        minProbability = p;
                        bestCell = new Cell(i, j);
                    }
                }
            }
        }
        return new Move(bestCell, OPEN);
    }

    private double getBombProbability(Board board, int row, int col) {
        List<Cell> numberCells = board.getNeighbours(row, col).stream().filter(cell -> board.get(cell) > 0).collect(Collectors.toList());
        if (numberCells.isEmpty()) {
            return getHiddenBombCnt(board) / (double) getUnknownCnt(board);
        } else {
            double maxP = 0;
            for (Cell numberCell : numberCells) {
                maxP = Math.max(maxP, getProbability(board, numberCell));
            }
            return maxP;
        }
    }

    private double getProbability(Board board, Cell numberCell) {
        List<Cell> neighbours = board.getNeighbours(numberCell);
        long unknownCnt = neighbours.stream().filter(cell -> board.get(cell) == UNKNOWN).count();
        long flagCnt = neighbours.stream().filter(cell -> board.get(cell) == FLAG).count();
        long hiddenBombs = board.get(numberCell) - flagCnt;
        return hiddenBombs / (double) unknownCnt;
    }

    private int getUnknownCnt(Board board) {
        return getCount(board, UNKNOWN);
    }

    private int getHiddenBombCnt(Board board) {
        return board.getBombCnt() - getCount(board, FLAG);
    }

    private int getCount(Board board, int value) {
        int cnt = 0;
        for (int i = 0; i < board.getHeight(); i++) {
            for (int j = 0; j < board.getWidth(); j++) {
                if (board.get(i, j) == value) {
                    cnt++;
                }
            }
        }
        return cnt;
    }

    private Cell find(List<Cell> neighbours, Board board, int value) {
        for (Cell cell : neighbours) {
            if (board.get(cell) == value) {
                return cell;
            }
        }
        return null;
    }

}