package modelsTests;

import models.Game;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GameTest {
    @Test
    public void testGameGetSB() {
        Game game = new Game("RC3", 0.02);
        assertEquals(game.getSB(), 0.01);

        game = new Game("RC3", 0.05);
        assertTrue(Math.abs(game.getSB() - 0.02) < 0.001);

        game = new Game("RC1234567", 0.25);
        assertEquals(game.getSB(), 0.1);

        game = new Game("RC3", 0.5);
        assertEquals(game.getSB(), 0.25);

        game = new Game("RC4", 1);
        assertEquals(game.getSB(), 0.5);
    }

    // toString() test

}
