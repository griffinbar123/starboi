package sst;


import static org.junit.Assert.assertTrue;
import org.junit.Test;
import Utils.Utils;

public class UtilsTest {
    @Test
    public void randIntShouldWorkAsExpected() {
        int randomVal = Utils.randInt(0, 10);
        assertTrue(randomVal >= 0);
        assertTrue(randomVal <= 10);

        randomVal = Utils.randInt(0, 100);
        assertTrue(randomVal >= 0);
        assertTrue(randomVal <= 100);
    }

    @Test
    public void randDoubleShouldWorkAsExcpeted() {
        double randomVal = Utils.randDouble(0, 10);
        assertTrue(randomVal > 0);
        assertTrue(randomVal < 10);

        randomVal = Utils.randInt(0, 100);
        assertTrue(randomVal > 0);
        assertTrue(randomVal < 100);
    }
}
