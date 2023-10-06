package sst;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
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
    private Status status;
    private Enterprise enterprise;
    private Console console;
    
    @Before
    public void setUp() {
        this.game = mock(Game.class);
        this.enterprise = mock(Enterprise.class);
        this.console = mock(Console.class);
        this.status = new Status(game);
        when(game.getEnterprise()).thenReturn(enterprise);
    }
    
    @Test
    public void testExecSTATUS() {
        when(enterprise.getStarDate()).thenReturn((float) 123.4);
        when(enterprise.getCondition()).thenReturn("GREEN");
        Position position = new Position(new Coordinate(0, 0), new Coordinate(0, 0));
        when(enterprise.getPosition()).thenReturn(position);
        when(enterprise.getLifeSupport()).thenReturn((byte)1);
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

        status.ExecSTATUS();
    
        String expectedOutput = "Stardate      123.4\n" +
                "Condition     GREEN\n" +
                "Position      1 - 1, 1 - 1\n" +
                "Life Support  ACTIVE\n" +
                "Warp Factor   1.0\n" +
                "Energy        100.00\n" +
                "Torpedoes     10\n" +
                "Shields       ACTIVE, 100% 1.0 units\n" +
                "Klingons Left 5\n" +
                "Time Left     10.00\n";
        assertEquals(expectedOutput, console.toString());
    }
    
    // @Before
    // public void init() {
    //     this.game = new Game();
    //     this.game.setEnterprise(new Enterprise(new Position(new Coordinate(1, 1), new Coordinate(1, 1))));
    //     this.game.setKlingons(new Klingon[] { new Klingon(new Position(new Coordinate(1, 1), new Coordinate(1, 1))) });
    // }

    // @Test(expected = Test.None.class)
    // public void commandsCommandShouldNotHaveException() {
    //     Commands commands = new Commands();
    //     commands.ExecCOMMANDS();
    // }

    // @Test(expected = Test.None.class)
    // public void statusCommandShouldNotHaveException() {
    //     Status stat = new Status(this.game);
    //     stat.ExecSTATUS();
    // }

    // @Test(expected = Test.None.class)
    // public void srScanCommandShouldNotHaveException() {
    //     SrScan srScan = new SrScan(this.game);
    //     srScan.ExecSRSCAN();
    // }

    // @Test(expected = Test.None.class)
    // public void lrScanCommandShouldNotHaveException() {
    //     LrScan lrScan = new LrScan(this.game);
    //     lrScan.ExecLRSCAN();
    // }
}


