package Analizer;

import Models.Card;

import java.util.ArrayList;

public class CombinationAnalizer {
    static public final int BOARD_SIZE = 5;
    static public enum Combinations {
        HIGH_CARD,
        PAIR,
        TWO_PAIRS,
        SET,
        STRAIGHT,
        FLUSH,
        FULL_HOUSE,
        QUADS,
        STRAIGHT_FLUSH,
        FLUSH_ROYAL
    }

    /**
     * Sort Cards on board by rank. Keeps the order of same cards.
     * Uses min sort
     * @param board
     * @return sorted board with all cards ordered by ascension.
     */
    public static ArrayList<Card> sortBoard(ArrayList<Card> board) {
        int min;
        int ind_min = 0;
        boolean isSwapNeeded;
        for (int i = 0; i < BOARD_SIZE; ++i) {
            isSwapNeeded = false;
            min = board.get(i).getRank().value;
            for (int j = i; j < BOARD_SIZE; ++j) {
                if (min > board.get(j).getRank().value) {
                    ind_min = j;
                    min = board.get(j).getRank().value;
                    isSwapNeeded = true;
                }
            }
            if (isSwapNeeded) {
                Card tmp = board.get(i);
                board.set(i, board.get(ind_min));
                board.set(ind_min, tmp);
            }
        }
        return board;
    }

    public static Combinations recognizeCombinationOnBoard(ArrayList<Card> board) {
        if (board.get(0).getRank().value < board.get(1).getRank().value) {

        }
        return null;
    }

}
