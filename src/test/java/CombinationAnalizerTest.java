import Analizer.CombinationAnalizer;
import Models.Card;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CombinationAnalizerTest {
    private ArrayList<Card> createBoard(Card c1, Card c2, Card c3, Card c4, Card c5) {
        ArrayList<Card> board = new ArrayList<>();
        board.add(c1);
        board.add(c2);
        board.add(c3);
        board.add(c4);
        board.add(c5);

        return board;
    }

    @Test
    public void testSortBoardW2SameCards() {
        Card H4 = new Card(Card.Rank.FOUR, Card.Suit.HEARTS);
        Card HA =new Card(Card.Rank.ACE, Card.Suit.HEARTS);
        Card SK = new Card(Card.Rank.KING, Card.Suit.SPADES);
        Card C4 = new Card(Card.Rank.FOUR, Card.Suit.CLUBS);
        Card DJ = new Card(Card.Rank.JACK, Card.Suit.DIAMONDS);

        ArrayList<Card> unsortedBoard = createBoard(H4, HA, SK, C4, DJ);
        ArrayList<Card> sortedBoard = CombinationAnalizer.sortBoard(unsortedBoard);
        ArrayList<Card> expectedBoard = createBoard(H4, C4, DJ, SK, HA);

        for (int i = 0; i < CombinationAnalizer.BOARD_SIZE; ++i) {
            assertEquals(expectedBoard.get(i), sortedBoard.get(i));
        }
    }

    @Test
    public void testSortBoardOnSortedBoard() {
        Card h2 = new Card("2h");

    }

}
