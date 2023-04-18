package modelsTests;

import javafx.geometry.Pos;
import models.Game;
import models.PlayerInGame;
import org.junit.jupiter.api.Test;
import parsers.gg.GGPokerokRushNCashParser;

import java.util.HashSet;
import java.util.List;

import static models.PositionType.BTN;
import static models.PositionType.CO;
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

    @Test
    public void testDecrementBalance() {
        Game game = new Game("Test", 10);
        HashSet<PlayerInGame> players = new HashSet<>(List.of(
                new PlayerInGame("player1", CO,  1000),
                new PlayerInGame("player2", BTN, 960)
        ));
        game.setPlayers(players);
        game.decrementPlayersBalance("player1", 50);
        assertEquals(game.getPlayer("player1").getBalance(), 950);

    }

}
