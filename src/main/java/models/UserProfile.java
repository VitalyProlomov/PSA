package models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class UserProfile {
    private final String userName;

    private final HashSet<String> allGamesIds;
    int vpip;
    int threeBetPercentage;
    double fourBetPercentage;
    double fiveBetPercentage;
    int handsAnalizedAmount;

    int bbWinlossAllTime;

    @JsonCreator
    public UserProfile(@JsonProperty("userName") String userName) {
        this.userName = userName;
        allGamesIds = new HashSet<>();
    }

    public String getUserName() {
        return userName;
    }

    public void addGamesIDs(Set<String> ids) {
        this.allGamesIds.addAll(ids);
    }

    public int getGamesAmount() {
        return this.allGamesIds.size();
    }

    public void findGames(PlayerInGame playerRepresenation, Game game, Set<Game> gameSet) throws IllegalArgumentException{
        if (game.getPlayer(playerRepresenation.getId()) == null) {
            throw new IllegalArgumentException("playerInGame must be present in the given game.");
        }
        for (Game g : gameSet) {
            if (g.getPlayer(playerRepresenation.getId()) != null) {
                // 86 400 000 is amount of milliseconds in 1 day.
                if (Math.abs(g.getDate().getTime() - game.getDate().getTime()) < 86400000) {
                    this.allGamesIds.add(g.getGameId());
                }
            }
        }
    }

    public HashSet<String> getAllGamesIds() {
        return new HashSet<>(this.allGamesIds);
    }

    @JsonIgnore
    public void get3BetPercent() {

    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != UserProfile.class) {
            return false;
        }

        return this.userName.equals(((UserProfile) obj).userName);
    }
}