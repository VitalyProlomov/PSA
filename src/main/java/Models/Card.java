package Models;

import Exceptions.IncorrectCardException;

import java.util.Locale;
import java.util.Objects;

/**
 * The class for Card entity. Card has a rank and a suit.
 */
public class Card {
    private final Rank rank;
    private final Suit suit;

    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public Card(String strRepresentation) throws IncorrectCardException {
        if (strRepresentation.length() != 2) {
            throw new IncorrectCardException("Representation of the card must be [Rank][suit], 10 being T," +
                    " suit = 1st letter (ex: 4 of spades = 4s)");
        }

        char valueChar = strRepresentation.toUpperCase(Locale.ROOT).charAt(0);

        if (valueChar == 'T') {
            this.rank = Rank.TEN;
        } else if (valueChar == 'J') {
            this.rank = Rank.JACK;
        } else if (valueChar == 'Q') {
            this.rank = Rank.QUEEN;
        } else if (valueChar == 'K') {
            this.rank = Rank.KING;
        } else if (valueChar == 'A') {
            this.rank = Rank.ACE;
        } else {
            if (valueChar > '9' || valueChar < '2') {
                throw new IncorrectCardException("Representation of the card must be [Rank][suit], 10 being T," +
                        " suit = 1st letter (ex: 4 of spades = 4s)");
            } else {
                int number = valueChar - '0';
                Rank rankToGive = Rank.TWO;
                for (Rank r : Rank.values()) {
                    if (number == r.value) {
                        rankToGive = r;
                    }
                }
                this.rank = rankToGive;
                // Just a check, to make sure my sloppy solution to
                // prevent error popping out (rankToGive) is working correctly
                if (rank == Rank.TWO && (number != 2 || valueChar != '2')) {
                    throw new RuntimeException("I have wrote the code incorrectly: " +
                            "Change the Card constructor (for some reason the rank is " +
                            "assigned incorrectly.");
                }
            }

        }

        char suitChar = strRepresentation.toLowerCase(Locale.ROOT).charAt(1);
        if (suitChar == 's') {
            suit = Suit.SPADES;
        } else if (suitChar == 'c') {
            suit = Suit.CLUBS;
        } else if (suitChar == 'h') {
            suit = Suit.HEARTS;
        } else if (suitChar == 'd') {
            suit = Suit.DIAMONDS;
        } else {
            throw new IncorrectCardException("Representation of the card must be [Rank][suit], 10 being T," +
                    " suit = 1st letter (ex: 4 of spades = 4s)");
        }
    }


    /**
     * Suit of the card - 1 of 4. Has a field w string icon.
     */
    public enum Suit {
        HEARTS("♥"),
        SPADES("♠"),
        DIAMONDS("♦"),
        CLUBS("♣");


        public final String icon;

        Suit(String icon) {
            this.icon = icon;
        }

    }

    /**
     * Rank of the card - value is the int number, increasing by one each next rank.
     */
    public enum Rank {
        TWO(2),
        THREE(3),
        FOUR(4),
        FIVE(5),
        SIX(6),
        SEVEN(7),
        EIGHT(8),
        NINE(9),
        TEN(10),
        JACK(11),
        QUEEN(12),
        KING(13),
        ACE(14);

        public final int value;

        Rank(int value) {
            this.value = value;
        }
    }

    public Rank getRank() {
        return rank;
    }

    public Suit getSuit() {
        return suit;
    }


    @Override
    public boolean equals(Object card) {
        if (card.getClass() == Card.class) {
            return this.rank == ((Card) card).rank && this.suit == ((Card) card).suit;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rank, suit);
    }

    @Override
    public String toString() {
        String rankRepr = "" + rank.value;
        if (rank.value == 10) {
            rankRepr = "T";
        } else if (rank.value == 11) {
            rankRepr = "J";
        } else if (rank.value == 12) {
            rankRepr = "Q";
        } else if (rank.value == 13) {
            rankRepr = "K";
        } else if (rank.value == 14) {
            rankRepr = "A";
        }

        return rankRepr + this.suit.icon;
    }
}
