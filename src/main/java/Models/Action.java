package Models;

/**
 * Class that describes what opponent has done.
 * Holds info about position, type of action (check, fold, bet, call) and (where applicable) amount of bet.
 */
public class Action {
    public enum ActionType {
        CHECK,
        BET,
        FOLD,
        CALL
    }
    private PositionType position;
    private ActionType action;
    private int amount;
}
