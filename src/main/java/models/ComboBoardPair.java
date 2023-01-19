package models;

import analizer.CombinationAnalizer;
import exceptions.IncorrectBoardException;

import java.util.Objects;

public class ComboBoardPair {
    private final CombinationAnalizer.Combinations combination;
    private final Board board;

    public ComboBoardPair(CombinationAnalizer.Combinations combo, Board board) {
        this.combination = combo;
        this.board = board;
    }

    public Board getBoard() throws IncorrectBoardException {
        return new Board(board.getCards());
    }

    public CombinationAnalizer.Combinations getCombination() {
        return combination;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != ComboBoardPair.class) {
            return false;
        }
        return combination == ((ComboBoardPair)obj).combination &&
                this.board.equals(((ComboBoardPair)obj).board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(combination, board);
    }

    @Override
    public String toString() {
        return "(ComboBoard| Combination: " + combination + ", Board: " + board + ")";
    }
}
