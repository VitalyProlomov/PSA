package models;

import exceptions.IncorrectHandException;

import java.text.DecimalFormat;
import java.util.Objects;

/**
 * Class that represents the player in a specific game.
 */
public class PlayerInGame {
    private final String id;
    private PositionType positionType;
    private UserProfile ref;
    private double balance$;
    private Hand hand;

//    private PlayerStatus status;


    /**
     * Constructs new PlayerInGame with given hash
     */
    public PlayerInGame(String id) {
        this.id = id;
    }

    /**
     * Constructs new PlayerInGame with given hash, position in the table
     * and balance in dollars.
     */
    public PlayerInGame(String id, PositionType position, double balance$) {
        this(id, position, balance$, null);
    }

    /**
     * Constructs new PlayerInGame with given hash, position in the table,
     * balance in dollars and a Userprofile object (link to the real user).
     */
    public PlayerInGame(String id, PositionType position, double balance, UserProfile ref) {
        this.id = id;
        this.positionType = position;
        this.balance$ = balance;
        // Do not construct new UserProfile
        this.ref = ref;
    }

    /**
     * Constructs new PlayerInGame, copying all the fields of the given PlayerInGame
     * @param copyPlayer Player, whose fields are going to be copied.
     */
    public PlayerInGame(PlayerInGame copyPlayer) {
        this.id = copyPlayer.id;
        this.positionType = copyPlayer.positionType;
        this.balance$ = copyPlayer.balance$;
        this.ref = copyPlayer.ref;
        if (copyPlayer.hand != null) {
            this.hand = new Hand(copyPlayer.hand);
        }

//        this.status = copyPlayer.status;

//        this.vpip = copyPlayer.vpip;
//        this.threeBetPercentage = copyPlayer.threeBetPercentage;
//        this.handsPlayed = copyPlayer.handsPlayed;
    }

//    private int vpip;
//    private int threeBetPercentage;
//    private int handsPlayed;

    /**
     * @return id of the player - string identification of the player in current session
     */
    public String getId() {
        return id;
    }

    /**
     * @return position of the player in table.
     */
    public PositionType getPosition() {
        return positionType;
    }

    /**
     * Sets position of the player in table.
     * @param positionType position to set (no validation, so make sure ypu put correct position)
     */
    public void setPositionType(PositionType positionType) {
        this.positionType = positionType;
    }

    /**
     * @return UserProfile that is linked to the current player in game
     */
    public UserProfile getRef() {
        return ref;
    }

    /**
     * Sets UserProfile to the current player
     * @param ref UserProfile link. Setting the actual object, NOT COPY.
     *            SO ALL THE CHANGES OUTSIDE THE CLASS WILL BE SEEN
     */
    public void setRef(UserProfile ref) {
        this.ref = ref;
    }

    /**
     * @return balance of this player in dollars
     */
    public double getBalance() {
        return balance$;
    }

    /**
     * Sets balance of that player
     * @param balance balance of that player in dollars
     */
    public void setBalance(double balance) {
        this.balance$ = balance;
    }

    /**
     * PlayerInGame can be equal to another object only if that is another PlayerInGame.
     * IMPORTANT: only use it to compare Players in one exact game, NOT players from different games
     * @param obj
     * @return true i the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || obj.getClass() != PlayerInGame.class) {
            return false;
        }
        // Maybe I should change this to not just comparing the hashes
        return ((PlayerInGame) obj).id.equals(this.id);

    }

    /**
     * @return hashcode of this object only using the id field
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     *
     * @return string representation of player in game like following:
     * (PlayerInGame| UserName: x, Id: h, Pos: p, Balance: b)
     * where x is the name of userName attached to PlayerInGame (if userProfile is not defined, x = _UNDEFINED_),
     * h is id, p is position, b is balance.
     */
    @Override
    public String toString() {
        String rep = "(PlayerInGame| UserName: ";
        if (ref != null) {
            rep +=  ref.getUserName();
        } else {
            rep += "_UNDEFINED_";
        }
        String balanceStr = new DecimalFormat("#0.00").format(balance$);
        balanceStr = balanceStr.replace(',', '.');
        rep += ", Id: " + id + ", Pos: " + positionType + ", Balance: " + balanceStr + ")";
        return rep;
    }

    public Hand getHand() throws IncorrectHandException {
        return new Hand(hand);
    }


    public void setHand(Hand hand){
        this.hand = new Hand(hand);
    }
}
