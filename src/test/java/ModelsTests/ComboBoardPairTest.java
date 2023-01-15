package ModelsTests;

import Analizer.CombinationAnalizer;
import Models.Board;
import Models.ComboBoardPair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ComboBoardPairTest {
    @Test
    public void testEquals() {
        Board b = new Board("2c", "Jd", "Th", "8h", "Ks");
        CombinationAnalizer.Combinations p = CombinationAnalizer.Combinations.PAIR;

        ComboBoardPair cbp = new ComboBoardPair(p, b);
        ComboBoardPair cbpSame = new ComboBoardPair(p, b);
        assertEquals(cbp, cbpSame);

        b = new Board("2c", "Jd", "Th", "8h", "Ad");
        ComboBoardPair cbpDiff = new ComboBoardPair(p, b);
        assertNotEquals(cbp, cbpDiff);

         p = CombinationAnalizer.Combinations.STRAIGHT;
         cbpDiff = new ComboBoardPair(p, b);
         assertNotEquals(cbp, cbpDiff);
    }
}
