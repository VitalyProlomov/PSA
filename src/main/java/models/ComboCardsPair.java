package models;

import analizer.CombinationAnalyzer;
import analizer.Combination;
import exceptions.IncorrectBoardException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * The Class for Pair of Combination on board and the cards that make up this combination
 */
public class ComboCardsPair {
    private final Combination combination;
    private final HashSet<Card> cards;

    /**
     * Constructs ComboCardsPair with given parameters.
     * Does not check if the Cards actually make the combination, does not check if cards
     * make up a valid board
     * @param combo Combination on board
     * @param cards Cards that make up a combination
     */
    public ComboCardsPair(Combination combo, ArrayList<Card> cards) {
        this.combination = combo;
        this.cards = new HashSet<>(cards);
    }

    public ComboCardsPair(Combination combo, Set<Card> cards) {
        this.combination = combo;
        this.cards = new HashSet<>(cards);
    }


    public HashSet<Card> getBoard() throws IncorrectBoardException {
        return new HashSet<>(cards);
    }

    public Combination getCombination() {
        return combination;
    }

    /**
     * ComboCardsPair is equal to another object only if it is another ComboCardsPair.
     * ComboCardsPairs are considered equal if both combination and cards fields are equal.
     * @param obj
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || obj.getClass() != ComboCardsPair.class) {
            return false;
        }
        return combination == ((ComboCardsPair)obj).combination &&
                this.cards.equals(((ComboCardsPair)obj).cards);
    }

    @Override
    public int hashCode() {
        return Objects.hash(combination, cards);
    }

    @Override
    public String toString() {
        ArrayList<Card> cardsAr = new ArrayList<>(cards);
        CombinationAnalyzer.sortBoard(cardsAr);
        return "(ComboBoard| Combination: " + combination + ", Cards: " + cardsAr + ")";
    }
}
