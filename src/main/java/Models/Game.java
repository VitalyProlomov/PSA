package Models;

import java.util.ArrayList;
import java.util.Date;

/**
 * Class defining the entity of a game (1 hand played among players).
 * Contains information about cards, pot size, hands dealt to players
 * (if revealed) and other.
 */
public class Game {
    private final String handId;

    public Game(String handId, double bigBlindSize$, Date date) {
        this.handId = handId;
        this.bigBlindSize$ = bigBlindSize$;
        this.date = date;
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

    public String getHandId() {
        return handId;
    }

    public double getBigBlindSize$() {
        return bigBlindSize$;
    }

    public Date getDate() {
        return date;
    }

    public ArrayList<PlayerInGame> getPlayers() {
        ArrayList<PlayerInGame> copyPlayers = new ArrayList<>();
        for (PlayerInGame p : players) {
            copyPlayers.add(p);
        }
        return copyPlayers;
    }

    public void setPlayerHand(PositionType pos, ArrayList<Card> hand) {
        for (int i = 0; i < players.size(); ++i) {
            if (players.get(i).getPositionType() == pos) {
                // players
            }
        }
    }

    public void setPlayers(ArrayList<PlayerInGame> players) {
        this.players = players;
    }
}