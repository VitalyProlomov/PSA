package Analizer;

import Exceptions.IncorrectBoardException;
import Exceptions.IncorrectHandException;
import Models.Board;
import Models.Card;
import Models.ComboBoardPair;
import Models.Hand;

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
     * Checks if the given set of cards is valid and could exist in real life.
     *
     * @param board Cards on the board
     * @param hand  hand of the player in the game
     * @return true if the board is valid, false otherwise.
     */
    public static boolean isBoardValid(Board board, Hand hand) throws IncorrectBoardException {
        return isBoardValid(board, hand.getCardsAsArrayList());
    }

    /**
     * Checks if the given set of cards is valid and could exist in real life.
     *
     * @param board     Cards on the board
     * @param deadCards all the cards not on the board that were either dealt to players or exposed or burned.
     * @return true if the board is valid, false otherwise.
     */
    public static boolean isBoardValid(Board board, ArrayList<Card> deadCards)
            throws IncorrectBoardException {
        if (deadCards != null) {
            ArrayList<Card> ext = board.getCards();
            ext.addAll(deadCards);
            board.setCards(ext);
        }
        return isBoardValid(board);
    }

    /**
     * Checks if the given set of cards is valid and could exist iin real life.
     *
     * @param extendedBoard Community cards
     * @return true if the board is valid, false otherwise.
     */
    public static boolean isBoardValid(ArrayList<Card> extendedBoard)
            throws IncorrectBoardException {
        Board board = new Board(extendedBoard);
        return isBoardValid(board);
    }

    /**
     * Checks if the given set of cards is valid and could exist iin real life.
     *
     * @param extendedBoard community cards
     * @return true if the board is valid, false otherwise.
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
     * @param cards arrayList of cards that need to be sorted (could be any amount).
     */
    public static void sortBoard(ArrayList<Card> cards) {
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
    }

    /**
     * Sort Cards on board by rank. Keeps the order of same cards.
     * Uses min sort
     *
     * @param board board that needs to be sorted - the order of its elements is going to change
     */
    public static void sortBoard(Board board) {
        ArrayList<Card> cards = board.getCards();
        sortBoard(cards);
    }

    /**
     * Finds the best combination possible, using the community cards on the board and the hand given
     *
     * @param board community cards
     * @param hand  hand of the player, that will be used to make combination.
     * @return a pair of combination and the board that recreates this combination (the exact 5 cards)
     */
    public static ComboBoardPair recognizeCombinationOnBoard(Board board, ArrayList<Card> hand)
            throws IncorrectHandException, IncorrectBoardException {
        if (hand != null && hand.size() != 2) {
            throw new IncorrectHandException();
        }
        if (!isBoardValid(board, hand)) {
            throw new IncorrectBoardException();
        }

        ArrayList<Card> extendedCards = new ArrayList<Card>(board.getCards());
        if (hand != null) {
            extendedCards.add(hand.get(0));
            extendedCards.add(hand.get(1));
        }

        if (extendedCards.size() < 5) {
            throw new IllegalArgumentException("Combination must consist of 5 cards, so at least 5 cards must be given");
        }

        // Sorts the board by increasing the card rank.
        sortBoard(extendedCards);

        Board combBoard = findBestRoyalFlush(extendedCards);
        if (combBoard != null) {
            return new ComboBoardPair(Combinations.FLUSH_ROYAL, combBoard);
        }

        combBoard = findBestStraightFlush(extendedCards);
        if (combBoard != null) {
            return new ComboBoardPair(Combinations.STRAIGHT_FLUSH, combBoard);
        }
        return null;
    }

    private static Board findBestStraightFlush(ArrayList<Card> extendedCards)
            throws IncorrectBoardException {
        Card.Suit majorSuit = countFlushSuit(extendedCards);
        if (majorSuit == null) {
            return null;
        }

        ArrayList<Card> suitedCards = new ArrayList<>();
        for (Card card : extendedCards) {
            if (card.getSuit() == majorSuit) {
                suitedCards.add(card);
            }
        }

        int cons = 1;
        // We can check a sequence of cards to be ordered by ascending and have a diff of 1
        // (finding the highest straight) by simply finding the largest 5 cards going
        // right after each other. And since the initial array os already ascending ordered,
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
                for (int j = 0; j < 5; ++j) {
                    sf.add(suitedCards.get(j + i));
                }
                return new Board(sf);
            }
        }
        return null;
    }

    // Should think if leave returning arrayList or some else
    private static Board findBestRoyalFlush(ArrayList<Card> extendedCards) throws IncorrectBoardException {
        Card.Suit majorSuit = countFlushSuit(extendedCards);
        if (majorSuit == null) {
            return null;
        }

        ArrayList<Card> suitedCards = new ArrayList<>();
        for (int i = 0; i < extendedCards.size(); ++i) {
            if (extendedCards.get(i).getSuit() == majorSuit) {
                suitedCards.add(extendedCards.get(i));
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
     *
     * @param extendedCards The board that is checked for flush suit.
     * @return suit of cards that make flush, null if no flush is present on the board.
     */
    public static Card.Suit countFlushSuit(ArrayList<Card> extendedCards) throws IncorrectBoardException {
        if (!isBoardValid(extendedCards)) {
            throw new IncorrectBoardException("The board is incorrect. It can have no more then 5 cards and " +
                    "must be valid.");
        }
        int s = 0;
        int c = 0;
        int h = 0;
        int d = 0;

        for (Card card : extendedCards) {
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
