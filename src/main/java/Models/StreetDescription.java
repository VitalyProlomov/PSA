package Models;

import Exceptions.IncorrectBoardException;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Class used for storing information about one street - all the action,
 * cards that came.
 */
public class StreetDescription {
    private double potAfterBetting;
    // Will be null for Pre-flop
    private Board board;
    private ArrayList<PlayerInGame> playersAfterBetting;
    private ArrayList<Action> allActions;

    public StreetDescription(double potAfterBetting, Board board, ArrayList<PlayerInGame> playersAfterBetting, ArrayList<Action> allActions)
            throws IncorrectBoardException {
        this.potAfterBetting = potAfterBetting;
        if (board != null) {
            this.board = new Board(board);
        } else {
            this.board = null;
        }

        this.playersAfterBetting = new ArrayList<>(playersAfterBetting);
        this.allActions = new ArrayList<>(allActions);
    }

    public StreetDescription(StreetDescription strCopy) {
        this.potAfterBetting = strCopy.potAfterBetting;
        this.playersAfterBetting = new ArrayList<>(strCopy.playersAfterBetting);
        this.allActions = new ArrayList<>(strCopy.allActions);

        if (strCopy.board != null) {
            this.board = new Board(strCopy.board);
        } else {
            this.board = null;
        }
    }

    public ArrayList<Action> getAllActions() {
        return new ArrayList<>(allActions);
    }

    public void setAllActions(ArrayList<Action> allActions) {
        this.allActions = new ArrayList<>(allActions);
    }

    public ArrayList<PlayerInGame> getPlayersAfterBetting() {
        return new ArrayList<>(playersAfterBetting);
    }

    public void setPlayersAfterBetting(ArrayList<PlayerInGame> playersAfterBetting) {
        this.playersAfterBetting = new ArrayList<>(playersAfterBetting);
    }

    public double getPotAfterBetting() {
        return potAfterBetting;
    }

    public Board getBoard() {
        if (board == null) {
            return null;
        }
        return new Board(board);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() == StreetDescription.class) {
            StreetDescription st = (StreetDescription)obj;
            boolean eq = Math.abs(this.potAfterBetting - st.potAfterBetting) < 0.01 &&
                    this.allActions.equals(st.allActions) &&
                    this.playersAfterBetting.equals((st.playersAfterBetting));
            if (this.board == null) {
                return eq && st.board == null;
            } else {
                return eq && this.board.equals(st.board);
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(potAfterBetting, board, playersAfterBetting, allActions);
    }

    @Override
    public String toString() {
        DecimalFormat dcf = new DecimalFormat("###.##");
        String dRep = dcf.format(potAfterBetting);
        return "(StreetDescription| Board: " + board + ", pot after betting: " + dRep + ", Players left: " + playersAfterBetting +
                 "\n, Actions: " + allActions;
    }
}