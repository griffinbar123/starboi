package sst;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class SrScanTest {
    public Game game;

    @Before
    public void init() {
        game = Mockito.mock(Game.class);
    }

    @Test (expected = Test.None.class)
    public void srScanCommandShouldNotHaveException() {
        SrScan srScan = new SrScan(this.game);
        srScan.ExecSRSCAN();
    }
}
