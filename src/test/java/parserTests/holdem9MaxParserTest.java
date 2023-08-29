package parserTests;

import exceptions.IncorrectBoardException;
import exceptions.IncorrectCardException;
import exceptions.IncorrectHandException;
import models.*;
import org.junit.jupiter.api.Test;
import parsers.Parser;
import parsers.gg.GGPokerokHoldem9MaxParser;

import java.io.IOException;
import java.util.ArrayList;

import static models.Action.ActionType.*;
import static models.PositionType.*;
import static org.junit.jupiter.api.Assertions.*;

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
        assertTrue(Math.abs(23.56 - game.getPlayer("eb8f3f2f").getBalance()) < 0.005);
        assertTrue(Math.abs(22.22 - game.getPlayer("b1807748").getBalance()) < 0.005);
        assertTrue(Math.abs(game.getInitialBalances().get("Hero") - game.getPlayer("Hero").getBalance() - 0.10) < 0.005);
        assertEquals(new Hand(new Card("Qs"), new Card("Jd")),
                game.getPlayer("Hero").getHand());
        assertNull(game.getFlop());

        game = g.get(1);
        assertEquals(6, game.getPlayers().size());
        assertEquals(UTG_2, game.getPlayer("Hero").getPosition());
        assertEquals(BB, game.getPlayer("f334408a").getPosition());
        assertEquals(UTG_1, game.getPlayer("b858c98b").getPosition());

        assertEquals(45.43, game.getInitialBalances().get("68f87b81"));
        assertEquals(56.97, game.getInitialBalances().get("2d6ccd2e"));
        assertEquals(10.86, game.getInitialBalances().get("f334408a"));

        assertEquals(0.69 + 56.97, game.getPlayer("2d6ccd2e").getBalance());
        assertEquals(10.86 - 0.05 - 0.45, game.getPlayer("f334408a").getBalance());
        assertEquals(29.7 - 0.05, game.getPlayer("Hero").getBalance());

        StreetDescription flop = new StreetDescription();
        flop.setBoard(new Board("3s", "5d", "Jh"));
        flop.setPotAfterBetting(1.19);
        ArrayList<PlayerInGame> pl = new ArrayList<>();
        pl.add(new PlayerInGame("2d6ccd2e"));
        flop.setPlayersAfterBetting(pl);

        assertEquals(new Action(CHECK, "f334408a", 0, 1.25), game.getFlop().getAllActions().get(0));
        assertEquals(new Action(BET, "2d6ccd2e", 0.42, 1.25), game.getFlop().getAllActions().get(1));
        assertEquals(new Action(FOLD, "f334408a", 0, 1.67), game.getFlop().getAllActions().get(2));

        assertEquals(flop.getPlayersAfterBetting(), game.getFlop().getPlayersAfterBetting());
        assertEquals(flop.getBoard(), game.getFlop().getBoard());
        assertEquals(0.06, game.getRake());
        assertEquals(flop.getPotAfterBetting() + game.getRake(), game.getFlop().getPotAfterBetting());
    }

}
