package Models;

public class Action {
    public enum Type {
        CHECK,
        BET,
        FOLD,
        CALL
    }
    private PositionType position;
    private Type action;
    private int amount;
}
