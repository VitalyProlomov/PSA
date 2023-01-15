package Parsers.GG;

import Models.Game;
import Models.PlayerInGame;
import Models.PositionType;
import Parsers.Parser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

public class GGPokerokRushNCashParser implements Parser {
    @Override
    public Game parseGame(String gameText) {
        String[] lines = gameText.split("\n");

        ArrayList<ArrayList<String>> wordsInLines = new ArrayList<>();
        for (String line : lines) {
            wordsInLines.add(new ArrayList(List.of(line.split(" "))));
        }

        String handId = lines[0].split(" ")[2];
        handId = handId.substring(0, handId.length() - 1);

        double bbSize = parseDouble(wordsInLines.get(0).get(7).split("/[$]")[1].split("[)]")[0]);

        String dateRep = lines[0].split(" ")[9];
        String[] datePieces = dateRep.split("/");
        Date date = new Date(parseInt(datePieces[0]), parseInt(datePieces[1]) - 1, parseInt(datePieces[2]));
        Game game = new Game(handId, bbSize, date);

        ArrayList<String> hashes = new ArrayList<>();
        ArrayList<Double> balances = new ArrayList<Double>();
        for (int lineInd = 2; lineInd < 2 + 6; ++lineInd) {
            hashes.add(wordsInLines.get(lineInd).get(2));
            String balanceStr = wordsInLines.get(lineInd).get(3);
            balances.add(parseDouble(balanceStr.substring(2, balanceStr.length())));
        }
        ArrayList<PositionType> positions = new ArrayList<>(List.of(PositionType.BTN,
                PositionType.SB, PositionType.BB, PositionType.LJ, PositionType.HJ, PositionType.CO));

        ArrayList<PlayerInGame> players = new ArrayList<>();
        for (int i = 0; i < 6; ++i) {
            players.add(new PlayerInGame(hashes.get(i), positions.get(i) , balances.get(i)));
        }

        game.setPlayers(players);
        // game.getPlayers().get();
        return game;
    }

    @Override
    public ArrayList<Game> parseFile(String path) {
        return null;
    }
}
