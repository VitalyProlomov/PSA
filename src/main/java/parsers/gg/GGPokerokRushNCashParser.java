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
import static models.PositionType.BB;
import static models.PositionType.SB;

public class GGPokerokRushNCashParser implements Parser {
    private int curLine = 0;

    @Override
    public Game parseGame(String gameText)
            throws IncorrectCardException, IncorrectHandException, IncorrectBoardException {
        curLine = 0;
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

        parseWinnings(game, wordsInLines);
        return game;
    }

    private Game initiateGame(ArrayList<ArrayList<String>> wordsInLines) {
        String handId = wordsInLines.get(curLine).get(2);
        handId = handId.substring(1, handId.length() - 1);

        double bbSize = parseDouble(wordsInLines.get(0).get(7).split("/[$]")[1].split("[)]")[0]);

        // Creating a game with BB size (in dollars) and hand id.
        return new Game(handId, bbSize);
    }

    private void parseDate(Game game, ArrayList<ArrayList<String>> wordsInLines) {
        String dateRep = wordsInLines.get(curLine).get(9);
        dateRep += " " + wordsInLines.get(curLine).get(10);
        Date date = new Date(dateRep);
        game.setDate(date);

        ++curLine;
    }


    private void parsePlayers(Game game, ArrayList<ArrayList<String>> wordsInLines) {
        // Getting info about players abd setting to the game
        ArrayList<String> hashes = new ArrayList<>();
        ArrayList<Double> balances = new ArrayList<>();

        ++curLine;
        for (int i = 0; i < 6; ++i) {
            hashes.add(wordsInLines.get(curLine + i).get(2));
            String balanceStr = wordsInLines.get(curLine + i).get(3);
            balances.add(parseDouble(balanceStr.substring(2)));
        }

        curLine += 6;


        ArrayList<PositionType> positions = new ArrayList<>(List.of(PositionType.BTN,
                SB, BB, PositionType.LJ, PositionType.HJ, PositionType.CO));

        ArrayList<PlayerInGame> players = new ArrayList<>();
        for (int i = 0; i < 6; ++i) {
            players.add(new PlayerInGame(hashes.get(i), positions.get(i), balances.get(i)));
        }
        game.setPlayers(players);
    }

    private void parseExtraCash(Game game, ArrayList<ArrayList<String>> wordsInLines) {
        if (wordsInLines.get(curLine).get(0).equals("Cash")) {
            ArrayList<String> cashLine = wordsInLines.get(curLine);
            // In case I would want to collect stats about cash drops.
            double extraCash = parseDouble(wordsInLines.get(curLine).get(cashLine.size() - 1).substring(1));
            game.setExtraCash(extraCash);
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
        Hand heroHand = new Hand(c1, c2);
        game.setHeroHand(heroHand);
    }

    private void parseStreetDescriptions(Game game, ArrayList<ArrayList<String>> wordsInLines, double extraCashAmount)
            throws IncorrectBoardException, IncorrectCardException, IncorrectHandException {
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
            throws IncorrectHandException, IncorrectCardException {
        while (wordsInLines.get(curLine).get(0).equals("Dealt")) {
            ++curLine;
        }

        double initPot = game.getBigBlindSize$() + game.getSB() + extraCashAmount;
        StreetDescription pfsd = parseStreetAction(game, wordsInLines, initPot);
        game.setPreFlop(pfsd);
    }

    private void parseFlop(Game game, ArrayList<ArrayList<String>> wordsInLines)
            throws IncorrectCardException, IncorrectBoardException, IncorrectHandException {
        Card c1 = new Card(wordsInLines.get(curLine).get(3).substring(1));
        Card c2 = new Card(wordsInLines.get(curLine).get(4));
        Card c3 = new Card(wordsInLines.get(curLine).get(5).substring(0, 2));
        Board flopBoard = new Board(c1, c2, c3);

        ++curLine;
        StreetDescription flop;
        if (!game.getPreFlop().isAllIn()) {
            flop = parseStreetAction(game, wordsInLines, game.getPreFlop().getPotAfterBetting());
        } else {
            flop = new StreetDescription(
                    game.getPreFlop().getPotAfterBetting(),
                    flopBoard,
                    game.getPreFlop().getPlayersAfterBetting(),
                    new ArrayList<Action>());
            flop.setAllIn(true);
        }

        flop.setBoard(flopBoard);
        game.setFlop(flop);

        while (!wordsInLines.get(curLine).get(0).equals("***")) {
            ++curLine;
        }
    }

    private void parseTurn(Game game, ArrayList<ArrayList<String>> wordsInLines) throws IncorrectCardException, IncorrectBoardException, IncorrectHandException {
        Card tCard = new Card(wordsInLines.get(curLine).get(6).substring(1, 3));
        ++curLine;

        double curPot = game.getFlop().getPotAfterBetting();

        // Cards will be added later in this method
        StreetDescription turn;
        if (!game.getFlop().isAllIn()) {
            turn = parseStreetAction(game, wordsInLines, curPot);
        } else {
            turn  = new StreetDescription(
                    game.getFlop().getPotAfterBetting(),
                    null,
                    game.getFlop().getPlayersAfterBetting(),
                    new ArrayList<Action>());
            turn.setAllIn(true);
        }

        ArrayList<Card> cards = new ArrayList<>(game.getFlop().getBoard().getCards());
        cards.add(tCard);
        turn.setBoard(new Board(cards));

        game.setTurn(turn);
    }

    private void parseRiver(Game game, ArrayList<ArrayList<String>> wordsInLines)
            throws IncorrectCardException, IncorrectBoardException, IncorrectHandException {
        Card rCard = new Card(wordsInLines.get(curLine).get(7).substring(1, 3));
        ++curLine;

        double curPot = game.getTurn().getPotAfterBetting();
        StreetDescription river;

        if(!game.getTurn().isAllIn()) {
            river = parseStreetAction(game, wordsInLines, curPot);
            // if (game.getRiver().getPlayersAfterBetting().size() > 1) {
                parseAndAddShownHands(game, wordsInLines);
           // }
        } else {
            river = new StreetDescription(
                    game.getTurn().getPotAfterBetting(),
                    null,
                    game.getTurn().getPlayersAfterBetting(),
                    new ArrayList<>());
            river.setAllIn(true);
        }

        ArrayList<Card> cards = new ArrayList<>(game.getTurn().getBoard().getCards());
        cards.add(rCard);
        river.setBoard(new Board(cards));

        game.setRiver(river);
    }

    private StreetDescription parseStreetAction(Game game, ArrayList<ArrayList<String>> wordsInLines, double curPot) throws IncorrectCardException, IncorrectHandException {
        StreetDescription st = new StreetDescription();
        // Adding blinds posting and players left on pre-flop.
        if (curPot - (game.getBigBlindSize$() + game.getSB() + game.getExtraCashAmount()) < 0.01) {
            st.addAction(new Action(Action.ActionType.BET, game.getPosPlayersMap().get(SB).getId(), game.getSB(), 0));
            st.addAction(new Action(Action.ActionType.BET, game.getPosPlayersMap().get(BB).getId(), game.getBigBlindSize$(), game.getSB()));

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
        while (wordsInLines.get(curLine).get(0).charAt(wordsInLines.get(curLine).get(0).length() - 1) == ':' &&
                st.getPlayersAfterBetting().size() > 1 &&
                !wordsInLines.get(curLine).get(1).equals("shows")) {
            String hash = wordsInLines.get(curLine).get(0);
            hash = hash.substring(0, hash.length() - 1);
            PlayerInGame curPlayer = game.getPlayer(hash);

            // For me
            if (curPlayer == null) {
                throw new RuntimeException("Code is incorrect - couldn't find the player " +
                        "with given hash in array of players in game.");
            }

            addAction(wordsInLines.get(curLine), st, curPlayer);
            ++curLine;
        }

        while (wordsInLines.get(curLine).get(0).equals("Uncalled")) {
            ++curLine;
        }

        // Getting shown hands if all players are all-in.
        while (wordsInLines.get(curLine).get(1).equals("shows")) {
            String hash = wordsInLines.get(curLine).get(0);
            hash = hash.substring(0, hash.length() - 1);
            Card card1 = new Card(wordsInLines.get(curLine).get(2).substring(1, 3));
            Card card2 = new Card(wordsInLines.get(curLine).get(3).substring(0, 2));
            Hand hand = new Hand(card1, card2);
            game.setPlayerHand(hash, hand);
            ++curLine;

            st.setAllIn(true);
        }

        return st;
    }

    private void addAction(ArrayList<String> line, StreetDescription st, PlayerInGame curPlayer) {
        Action action;
        double amount;
        st.addPlayerAfterBetting(curPlayer);
        switch (line.get(1)) {
            case "folds" -> {
                action = new Action(Action.ActionType.FOLD, curPlayer.getId(), 0, st.getPotAfterBetting());
                st.removePlayerAfterBetting(curPlayer);
            }
            case "raises" -> {
                double lastAmount = 0;
                for (int i = st.getAllActions().size() - 1; i >= 0; --i) {
                    if (st.getAllActions().get(i).getPlayerId().equals(curPlayer.getId())) {
                        // If he folded, he wouldnt be raising now, so old case is impossible.
                        // If he checked, it meant no one bet before him (or it is BB on pre-flop)
                        lastAmount = st.getAllActions().get(i).getAmount();
                        break;
                    }
                }
                amount = parseDouble(line.get(4).substring(1));
                action = new Action(Action.ActionType.RAISE, curPlayer.getId(), amount, st.getPotAfterBetting());
                st.setPotAfterBetting(st.getPotAfterBetting() + amount - lastAmount);
            }
            case "calls" -> {
                amount = parseDouble(line.get(2).substring(1));
                action = new Action(Action.ActionType.CALL, curPlayer.getId(), amount, st.getPotAfterBetting());
                st.setPotAfterBetting(st.getPotAfterBetting() + amount);
            }
            case "bets" -> {
                amount = parseDouble(line.get(2).substring(1));
                action = new Action(Action.ActionType.BET, curPlayer.getId(), amount, st.getPotAfterBetting());
                st.setPotAfterBetting(st.getPotAfterBetting() + amount);
            }
            default -> action = new Action(Action.ActionType.CHECK, curPlayer.getId(), 0, st.getPotAfterBetting());
        }

        st.addAction(action);
    }

    private void parseAndAddShownHands(Game game, ArrayList<ArrayList<String>> wordsInLines) throws IncorrectCardException, IncorrectHandException {
        while (!wordsInLines.get(curLine).get(0).equals("***")) {
            if (wordsInLines.get(curLine).get(1).equals("shows")) {
                String hash = (wordsInLines.get(curLine).get(0));
                hash = hash.substring(0, hash.length() - 1);

                Card c1 = new Card(wordsInLines.get(curLine).get(2).substring(1));
                Card c2 = new Card(wordsInLines.get(curLine).get(3).substring(0, 2));
                Hand hand = new Hand(c1, c2);

                game.setPlayerHand(hash, hand);
            }
            // Should see what other lines could be here (excluding showing of hands).
            ++curLine;
        }
    }

    private void parseWinnings(Game game, ArrayList<ArrayList<String>> wordsInLines) {
        while (!wordsInLines.get(curLine).get(1).equals("collected")) {
            ++curLine;
        }
        ArrayList<String> allWinners = new ArrayList<>();
        while (wordsInLines.size() < curLine + 1 && wordsInLines.get(curLine + 1).get(1).equals("collected")) {
            ++curLine;
            allWinners.add(wordsInLines.get(curLine).get(0));
        }

        String winnerHash = wordsInLines.get(curLine).get(0);
        game.setWinner(game.getPlayer(winnerHash));

        curLine += 2;

        double finalPot = parseDouble(wordsInLines.get(curLine).get(2).substring(1));
        double rake = parseDouble(wordsInLines.get(curLine).get(5).substring(1));
        // Jackpot rake.
        rake += parseDouble(wordsInLines.get(curLine).get(8).substring(1));
        // Bingo rake
        rake += parseDouble(wordsInLines.get(curLine).get(11).substring(1));
        // Only for new games (not sure about date, but definitely not
        // before 15.12.2022)
        if (wordsInLines.get(curLine).size() > 13) {
            // "Fortune" rake
            rake += parseDouble(wordsInLines.get(curLine).get(14).substring(1));
        }
        game.setFinalPot(finalPot);
        game.setRake(rake);
    }


    @Override
    public ArrayList<Game> parseFile(String path) {
        return null;
    }
}
