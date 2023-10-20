package sst;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import java.io.Console;
import org.junit.Before;
import org.junit.Test;

import Model.Coordinate;
import Model.Enterprise;
import Model.Game;
import Model.Klingon;
import Model.Position;
import Model.Sheild;

public class CommandHandlerTest {
    private Game game;
    private Enterprise enterprise;
    private CommandHandler handler;
    private SrScan srScan;
    private LrScan lrScan;
    private Commands commands;
    private Status status;
    private Computer computer;
    private Chart chart;
    private Freeze freeze;

    @Before
    public void setUp() {
        game = mock(Game.class);
        game.con = mock(Console.class);
        enterprise = (mock(Enterprise.class));

        srScan = mock(SrScan.class);
        lrScan = mock(LrScan.class);
        commands = mock(Commands.class);
        status = mock(Status.class);
        computer = mock(Computer.class);
        chart = mock(Chart.class);
        freeze = mock(Freeze.class);
        handler = new CommandHandler(game, srScan, lrScan, commands, status, computer, chart, freeze);

        mockObjects();
    }

    @Test
    public void getAndExecuteCommandsShouldMatchCommands() {
        when(game.con.readLine(anyString())).thenReturn("sr 1 2 3").thenReturn("QUIT");

        handler.getAndExecuteCommands();

        verify(srScan, times(1)).ExecSRSCAN();
        verifyNoInteractions(lrScan);
        verifyNoInteractions(commands);
        verifyNoInteractions(status);
        verifyNoInteractions(computer);
        verifyNoInteractions(chart);
        verifyNoInteractions(freeze);
    }

    @Test
    public void getAndExecuteCommandsShouldNotExecuteUndefinedCommand() {
        when(game.con.readLine(anyString())).thenReturn("USER entered... Something stup1d").thenReturn("QUIT");

        handler.getAndExecuteCommands();

        verifyNoInteractions(srScan);
        verifyNoInteractions(lrScan);
        verifyNoInteractions(commands);
        verifyNoInteractions(status);
        verifyNoInteractions(computer);
        verifyNoInteractions(chart);
        verifyNoInteractions(freeze);
    }

    @Test
    public void getAndExecuteCommandsShouldExecuteSrScanWhenPrompted() {
        when(game.con.readLine(anyString())).thenReturn("SRSCAN").thenReturn("QUIT");

        handler.getAndExecuteCommands();

        verify(srScan, times(1)).ExecSRSCAN();
        verifyNoInteractions(lrScan);
        verifyNoInteractions(commands);
        verifyNoInteractions(status);
        verifyNoInteractions(computer);
        verifyNoInteractions(chart);
        verifyNoInteractions(freeze);
    }

    @Test
    public void getAndExecuteCommandsShouldExecuteLrScanWhenPrompted() {
        when(game.con.readLine(anyString())).thenReturn("LRSCAN").thenReturn("QUIT");

        handler.getAndExecuteCommands();

        verifyNoInteractions(srScan);
        verify(lrScan, times(1)).ExecLRSCAN();
        verifyNoInteractions(commands);
        verifyNoInteractions(status);
        verifyNoInteractions(computer);
        verifyNoInteractions(chart);
        verifyNoInteractions(freeze);
    }

    @Test
    public void getAndExecuteCommandsShouldExecuteCommandsWhenPrompted() {
        when(game.con.readLine(anyString())).thenReturn("COMMANDS").thenReturn("QUIT");

        handler.getAndExecuteCommands();

        verifyNoInteractions(srScan);
        verifyNoInteractions(lrScan);
        verify(commands, times(1)).ExecCOMMANDS();
        verifyNoInteractions(status);
        verifyNoInteractions(computer);
        verifyNoInteractions(chart);
        verifyNoInteractions(freeze);
    }

    @Test
    public void getAndExecuteCommandsShouldExecuteStatusWhenPrompted() {
        when(game.con.readLine(anyString())).thenReturn("STATUS").thenReturn("QUIT");

        handler.getAndExecuteCommands();

        verifyNoInteractions(srScan);
        verifyNoInteractions(lrScan);
        verifyNoInteractions(commands);
        verify(status, times(1)).ExecSTATUS();
        verifyNoInteractions(computer);
        verifyNoInteractions(chart);
        verifyNoInteractions(freeze);
    }

    @Test
    public void getAndExecuteCommandsShouldExecuteComputerWhenPrompted() {
        when(game.con.readLine(anyString())).thenReturn("COMPUTER").thenReturn("QUIT");

        handler.getAndExecuteCommands();

        verifyNoInteractions(srScan);
        verifyNoInteractions(lrScan);
        verifyNoInteractions(commands);
        verifyNoInteractions(status);
        verify(computer, times(1)).ExecCOMPUTER(any());
        verifyNoInteractions(chart);
        verifyNoInteractions(freeze);
    }

    @Test
    public void getAndExecuteCommandsShouldExecuteChartWhenPrompted() {
        when(game.con.readLine(anyString())).thenReturn("CHART").thenReturn("QUIT");

        handler.getAndExecuteCommands();

        verifyNoInteractions(srScan);
        verifyNoInteractions(lrScan);
        verifyNoInteractions(commands);
        verifyNoInteractions(status);
        verifyNoInteractions(computer);
        verify(chart, times(1)).ExecCHART();
        verifyNoInteractions(freeze);
    }

    @Test
    public void getAndExecuteCommandsShouldExecuteFreezeWhenPrompted() {
        when(game.con.readLine(anyString())).thenReturn("FREEZE").thenReturn("QUIT");

        handler.getAndExecuteCommands();

        verifyNoInteractions(srScan);
        verifyNoInteractions(lrScan);
        verifyNoInteractions(commands);
        verifyNoInteractions(status);
        verifyNoInteractions(computer);
        verifyNoInteractions(chart);
        verify(freeze, times(1)).ExecFREEZE();
    }

    private void mockObjects() {
        Position position = new Position(new Coordinate(0, 0), new Coordinate(0, 0));
        Klingon klingons[] = new Klingon[] { new Klingon(position) };
        Sheild shields = new Sheild();
        shields.setActive("UP");
        shields.setLevel(100);
        shields.setUnits(1.0);
        when(game.getEnterprise()).thenReturn(enterprise);
        when(game.getMap()).thenReturn(new char[8][8][10][10]);
        when(game.getStarDate()).thenReturn(123.4);
        when(game.getKlingons()).thenReturn(klingons);
        when(game.getTime()).thenReturn(10.0);
        when(game.getCoordinateString(anyInt(), anyInt())).thenReturn("1");
        when(enterprise.getCondition()).thenReturn("GREEN");
        when(enterprise.getPosition()).thenReturn(position);
        when(enterprise.getLifeSupport()).thenReturn((byte) 1);
        when(enterprise.getWarp()).thenReturn(1.0);
        when(enterprise.getEnergy()).thenReturn(100.0);
        when(enterprise.getTorpedoes()).thenReturn(10);
        when(enterprise.getSheilds()).thenReturn(shields);
    }
}
