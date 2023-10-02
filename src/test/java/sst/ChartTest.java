package sst;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class ChartTest {
    public Game game;

    @Before
    public void init() {
        game = Mockito.mock(Game.class);
    }

    @Test (expected = Test.None.class)
    public void chartCommandShouldNotHaveException() {
        Chart chart = new Chart(this.game);
        chart.ExecCHART();
    }
}
