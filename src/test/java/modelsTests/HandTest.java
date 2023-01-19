package modelsTests;

import exceptions.IncorrectCardException;
import exceptions.IncorrectHandException;
import models.Card;
import models.Hand;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HandTest {
    @Test
    public void testHandConstructor() throws IncorrectCardException, IncorrectHandException {
        Card c1 = new Card("Kc");
        Card c2 = new Card("Js");
        Hand h = new Hand(c1, c2);
        assertEquals(h.getFirstCard(), c1);
        assertEquals(h.getSecondCard(), c2);

        Card cSame = new Card("Kc");
        assertThrows(IncorrectHandException.class, () -> new Hand(c1, cSame));
    }

    @Test
    public void testHandEquals() throws IncorrectCardException, IncorrectHandException {
        Card c1 = new Card("Kc");
        Card c2 = new Card("Js");
        Hand h1 = new Hand(c1, c2);
        Hand h2 = new Hand(c1, new Card("Js"));
        assertEquals(h1, h2);

        Card c3 = new Card("Th");
        Hand h3 = new Hand(c1, c3);
        assertNotEquals(h1, h3);
        assertNotEquals(h2, h3);

        Hand h4 = new Hand(new Card("2h"), new Card("2d"));
        Hand h5 = new Hand(new Card("2d"), new Card("2h"));
        assertEquals(h4, h5);

        Hand h6 = new Hand(new Card("2d"), new Card("2s"));
        assertNotEquals(h5, h6);
        assertNotEquals(h4, h6);
    }

    @Test
    public void testHandToString() throws IncorrectHandException, IncorrectCardException {
        Hand h = new Hand(new Card("9c"), new Card("5d"));
        assertEquals(h.toString(), "[9♣ 5♦]");
    }
}
