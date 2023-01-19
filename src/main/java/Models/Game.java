package Models;

import Exceptions.IncorrectBoardException;
import Exceptions.IncorrectHandException;

import java.util.ArrayList;
import java.util.Date;

/**
 * Class defining the entity of a game (1 hand played among players).
 * Contains information about cards, pot size, hands dealt to players
 * (if revealed) and other.
 */
public class Game {
    private final String gameId;

    public Game(String gameId, double bigBlindSize$) {
        this.gameId = gameId;
        this.bigBlindSize$ = bigBlindSize$;
    }

    // List of all players in the hand, player on index 0 is sitting on dealer position,
    // then following all players ordered by going clockwise (to the left - SB, BB, LJ, HJ, CO).
    private ArrayList<PlayerInGame> players;
    private double extraCashAmount = 0;
    private Date date;
    private final double bigBlindSize$;
    private Board board;

    private StreetDescription preFlop;
    private StreetDescription flop;
    private StreetDescription turn;
    private StreetDescription river;

    // It is probably better to just make methods, that will
    // return the following info - will save space.
    // However, maybe it will make the analyzing time go up.
    private boolean isPot3Bet;
    private boolean isPot4Bet;
    private boolean isPot5Bet;
    private boolean isPotMultiWay;
    private boolean isGameFoldedPF;
    private boolean isHeroInGame;

    public String getGameId() {
        return gameId;
    }

    public double getBigBlindSize$() {
        return bigBlindSize$;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public ArrayList<PlayerInGame> getPlayers() {
        return new ArrayList<>(players);
    }

    public void setPlayerHand(PositionType pos, ArrayList<Card> hand) throws IncorrectHandException {
        if (hand.size() != 2 || hand.get(0).equals(hand.get(1))) {
            throw new IllegalArgumentException("The hand must consist of 2 different cards");
        }
        for (PlayerInGame player : players) {
            if (player.getPositionType() == pos) {
                player.setHand(hand);
                return;
            }
        }
    }

    public void setHeroHand(ArrayList<Card> hand) throws IncorrectHandException {
        for (PlayerInGame p : players) {
            if (p.getHash().equals("Hero")) {
                setPlayerHand(p.getPositionType(), hand);
                return;
            }
        }
        // Not sure which exception should i invoke here.
        throw new RuntimeException("Hero was not found in game with id" + gameId);
    }

    public void setPlayers(ArrayList<PlayerInGame> players) {
        this.players = players;
    }

    public boolean isExtraCash() {
        return extraCashAmount != 0;
    }

    public void setExtraCash(double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("amount must be >= 0");
        }

        extraCashAmount = amount;
    }

//    public StreetDescription getPreFlop() {
//        return preFlop;
//    }

    public double getExtraCashAmount() {
        return extraCashAmount;
    }

    public double getSB() {
        if (bigBlindSize$ % 2 == 0) {
            return bigBlindSize$ / 2;
        }
        return bigBlindSize$ * 0.4;
    }

    public void setPreFlop(StreetDescription preFlop) throws IncorrectBoardException {
        this.preFlop = new StreetDescription(preFlop.getPotAfterBetting(), preFlop.getBoard(), preFlop.getPlayersAfterBetting(), preFlop.getAllActions());
    }

    public StreetDescription getPreFlop() {
        return new StreetDescription(preFlop);
    }

    public StreetDescription getFlop() {
        return new StreetDescription(flop);
    }

    public void setFlop(StreetDescription flop) {
        this.flop = new StreetDescription(flop);
    }

    public StreetDescription getTurn() {
        return new StreetDescription(turn);
    }

    public void setTurn(StreetDescription turn) {
        this.turn = new StreetDescription(turn);
    }

    public StreetDescription getRiver() {
        return new StreetDescription(river);
    }

    public void setRiver(StreetDescription river) {
        this.river = new StreetDescription(river);
    }

    public Board getBoard() {
        return new Board(board);
    }

    public void setBoard(Board board) {
        this.board = new Board(board);
    }
}