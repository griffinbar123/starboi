package sst;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.io.Console;
import org.junit.Before;
import org.junit.Test;
import Model.Coordinate;
import Model.Enterprise;
import Model.Position;
import Model.Sheild;

public class CommandTest {
    private Game game;
    private Enterprise enterprise;
    private Status status;
    private Commands commands;
    private SrScan srScan;
    private LrScan lrScan;

    @Before
    public void setUp() {
        // Mocking the game
        this.game = mock(Game.class);
        this.game.con = mock(Console.class);
        this.enterprise = mock(Enterprise.class);

        // Mocking the commands
        this.status = new Status(this.game);
        this.commands = new Commands(this.game);
        this.srScan = new SrScan(this.game);
        this.lrScan = new LrScan(this.game);

        // Mocking the Enterprise functionality
        when(game.getEnterprise()).thenReturn(this.enterprise);
        when(enterprise.getStarDate()).thenReturn((float) 123.4);
        when(enterprise.getCondition()).thenReturn("GREEN");
        Position position = new Position(new Coordinate(0, 0), new Coordinate(0, 0));
        when(enterprise.getPosition()).thenReturn(position);
        when(enterprise.getLifeSupport()).thenReturn((byte) 1);
        when(enterprise.getWarp()).thenReturn((float) 1.0);
        when(enterprise.getEnergy()).thenReturn((float) 100.0);
        when(enterprise.getTorpedoes()).thenReturn(10);
        Sheild shields = new Sheild();
        shields.setActive((byte) 1);
        shields.setLevel((float) 100.0);
        shields.setUnits((float) 1.0);
        when(enterprise.getSheilds()).thenReturn(shields);
        when(enterprise.getKlingons()).thenReturn(5);
        when(enterprise.getTime()).thenReturn((float) 10.0);
    }

    @Test
    public void statusCommandShouldWorkAsExpected() {
        this.game.con.flush();
        status.ExecSTATUS();
        String stat = "Stardate      %.1f\n" +
                "Condition     %s\n" +
                "Position      %d - %d, %d - %d\n" +
                "Life Support  %s\n" +
                "Warp Factor   %.1f\n" +
                "Energy        %.2f\n" +
                "Torpedoes     %d\n" +
                "Shields       %s, %.0f%% %.1f units\n" +
                "Klingons Left %d\n" +
                "Time Left     %.2f\n";

        verify(game.con).printf(stat, (float) 123.4, "GREEN", 0, 0, 0, 0, "ACTIVE", (float) 1.0, (float) 100.00, 10,
                "ACTIVE", (float) 1.0, (float) 1.0, 5, (float) 10.00);
    }

    @Test(expected = Test.None.class)
    public void statusCommandShouldNotHaveException() {
        status.ExecSTATUS();
    }

    @Test(expected = Test.None.class)
    public void commandsCommandShouldNotHaveException() {
    commands.ExecCOMMANDS();
    }

    // @Test(expected = Test.None.class)
    // public void srScanCommandShouldNotHaveException() {
    // srScan.ExecSRSCAN();
    // }

    // @Test(expected = Test.None.class)
    // public void lrScanCommandShouldNotHaveException() {
    // lrScan.ExecLRSCAN();
    // }
}
