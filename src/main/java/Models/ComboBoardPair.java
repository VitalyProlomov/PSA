package Models;

import Analizer.CombinationAnalizer;
import Exceptions.IncorrectBoardException;

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
    public boolean equals(Object object) {
        if (object.getClass() != ComboBoardPair.class) {
            return false;
        }
        return combination == ((ComboBoardPair)object).combination &&
                this.board.equals(((ComboBoardPair)object).board);
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
