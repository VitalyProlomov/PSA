package parserTests;

import exceptions.IncorrectBoardException;
import exceptions.IncorrectCardException;
import exceptions.IncorrectHandException;
import models.*;
import org.junit.jupiter.api.Test;
import parsers.gg.GGPokerokRushNCashParser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.*;

import static models.Action.ActionType.*;
import static models.PositionType.*;
import static org.junit.jupiter.api.Assertions.*;

public class GameInfoParserTest {
    public ArrayList<PositionType> orderedPositions =
            new ArrayList<>(List.of(SB, BB, LJ, HJ, CO, BTN));

    @Test
    public void testFullGameInfoSplitting() throws IOException, IncorrectCardException, IncorrectHandException, IncorrectBoardException {
        GGPokerokRushNCashParser parser = new GGPokerokRushNCashParser();

        Game topG = parser.parseGame(getTextFromFile("/RushNCashGamesFiles/gameExample"));

        String correctID = "RC1221829603";
        assertEquals(correctID, topG.getGameId());

        double correctBbSize = 0.25;
        assertEquals(correctBbSize, topG.getBigBlindSize$());

        Date date = new Date("2022/12/15 21:35:07");
        assertEquals(date, topG.getDate());

        ArrayList<PlayerInGame> correctPlayers = new ArrayList<>(List.of(
                new PlayerInGame("96112e6e", BTN, 42.6),
                new PlayerInGame("bdfdf5d3", SB, 35.23),
                new PlayerInGame("Hero", PositionType.BB, 58.12),
                new PlayerInGame("906a880b", PositionType.LJ, 148.44),
                new PlayerInGame("40b398d7", PositionType.HJ, 30.35),
                new PlayerInGame("805c6855", CO, 21.99)
        ));

        assertEquals(new HashSet<>(correctPlayers), new HashSet<>(topG.getPlayers()));

        Hand heroHand = new Hand(new Card("Ad"), new Card("Ts"));
        correctPlayers.get(2).setHand(heroHand);
        assertEquals(topG.getPosPlayersMap().get(PositionType.BB).getHand(), new Hand(heroHand));

        ArrayList<Action> actions = new ArrayList<>();
        actions.add(new Action(BET, correctPlayers.get(1).getId(), 0.1, 0));
        actions.add(new Action(BET, correctPlayers.get(2).getId(), 0.25, 0.1));
        actions.add(new Action(Action.ActionType.FOLD, correctPlayers.get(3).getId(), 0, 0.35));
        actions.add(new Action(Action.ActionType.FOLD, correctPlayers.get(4).getId(), 0, 0.35));
        actions.add(new Action(Action.ActionType.RAISE, correctPlayers.get(5).getId(), 0.63, 0.35));
        actions.add(new Action(Action.ActionType.FOLD, correctPlayers.get(0).getId(), 0, 0.98));
        actions.add(new Action(Action.ActionType.FOLD, correctPlayers.get(1).getId(), 0, 0.98));
        actions.add(new Action(Action.ActionType.CALL, correctPlayers.get(2).getId(), 0.38, 0.98));

        ArrayList<PlayerInGame> left = new ArrayList<>(List.of(correctPlayers.get(2), correctPlayers.get(5)));
        StreetDescription correctPreFlop = new StreetDescription(1.36, null, left, actions);
        //for (PositionType pos : orderedPositions) {
        assertEquals(correctPreFlop, topG.getPreFlop());

        actions = new ArrayList<>();
        actions.add((new Action(Action.ActionType.CHECK, correctPlayers.get(2).getId(), 0, 1.36)));
        actions.add((new Action(Action.ActionType.CHECK, correctPlayers.get(5).getId(), 0, 1.36)));

        StreetDescription correctFLop = new StreetDescription(1.36,
                new Board("8d", "6d", "Qh"),
                new ArrayList<PlayerInGame>(List.of(
                        correctPlayers.get(2), correctPlayers.get(5))), actions);

        correctFLop.setPlayersAfterBetting(new ArrayList<>(
                List.of(correctPlayers.get(2), correctPlayers.get(5))));

        assertEquals(topG.getFlop(), correctFLop);

        actions = new ArrayList<>(List.of(new Action(Action.ActionType.CHECK,
                        correctPlayers.get(2).getId(), 0, 1.36),
                new Action(Action.ActionType.CHECK, correctPlayers.get(5).getId(), 0, 1.36)));

        StreetDescription correctTurn = new StreetDescription(1.36,
                new Board("8d", "6d", "Qh", "4s"),
                new ArrayList<>(List.of(correctPlayers.get(2), correctPlayers.get(5))), actions);

        assertEquals(correctTurn, topG.getTurn());

        actions = new ArrayList<>(List.of(
                new Action(Action.ActionType.CHECK, correctPlayers.get(2).getId(), 0, 1.36),
                new Action(Action.ActionType.CHECK, correctPlayers.get(5).getId(), 0, 1.36))
        );

        StreetDescription correctRiver = new StreetDescription(
                1.36,
                new Board("8d", "6d", "Qh", "4s", "9s"),
                new ArrayList<>(List.of(correctPlayers.get(2), correctPlayers.get(5))),
                actions
        );

        assertEquals(correctRiver, topG.getRiver());

        double finalPot = 1.36;
        double rake = 0.06;

        PlayerInGame winner = new PlayerInGame(correctPlayers.get(5));

        assertEquals(winner, topG.getWinner());
        assertEquals(finalPot, topG.getFinalPot());
        assertEquals(rake, topG.getRake());
    }


    private String getTextFromFile(String path) throws FileNotFoundException {
        URL gameURL = GameInfoParserTest.class.getResource(path);
        assert gameURL != null;
        FileReader fr = new FileReader(gameURL.getFile().replace("%20", " "));
        Scanner scanner = new Scanner(fr);

        StringBuilder gameText = new StringBuilder();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            gameText.append(line).append("\n");
        }
        return gameText.toString();
    }

    @Test
    public void testEarlyAllinGameParsing()
            throws FileNotFoundException, IncorrectHandException,
            IncorrectBoardException, IncorrectCardException {
        GGPokerokRushNCashParser parser = new GGPokerokRushNCashParser();
        String text = getTextFromFile("/RushNCashGamesFiles/earlyAllInGame");

        Game topG = parser.parseGame(text);

        assertEquals("RC1281883052", topG.getGameId());
        assertEquals(new Date("2023/01/15 20:20:28"), topG.getDate());
        assertEquals(0.05, topG.getBigBlindSize$());

        ArrayList<PlayerInGame> correctPlayers = new ArrayList<>(List.of(
                new PlayerInGame("c491325f", BTN, 6.12),
                new PlayerInGame("Hero", SB, 13.4),
                new PlayerInGame("16d75d78", BB, 6.28),
                new PlayerInGame("c1f81489", LJ, 10.52),
                new PlayerInGame("361dc4bb", HJ, 2.31),
                new PlayerInGame("219f215e", CO, 7.88))
        );

        for (int i = 0; i < 6; ++i) {
            assertTrue(topG.getPlayers().contains(correctPlayers.get(i)));
            assertEquals(correctPlayers.get(i).getPosition(),
                    topG.getPlayer(correctPlayers.get(i).getId()).getPosition());
            assertEquals(correctPlayers.get(i).getBalance(),
                    topG.getPlayer(correctPlayers.get(i).getId()).getBalance());
        }
        Hand heroHand = new Hand("3s", "9d");
        assertEquals(heroHand, topG.getPlayer("Hero").getHand());

        ArrayList<Action> preFlopActions = new ArrayList<>(List.of(
                new Action(BET, "Hero", 0.02, 0),
                new Action(BET, "16d75d78", 0.05, 0.02),
                new Action(FOLD, "c1f81489", 0, 0.07),
                new Action(FOLD, "361dc4bb", 0, 0.07),
                new Action(RAISE, "219f215e", 0.13, 0.07),
                new Action(CALL, "c491325f", 0.13, 0.2),
                new Action(FOLD, "Hero", 0, 0.33),
                new Action(FOLD, "16d75d78", 0, 0.33))
        );
        assertEquals(preFlopActions, topG.getPreFlop().getAllActions());

        // Flop assertions
        ArrayList<Action> flopActions = new ArrayList<>(List.of(
                new Action(BET, "219f215e", 0.2, 0.33),
                new Action(RAISE, "c491325f", 5.99, 0.53),
                new Action(CALL, "219f215e", 5.79, 6.52)
        ));

        assertEquals(12.31, topG.getFlop().getPotAfterBetting());
        assertEquals(flopActions, topG.getFlop().getAllActions());
        assertTrue(topG.getFlop().getPlayersAfterBetting().contains(new PlayerInGame("c491325f")));
        assertTrue(topG.getFlop().getPlayersAfterBetting().contains(new PlayerInGame("219f215e")));
        assertEquals(2, topG.getFlop().getPlayersAfterBetting().size());
        assertEquals(new Board( "Ks", "5s", "3c"), topG.getFlop().getBoard());

        // Turn assertions
        assertEquals(12.31, topG.getTurn().getPotAfterBetting());
        assertEquals(new ArrayList<>(), topG.getTurn().getAllActions());
        assertTrue(topG.getTurn().getPlayersAfterBetting().contains(new PlayerInGame("c491325f")));
        assertTrue(topG.getTurn().getPlayersAfterBetting().contains(new PlayerInGame("219f215e")));
        assertEquals(2, topG.getTurn().getPlayersAfterBetting().size());
        assertEquals(new Board( "Ks", "5s", "3c", "Kd"), topG.getTurn().getBoard());

        // River assertions
        assertEquals(12.31, topG.getRiver().getPotAfterBetting());
        assertEquals(new ArrayList<>(), topG.getRiver().getAllActions());
        assertTrue(topG.getRiver().getPlayersAfterBetting().contains(new PlayerInGame("c491325f")));
        assertTrue(topG.getRiver().getPlayersAfterBetting().contains(new PlayerInGame("219f215e")));
        assertEquals(2, topG.getRiver().getPlayersAfterBetting().size());
        assertEquals(new Board( "Ks", "5s", "3c", "Kd", "6c"), topG.getRiver().getBoard());

        assertEquals(correctPlayers.get(0), topG.getWinner());
        assertEquals(12.31, topG.getFinalPot());
        assertEquals(0.22, topG.getRake());
        assertEquals(new Hand("5h", "5d"), topG.getPlayer("c491325f").getHand());
        assertEquals(new Hand("Ah", "Kh"), topG.getPlayer("219f215e").getHand());
    }

    @Test
    public void testPreFlopAllInExtraCashGameParsing() throws FileNotFoundException, IncorrectHandException, IncorrectBoardException, IncorrectCardException {
        GGPokerokRushNCashParser parser = new GGPokerokRushNCashParser();
        String text = getTextFromFile("/RushNCashGamesFiles/preFlopAllInExtraCashGame");

        Game topG = parser.parseGame(text);
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
