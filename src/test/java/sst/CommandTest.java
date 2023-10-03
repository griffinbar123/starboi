package sst;

import org.junit.Before;
import org.junit.Test;
import Model.Coordinate;
import Model.Enterprise;
import Model.Klingon;
import Model.Position;

import static org.mockito.Matchers.anyString;

public class CommandTest {
    public Game game;

    @Before
    public void init() {
        this.game = new Game();
        this.game.setEnterprise(new Enterprise(new Position(new Coordinate(1, 1), new Coordinate(1, 1))));
        this.game.setKlingons(new Klingon[] { new Klingon(new Position(new Coordinate(1, 1), new Coordinate(1, 1))) });
    }

    @Test(expected = Test.None.class)
    public void commandsCommandShouldNotHaveException() {
        Commands commands = new Commands();
        commands.ExecCOMMANDS();
    }

    @Test(expected = Test.None.class)
    public void statusCommandShouldNotHaveException() {
        Status stat = new Status(this.game);
        stat.ExecSTATUS();
    }

    @Test(expected = Test.None.class)
    public void srScanCommandShouldNotHaveException() {
        SrScan srScan = new SrScan(this.game);
        srScan.ExecSRSCAN();
    }

    @Test(expected = Test.None.class)
    public void lrScanCommandShouldNotHaveException() {
        LrScan lrScan = new LrScan(this.game);
        lrScan.ExecLRSCAN();
    }
}
