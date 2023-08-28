package parserTests;

import exceptions.IncorrectBoardException;
import exceptions.IncorrectCardException;
import exceptions.IncorrectHandException;
import models.Game;
import org.junit.jupiter.api.Test;
import parsers.Parser;
import parsers.gg.GGPokerokHoldem9MaxParser;

import java.io.IOException;
import java.util.ArrayList;

import static models.PositionType.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class holdem9MaxParserTest {
    @Test
    public void testParsingPlayersAmount() throws IncorrectHandException, IncorrectBoardException, IncorrectCardException, IOException {
        Parser parser = new GGPokerokHoldem9MaxParser();
        String path = "";
        ArrayList<Game> g = parser.parseFile("src/test/resources/ggPokerokFiles/gamesFiles/holdem9Max/extraCashGames.txt");

        Game game = g.get(0);
        assertEquals(8, game.getPlayers().size());
        assertEquals(SB, game.getPlayer("Hero").getPosition());
        assertEquals(BTN, game.getPlayer("b858c98b").getPosition());
        assertEquals(UTG, game.getPlayer("b1807748").getPosition());
        assertTrue(Math.abs(16.51 - game.getInitialBalances().get("f334408a")) < 0.005);
        assertTrue(Math.abs(23.06 - game.getInitialBalances().get("eb8f3f2f")) < 0.005);
        assertTrue(Math.abs(5.12 - game.getInitialBalances().get("917be6aa")) < 0.005);
        assertTrue(Math.abs(4.97 - game.getPlayer("917be6aa").getBalance()) < 0.005);

        System.out.println(game.getPreFlop());

        assertEquals(6, g.get(1).getPlayers().size());


    }

}
