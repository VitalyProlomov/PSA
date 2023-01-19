package Models;

import java.text.DecimalFormat;
import java.util.Objects;

/**
 * Class that describes what opponent has done.
 * Holds info about position, type of action (check, fold, bet, call, raise) and (where applicable) amount of bet.
 */
public class Action {
    public enum ActionType {
        CHECK,
        BET,
        FOLD,
        CALL,
        RAISE
    }

    private ActionType actionType;
    private double amount;
    private PlayerInGame playerInGame;
    private double potBeforeAction;

//    public Action(ActionType actionType, PlayerInGame playerInGame) {
//        this(actionType, playerInGame, 0);
//    }
//
//    public Action(ActionType actionType, PlayerInGame playerInGame, double amount) {
//
//    }

    public Action(ActionType actionType, PlayerInGame playerInGame, double amount, double potBeforeAction) {
        this.actionType = actionType;
        this.playerInGame = playerInGame;
        // if (actionType == ActionType.BET || actionType == ActionType.RAISE || actionType == ActionType.CALL) {
        this.amount = amount;

        this.potBeforeAction = potBeforeAction;
    }


    public ActionType getActionType() {
        return actionType;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public PlayerInGame getPlayerInGame() {
        return playerInGame;
    }

    public void setPlayerInGame(PlayerInGame playerInGame) {
        this.playerInGame = playerInGame;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() == Action.class) {
            Action ac = (Action) obj;
            return this.actionType == ac.actionType &&
                    Math.abs(this.potBeforeAction - ac.potBeforeAction) < 0.01 &&
                    Math.abs(this.amount - ac.amount) < 0.01 &&
                    this.playerInGame.equals(ac.playerInGame);
        }
        return false;
    }


    @Override
    public int hashCode() {
        return Objects.hash(actionType, amount, playerInGame, potBeforeAction);
    }

    @Override
    public String toString() {
        String repr = "(Action| Type: " + actionType;
        if (actionType == ActionType.RAISE ||
                actionType == ActionType.BET ||
                actionType == ActionType.CALL) {
            repr += ", Amount: " + amount;
        }
        DecimalFormat dcf = new DecimalFormat("###.##");
        String rep = dcf.format(potBeforeAction);
        repr += ", Pot before action: " + rep + ", " + playerInGame;
        return repr;
    }
}
