package minesweeper;

import java.util.ArrayList;
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
        int maxSize = 0;
        Position bestPos = null;
        for (int i = 0; i < board.getHeight(); i++) {
            for (int j = 0; j < board.getWidth(); j++) {
                List<Position> unknownNeighbours = board.getUnknownNeighbours(i, j);
                if (board.get(i, j) > 0 && !unknownNeighbours.isEmpty() && board.getFlagNeighbours(i, j).size() == board.get(i, j)) {
                    if (unknownNeighbours.size() > maxSize) {
                        maxSize = unknownNeighbours.size();
                        bestPos = new Position(i, j);
                    }
                }
            }
        }
        if (bestPos != null) {
            return new Move(bestPos, Move.MoveType.BOTH);
        }
        for (int i = 0; i < board.getHeight(); i++) {
            for (int j = 0; j < board.getWidth(); j++) {
                int unknownBombCnt = board.get(i, j);
                if (unknownBombCnt <= 0) {
                    continue;
                }
                List<Position> neighbours = board.getNeighbours(i, j);
                int unknownNeighboursCnt = 0;
                for (Position cell : neighbours) {
                    if (board.get(cell) == UNKNOWN) {
                        unknownNeighboursCnt++;
                    } else if (board.get(cell) == FLAG) {
                        unknownBombCnt--;
                    }
                }
                if (unknownNeighboursCnt == 0) {
                    continue;
                }

                Position unknownCell = find(neighbours, board, UNKNOWN);
                if (unknownNeighboursCnt == unknownBombCnt) {
                    return new Move(unknownCell, MARK_BOMB);
                }
                if (unknownBombCnt == 0) {
                    return new Move(unknownCell, OPEN);
                }
            }
        }

        for (int i = 0; i < board.getHeight(); i++) {
            for (int j = 0; j < board.getWidth(); j++) {
                if (board.get(i, j) == UNKNOWN && !board.getNumberNeighbours(i, j).isEmpty()) {
                    board.set(i, j, EMPTY);
                    boolean canBeEmpty = !contradictory(board, i, j);
                    board.set(i, j, FLAG);
                    boolean canBeBomb = !contradictory(board, i, j);
                    board.set(i, j, UNKNOWN);
                    if (canBeEmpty && !canBeBomb) {
                        return new Move(i, j, OPEN);
                    } else if (canBeBomb && !canBeEmpty) {
                        return new Move(i, j, MARK_BOMB);
                    }
                }
            }
        }
        return null;
    }

    private boolean contradictory(Board board, int row, int col) {
        for (Position pos1 : board.getNumberNeighbours(row, col)) {
            int hiddenBombs1 = board.get(pos1) - board.getFlagNeighbours(pos1).size();
            List<Position> unknowns1 = board.getUnknownNeighbours(pos1);
            for (Position pos2 : board.getNumberNeighbours(pos1)) {
                int hiddenBombs2 = board.get(pos2) - board.getFlagNeighbours(pos2).size();
                List<Position> unknowns2 = board.getUnknownNeighbours(pos2);
                List<Position> intersection = new ArrayList<>(unknowns2);
                intersection.retainAll(unknowns1);
                int intSize = intersection.size();
                if (contradictory(unknowns1.size() - intSize, unknowns2.size() - intSize, hiddenBombs1, hiddenBombs2, intSize)) {
                    contradictory(unknowns1.size() - intSize, unknowns2.size() - intSize, hiddenBombs1, hiddenBombs2, intSize);
                    return true;
                }
            }
        }
        return false;
    }

    private boolean contradictory(int size1, int size2, int bombs1, int bombs2, int intersectionSize) {
        int maxBombsInIntersection = Math.min(intersectionSize, Math.max(bombs1, bombs2));
        int minBombsInIntersection = Math.max(0, Math.max(bombs1 - size1, bombs2 - size2));
        if (size1 + maxBombsInIntersection < bombs1) {
            return true;
        }
        if (size2 + maxBombsInIntersection < bombs2) {
            return true;
        }
        if (bombs1 < minBombsInIntersection) {
            return true;
        }
        if (bombs2 < minBombsInIntersection) {
            return true;
        }
        return false;
    }

    private Move getProbabilisticMove(Board board) {
        System.out.println("trying to find probabilistic move!");
        double minProbability = 1;
        Position bestPosition = null;
        for (int i = 0; i < board.getHeight(); i++) {
            for (int j = 0; j < board.getWidth(); j++) {
                if (board.get(i, j) == UNKNOWN) {
                    double p = getBombProbability(board, i, j);
                    if (p <= 0 || p >= 1) {
                        throw new RuntimeException("Incorrect probability! " + i + " " + j + " " + p);
                    }
                    if (p < minProbability) {
                        minProbability = p;
                        bestPosition = new Position(i, j);
                    }
                }
            }
        }
        return new Move(bestPosition, OPEN);
    }

    private double getBombProbability(Board board, int row, int col) {
        List<Position> numberCells = board.getNeighbours(row, col).stream().filter(cell -> board.get(cell) > 0).collect(Collectors.toList());
        if (numberCells.isEmpty()) {
            return getHiddenBombCnt(board) / (double) getUnknownCnt(board);
        } else {
            double maxP = 0;
            for (Position numberCell : numberCells) {
                maxP = Math.max(maxP, getProbability(board, numberCell));
            }
            return maxP;
        }
    }

    private double getProbability(Board board, Position numberCell) {
        List<Position> neighbours = board.getNeighbours(numberCell);
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

    private Position find(List<Position> neighbours, Board board, int value) {
        for (Position cell : neighbours) {
            if (board.get(cell) == value) {
                return cell;
            }
        }
        return null;
    }

}