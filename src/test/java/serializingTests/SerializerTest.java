package serializingTests;

import analizer.Combination;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import exceptions.IncorrectBoardException;
import exceptions.IncorrectCardException;
import exceptions.IncorrectHandException;
import javafx.geometry.Pos;
import models.*;
import org.junit.jupiter.api.Test;
import parserTests.rushNCashParsingTest;
import parsers.gg.GGPokerokRushNCashParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SerializerTest {
    private String getFullPath(String path) {
        URL url = rushNCashParsingTest.class.getResource(path);
        assert url != null;

        return url.getFile().replace("%20", " ");
    }

    @Test
    public void serializeGamesTest() throws IncorrectHandException, IncorrectBoardException, IOException, IncorrectCardException {
        String path = getFullPath("/ggPokerokFiles/rushNCashGamesFiles/severalSessions");
        GGPokerokRushNCashParser parser = new GGPokerokRushNCashParser();

        ArrayList<Game> allGames = parser.parseDirectoryFiles(path);

        // Verified Amount on pokerCraft
        assertEquals(3205, allGames.size());

        ObjectMapper objectMapper = new ObjectMapper();
        File file = new File(getFullPath("/serializedFiles/serializedGames1.txt"));

        GamesSet gamesSet = new GamesSet();
        gamesSet.setGames(new HashSet<>(allGames));

        objectMapper.writeValue(file, gamesSet);

        StringBuilder text = new StringBuilder();
        FileReader fr = new FileReader(file);
        BufferedReader bfr = new BufferedReader(fr);
        String line = bfr.readLine();
        while (line != null) {
            text.append(line).append("\n");
            line = bfr.readLine();
        }

        GamesSet deserializedGame = objectMapper.readValue(text.toString(), GamesSet.class);
        assertEquals(gamesSet.getGames(), deserializedGame.getGames());
    }

    @Test
    public void serializeActionTest() throws JsonProcessingException {
        Action action = new Action(Action.ActionType.RAISE, "Fish", 10,1.65);
        ObjectMapper objectMapper = new ObjectMapper();

        String JSONtext = "";

        JSONtext = objectMapper.writeValueAsString(action);
        Action deserializedAction = objectMapper.readValue(JSONtext, Action.class);
    }

    @Test
    public void serializeBoardTest() throws JsonProcessingException, IncorrectBoardException, IncorrectCardException {
        Board board = new Board("As", "5d", "5h");
        ObjectMapper objectMapper = new ObjectMapper();

        String JSONtext = "";

        JSONtext = objectMapper.writeValueAsString(board);
        Board deserializedAction = objectMapper.readValue(JSONtext, board.getClass());
    }

    private Object serializeAndDeserializeGivenObject(Object object, Class objClass) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        String JSONtext = "";

        JSONtext = objectMapper.writeValueAsString(object);

        return objectMapper.readValue(JSONtext, objClass);
    }

    @Test
    public void serializeAndDeserializeEveryClass() throws IncorrectCardException, JsonProcessingException, IncorrectBoardException, IncorrectHandException {
        Object obj = new Action(Action.ActionType.CHECK, "", 20, 10);
        Object deserObject = serializeAndDeserializeGivenObject(obj, obj.getClass());
        assertEquals(obj, deserObject);

        obj = new Board("Ah", "Kd", "Qh");
        deserObject = serializeAndDeserializeGivenObject(obj, obj.getClass());
        assertEquals(obj, deserObject);

        obj = new Card("2d");
        deserObject = serializeAndDeserializeGivenObject(obj, obj.getClass());
        assertEquals(obj, deserObject);

        obj = new ComboCardsPair(Combination.PAIR, new ArrayList<Card>(
                List.of(new Card("3h"),
                new Card("Ah"),
                new Card("3d"),
                new Card("4h"),
                new Card("5h"))));
        deserObject = serializeAndDeserializeGivenObject(obj, obj.getClass());
        assertEquals(obj, deserObject);

        // Game

        obj = new Hand("7s", "8d");
        deserObject = serializeAndDeserializeGivenObject(obj, obj.getClass());
        assertEquals(obj, deserObject);

        obj = new PlayerInGame("Fish234", PositionType.CO, 20.00);
        deserObject = serializeAndDeserializeGivenObject(obj, obj.getClass());
        assertEquals(obj, deserObject);

        obj = PositionType.SB;
        deserObject = serializeAndDeserializeGivenObject(obj, obj.getClass());
        assertEquals(obj, deserObject);

        obj = new StreetDescription();
        deserObject = serializeAndDeserializeGivenObject(obj, obj.getClass());
        assertEquals(obj, deserObject);
        obj = new StreetDescription(100,  new Board("Ah", "7h", "Qh", "7s", "7c"),
                new ArrayList<>(List.of(
                        new PlayerInGame("P1", PositionType.BTN, 50.0),
                        new PlayerInGame("P2", PositionType.BB, 50.0))),
                new ArrayList<>(List.of(
                        new Action(Action.ActionType.BET,"P1", 50, 2),
                        new Action(Action.ActionType.CALL,"P2", 50, 2)
                )));
        deserObject = serializeAndDeserializeGivenObject(obj, obj.getClass());
        assertEquals(obj, deserObject);

        obj = new UserProfile("usersId");
        deserObject = serializeAndDeserializeGivenObject(obj, obj.getClass());
        assertEquals(obj, deserObject);

//        obj = new PlayerInGame()
    }



}
