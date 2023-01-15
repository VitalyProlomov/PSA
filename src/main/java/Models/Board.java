package Models;

import Exceptions.IncorrectBoardException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class Board {
    private ArrayList<Card> cards = new ArrayList<>();

    private final String incorrectLengthMessage = "Board must contain from 3 to 7 cards.";

    public Board (Card ... cards) {
        if (cards.length < 3 || cards.length > 7) {
            throw new IncorrectBoardException(incorrectLengthMessage);
        }
        Collections.addAll(this.cards, cards);
    }

    public Board(String ... cardReps) {
        if (cardReps.length < 3 || cardReps.length > 7) {
            throw new IncorrectBoardException(incorrectLengthMessage);
        }
        for (String rep : cardReps) {
            Card c = new Card(rep);
            this.cards.add(c);
        }
    }

    public Board(ArrayList<Card> cards) {
        if (cards.size() < 3 || cards.size()> 7) {
            throw new IncorrectBoardException(incorrectLengthMessage);
        }
        this.cards.addAll(cards);
    }

    public Card get(int index) {
        ArrayList<Card> cardsCopy = getCards();
        return cardsCopy.get(index);
    }


    public ArrayList<Card> getCards() {
        return new ArrayList<>(cards);
    }

    public void setCards(ArrayList<Card> cards) {
        if (cards.size() < 3 || cards.size() > 7) {
            throw new IncorrectBoardException(incorrectLengthMessage);
        }
        this.cards = new ArrayList<>(cards);
    }

    public int size() {
        return cards.size();
    }


    /**
     * The boards are considered equal if the boards contain same amount of same cards.
     * The order of cards does not matter.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != Board.class) {
            return false;
        }
        if (this.cards.size() != ((Board)obj).cards.size()) {
            return false;
        }
        HashSet<Card> set1 = new HashSet<>(this.cards);
        HashSet<Card> set2 = new HashSet<>(((Board)obj).cards);


        for (Card card1 : set1) {
            for (Card card2 : set2) {
                if (card1.equals(card2)) {
                    set2.remove(card2);
                    break;
                }
            }
        }
        return set2.size() == 0;
    }

    @Override
    public String toString() {
        return "(" + this.cards + ")";
    }

}
