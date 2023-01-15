package ParserTests.RushNCashParserTest;

import Models.Game;
import Models.PlayerInGame;
import Models.PositionType;
import Parsers.GG.GGPokerokRushNCashParser;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameInfoParserTest {
    @Test
    public void testGameInfoSplitting() throws IOException {
        GGPokerokRushNCashParser parser = new GGPokerokRushNCashParser();
        URL gameURL =  GameInfoParserTest.class.getResource("/gameExample");
        FileReader fr = new FileReader(gameURL.getFile().replace("%20", " "));
        Scanner scanner = new Scanner(fr);

        StringBuilder gameText = new StringBuilder();
        ArrayList<String> lines = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            gameText.append(line + "\n");
            lines.add(line);
        }
        Game topG = parser.parseGame(gameText.toString());

        String correctID = "#RC1221829603";
        assertEquals(correctID, topG.getHandId());

        double correctBbSize = 0.25;
        assertEquals(correctBbSize, topG.getBigBlindSize$());

        Date date = new Date(2022, 12 - 1, 15);
        assertEquals(date, topG.getDate());

        ArrayList<PlayerInGame> correctPlayers = new ArrayList<>(List.of(
                new PlayerInGame("96112e6e", PositionType.BTN, 42.6),
                new PlayerInGame("bdfdf5d3", PositionType.SB, 35.23),
                new PlayerInGame("Hero", PositionType.BB, 58.12),
                new PlayerInGame("906a880b", PositionType.LJ, 148.44),
                new PlayerInGame("40b398d7", PositionType.HJ, 30.35),
                new PlayerInGame("805c6855", PositionType.CO, 21.99)
        ));

        assertEquals(correctPlayers, topG.getPlayers());

    }

    @Test
    public void testFindingFile() throws FileNotFoundException {
        FileReader fr = new FileReader("D:\\HSE\\3rd course\\Course Project\\PSA\\target\\test-classes\\gameExample.txt");
        Scanner sc = new Scanner(fr);
        String line = sc.nextLine();
        System.out.println(line);
    }
}
