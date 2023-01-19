package analizerTests;

import analizer.CombinationAnalizer;
import exceptions.IncorrectBoardException;
import exceptions.IncorrectCardException;
import exceptions.IncorrectHandException;
import models.Board;
import models.Card;
import models.ComboBoardPair;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CombinationAnalizerTest {


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
        assertEquals(new ComboBoardPair(CombinationAnalizer.Combinations.FLUSH_ROYAL, c),
                        CombinationAnalizer.recognizeCombinationOnBoard(rf, null));
    }


    @Test
    public void testFindBestStraightFLush() {

    }

    @Test
    public void testBoardValid1() throws IncorrectBoardException, IncorrectCardException {
        // Invalid boards
        Board b = new Board("2c", "3c", "4c", "5c", "2c");
        assertFalse(CombinationAnalizer.isBoardValid(b));

        b = new Board("2c", "2c", "3d", "Ad");
        assertFalse(CombinationAnalizer.isBoardValid(b));

        b = new Board("5c", "Qd", "2c", "8d", "Qd");
        assertFalse(CombinationAnalizer.isBoardValid(b));

        b = new Board("2h", "2h", "2h");
        assertFalse(CombinationAnalizer.isBoardValid(b));

        b = new Board("6h", "9d", "8h", "Ts", "Ts");
        assertFalse(CombinationAnalizer.isBoardValid(b));

        // Valid boards
        b = new Board("3d", "Ks", "Jd", "3h", "Jh");
        assertTrue(CombinationAnalizer.isBoardValid(b));

        b = new Board("Ad", "6c", "7c", "8d", "8s");
        assertTrue(CombinationAnalizer.isBoardValid(b));
    }

    @Test
    public void testBoardValid2() throws IncorrectBoardException, IncorrectCardException {
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
}
