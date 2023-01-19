package modelsTests;

import analizer.CombinationAnalizer;
import exceptions.IncorrectBoardException;
import exceptions.IncorrectCardException;
import models.Board;
import models.ComboBoardPair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ComboBoardPairTest {
    @Test
    public void testEquals() throws IncorrectBoardException, IncorrectCardException {
        Board b = new Board("2c", "Jd", "Th", "8h", "Ks");
        CombinationAnalizer.Combinations p = CombinationAnalizer.Combinations.PAIR;

        ComboBoardPair cbp = new ComboBoardPair(p, b);
        ComboBoardPair cbpSame = new ComboBoardPair(p, new Board("2c", "Jd", "Th", "8h", "Ks"));
        assertEquals(cbp, cbpSame);

        b = new Board("2c", "Jd", "Th", "8h", "Ad");
        ComboBoardPair cbpDiff = new ComboBoardPair(p, new Board("2c", "Jd", "Th", "8h", "Ad"));
        assertNotEquals(cbp, cbpDiff);

         p = CombinationAnalizer.Combinations.STRAIGHT;
         cbpDiff = new ComboBoardPair(p, b);
         assertNotEquals(cbp, cbpDiff);
    }

    @Test
    public void testComboBoardPairToString() throws IncorrectBoardException, IncorrectCardException {
        ComboBoardPair cbp = new ComboBoardPair(CombinationAnalizer.Combinations.STRAIGHT,
                new Board("5d", "4c", "6c", "7c", "8h"));

        assertEquals(cbp.toString(), "(ComboBoard| Combination: STRAIGHT, Board: ([5♦, 4♣, 6♣, 7♣, 8♥]))");
        System.out.println(cbp.toString());
    }
}
