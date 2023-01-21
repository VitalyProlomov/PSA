package analizer;

import exceptions.IncorrectBoardException;
import models.Board;
import models.Card;
import models.ComboCardsPair;
import models.Hand;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Class that contains methods for analyzing the board and identifying the
 * combination that are present on the board.
 */
public class CombinationAnalizer {
    /**
     * Names of all possible combinations
     */
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
        ArrayList<Card> ext = board.getCards();
        if (hand != null) {
            ext.addAll(hand.getCardsAsArrayList());
        }
        return isBoardValid(ext);
    }

    /**
     * Checks if the given set of cards is valid and could exist in real life.
     *
     * @param board     Cards on the board
     * @param deadCards all the cards not on the board that were either dealt to players or exposed or burned.
     * @return true if the board is valid, false otherwise.
     */
    public static boolean isBoardValid(Board board, ArrayList<Card> deadCards) {
        ArrayList<Card> ext = board.getCards();
        ext.addAll(deadCards);
        return isBoardValid(ext);
    }

    /**
     * Checks if the given set of cards is valid and could exist in real life.
     *
     * @param extendedBoard all the cards that are being checked
     * @return true if the board is valid, false otherwise.
     */
    public static boolean isBoardValid(ArrayList<Card> extendedBoard) {
        HashSet<Card> set = new HashSet<>(extendedBoard);
        return extendedBoard.size() == set.size();
    }

    /**
     * Sort Cards on board by rank. Keeps the order of same cards.
     * Uses min sort
     *
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
    public static void sortBoard(Board board) throws IncorrectBoardException {
        ArrayList<Card> cards = board.getCards();
        sortBoard(cards);
        board.setCards(cards);
    }

    /**
     * Finds the best combination possible, using the community cards on the board and the hand given
     * Amount of cards of the board and the hand given must sum up to at least 5.
     * @param board community cards
     * @param hand  hand of the player, that will be used to make combination.
     * @return a pair of combination and the board that recreates this combination (the exact 5 cards)
     * @throws IncorrectBoardException
     * @exception IllegalArgumentException if amount of cards of the board and the hand combined is less than 5.
     */
    public static ComboCardsPair recognizeCombinationOnBoard(Board board, Hand hand)
           throws IncorrectBoardException {
        if (!isBoardValid(board, hand)) {
            throw new IncorrectBoardException();
        }

        ArrayList<Card> extendedCards = new ArrayList<Card>(board.getCards());
        if (hand != null) {
            extendedCards.addAll(hand.getCardsAsArrayList());
        }

        if (extendedCards.size() < 5) {
            throw new IllegalArgumentException("Combination must consist of 5 cards, so at least 5 cards must be given");
        }

        // Sorts the board by increasing the card rank.
        sortBoard(extendedCards);

        ArrayList<Card> combCards = findBestRoyalFlush(extendedCards);
        if (combCards != null) {
            return new ComboCardsPair(Combinations.FLUSH_ROYAL, combCards);
        }

        combCards = findBestStraightFlush(extendedCards);
        if (combCards != null) {
            return new ComboCardsPair(Combinations.STRAIGHT_FLUSH, combCards);
        }

//        combCards = findBestQuads(extendedCards);
//        if (combCards != null) {
//            return new ComboCardsPair(Combinations.QUADS, combCards);
//        }

        return null;
    }

    /**
     * @param extendedCards Checks if there is a Royal flush on the board.
     * @return Board containing the cards of the combination or {@code}null if the combination was not found
     * @throws IncorrectBoardException in case the cards do not form a valid board
     */
    private static ArrayList<Card> findBestRoyalFlush(ArrayList<Card> extendedCards)
            throws IncorrectBoardException {
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
            return suitedCards;
        }
        return null;
    }

    /**
     * Checks if there is a straight flush on the board and finds the best one of there is.
     *
     * @param extendedCards cards being checked
     * @return Board containing the cards of the combination or {@code}null if the combination was not found
     * @throws IncorrectBoardException in case the cards do not form a valid board
     */
    private static ArrayList<Card> findBestStraightFlush(ArrayList<Card> extendedCards) throws IncorrectBoardException {
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
                return sf;
            }
        }
        return null;
    }


    /**
     * Checks if the flush is present on the given board.
     *
     * @param extendedCards The board that is checked for flush suit.
     * @return suit of cards that make flush, null if no flush is present on the board.
     */
    public static Card.Suit countFlushSuit(ArrayList<Card> extendedCards) throws IncorrectBoardException {
        if (!isBoardValid(extendedCards)) {
            throw new IncorrectBoardException("The board is incorrect. It must be valid.");
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
