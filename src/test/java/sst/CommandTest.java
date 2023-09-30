package sst;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class CommandTest {
    public Game game;

    @Before
    public void init() {
        game = Mockito.mock(Game.class);
    }

    @Test (expected = Test.None.class)
    public void commandsCommandShouldNotHaveException() {
        Commands commands = new Commands();
        commands.ExecCOMMANDS();
    }
}
