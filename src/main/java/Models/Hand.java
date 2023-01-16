package Models;

import Exceptions.IncorrectHandException;

import java.util.ArrayList;
import java.util.List;

public class Hand {
    private final Card card1;
    private final Card card2;

    public Hand(Card c1, Card c2) throws IncorrectHandException {
        if (c1.equals(c2)) {
            throw new IncorrectHandException();
        }
        this.card1 = c1;
        this.card2 = c2;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() == Hand.class) {
            Hand h = (Hand)obj;
            return (card1.equals(h.card1) && card2.equals(h.card2)) ||
                    (card1.equals(h.card2) && card2.equals(h.card1));
        }
        return false;
    }

    @Override
    public String toString() {
        return "[" + this.card1 + " " + this.card2 + "]";
    }

    public Card getFirstCard() {
        return new Card(card1.getRank(), card1.getSuit());
    }
    public Card getSecondCard() {
        return new Card(card2.getRank(), card2.getSuit());
    }

    public ArrayList<Card> getCardsAsArrayList() {
        return new ArrayList<>(List.of(card1, card2));
    }
}
