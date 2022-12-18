package Models;

import java.util.ArrayList;
import java.util.Date;

public class Game {
    private String handId;

    // List of all players in the hand, player on index 0 is sitting on dealer position,
    // then following all players ordered by going clockwise (to the left - SB, BB, LJ, HJ, CO).
    // orgy
    private ArrayList<PlayerInGame> players;
    private boolean isExtraCash;
    private Date date;

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
}