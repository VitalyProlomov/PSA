import Models.Card;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CardTest {
    @Test
    public void testCardInitialization() {
        Card card1 = new Card(Card.Rank.NINE, Card.Suit.SPADES);
        Card card2 = new Card(Card.Rank.KING, Card.Suit.HEARTS);

        assertEquals(Card.Rank.NINE, card1.getRank());
        assertEquals(Card.Suit.SPADES, card1.getSuit());

        assertEquals(Card.Rank.KING, card2.getRank());
        assertEquals(Card.Suit.HEARTS, card2.getSuit());
    }

    @Test
    public void testCardComparison() {
        Card card = new Card(Card.Rank.ACE, Card.Suit.SPADES);
        Card sameCars = new Card(Card.Rank.ACE, Card.Suit.SPADES);
        Card diffCard1 = new Card(Card.Rank.ACE, Card.Suit.HEARTS);
        Card diffCard2 = new Card(Card.Rank.QUEEN, Card.Suit.SPADES);
        Card diffCard3 = new Card(Card.Rank.NINE, Card.Suit.CLUBS);

        assertEquals(card, sameCars);
        assertNotEquals(card, diffCard1);
        assertNotEquals(card, diffCard2);
        assertNotEquals(card, diffCard3);

        card = new Card(Card.Rank.EIGHT, Card.Suit.DIAMONDS);
        sameCars = new Card(Card.Rank.EIGHT, Card.Suit.DIAMONDS);
        diffCard1 = new Card(Card.Rank.JACK, Card.Suit.DIAMONDS);
        diffCard2 = new Card(Card.Rank.EIGHT, Card.Suit.SPADES);
        diffCard3 = new Card(Card.Rank.SIX, Card.Suit.CLUBS);

        assertEquals(card, sameCars);
        assertNotEquals(card, diffCard1);
        assertNotEquals(card, diffCard2);
        assertNotEquals(card, diffCard3);
    }

    @Test
    public void testCorrectCardInitializationWStrRepresentation() {
        Card Ah = new Card("Ah");
        Card Kc = new Card("Kc");
        Card Jd = new Card("Jd");
        Card JD = new Card("JD");
        Card jd = new Card("jd");
        Card eightS = new Card("8S");
        Card eights = new Card("8s");
        Card twoH = new Card("2H");
        Card Td = new Card("Td");


        assertEquals(Ah, new Card(Card.Rank.ACE, Card.Suit.HEARTS));
        assertEquals(Kc, new Card(Card.Rank.KING, Card.Suit.CLUBS));
        assertEquals(Jd, new Card(Card.Rank.JACK, Card.Suit.DIAMONDS));
        assertEquals(JD, new Card(Card.Rank.JACK, Card.Suit.DIAMONDS));
        assertEquals(jd, new Card(Card.Rank.JACK, Card.Suit.DIAMONDS));
        assertEquals(eightS, new Card(Card.Rank.EIGHT, Card.Suit.SPADES));
        assertEquals(eights, new Card(Card.Rank.EIGHT, Card.Suit.SPADES));
        assertEquals(twoH, new Card(Card.Rank.TWO, Card.Suit.HEARTS));
        assertEquals(Td, new Card(Card.Rank.TEN, Card.Suit.DIAMONDS));
    }

    @Test
    public void testIncorrectCardsInitializations() {
        assertThrows(IllegalArgumentException.class, () -> new Card("10h"));
        assertThrows(IllegalArgumentException.class, () -> new Card("15d"));
        assertThrows(IllegalArgumentException.class, () -> new Card("11s"));
        assertThrows(IllegalArgumentException.class, () -> new Card("9i"));
        assertThrows(IllegalArgumentException.class, () -> new Card("8"));
        assertThrows(IllegalArgumentException.class, () -> new Card("Ae"));
        assertThrows(IllegalArgumentException.class, () -> new Card("J2"));
    }

}