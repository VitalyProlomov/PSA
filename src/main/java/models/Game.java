package models;

import exceptions.IncorrectHandException;

import java.util.*;

import static models.Action.ActionType.RAISE;
import static models.PositionType.*;

/**
 * Class defining the entity of a game (1 hand played among players).
 * Contains information about cards, pot size, hands dealt to players
 * (if revealed) and other.
 */
public class Game {
    // Hope that it is unique for every game. But I will probably need to
    // add the date as the key too. (And add in equals)
    private final String gameId;

    /**
     * Constructs a new game with given ID and BB (given in dollars)
     *
     * @param gameId        Id of the game from PokerCraft parsed text view of the game
     * @param bigBlindSize$ value of 1 big blind in dollars
     */
    public Game(String gameId, double bigBlindSize$) {
        this.gameId = gameId;
        this.bigBlindSize$ = bigBlindSize$;
    }

    private HashMap<PositionType, PlayerInGame> players = new HashMap<>();

    // Amount of dollars as a cash drop (or 0 if there is no cash drop)
    private double extraCashAmount = 0;
    private Date date;
    private final double bigBlindSize$;

    private StreetDescription preFlop;
    private StreetDescription flop;
    private StreetDescription turn;
    private StreetDescription river;

    private HashMap<String, Double> allWinners;
    private double finalPot;
    private double rake;

    private final HashMap<String, Card> shownOneCards = new HashMap<>();

    /**
     * Adds information about one shown card (needed when player shows just one of his cards, not both).
     *
     * @param playerId id of hte player, that showed card.
     * @param card card that was shown
     * @return true, if the player with given id is present in that game and information is added;
     * false, otherwise.
     */
    public boolean addShownOneCard(String playerId, Card card) {
        if (this.players.containsValue(new PlayerInGame(playerId))) {
            shownOneCards.put(playerId, card);
            return true;
        } else {
            return false;
        }
    }

    public HashMap<String, Card> getShownOneCards() {
        return new HashMap<>(shownOneCards);
    }

    private int preFlopRaisesAmount = -1;

    public void countPreFlopRaises() {
        StreetDescription pf = getFlop();
        preFlopRaisesAmount = 0;
        for (int i = 0; i < pf.getAllActions().size(); ++i) {
            if (pf.getAllActions().get(i).getActionType().equals(RAISE)) {
                ++preFlopRaisesAmount;
            }
        }
    }

    public boolean isSingleRaised() {
        if (preFlopRaisesAmount == -1) {
            countPreFlopRaises();
        }
        return preFlopRaisesAmount == 2;
    }

    public boolean isPot3Bet() {
        if (preFlopRaisesAmount == -1) {
            countPreFlopRaises();
        }
        return preFlopRaisesAmount == 3;
    }

    public boolean isPot4Bet() {
        if (preFlopRaisesAmount == -1) {
            countPreFlopRaises();
        }
        return preFlopRaisesAmount == 4;
    }

    public boolean isPot5Bet() {
        if (preFlopRaisesAmount == -1) {
            countPreFlopRaises();
        }
        return preFlopRaisesAmount == 5;
    }

    public boolean isPotMultiWay() {
        return preFlop.getPlayersAfterBetting().size() > 2;
    }

    public boolean isGameFoldedPreFlop() {
        return preFlop.getPlayersAfterBetting().size() == 1;
    }

    public boolean isHeroInGame() {
        return getPlayer("Hero") != null;
    }

    public boolean isHeroPostFlop() {
        return preFlop.getPlayersAfterBetting().contains(new PlayerInGame("Hero"));
    }

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

    public HashSet<PlayerInGame> getPlayers() {
        return new HashSet<>(players.values());
    }

    public HashMap<PositionType, PlayerInGame> getPosPlayersMap() {
        return new HashMap<PositionType, PlayerInGame>(players);
    }

    /**
     * Returns the copy player in the game with the corresponding hash. If there is
     * no player in game with such hash, null is returned
     *
     * @param hash hash field of PlayerInGame to get
     * @return player in game with same hash. Or null if no such player is found
     */
    public PlayerInGame getPlayer(String hash) {
        for (PositionType pos : players.keySet()) {
            PlayerInGame p = players.get(pos);

            if (p.getId().equals(hash)) {
                return new PlayerInGame(p);
            }
        }
        return null;
    }

    /**
     * Sets the hand of the player on given poosition to the given hand
     *
     * @param pos  position of the player
     * @param hand Hand to set
     */
    public void setPlayerHand(PositionType pos, Hand hand) {
        players.get(pos).setHand(hand);
    }

    public void setPlayerHand(String playerHash, Hand hand) throws IncorrectHandException {

        for (PositionType pos : players.keySet()) {
            if (players.get(pos).getId().equals(playerHash)) {
                players.get(pos).setHand(hand);
                return;
            }
        }
    }

    /**
     * Sets Hero`s hand to the given hand
     *
     * @param hand
     */
    public void setHeroHand(Hand hand) {
        for (PositionType pos : players.keySet()) {
            if (players.get(pos).getId().equals("Hero")) {
                setPlayerHand(pos, hand);
                return;
            }
        }
        // Not sure which exception should i invoke here.
        throw new RuntimeException("Hero was not found in game with id" + gameId);
    }

    public void setPlayers(ArrayList<PlayerInGame> players) {
        // Should think about working w nulls.
        this.players = new HashMap<>();
        for (PlayerInGame p : players) {
            this.players.put(p.getPosition(), p);
        }
    }

    public void setPlayers(Set<PlayerInGame> players) {
        this.players = new HashMap<>();
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

    public double getExtraCashAmount() {
        return extraCashAmount;
    }

    public double getSB() {
        if (bigBlindSize$ * 100 % 2 == 0) {
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
        if (preFlop == null) {
            this.preFlop = null;
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

//    public Board getBoard() {
//        if (this.board == null) {
//            return null;
//        }
//        return new Board(board);
//    }
//
//    public void setBoard(Board board) {
//        if (board == null) {
//            this.board = null;
//            return;
//        }
//        this.board = new Board(board);
//    }

    public HashMap<String, Double> getWinners() {
        if (allWinners == null) {
            return null;
        }
        return new HashMap<String, Double>(allWinners);
    }

    public void setWinner(PlayerInGame winner, double amount) {
        this.allWinners = new HashMap<>();
        allWinners.put(winner.getId(), amount);
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

    @Override
    public String toString() {
        ArrayList<PlayerInGame> orderedPlayers = new ArrayList<>();
        ArrayList<PositionType> orderPos = new ArrayList<>(List.of(SB, BB, TB, UTG, UTG_1, UTG_2, LJ, HJ, CO, BTN));
        for (int i = 0; i < orderPos.size(); ++i) {
            PlayerInGame p = players.get(orderPos.get(i));
            if (p != null)
                orderedPlayers.add(p);
        }

        return "(Game| Game Id: " + gameId + ",\nPlayers: " + orderedPlayers +
                ",\nPreflop: " + preFlop + ",\nFlop: " + flop + ",\nTurn: " + turn + ",\nRiver: " + river + ")";
    }
}