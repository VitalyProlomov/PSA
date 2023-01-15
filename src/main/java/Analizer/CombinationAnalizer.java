package Analizer;

import Exceptions.IncorrectBoardException;
import Models.Board;
import Models.Card;
import Models.ComboBoardPair;

import java.util.ArrayList;

/**
 *
 */
public class CombinationAnalizer {
    // static public final int BOARD_SIZE = 5;

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
     * Checks if the given set of cards is valid and could exist iin real life.
     *
     * @param board     Cards on the board
     * @param deadCards all the cards not on the board that were either dealt to players or exposed or burned.
     * @return
     */
    public static boolean isBoardValid(Board board, ArrayList<Card> deadCards) {
        if (deadCards != null) {
            ArrayList<Card> ext = board.getCards();
            ext.addAll(deadCards);
            board.setCards(ext);
        }
        return isBoardValid(board);
    }

    /**
     * Checks if the given set of cards is valid and could exist iin real life.
     * @param extendedBoard Community cards + hand cards (or just community cards)
     * @return if the board is valid or not
     */
    public static boolean isBoardValid(ArrayList<Card> extendedBoard) {
        Board board = new Board(extendedBoard);
        return isBoardValid(board);
    }

    /**
     * Checks if the given set of cards is valid and could exist iin real life.
     * @param extendedBoard Instance of board, containing community cards + hand cards
     *                      (or just community cards)
     * @return if the board is valid or not
     */
    public static boolean isBoardValid(Board extendedBoard) {
        for (int cur = 0; cur < extendedBoard.size(); ++cur) {
            for (int j = 0; j < cur; ++j) {
                if (extendedBoard.get(cur).equals(extendedBoard.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Sort Cards on board by rank. Keeps the order of same cards.
     * Uses min sort
     *
     * @param board
     * @return sorted board with all cards ordered by ascension.
     */
    public static void sortBoard(Board board) {
        ArrayList<Card> cards = board.getCards();
        int min;
        int ind_min = 0;
        boolean isSwapNeeded;
        for (int i = 0; i < cards.size(); ++i) {
            isSwapNeeded = false;
            min = cards.get(i).getRank().value;
            for (int j = i; j < cards.size(); ++j) {
                if (min > cards.get(j).getRank().value) {
                    ind_min = j;
                    min = cards.get(j).getRank().value;
                    isSwapNeeded = true;
                }
            }
            if (isSwapNeeded) {
                Card tmp = cards.get(i);
                cards.set(i, cards.get(ind_min));
                cards.set(ind_min, tmp);
            }
        }

        board.setCards(cards);
    }

    /**
     *
     * @param board
     * @param hand
     * @return
     */
    public static ComboBoardPair recognizeCombinationOnBoard(Board board, ArrayList<Card> hand) {
        // extendedBoard is  board that includes not only community cards, but also 2 hero`s cards.
        // Hence, can include from 5 to 7 cards
        if (hand != null && hand.size() != 2 || (hand != null && !isBoardValid(board, hand))) {
            throw new IllegalArgumentException("");
        }

        ArrayList<Card> extendedCards = new ArrayList<Card>(board.getCards());
        if (hand != null) {
            extendedCards.add(hand.get(0));
            extendedCards.add(hand.get(1));
        }

        if (extendedCards.size() < 5) {
            throw new IllegalArgumentException("Combination must consist of 5 cards, so at least 5 cards must be given");
        }

        Board extendedBoard = new Board(extendedCards);

        // Sorts the board by increasing the card rank.
        sortBoard(extendedBoard);

        Board combBoard = findBestRoyalFlush(extendedBoard);
        if (combBoard != null) {
            return new ComboBoardPair(Combinations.FLUSH_ROYAL, combBoard);
        }

        combBoard = findBestStraightFlush(extendedBoard);
        if (combBoard != null) {
            return new ComboBoardPair(Combinations.STRAIGHT_FLUSH, combBoard);
        }
        return null;
    }

    private static Board findBestStraightFlush(Board extendedBoard) {
        ArrayList<Card> cards = extendedBoard.getCards();
        Card.Suit majorSuit = countFlushSuit(extendedBoard);
        if (majorSuit == null) {
            return null;
        }

        ArrayList<Card> suitedCards = new ArrayList<>();
        for (Card card : cards) {
            if (card.getSuit() == majorSuit) {
                suitedCards.add(card);
            }
        }

        int cons = 1;
        // We can check a sequence of cards to be ordered by ascending and have a diff of 1
        // (finding the highest straight) by simply finding the largest 5 cards going
        // right after each other. And since the initial array os already ascendingly ordered,
        // we can just skip reset the amount counted if one of the cards is not 1 value less than
        // the previous card.
        for (int i = suitedCards.size() - 1; i >= 0; --i) {
            if (suitedCards.get(i).getRank() == suitedCards.get(i + 1).getRank()) {
                cons += 1;
            } else {
                cons = 1;
            }
            if (cons == 5) {
                ArrayList<Card> sf = new ArrayList<>();
                for (int j = 0 ; j < 5; ++j) {
                    sf.add(suitedCards.get(j + i));
                }
                return new Board(sf);
            }
        }
        return null;
    }

    // Should think if leave returning arrayList or some else
    private static Board findBestRoyalFlush(Board extendedBoard) {
        ArrayList<Card> copyBoard = new ArrayList<>(extendedBoard.getCards());
        Card.Suit majorSuit = countFlushSuit(new Board(copyBoard));
        if (majorSuit == null) {
            return null;
        }

        ArrayList<Card> suitedCards = new ArrayList<>();
        for (int i = 0; i < copyBoard.size(); ++i) {
            if (copyBoard.get(i).getSuit() == majorSuit) {
                suitedCards.add(copyBoard.get(i));
            }
        }

        int sum = 0;
        for (Card card : suitedCards) {
            sum += card.getRank().value;
        }
        // A + K + Q + J + T = 14 + 13 + 12 + 11 + 10 = 60
        if (sum == 60) {
            return new Board(suitedCards);
        }
        return null;
    }


    /**
     * Returns the suit that makes a flush on board. If there is no such suit (5 or more cards
     * of one suit), then null is returned
     * @param extendedBoard The board that is checked for flush suit.
     * @return suit of cards that make flush, null if no flush is present on the board.
     */
    public static Card.Suit countFlushSuit(Board extendedBoard) {
        if (extendedBoard.size() > 7 || !isBoardValid(extendedBoard)) {
            throw new IncorrectBoardException("The board is incorrect. It can have no more then 7 cards and " +
                    "must be valid.");
        }
        int s = 0;
        int c = 0;
        int h = 0;
        int d = 0;

        for (Card card : extendedBoard.getCards()) {
            if (card.getSuit() == Card.Suit.SPADES) {
                s++;
            }
            if (card.getSuit() == Card.Suit.CLUBS) {
                c++;
            }
            if (card.getSuit() == Card.Suit.HEARTS) {
                h++;
            }
            if (card.getSuit() == Card.Suit.DIAMONDS) {
                d++;
            }
        }

        if (s >= 5) {
            return Card.Suit.SPADES;
        } else if (c >= 5) {
            return Card.Suit.CLUBS;
        } else if (h >= 5) {
            return Card.Suit.HEARTS;
        } else if (d >= 5) {
            return Card.Suit.DIAMONDS;
        }
        return null;
    }
}
