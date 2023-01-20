package parsers.gg;

import exceptions.IncorrectBoardException;
import exceptions.IncorrectCardException;
import exceptions.IncorrectHandException;
import models.*;
import parsers.Parser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;
import static models.PositionType.BB;
import static models.PositionType.SB;

public class GGPokerokRushNCashParser implements Parser {
    private int curLine = 0;

    @Override
    public Game parseGame(String gameText) throws IncorrectCardException, IncorrectHandException, IncorrectBoardException {
        String[] lines = gameText.split("\n");

        ArrayList<ArrayList<String>> wordsInLines = new ArrayList<>();
        for (String line : lines) {
            wordsInLines.add(new ArrayList<>(List.of(line.split(" "))));
        }

        Game game = initiateGame(wordsInLines);
        parseDate(game, wordsInLines);
        parsePlayers(game, wordsInLines);
        parseExtraCash(game, wordsInLines);
        parseHeroHand(game, wordsInLines);
        parseStreetDescriptions(game, wordsInLines, game.getExtraCashAmount());
        if (game.getRiver().getPlayersAfterBetting().size() > 1) {
            parseAndAddShownHands(game, wordsInLines);
        }
        parseWinnings(game, wordsInLines);
        return game;
    }

    private Game initiateGame(ArrayList<ArrayList<String>> wordsInLines) {
        String handId = wordsInLines.get(0).get(2);
        handId = handId.substring(0, handId.length() - 1);

        double bbSize = parseDouble(wordsInLines.get(0).get(7).split("/[$]")[1].split("[)]")[0]);

        // Creating a game with BB size (in dollars) and hand id.
        return new Game(handId, bbSize);
    }

    private void parseDate(Game game, ArrayList<ArrayList<String>> wordsInLines) {
        String dateRep = wordsInLines.get(0).get(9);
        String[] datePieces = dateRep.split("/");
        Date date = new Date(parseInt(datePieces[0]), parseInt(datePieces[1]) - 1, parseInt(datePieces[2]));
        game.setDate(date);

        wordsInLines.remove(0);
    }


    private void parsePlayers(Game game, ArrayList<ArrayList<String>> wordsInLines) {
        // Getting info about players abd setting to the game
        ArrayList<String> hashes = new ArrayList<>();
        ArrayList<Double> balances = new ArrayList<>();

        for (curLine = 1; curLine < 1 + 6; ++curLine) {
            hashes.add(wordsInLines.get(curLine).get(2));
            String balanceStr = wordsInLines.get(curLine).get(3);
            balances.add(parseDouble(balanceStr.substring(2)));
        }


        ArrayList<PositionType> positions = new ArrayList<>(List.of(PositionType.BTN,
                SB, BB, PositionType.LJ, PositionType.HJ, PositionType.CO));

        ArrayList<PlayerInGame> players = new ArrayList<>();
        for (int i = 0; i < 6; ++i) {
            players.add(new PlayerInGame(hashes.get(i), positions.get(i), balances.get(i)));
        }
        game.setPlayers(players);
    }

    private void parseExtraCash(Game game, ArrayList<ArrayList<String>> wordsInLines) {
        if (wordsInLines.get(0).get(0).equals("Cash")) {
            ArrayList<String> cashLine = wordsInLines.get(0);
            // In case I would want to collect stats about cash drops.
            double extraCash = parseDouble(wordsInLines.get(0).get(cashLine.size() - 1).substring(1));
            game.setExtraCash(extraCash);
            wordsInLines.remove(0);
        } else {
            game.setExtraCash(0);
        }

    }

    private void parseHeroHand(Game game, ArrayList<ArrayList<String>> wordsInLines)
            throws IncorrectCardException, IncorrectHandException {
        while (!wordsInLines.get(curLine).get(2).equals("Hero")) {
            ++curLine;
        }

        Card c1 = new Card(wordsInLines.get(curLine).get(3).substring(1));
        Card c2 = new Card((wordsInLines.get(curLine).get(4).substring(0, 2)));
        ArrayList<Card> heroHand = new ArrayList<>(List.of(c1, c2));
        game.setHeroHand(heroHand);
    }

    private void parseStreetDescriptions(Game game, ArrayList<ArrayList<String>> wordsInLines, double extraCashAmount)
            throws IncorrectBoardException, IncorrectCardException {
        parsePreFlop(game, wordsInLines, extraCashAmount);

//        if (game.getPreFlop().isAllIn()) {
//            parseAndAddShownHands();
//        }
        parseFlop(game, wordsInLines);
        parseTurn(game, wordsInLines);
        parseRiver(game, wordsInLines);
//        double pot = game.
//        parseFlop(game, wordsInLines, pot);
    }

    private void parsePreFlop(Game game, ArrayList<ArrayList<String>> wordsInLines, double extraCashAmount)
            throws IncorrectBoardException {
        while (wordsInLines.get(curLine).get(0).equals("Dealt")) {
            ++curLine;
        }

        double initPot = game.getBigBlindSize$() + game.getSB() + extraCashAmount;
        StreetDescription pfsd = parseStreetAction(game, wordsInLines, initPot);
        game.setPreFlop(pfsd);
    }

    private void parseFlop(Game game, ArrayList<ArrayList<String>> wordsInLines)
            throws IncorrectCardException, IncorrectBoardException {
        Card c1 = new Card(wordsInLines.get(curLine).get(3).substring(1));
        Card c2 = new Card(wordsInLines.get(curLine).get(4));
        Card c3 = new Card(wordsInLines.get(curLine).get(5).substring(0, 2));
        Board flopBoard = new Board(c1, c2, c3);

        ++curLine;

        StreetDescription flop = parseStreetAction(game, wordsInLines, game.getPreFlop().getPotAfterBetting());
        flop.setBoard(flopBoard);
        game.setFlop(flop);
    }

    private void parseTurn(Game game, ArrayList<ArrayList<String>> wordsInLines) throws IncorrectCardException, IncorrectBoardException {
        Card tCard = new Card(wordsInLines.get(curLine).get(6).substring(1, 3));
        ++curLine;

        double curPot = game.getFlop().getPotAfterBetting();
        StreetDescription turn = parseStreetAction(game, wordsInLines, curPot);

        ArrayList<Card> cards = new ArrayList<>(game.getFlop().getBoard().getCards());
        cards.add(tCard);
        turn.setBoard(new Board(cards));

        game.setTurn(turn);
    }

    private void parseRiver(Game game, ArrayList<ArrayList<String>> wordsInLines)
            throws IncorrectCardException, IncorrectBoardException {
        Card rCard = new Card(wordsInLines.get(curLine).get(7).substring(1, 3));
        ++curLine;

        double curPot = game.getTurn().getPotAfterBetting();
        StreetDescription river = parseStreetAction(game, wordsInLines, curPot);

        ArrayList<Card> cards = new ArrayList<>(game.getTurn().getBoard().getCards());
        cards.add(rCard);
        river.setBoard(new Board(cards));

        game.setRiver(river);
    }

    private StreetDescription parseStreetAction(Game game, ArrayList<ArrayList<String>> wordsInLines, double curPot) {
        StreetDescription st = new StreetDescription();
        // Adding blinds posting and players left on pre-flop.
        if (curPot - (game.getBigBlindSize$() + game.getSB()) < 0.01) {
            st.addAction(new Action(Action.ActionType.BET, game.getPosPlayersMap().get(SB), game.getSB(), 0));
            st.addAction(new Action(Action.ActionType.BET, game.getPosPlayersMap().get(BB), game.getBigBlindSize$(), game.getSB()));

            st.setPlayersAfterBetting(game.getPlayers());
        } else {
            if (game.getTurn() != null) {
                st.setPlayersAfterBetting(game.getTurn().getPlayersAfterBetting());
            } else if (game.getFlop() != null) {
                st.setPlayersAfterBetting(game.getFlop().getPlayersAfterBetting());
            } else if (game.getPreFlop() != null) {
                st.setPlayersAfterBetting(game.getPreFlop().getPlayersAfterBetting());
            }
        }
        st.setPotAfterBetting(curPot);

        int allInPlayers = 0;
        while (!wordsInLines.get(curLine).get(0).equals("***") &&
                st.getPlayersAfterBetting().size() > 1 &&
                !wordsInLines.get(curLine).get(1).equals("shows")) {
            String hash = wordsInLines.get(curLine).get(0);
            hash = hash.substring(0, hash.length() - 1);
            PlayerInGame curPlayer = game.getPlayers().get(0);
            for (PlayerInGame p : game.getPlayers()) {
                if (p.getHash().equals(hash)) {
                    curPlayer = p;
                    break;
                }
            }
            // For me
            if (!curPlayer.getHash().equals(hash)) {
                throw new RuntimeException("Code is incorrect - couldn`t find the player " +
                        "with given hash in array of players in game.");
            }

            addAction(wordsInLines.get(curLine), st, curPlayer);
            ++curLine;
        }
        return st;
    }

    private void addAction(ArrayList<String> line, StreetDescription st, PlayerInGame curPlayer) {
        Action action;
        double amount;
        st.addPlayerAfterBetting(curPlayer);
        switch (line.get(1)) {
            case "folds" -> {
                action = new Action(Action.ActionType.FOLD, curPlayer, 0, st.getPotAfterBetting());
                st.removePlayerAfterBetting(curPlayer);
            }
            case "raises" -> {
                double lastAmount = 0;
                for (int i = st.getAllActions().size() - 1; i >= 0; --i) {
                    if (st.getAllActions().get(i).getPlayerInGame().equals(curPlayer)) {
                        // If he folded, he wouldnt be raising now, so old case is impossible.
                        // If he checked, it meant no one bet before him (or it is BB on pre-flop)
                        lastAmount = st.getAllActions().get(i).getAmount();
                        break;
                    }
                }
                amount = parseDouble(line.get(4).substring(1));
                action = new Action(Action.ActionType.RAISE, curPlayer, amount, st.getPotAfterBetting());
                st.setPotAfterBetting(st.getPotAfterBetting() + amount - lastAmount);
            }
            case "calls" -> {
                amount = parseDouble(line.get(2).substring(1));
                action = new Action(Action.ActionType.CALL, curPlayer, amount, st.getPotAfterBetting());
                st.setPotAfterBetting(st.getPotAfterBetting() + amount);
            }
            case "bets" -> {
                amount = parseDouble(line.get(2).substring(1));
                action = new Action(Action.ActionType.BET, curPlayer, amount, st.getPotAfterBetting());
                st.setPotAfterBetting(st.getPotAfterBetting() + amount);
            }
            default -> action = new Action(Action.ActionType.CHECK, curPlayer, 0, st.getPotAfterBetting());
        }

        st.addAction(action);
    }

    private void parseAndAddShownHands(Game game, ArrayList<ArrayList<String>> wordsInLines) throws IncorrectCardException, IncorrectHandException {
        while (!wordsInLines.get(curLine).get(0).equals("***")) {
            String hash = (wordsInLines.get(curLine).get(0));
            hash = hash.substring(0, hash.length() - 1);

            Card c1 = new Card(wordsInLines.get(curLine).get(2).substring(1));
            Card c2 = new Card(wordsInLines.get(curLine).get(3).substring(0, 2));
            Hand hand = new Hand(c1, c2);

            game.setPlayerHand(hash, hand);
            ++curLine;
        }
        ++curLine;
    }

    private void parseWinnings(Game game, ArrayList<ArrayList<String>> wordsInLines) {
        String winnerHash = wordsInLines.get(curLine).get(0);
        game.setWinner(game.getPlayer(winnerHash));

        curLine += 2;

        double finalPot = parseDouble(wordsInLines.get(curLine).get(2).substring(1));
        double rake = parseDouble(wordsInLines.get(curLine).get(5).substring(1));

        game.setFinalPot(finalPot);
        game.setRake(rake);

        // Will adjust later - dont forget about new style changes (e.g: fortune)
//        double jackpot = parseDouble(wordsInLines.get(curLine).get(8).substring(1));
//        double bingo = parseDouble(wordsInLines.get(curLine).get(11).substring(1));
    }


    @Override
    public ArrayList<Game> parseFile(String path) {
        return null;
    }
}
