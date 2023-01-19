package models;

import exceptions.IncorrectBoardException;
import exceptions.IncorrectCardException;

import java.util.*;

public class Board {
    private ArrayList<Card> cards = new ArrayList<>();

    private final String incorrectLengthMessage = "Board must contain from 3 to 5 cards.";

    public Board (Card ... cards) throws IncorrectBoardException {
        if (cards.length < 3 || cards.length > 5) {
            throw new IncorrectBoardException(incorrectLengthMessage);
        }
        Collections.addAll(this.cards, cards);
    }

    public Board(String ... cardReps) throws IncorrectBoardException, IncorrectCardException {
        if (cardReps.length < 3 || cardReps.length > 5) {
            throw new IncorrectBoardException(incorrectLengthMessage);
        }
        for (String rep : cardReps) {
            Card c = new Card(rep);
            this.cards.add(c);
        }
    }

    public Board(ArrayList<Card> cards) throws IncorrectBoardException {
        if (cards.size() < 3 || cards.size()> 5) {
            throw new IncorrectBoardException(incorrectLengthMessage);
        }
        this.cards.addAll(cards);
    }

    public Board(Board copyBoard) {
        this.cards.addAll(copyBoard.getCards());
    }

    public Card get(int index) {
        ArrayList<Card> cardsCopy = getCards();
        return cardsCopy.get(index);
    }


    public ArrayList<Card> getCards() {
        return new ArrayList<>(cards);
    }

    public void setCards(ArrayList<Card> cards) throws IncorrectBoardException {
        if (cards.size() < 3 || cards.size() > 5) {
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
        if (this.size() != ((Board)obj).size()) {
            return false;
        }

        Board b = ((Board)obj);
        Set<Card> set1 = new HashSet<>(this.cards.subList(0, 3));
        Set<Card> set2 = new HashSet<>(b.cards.subList(0, 3));

        for (Card card1 : set1) {
            set2.remove(card1);
        }

        boolean trSame = true;
        for (int i = 3; i < this.size(); ++i) {
            if (b.size() < i || !this.get(i).equals(b.get(i))) {
                trSame = false;
            }
        }
        return set2.size() == 0 && trSame;
    }

    @Override
    public int hashCode() {
        if (size() == 3) {
            return Objects.hash(new HashSet<>(cards.subList(0, 3)));
        } else if (size() == 4) {
            return Objects.hash(new HashSet<>(cards.subList(0, 3)), get(3));
        }
        return Objects.hash(new HashSet<>(cards.subList(0, 3)), get(3), get(4));
    }

    @Override
    public String toString() {
        return "(" + this.cards + ")";
    }
}
