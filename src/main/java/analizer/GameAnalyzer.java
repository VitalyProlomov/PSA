package analizer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import models.Action;
import models.Game;
import models.PlayerInGame;
import models.StreetDescription;

import java.util.HashSet;

import static models.Action.ActionType.*;

public class GameAnalyzer {
    /**
     * @return if Hero did NOT fold on preflop,
     * false otherwise
     */
    @JsonIgnore
    public static boolean isHeroPostFlop(Game game) {
        return game.getPreFlop().getPlayersAfterBetting().contains(new PlayerInGame("Hero"));
    }

    public static boolean isFlopCheckRaisedByCaller(Game game) {
        String pfrHash = getPFRHash(game);
        if (pfrHash == null) {
            return false;
        }
        if (game.getFlop() == null) {
            return false;
        }

        HashSet<String> checkedPlayers = new HashSet<String>();
        for (Action action : game.getFlop().getAllActions()) {
            if (!action.getPlayerId().equals(pfrHash) && action.getActionType().equals(CHECK)) {
                checkedPlayers.add(action.getPlayerId());
            }
            if (!action.getPlayerId().equals(pfrHash) && action.getActionType().equals(RAISE) &&
                    checkedPlayers.contains(action.getPlayerId())) {
                return true;
            }
        }
        return false;
    }


    /**
     * counts amount of preflop raises (EXcludes blind raises).
     */
    private static int countPreFlopRaises(Game g) {
        StreetDescription pf = g.getPreFlop();
        if (pf == null) {
            throw new RuntimeException("Preflop was null");
        }
        int preFlopRaisesAmount = 0;
        for (int i = 0; i < pf.getAllActions().size(); ++i) {
            if (pf.getAllActions().get(i).getActionType().equals(RAISE)) {
                ++preFlopRaisesAmount;
            }
        }
        return preFlopRaisesAmount;
    }

    @JsonIgnore
    public static boolean isUnRaised(Game game) {
        return countPreFlopRaises(game) == 0;
    }

    /**
     * @return true, if the pot is single raised (only one raise took pace during preflop),
     * false otherwise
     */
    @JsonIgnore
    public static boolean isSingleRaised(Game game) {
        return countPreFlopRaises(game) == 1;
    }

    /**
     * @return true, if the pot is 3 bet (2 raises took pace during preflop),
     * false otherwise
     */
    @JsonIgnore
    public static boolean isPot3Bet(Game game) {
        return countPreFlopRaises(game) == 2;
    }

    /**
     * @return true, if the pot is 4 bet (3 raises took pace during preflop),
     * false otherwise
     */
    @JsonIgnore
    public static boolean isPot4Bet(Game game) {
        return countPreFlopRaises(game) == 3;
    }

    /**
     * @return true, if the pot is 5+ bet (4 or more raises took pace during preflop),
     * false otherwise
     */
    @JsonIgnore
    public static boolean isPot5PlusBet(Game game) {
        return countPreFlopRaises(game) >= 4;
    }

    /**
     * @return true if more than 2 players were active (didnt fold) after preflop,
     * false otherwise.
     */
    @JsonIgnore
    public static boolean isPotMultiWay(Game game) {
        return game.getPreFlop().getPlayersAfterBetting().size() > 2;
    }

    /**
     * @return true if everybody but winner of the game folded on preflop,
     * false otherwise
     */
    @JsonIgnore
    public static boolean isGameFoldedPreFlop(Game game) {
        return game.getPreFlop().getPlayersAfterBetting().size() == 1;
    }

    /**
     * @return true if Hero is in the game
     */
    @JsonIgnore
    public static boolean isHeroInGame(Game game) {
        return game.getPlayer("Hero") != null;
    }

    @JsonIgnore
    public static boolean isPlayerPFR(Game game, String hash) {
        if (game.getPlayers().get(hash) == null) {
            return false;
        }
        for (int i = game.getPreFlop().getAllActions().size() - 1; i >= 0; --i) {
            if (game.getPreFlop().getAllActions().get(i).getActionType().equals(RAISE)) {
                return game.getPreFlop().getAllActions().get(i).getPlayerId().equals(hash);
            }
        }
        return false;
    }

    @JsonIgnore
    public static boolean is3BetRaiser(Game game, String hash) {
        if (game.getPlayers().get(hash) == null) {
            return false;
        }
        boolean was1RaiseFound = false;
        for (int i = 0; i < game.getPreFlop().getAllActions().size(); ++i) {
            if (game.getPreFlop().getAllActions().get(i).getActionType().equals(RAISE)) {
                if (was1RaiseFound) {
                    return game.getPreFlop().getAllActions().get(i).getPlayerId().equals(hash);
                }
                was1RaiseFound = true;
            }
        }
        return false;
    }

    @JsonIgnore
    public static boolean is4BetRaiser(Game game, String hash) {
        if (game.getPlayers().get(hash) == null) {
            return false;
        }
        int raisesAmount = 0;
        for (int i = 0; i < game.getPreFlop().getAllActions().size(); ++i) {
            if (game.getPreFlop().getAllActions().get(i).getActionType().equals(RAISE)) {
                if (raisesAmount == 2) {
                    return game.getPreFlop().getAllActions().get(i).getPlayerId().equals(hash);
                }
                ++raisesAmount;
            }
        }
        return false;
    }

    @JsonIgnore
    public static boolean is5BetRaiser(Game game, String hash) {
        if (game.getPlayers().get(hash) == null) {
            return false;
        }
        int raisesAmount = 0;
        for (int i = 0; i < game.getPreFlop().getAllActions().size(); ++i) {
            if (game.getPreFlop().getAllActions().get(i).getActionType().equals(RAISE)) {
                if (raisesAmount == 3) {
                    return game.getPreFlop().getAllActions().get(i).getPlayerId().equals(hash);
                }
                ++raisesAmount;
            }
        }
        return false;
    }

    @JsonIgnore
    public static String getPFRHash(Game game) {
        if (isUnRaised(game)) {
            return null;
        }

        return game.getPreFlop().getLastAggressorHash();
    }

    @JsonIgnore
    public static boolean didCBetFLop(Game game, String hash) {
        if (game.getFlop() == null) {
            return false;
        }
        if (!isPlayerPFR(game, hash)) {
            return false;
        }
        int i = 0;
        while (i < game.getFlop().getAllActions().size() && game.getFlop().getAllActions().get(i).getActionType().equals(BET)) {
            ++i;
        }
        if (i >= game.getFlop().getAllActions().size()) {
            return false;
        }
        return game.getFlop().getAllActions().get(i).getPlayerId().equals(hash);
    }


    @JsonIgnore
    public static boolean didCheckRaiseFlop(Game game, String hash) {
        if (game.getFlop() == null) {
            return false;
        }
        boolean wasChecked = false;
        for (int i = 0; i < game.getFlop().getAllActions().size(); ++i) {
            if (game.getFlop().getAllActions().get(i).getPlayerId().equals(hash)) {
                if (wasChecked) {
                    return game.getFlop().getAllActions().get(i).getActionType().equals(RAISE);
                }
                wasChecked = true;
            }
        }
        return false;
    }

    @JsonIgnore
    public static boolean didCallCBetFlop(Game game, String hash) {
        if (game.getFlop() == null) {
            return false;
        }
        if (isPlayerPFR(game, hash)) {
            return false;
        }
        for (int i = 0; i < game.getFlop().getAllActions().size(); ++i) {
            if (game.getFlop().getAllActions().get(i).getPlayerId().equals(hash) &&
                    game.getFlop().getAllActions().get(i).getActionType().equals(CALL)) {
                return true;
            }
        }
        return false;
    }

    @JsonIgnore
    public static boolean didRaiseFlop(Game game, String hash) {
        if (game.getFlop() == null) {
            return false;
        }
        for (int i = 0; i < game.getFlop().getAllActions().size(); ++i) {
            if (game.getFlop().getAllActions().get(i).getPlayerId().equals(hash) &&
                    game.getFlop().getAllActions().get(i).getActionType().equals(RAISE)) {
                return true;
            }
        }
        return false;
    }

    @JsonIgnore
    public static boolean didCBetTurn(Game game, String hash) {
        if (game.getTurn() == null) {
            return false;
        }
        // ???? Need to see definition of turn c-bet.
        if (!didCBetFLop(game,hash)) {
            return false;
        }
        for (int i = 0; i < game.getTurn().getAllActions().size(); ++i) {
            if (game.getTurn().getAllActions().get(i).getActionType().equals(BET) &&
                    game.getTurn().getAllActions().get(i).getPlayerId().equals(hash)) {
                return true;
            }
        }
        return false;
    }

    @JsonIgnore
    public static boolean didCheckRaiseTurn(Game game, String hash) {
        if (game.getTurn() == null) {
            return false;
        }

        boolean wasChecked = false;
        for (int i = 0; i < game.getTurn().getAllActions().size(); ++i) {
            if (game.getTurn().getAllActions().get(i).getPlayerId().equals(hash)) {
                if (wasChecked) {
                    return game.getTurn().getAllActions().get(i).getActionType().equals(RAISE);
                }
                wasChecked = true;
            }
        }
        return false;
    }

    @JsonIgnore
    public static boolean didCallCBetTurn(Game game, String hash) {
        if (game.getTurn() == null) {
            return false;
        }
        if (isPlayerPFR(game, hash)) {
            return false;
        }
        for (int i = 0; i < game.getTurn().getAllActions().size(); ++i) {
            if (game.getTurn().getAllActions().get(i).getPlayerId().equals(hash) &&
                    game.getTurn().getAllActions().get(i).getActionType().equals(CALL)) {
                return true;
            }
        }
        return false;
    }

    @JsonIgnore
    public static boolean didRaiseTurn(Game game, String hash) {
        if (game.getTurn() == null) {
            return false;
        }
        for (int i = 0; i < game.getTurn().getAllActions().size(); ++i) {
            if (game.getTurn().getAllActions().get(i).getPlayerId().equals(hash) &&
                    game.getTurn().getAllActions().get(i).getActionType().equals(RAISE)) {
                return true;
            }
        }
        return false;
    }

    @JsonIgnore
    public static boolean didCBetRiver(Game game, String hash) {
        if (game.getRiver() == null) {
            return false;
        }
        if (!didCBetTurn(game, hash)) {
            return false;
        }
        for (int i = 0; i < game.getRiver().getAllActions().size(); ++i) {
            if (game.getRiver().getAllActions().get(i).getActionType().equals(BET) &&
                    game.getRiver().getAllActions().get(i).getPlayerId().equals(hash)) {
                return true;
            }
        }
        return false;
    }

    @JsonIgnore
    public static boolean didLeadRiver(Game game, String hash) {
        if (game.getRiver() == null) {
            return false;
        }
        if (didCBetTurn(game, hash)) {
            return false;
        }
        for (int i = 0; i < game.getRiver().getAllActions().size(); ++i) {
            if (game.getRiver().getAllActions().get(i).getActionType().equals(BET) &&
                    game.getRiver().getAllActions().get(i).getPlayerId().equals(hash)) {
                return true;
            }
        }
        return false;
    }

    @JsonIgnore
    public static boolean didWinAtShowdownRiver(Game game, String hash) {
        if (game.getRiver() == null) {
            return false;
        }
        if (game.getRiver().getPlayersAfterBetting().contains(new PlayerInGame(hash))) {
            return (game.getWinners().containsKey(hash));
        }
        return false;
    }

    @JsonIgnore
    public static boolean didCallRiver(Game game, String hash) {
        if (game.getRiver() == null) {
            return false;
        }
        for (int i = 0; i < game.getRiver().getAllActions().size(); ++i) {
            if (game.getRiver().getAllActions().get(i).getPlayerId().equals(hash)) {
                return game.getRiver().getAllActions().get(i).getActionType().equals(CALL);
            }
        }
        return false;
    }
}
