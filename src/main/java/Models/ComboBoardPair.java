package Models;

import Analizer.CombinationAnalizer;

public class ComboBoardPair {
    private CombinationAnalizer.Combinations combination;
    private Board board;

    public ComboBoardPair(CombinationAnalizer.Combinations combo, Board board) {
        this.combination = combo;
        this.board = board;
    }

    public Board getBoard() {
        Board nB = new Board(board.getCards());
        return nB;
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
                this.board == ((ComboBoardPair)object).board;
    }
}
