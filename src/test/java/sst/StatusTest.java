package sst;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class StatusTest {
    public Game game;

    @Before
    public void init() {
        game = Mockito.mock(Game.class);
    }

    @Test (expected = Test.None.class)
    public void statusCommandShouldNotHaveException() {
        Status status = new Status(this.game);
        status.ExecSTATUS();
    }
}
