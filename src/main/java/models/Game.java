package models;

import exceptions.IncorrectHandException;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

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

    private HashMap<PositionType, PlayerInGame> players = new HashMap<>();

    private double extraCashAmount = 0;
    private Date date;
    private final double bigBlindSize$;
    private Board board;

    private StreetDescription preFlop;
    private StreetDescription flop;
    private StreetDescription turn;
    private StreetDescription river;

    private PlayerInGame winner;
    private double finalPot;
    private double rake;

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
        return new ArrayList<>(players.values());
    }

    public HashMap<PositionType, PlayerInGame> getPosPlayersMap() {
        return new HashMap<PositionType, PlayerInGame>(players);
    }

    /**
     * Returns the player in the game with the corresponding hash. If there is
     * no player in game with such hash, null is returned
     * @param hash hash field of PlayerInGame
     * @return player in game with same hash. Or null if no such player is found
     */
    public PlayerInGame getPlayer(String hash) {
        for (PositionType pos : players.keySet()) {
            PlayerInGame p = players.get(pos);

            if (p.getHash().equals(hash)) {
                return new PlayerInGame(p);
            }
        }
        return null;
    }

    public void setPlayerHand(PositionType pos, ArrayList<Card> hand) throws IncorrectHandException {
        if (hand.size() != 2 || hand.get(0).equals(hand.get(1))) {
            throw new IllegalArgumentException("The hand must consist of 2 different cards");
        }
        players.get(pos).setHand(hand);
    }

    public void setPlayerHand(String playerHash, Hand hand) throws IncorrectHandException {

        for (PositionType pos : players.keySet()) {
            if (players.get(pos).getHash().equals(playerHash)) {
                players.get(pos).setHand(hand);
                return;
            }
        }
    }

    public void setHeroHand(ArrayList<Card> hand) throws IncorrectHandException {
        for (PositionType pos : players.keySet()) {
            if (players.get(pos).getHash().equals("Hero")) {
                setPlayerHand(pos, hand);
                return;
            }
        }
        // Not sure which exception should i invoke here.
        throw new RuntimeException("Hero was not found in game with id" + gameId);
    }

    public void setPlayers(ArrayList<PlayerInGame> players) {
        for (PlayerInGame p : players) {
            this.players.put(p.getPosition(), p);
        }
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

    public StreetDescription getPreFlop() {
        if (this.preFlop == null) {
            return null;
        }
        return new StreetDescription(preFlop);
    }

    public void setPreFlop(StreetDescription preFlop) {
        if (this.preFlop == null) {
            this.preFlop = preFlop;
            return;
        }
        this.preFlop = new StreetDescription(preFlop);
    }

    public StreetDescription getFlop() {
        if (flop == null) {
            return null;
        }
        return new StreetDescription(flop);
    }

    public void setFlop(StreetDescription flop) {
        if (flop == null) {
            this.flop = null;
            return;
        }
        this.flop = new StreetDescription(flop);
    }

    public StreetDescription getTurn() {
        if (turn == null) {
            return null;
        }
        return new StreetDescription(turn);
    }

    public void setTurn(StreetDescription turn) {
        if (turn == null) {
            this.turn = null;
            return;
        }
        this.turn = new StreetDescription(turn);
    }

    public StreetDescription getRiver() {
        if (this.river == null) {
            return null;
        }
        return new StreetDescription(river);
    }

    public void setRiver(StreetDescription river) {
        if (river == null) {
            this.river = null;
            return;
        }
        this.river = new StreetDescription(river);
    }

    public Board getBoard() {
        if (this.board == null) {
            return null;
        }
        return new Board(board);
    }

    public void setBoard(Board board) {
        if (board == null) {
            this.board = null;
            return;
        }
        this.board = new Board(board);
    }

    public PlayerInGame getWinner() {
        if (winner == null) {
            return null;
        }
        return new PlayerInGame(winner);
    }

    public void setWinner(PlayerInGame winner) {
        if (winner == null) {
            this.winner = null;
            return;
        }
        this.winner = new PlayerInGame(winner);
    }

    public double getFinalPot() {
        return finalPot;
    }

    public void setFinalPot(double finalPot) {
        this.finalPot = finalPot;
    }

    public double getRake() {
        return rake;
    }

    public void setRake(double rake) {
        this.rake = rake;
    }
}