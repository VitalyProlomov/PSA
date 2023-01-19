package ModelsTests;

import Exceptions.IncorrectCardException;
import Exceptions.IncorrectHandException;
import Models.Card;
import Models.Hand;
import Models.PlayerInGame;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerInGameTests {
    @Test
    public void testSetHand() throws IncorrectCardException, IncorrectHandException {
        PlayerInGame p = new PlayerInGame("96112e6e");
        Hand h = new Hand(new Card("Ad"), new Card("4c"));
        p.setHand(h);
        assertEquals(p.getHand().getFirstCard(), h.getFirstCard());
        assertEquals(p.getHand().getSecondCard(), h.getSecondCard());

        assertThrows(IncorrectHandException.class,
                () -> p.setHand(new ArrayList<>(List.of(new Card("Jd"), new Card("Jd")))));

        h = new Hand(new Card("Kd"), new Card("Jd"));
        p.setHand(new ArrayList<>(List.of(new Card("Kd"), new Card("Jd"))));

        assertEquals(p.getHand(), h);
    }

    @Test
    public void testEquals() {
        PlayerInGame p1 = new PlayerInGame("7htb6k23");
        PlayerInGame p2 = new PlayerInGame("7htb6k23");
        assertEquals(p1, p2);

        p2 = new PlayerInGame("23");
        assertNotEquals(p1, p2);

    }

}
