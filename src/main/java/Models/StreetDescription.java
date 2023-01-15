package Models;

import java.util.ArrayList;

/**
 * Class used for storing information about one street - all the action,
 * cards that came (post-flop).
 */
public class StreetDescription {
    int pot;
    int playersLeft;
    Board board;
    ArrayList<Action> allActions;
}
