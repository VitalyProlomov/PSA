package parserTests;

import exceptions.IncorrectBoardException;
import exceptions.IncorrectCardException;
import exceptions.IncorrectHandException;
import models.*;
import parsers.gg.GGPokerokRushNCashParser;
import org.junit.jupiter.api.Test;

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
    public void testFullGameInfoSplitting() throws IOException, IncorrectCardException, IncorrectHandException, IncorrectBoardException {
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
        assertEquals(correctID, topG.getGameId());

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

        ArrayList<Card> heroHand = new ArrayList<>(List.of(new Card("Ad"), new Card("Ts")));
        correctPlayers.get(2).setHand(heroHand);
        assertEquals(topG.getPlayers().get(2).getHand(), new Hand(heroHand.get(0), heroHand.get(1)));

        ArrayList<Action> actions = new ArrayList<>();
        actions.add(new Action(Action.ActionType.BET, correctPlayers.get(1), 0.1, 0));
        actions.add(new Action(Action.ActionType.BET, correctPlayers.get(2), 0.25, 0.1));
        actions.add(new Action(Action.ActionType.FOLD, correctPlayers.get(3), 0, 0.35));
        actions.add(new Action(Action.ActionType.FOLD, correctPlayers.get(4), 0, 0.35));
        actions.add(new Action(Action.ActionType.RAISE, correctPlayers.get(5), 0.63, 0.35));
        actions.add(new Action(Action.ActionType.FOLD, correctPlayers.get(0), 0, 0.98));
        actions.add(new Action(Action.ActionType.FOLD, correctPlayers.get(1), 0, 0.98));
        actions.add(new Action(Action.ActionType.CALL, correctPlayers.get(2), 0.38, 0.98));

        ArrayList<PlayerInGame> left = new ArrayList<>(List.of(topG.getPlayers().get(2), topG.getPlayers().get(5)));
        StreetDescription correctPreFlop = new StreetDescription(1.36, null, left ,actions);
        assertEquals(correctPreFlop, topG.getPreFlop());

        actions = new ArrayList<>();
        actions.add((new Action(Action.ActionType.CHECK, correctPlayers.get(2), 0, 1.36)));
        actions.add((new Action(Action.ActionType.CHECK, correctPlayers.get(5), 0, 1.36)));

        StreetDescription correctFLop = new StreetDescription(1.36, new Board("8d" , "6d", "Qh"),
                new ArrayList<PlayerInGame>(List.of(correctPlayers.get(2), correctPlayers.get(5))), actions);
//        System.out.println(topG.getFlop());
//        System.out.println(correctFLop);
        assertEquals(topG.getFlop(), correctFLop);
    }

    @Test
    public void testGameCreation() {

    }

    @Test
    public void testGameDateSetting() {

    }

    @Test
    public void testPlayerSetting() {

    }


//    @Test
//    public void testFindingFile() throws FileNotFoundException {
//        FileReader fr = new FileReader("D:\\HSE\\3rd course\\Course Project\\PSA\\src\\test\\resources\\gameExample");
//        Scanner sc = new Scanner(fr);
//        String line = sc.nextLine();
//        System.out.println(line);
//    }
}
