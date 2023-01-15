package Models;

import java.util.ArrayList;

public class PlayerInGame {
    private final String hash;
    private PositionType positionType;
    private UserProfile ref;
    private double balance$;
    private ArrayList<Card> hand;

    public PlayerInGame(String hash) {
        this.hash = hash;
    }

    public PlayerInGame(String hash, PositionType position, double balance$) {
        this(hash, position, balance$, null);
    }

    public PlayerInGame(String hash, PositionType position, double balance, UserProfile ref) {
        this.hash = hash;
        this.positionType = position;
        this.balance$ = balance;
        this.ref = ref;
    }

    // Not sure if i need that, but will leave for now
    // Temporary stats - makes sense to understand how they are percieved
    private int vpip;
    private int threeBetPercentage;
    private int handsPlayed;

    public int getVpip() {
        return vpip;
    }

    public void setVpip(int vpip) {
        this.vpip = vpip;
    }

    public int getThreeBetPercentage() {
        return threeBetPercentage;
    }

    public void setThreeBetPercentage(int threeBetPercentage) {
        this.threeBetPercentage = threeBetPercentage;
    }

    public int getHandsPlayed() {
        return handsPlayed;
    }

    public void setHandsPlayed(int handsPlayed) {
        this.handsPlayed = handsPlayed;
    }

    public String getHash() {
        return hash;
    }

    public PositionType getPositionType() {
        return positionType;
    }

    public void setPositionType(PositionType positionType) {
        this.positionType = positionType;
    }

    public UserProfile getRef() {
        return ref;
    }

    public void setRef(UserProfile ref) {
        this.ref = ref;
    }

    public double getBalance() {
        return balance$;
    }

    public void setBalance(double balance) {
        this.balance$ = balance;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() != PlayerInGame.class) {
            return false;
        }
        return ((PlayerInGame) obj).positionType == this.positionType &&
                ((PlayerInGame) obj).hash.equals(this.hash) &&
                ((PlayerInGame) obj).balance$ == this.balance$ &&
                ((PlayerInGame) obj).ref == this.ref;
    }

    @Override
    public String toString() {
        String rep = "PlayerInGame: {UserName: ";
        if (ref != null) {
            rep +=  ref.userName;
        } else {
            rep += "_UNDEFINED_";
        }
        rep += ", Hash" + hash + ", Pos: " + positionType + ", Balance: " + balance$ + "}";
        return rep;
    }

    public ArrayList<Card> getHand() {
        return new ArrayList<>(hand);
    }

    public void setHand(ArrayList<Card> hand) {
        if (hand.size() != 2 || hand.get(0).equals(hand.get(1))) {
            throw new IllegalArgumentException("Hand must consist of 2 different cards.");
        }
        this.hand = new ArrayList<>(hand);
    }
}
