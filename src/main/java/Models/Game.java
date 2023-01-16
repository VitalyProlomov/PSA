package Models;

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
    // orgy
    private ArrayList<PlayerInGame> players;
    private boolean isExtraCash;
    private Date date;
    private final double bigBlindSize$;

    StreetDescription preFlop;
    StreetDescription flop;
    StreetDescription turn;
    StreetDescription river;

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

    public void setPlayerHand(PositionType pos, ArrayList<Card> hand) {
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

    public void setHeroHand(ArrayList<Card> hand) {
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
}