package models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashSet;

public class UserProfile {
    private final String userName;

    private HashSet<String> allGamesIds;
    int vpip;
    int threeBetPercentage;
    double fourBetPercentage;
    double fiveBetPercentage;
    int handsAnalizedAmount;

    int bbWinlossAllTime;

    @JsonCreator
    public UserProfile(@JsonProperty("userName") String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

//    public addGames(Date startDate, Date finishDate, String userIdInSession) {
//        pathWithGames
//
//    }

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