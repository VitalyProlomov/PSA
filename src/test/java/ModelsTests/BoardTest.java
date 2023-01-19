package ModelsTests;

import Exceptions.IncorrectBoardException;
import Exceptions.IncorrectCardException;
import Models.Board;
import Models.Card;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {
    @Test
    public void testBoardConstructor() throws IncorrectCardException, IncorrectBoardException {
        Card c1 = new Card("4c");
        Card c2 = new Card("As");
        Card c3 = new Card("Ks");
        Card c4 = new Card("6h");
        Card c5 = new Card("Td");
        Card c6 = new Card("9d");
        Card c7 = new Card("Js");
        Card c8 = new Card("Qs");

        assertThrows(IncorrectBoardException.class, () -> new Board(c1));
        assertThrows(IncorrectBoardException.class, () -> new Board(c1, c2));
        assertThrows(IncorrectBoardException.class, () -> new Board(c1, c2, c3, c4, c5, c6, c7, c8));

        assertDoesNotThrow(() -> new Board(c1, c2, c3));
        assertDoesNotThrow(() -> new Board(c1, c2, c3, c4));
        assertDoesNotThrow(() -> new Board(c1, c2, c3, c4, c5));

        Board b = new Board(c1, c2, c3, c4, c5);
        ArrayList<Card> actual = b.getCards();
        ArrayList<Card> expected = new ArrayList<Card>(List.of(new Card[]{c1, c2, c3, c4, c5}));

        assertEquals(actual, expected);
        ArrayList<Card> nc = new ArrayList<>(List.of(new Card[]{c2, c5, c3, c8, c4}));
        b.setCards(nc);

        assertEquals(b.getCards(), new ArrayList<Card>(List.of(new Card[]{c2, c5, c3, c8, c4})));

        assertThrows(IncorrectBoardException.class, () -> b.setCards(new ArrayList<>(List.of(new Card[]{c2, c5}))));
    }

    @Test
    public void testBoardEquals() throws IncorrectBoardException, IncorrectCardException {
        Board a = new Board("3c", "7d", "As");
        Board b = new Board("3c", "7d", "As");
        boolean s = a.equals(b);
        assertEquals(a, b);

        b = new Board("7d", "As", "3c");
        assertEquals(a, b);

        b = new Board("3c", "7d", "As", "4h");
        assertNotEquals(a, b);

        b = new Board("3c", "7d", "Ad");
        assertNotEquals(a, b);

        b = null;
        assertNotEquals(a, b);

        Object obj = new Object();
        assertNotEquals(a, obj);

        a = new Board("2c", "3c", "4c", "5c", "6c");
        b = new Board("2c", "3c", "4c", "5c", "6c");
        assertEquals(a, b);

        b = new Board("2c", "3c", "4c", "5c");
        assertNotEquals(a, b);
    }

    @Test
    public void testCopyConstructor() throws IncorrectBoardException, IncorrectCardException {
        Board b = new Board("Kc", "Jd", "6s", "As", "2s");
        Board copy = new Board(b);
        assertEquals(b, copy);

        ArrayList<Card> cards = b.getCards();
        cards.set(2, new Card("Qh"));
        b.setCards(cards);
        assertNotEquals(b, copy);
    }

}
