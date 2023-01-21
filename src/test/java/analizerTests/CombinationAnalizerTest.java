package analizerTests;

import analizer.CombinationAnalizer;
import exceptions.IncorrectBoardException;
import exceptions.IncorrectCardException;
import exceptions.IncorrectHandException;
import models.Board;
import models.Card;
import models.ComboCardsPair;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CombinationAnalizerTest {

    // region Finding Combinations
    @Test
    public void testFlushCountingMethod() throws IncorrectCardException, IncorrectBoardException {
        Board b = new Board("2c", "4c", "8c", "Kc", "Jc");
        assertEquals(CombinationAnalizer.countFlushSuit(b.getCards()), Card.Suit.CLUBS);

        b = new Board("5d", "Ad", "Kd", "2d", "5s");
        assertNull(CombinationAnalizer.countFlushSuit(b.getCards()));

        b = new Board("5d", "Ad", "Kd", "2d", "6d");
        assertEquals(CombinationAnalizer.countFlushSuit(b.getCards()), Card.Suit.DIAMONDS);
    }


    @Test
    public void testFindFLushRoyal() throws IncorrectBoardException, IncorrectCardException, IncorrectHandException {
        Board rf = new Board("Ah", "Kh", "6s", "Ts", "Th");
        Board finalRf = rf;
        assertDoesNotThrow(() -> CombinationAnalizer.recognizeCombinationOnBoard(finalRf, null));

        rf = new Board("Ts", "As", "Js", "Ks", "Qs");
        Board c = new Board("Ts", "As", "Js", "Ks", "Qs");
        CombinationAnalizer.sortBoard(c);
        assertEquals(new ComboCardsPair(CombinationAnalizer.Combinations.FLUSH_ROYAL, c.getCards()),
                        CombinationAnalizer.recognizeCombinationOnBoard(rf, null));
    }


    @Test
    public void testFindBestStraightFLush() {

    }
    //endregion

    //region

    //endregion

    //region Board Validation method
    @Test
    public void testBoardValid() throws IncorrectBoardException, IncorrectCardException {
        Card As = new Card("As");
        Card Ks = new Card("Ks");
        Card Qs = new Card("Qs");
        Card Js = new Card("Js");
        Card Ts = new Card("Ts");
        Card NineH = new Card("9h");
        Card EightH = new Card("8h");
        Card EightC = new Card("8c");

        ArrayList<Card> invalid = new ArrayList<>(List.of(As, Ks, Qs, Qs, Ts));
        assertFalse(CombinationAnalizer.isBoardValid(invalid));

        invalid = new ArrayList<>(List.of(As, Js, Js, NineH));
        assertFalse(CombinationAnalizer.isBoardValid(invalid));

        invalid = new ArrayList<>(List.of(EightC, As, EightC));
        assertFalse(CombinationAnalizer.isBoardValid(invalid));

        ArrayList<Card> valid = new ArrayList<>(List.of(As, Ks, Qs, Js, Ts));
        assertTrue(CombinationAnalizer.isBoardValid(valid));

        valid = new ArrayList<>(List.of(EightH, EightC, Js, Ks, Qs));
        assertTrue(CombinationAnalizer.isBoardValid(valid));
    }
    //endregion

    //region Board Sorting
    @Test
    public void testSortBoardW2SameCards() throws IncorrectBoardException {
        Card H4 = new Card(Card.Rank.FOUR, Card.Suit.HEARTS);
        Card HA = new Card(Card.Rank.ACE, Card.Suit.HEARTS);
        Card SK = new Card(Card.Rank.KING, Card.Suit.SPADES);
        Card C4 = new Card(Card.Rank.FOUR, Card.Suit.CLUBS);
        Card DJ = new Card(Card.Rank.JACK, Card.Suit.DIAMONDS);

        Board sortedBoard = new Board(H4, HA, SK, C4, DJ);
        CombinationAnalizer.sortBoard(sortedBoard);
        Board expectedBoard =new Board(H4, C4, DJ, SK, HA);

        assertEquals(expectedBoard, sortedBoard);
    }

    @Test
    public void testSortBoardOnSortedBoard() throws IncorrectCardException, IncorrectBoardException {
        Card h2 = new Card("2h");
        Card s2 = new Card("2s");
        Card d2 = new Card("2d");
        Card c2 = new Card("2c");
        Card sA = new Card("As");

        Board board = new Board(h2, s2, d2, c2, sA);

        CombinationAnalizer.sortBoard(board);
        Board expected = new Board(h2, s2, d2, c2, sA);

        assertEquals(board, expected);
    }

    @Test
    public void testSortRandomBoards() throws IncorrectCardException, IncorrectBoardException {
        Card Jh = new Card(Card.Rank.JACK, Card.Suit.HEARTS);
        Card twoS = new Card(Card.Rank.TWO, Card.Suit.SPADES);
        Card threeS = new Card(Card.Rank.THREE, Card.Suit.SPADES);
        Card fourS = new Card(Card.Rank.FOUR, Card.Suit.SPADES);
        Card fiveS = new Card(Card.Rank.FIVE, Card.Suit.SPADES);

        Board board1 = new Board(Jh, twoS, threeS, fourS, fiveS);
        CombinationAnalizer.sortBoard(board1);

        Board expected1 = new Board(twoS, threeS, fourS, fiveS, Jh);
        assertEquals(board1, expected1);

        Card As = new Card(Card.Rank.ACE, Card.Suit.SPADES);
        Card Td = new Card(Card.Rank.TEN, Card.Suit.DIAMONDS);
        Card nineC = new Card(Card.Rank.NINE, Card.Suit.CLUBS);
        Card sixC = new Card(Card.Rank.SIX, Card.Suit.CLUBS);
        Card fourD = new Card(Card.Rank.FOUR, Card.Suit.DIAMONDS);

        Board board2 = new Board(As, Td, nineC, sixC, fourD);
        CombinationAnalizer.sortBoard(board2);

        Board expected2 = new Board(fourD, sixC, nineC, Td, As);
        assertEquals(board2, expected2);

        Card sixD = new Card("6d");
        Card nineD = new Card("9d");
        Card fiveD = new Card("5d");
        Card Ad = new Card("ad");
        Card threeD = new Card("3d");

        Board board3 = new Board(sixC, nineD, fiveD, Ad, threeD);
        CombinationAnalizer.sortBoard(board3);

        Board expected3 = new Board(threeD, fiveD, sixC, nineD, Ad);
        assertEquals(board3, expected3);
    }
    // endregion
}
