package AnalizerTests;

import Analizer.CombinationAnalizer;
import Models.Board;
import Models.Card;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CombinationAnalizerTest {


    @Test
    public void testFlushCountingMethod() {
        Board b = new Board("2c", "4c", "8c", "Kc", "Jc");
        assertEquals(CombinationAnalizer.countFlushSuit(b), Card.Suit.CLUBS);

        b = new Board("5d", "Ad", "Kd", "2d", "5s");
        assertNull(CombinationAnalizer.countFlushSuit(b));

        b = new Board("5d", "Ad", "Kd", "2d", "5s", "6d", "Ks");
        assertEquals(CombinationAnalizer.countFlushSuit(b), Card.Suit.DIAMONDS);

        ArrayList<Card> cards = new ArrayList<Card>(List.of(
                new Card("5d"),
                new Card("Ad"),
                new Card("Kd"),
                new Card("2d"),
                new Card("5s"),
                new Card("6d"),
                new Card("Ks"),
                new Card("6s"),
                new Card("9s"),
                new Card("Ts"),
                new Card("Qs"),
                new Card("As"),
                new Card("Js")));
    }


    @Test
    public void testFindFLushRoyal() {
        Board rf = new Board("Ah", "Kh", "6s", "Ts", "Th", "Qh", "Jh");
        assertDoesNotThrow(() -> CombinationAnalizer.recognizeCombinationOnBoard(rf, null));


    }


    @Test
    public void testFindBestStraightFLush() {

    }

    @Test
    public void testBoardValid1() {
        // Invalid boards
        Board b = new Board("2c", "3c", "4c", "5c", "2c");
        assertFalse(CombinationAnalizer.isBoardValid(b));

        b = new Board("2c", "2c", "3d", "Ad");
        assertFalse(CombinationAnalizer.isBoardValid(b));

        b = new Board("5c", "Qd", "2c", "8d", "Qd", "6s");
        assertFalse(CombinationAnalizer.isBoardValid(b));

        b = new Board("2h", "2h", "2h");
        assertFalse(CombinationAnalizer.isBoardValid(b));

        b = new Board("6h", "9d", "8h", "Ts", "Ts");
        assertFalse(CombinationAnalizer.isBoardValid(b));

        // Valid boards
        b = new Board("3d", "Ks", "Jd", "3h", "Jh");
        assertTrue(CombinationAnalizer.isBoardValid(b));

        b = new Board("Ad", "6c", "7c", "8d", "Js", "5s", "8s");
        assertTrue(CombinationAnalizer.isBoardValid(b));
    }

    @Test
    public void testBoardValid2() {
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

        valid = new ArrayList<>(List.of(EightH, EightC, Js, Ks, Qs, Ts, NineH));
        assertTrue(CombinationAnalizer.isBoardValid(valid));
    }
}
