package models;

import exceptions.IncorrectHandException;

import java.util.ArrayList;
import java.util.Objects;

public class PlayerInGame {
    private final String hash;
    private PositionType positionType;
    private UserProfile ref;
    private double balance$;
    private Hand hand;

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

    public PlayerInGame(PlayerInGame copyPlayer) {
        this.hash = copyPlayer.hash;
        this.positionType = copyPlayer.positionType;
        this.balance$ = copyPlayer.balance$;
        this.ref = copyPlayer.ref;
        this.hand = copyPlayer.hand;
        this.vpip = copyPlayer.vpip;
        this.threeBetPercentage = copyPlayer.threeBetPercentage;
        this.handsPlayed = copyPlayer.handsPlayed;
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
        if (obj == null || obj.getClass() != PlayerInGame.class) {
            return false;
        }
        // Maybe I should change this to not just comparing the hashes
        return ((PlayerInGame) obj).hash.equals(this.hash);

    }

    @Override
    public int hashCode() {
        return Objects.hash(hash);
    }

    @Override
    public String toString() {
        String rep = "(PlayerInGame| {UserName: ";
        if (ref != null) {
            rep +=  ref.userName;
        } else {
            rep += "_UNDEFINED_";
        }
        rep += ", Hash: " + hash + ", Pos: " + positionType + ", Balance: " + balance$ + "})";
        return rep;
    }

    public Hand getHand() throws IncorrectHandException {
        return new Hand(hand.getFirstCard(), hand.getSecondCard());
    }

    public void setHand(ArrayList<Card> hand) throws IncorrectHandException {
        if (hand.size() != 2 || hand.get(0).equals(hand.get(1))) {
            throw new IncorrectHandException();
        }

        this.hand = new Hand(hand.get(0), hand.get(1));
    }

    public void setHand(Hand hand) throws IncorrectHandException {
        this.hand = new Hand(hand.getFirstCard(), hand.getSecondCard());
    }
}
