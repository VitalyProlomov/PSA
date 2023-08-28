package analizerTests;

import analizer.GameAnalyzer;
import exceptions.IncorrectBoardException;
import exceptions.IncorrectCardException;
import exceptions.IncorrectHandException;
import models.Game;
import org.junit.jupiter.api.Test;
import parsers.Parser;
import parsers.gg.GGPokerokRushNCashParser;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class GameAnalyzerTest {
    @Test
    public void testIsFlopCheckRaisedByCaller() throws IncorrectHandException, IncorrectBoardException, IOException, IncorrectCardException {
        Parser parser = new GGPokerokRushNCashParser();

        String path = "src/test/resources/ggPokerokFiles/rushNCashGamesFiles/fullGame.txt";
        Game game = parser.parseFile(path).get(0);
        assertFalse(GameAnalyzer.isFlopCheckRaisedByCaller(game));

        path = "src/test/resources/ggPokerokFiles/rushNCashGamesFiles/checkRaisingTestsGames/flopCheckRaisedTwoPlayersGames.txt";
        ArrayList<Game> twoPlayersCheckRaiseGames = parser.parseFile(path);
        for (Game g : twoPlayersCheckRaiseGames) {
            assertTrue(GameAnalyzer.isFlopCheckRaisedByCaller(g));
        }

        path = "src/test/resources/ggPokerokFiles/rushNCashGamesFiles/checkRaisingTestsGames/falseCheckRaisingGames.txt";
        ArrayList<Game> falseCheckRaiseGames = parser.parseFile(path);
        for (Game g : falseCheckRaiseGames) {
            assertFalse(GameAnalyzer.isFlopCheckRaisedByCaller(g));
        }

        path = "src/test/resources/ggPokerokFiles/rushNCashGamesFiles/gameSession2.txt";
        ArrayList<Game> games = parser.parseFile(path);
        for (int i = 0; i < games.size(); ++i) {
            if (GameAnalyzer.isFlopCheckRaisedByCaller(games.get(i))) {
                System.out.println(games.get(i).getGameId());
            }
        }
    }

}
