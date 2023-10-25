package sst;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import java.io.Console;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import Model.Game;

public class CommandHandlerTest {
    private Game game;
    private CommandHandler handler;
    private SrScan srScan;
    private LrScan lrScan;
    private Status status;
    private Computer computer;
    private Chart chart;
    private Freeze freeze;
    private Help help;
    private Damages damages;
    private Move move;

    @Before
    public void setUp() {
        game = mock(Game.class);
        game.con = mock(Console.class);
        srScan = mock(SrScan.class);
        lrScan = mock(LrScan.class);
        status = mock(Status.class);
        computer = mock(Computer.class);
        chart = mock(Chart.class);
        freeze = mock(Freeze.class);
        help = mock(Help.class);
        damages = mock(Damages.class);
        move = mock(Move.class);

        handler = new CommandHandler(game, srScan, lrScan, status, computer,
                chart, freeze, help, damages, move);
    }

    @Test
    public void matchCommandShouldWorkAsExpected() {
        assert(handler.matchCommand("SRSCAN") == CommandHandler.Command.SRSCAN);
        assert(handler.matchCommand("MOVE") == CommandHandler.Command.MOVE);
        assert(handler.matchCommand("PHASERS") == CommandHandler.Command.PHASERS);
        assert(handler.matchCommand("CALL") == CommandHandler.Command.CALL);
        assert(handler.matchCommand("STATUS") == CommandHandler.Command.STATUS);
        assert(handler.matchCommand("IMPULSE") == CommandHandler.Command.IMPULSE);
        assert(handler.matchCommand("PHOTONS") == CommandHandler.Command.PHOTONS);
        assert(handler.matchCommand("ABANDON") == CommandHandler.Command.ABANDON);
        assert(handler.matchCommand("LRSCAN") == CommandHandler.Command.LRSCAN);
        assert(handler.matchCommand("WARP") == CommandHandler.Command.WARP);
        assert(handler.matchCommand("SHIELDS") == CommandHandler.Command.SHIELDS);
        assert(handler.matchCommand("DESTRUCT") == CommandHandler.Command.DESTRUCT);
        assert(handler.matchCommand("CHART") == CommandHandler.Command.CHART);
        assert(handler.matchCommand("REST") == CommandHandler.Command.REST);
        assert(handler.matchCommand("DOCK") == CommandHandler.Command.DOCK);
        assert(handler.matchCommand("QUIT") == CommandHandler.Command.QUIT);
        assert(handler.matchCommand("DAMAGES") == CommandHandler.Command.DAMAGES);
        assert(handler.matchCommand("REPORT") == CommandHandler.Command.REPORT);
        assert(handler.matchCommand("SENSORS") == CommandHandler.Command.SENSORS);
        assert(handler.matchCommand("ORBIT") == CommandHandler.Command.ORBIT);
        assert(handler.matchCommand("TRANSPORT") == CommandHandler.Command.TRANSPORT);
        assert(handler.matchCommand("MINE") == CommandHandler.Command.MINE);
        assert(handler.matchCommand("CRYSTALS") == CommandHandler.Command.CRYSTALS);
        assert(handler.matchCommand("SHUTTLE") == CommandHandler.Command.SHUTTLE);
        assert(handler.matchCommand("PLANETS") == CommandHandler.Command.PLANETS);
        assert(handler.matchCommand("REQUEST") == CommandHandler.Command.REQUEST);
        assert(handler.matchCommand("DEATHRAY") == CommandHandler.Command.DEATHRAY);
        assert(handler.matchCommand("FREEZE") == CommandHandler.Command.FREEZE);
        assert(handler.matchCommand("COMPUTER") == CommandHandler.Command.COMPUTER);
        assert(handler.matchCommand("EMEXIT") == CommandHandler.Command.EMEXIT);
        assert(handler.matchCommand("PROBE") == CommandHandler.Command.PROBE);
        assert(handler.matchCommand("COMMANDS") == CommandHandler.Command.COMMANDS);
        assert(handler.matchCommand("SCORE") == CommandHandler.Command.SCORE);
        assert(handler.matchCommand("CLOAK") == CommandHandler.Command.CLOAK);
        assert(handler.matchCommand("CAPTURE") == CommandHandler.Command.CAPTURE);
        assert(handler.matchCommand("HELP") == CommandHandler.Command.HELP);
        assert(handler.matchCommand("invalid") == CommandHandler.Command.undefined);
    }

    @Test
    public void matchCommandShouldWorkOnAbbreviations() {
        assert(handler.matchCommand("SR") == CommandHandler.Command.SRSCAN);
        assert(handler.matchCommand("MO") == CommandHandler.Command.MOVE);
        assert(handler.matchCommand("PH") == CommandHandler.Command.PHASERS);
        assert(handler.matchCommand("ST") == CommandHandler.Command.STATUS);
        assert(handler.matchCommand("IM") == CommandHandler.Command.IMPULSE);
        assert(handler.matchCommand("PHO") == CommandHandler.Command.PHOTONS);
        assert(handler.matchCommand("LR") == CommandHandler.Command.LRSCAN);
        assert(handler.matchCommand("WA") == CommandHandler.Command.WARP);
        assert(handler.matchCommand("SH") == CommandHandler.Command.SHIELDS);
        assert(handler.matchCommand("CH") == CommandHandler.Command.CHART);
        assert(handler.matchCommand("RE") == CommandHandler.Command.REST);
        assert(handler.matchCommand("DO") == CommandHandler.Command.DOCK);
        assert(handler.matchCommand("DA") == CommandHandler.Command.DAMAGES);
        assert(handler.matchCommand("REP") == CommandHandler.Command.REPORT);
        assert(handler.matchCommand("SE") == CommandHandler.Command.SENSORS);
        assert(handler.matchCommand("OR") == CommandHandler.Command.ORBIT);
        assert(handler.matchCommand("TR") == CommandHandler.Command.TRANSPORT);
        assert(handler.matchCommand("MI") == CommandHandler.Command.MINE);
        assert(handler.matchCommand("CR") == CommandHandler.Command.CRYSTALS);
        assert(handler.matchCommand("SHU") == CommandHandler.Command.SHUTTLE);
        assert(handler.matchCommand("PL") == CommandHandler.Command.PLANETS);
        assert(handler.matchCommand("REQ") == CommandHandler.Command.REQUEST);
        assert(handler.matchCommand("CO") == CommandHandler.Command.COMPUTER);
        assert(handler.matchCommand("EM") == CommandHandler.Command.EMEXIT);
        assert(handler.matchCommand("PR") == CommandHandler.Command.PROBE);
        assert(handler.matchCommand("COMM") == CommandHandler.Command.COMMANDS);
        assert(handler.matchCommand("SC") == CommandHandler.Command.SCORE);
        assert(handler.matchCommand("CL") == CommandHandler.Command.CLOAK);
        assert(handler.matchCommand("CA") == CommandHandler.Command.CAPTURE);

        // cannot abbreviate
        assert(handler.matchCommand("CAL") == CommandHandler.Command.undefined);
        assert(handler.matchCommand("AB") == CommandHandler.Command.undefined);
        assert(handler.matchCommand("DE") == CommandHandler.Command.undefined);
        assert(handler.matchCommand("QU") == CommandHandler.Command.undefined);
        assert(handler.matchCommand("DE") == CommandHandler.Command.undefined);
        assert(handler.matchCommand("FRE") == CommandHandler.Command.undefined);
        assert(handler.matchCommand("HE") == CommandHandler.Command.undefined);
    }

    @Test
    public void getAndExecuteCommandsShouldMatchCommands() {
        when(game.con.readLine(anyString())).thenReturn(" sr 1 2 3").thenReturn("QUIT");

        handler.getAndExecuteCommands();

        verify(srScan, times(1)).ExecSRSCAN();
        verifyNoInteractions(lrScan);
        verifyNoInteractions(status);
        verifyNoInteractions(computer);
        verifyNoInteractions(chart);
        verifyNoInteractions(freeze);
        verifyNoInteractions(help);
        verifyNoInteractions(damages);
        verifyNoInteractions(move);
    }

    @Test
    public void getAndExecuteCommandsShouldNotExecuteUndefinedCommand() {
        when(game.con.readLine(anyString())).thenReturn("USER entered... Something stup1d").thenReturn("QUIT");

        handler.getAndExecuteCommands();

        verifyNoInteractions(srScan);
        verifyNoInteractions(lrScan);
        verifyNoInteractions(status);
        verifyNoInteractions(computer);
        verifyNoInteractions(chart);
        verifyNoInteractions(freeze);
        verify(help, times(1)).printValidCommands();
        verifyNoInteractions(damages);
        verifyNoInteractions(move);
    }

    @Test
    public void getAndExecuteCommandsShouldExecuteSrScanWhenPrompted() {
        when(game.con.readLine(anyString())).thenReturn("SRSCAN").thenReturn("QUIT");

        handler.getAndExecuteCommands();

        verify(srScan, times(1)).ExecSRSCAN();
        verifyNoInteractions(lrScan);
        verifyNoInteractions(status);
        verifyNoInteractions(computer);
        verifyNoInteractions(chart);
        verifyNoInteractions(freeze);
        verifyNoInteractions(help);
        verifyNoInteractions(damages);
        verifyNoInteractions(move);
    }

    @Test
    public void getAndExecuteCommandsShouldExecuteLrScanWhenPrompted() {
        when(game.con.readLine(anyString())).thenReturn("LRSCAN").thenReturn("QUIT");

        handler.getAndExecuteCommands();

        verifyNoInteractions(srScan);
        verify(lrScan, times(1)).ExecLRSCAN();
        verifyNoInteractions(status);
        verifyNoInteractions(computer);
        verifyNoInteractions(chart);
        verifyNoInteractions(freeze);
        verifyNoInteractions(help);
        verifyNoInteractions(damages);
        verifyNoInteractions(move);
    }

    @Test
    public void getAndExecuteCommandsShouldExecuteStatusWhenPrompted() {
        when(game.con.readLine(anyString())).thenReturn("STATUS").thenReturn("QUIT");

        handler.getAndExecuteCommands();

        verifyNoInteractions(srScan);
        verifyNoInteractions(lrScan);
        verify(status, times(1)).ExecSTATUS();
        verifyNoInteractions(computer);
        verifyNoInteractions(chart);
        verifyNoInteractions(freeze);
        verifyNoInteractions(help);
        verifyNoInteractions(damages);
        verifyNoInteractions(move);
    }

    @Test
    public void getAndExecuteCommandsShouldExecuteComputerWhenPrompted() {
        when(game.con.readLine(anyString())).thenReturn("COMPUTER").thenReturn("QUIT");

        handler.getAndExecuteCommands();

        verifyNoInteractions(srScan);
        verifyNoInteractions(lrScan);
        verifyNoInteractions(status);
        verify(computer, times(1)).ExecCOMPUTER(any());
        verifyNoInteractions(chart);
        verifyNoInteractions(freeze);
        verifyNoInteractions(help);
        verifyNoInteractions(damages);
        verifyNoInteractions(move);
    }

    @Test
    public void getAndExecuteCommandsShouldExecuteChartWhenPrompted() {
        when(game.con.readLine(anyString())).thenReturn("CHART").thenReturn("QUIT");

        handler.getAndExecuteCommands();

        verifyNoInteractions(srScan);
        verifyNoInteractions(lrScan);
        verifyNoInteractions(status);
        verifyNoInteractions(computer);
        verify(chart, times(1)).ExecCHART();
        verifyNoInteractions(freeze);
        verifyNoInteractions(help);
        verifyNoInteractions(damages);
        verifyNoInteractions(move);
    }

    @Test
    public void getAndExecuteCommandsShouldExecuteFreezeWhenPrompted() {
        when(game.con.readLine(anyString())).thenReturn("FREEZE").thenReturn("QUIT");

        handler.getAndExecuteCommands();

        verifyNoInteractions(srScan);
        verifyNoInteractions(lrScan);
        verifyNoInteractions(status);
        verifyNoInteractions(computer);
        verifyNoInteractions(chart);
        verify(freeze, times(1)).ExecFREEZE();
        verifyNoInteractions(help);
        verifyNoInteractions(damages);
        verifyNoInteractions(move);
    }

    @Test
    public void getAndExecuteCommandsShouldExecuteHelpWhenPrompted() {
        when(game.con.readLine(anyString())).thenReturn("HELP").thenReturn("QUIT");

        handler.getAndExecuteCommands();

        verifyNoInteractions(srScan);
        verifyNoInteractions(lrScan);
        verifyNoInteractions(status);
        verifyNoInteractions(computer);
        verifyNoInteractions(chart);
        verifyNoInteractions(freeze);
        verify(help, times(1)).ExecHELP(new ArrayList<String>());
        verifyNoInteractions(damages);
        verifyNoInteractions(move);
    }

    @Test
    public void getAndExecuteCommandsShouldExecuteDamagesWhenPrompted() {
        when(game.con.readLine(anyString())).thenReturn("DAMAGES").thenReturn("QUIT");

        handler.getAndExecuteCommands();

        verifyNoInteractions(srScan);
        verifyNoInteractions(lrScan);
        verifyNoInteractions(status);
        verifyNoInteractions(computer);
        verifyNoInteractions(chart);
        verifyNoInteractions(freeze);
        verifyNoInteractions(help);
        verify(damages, times(1)).ExecDAMAGES();
        verifyNoInteractions(move);
    }
    
    @Test
    public void getAndExecuteCommandsShouldExecuteMoveWhenPrompted() {
        when(game.con.readLine(anyString())).thenReturn("MOVE").thenReturn("QUIT");
        
        handler.getAndExecuteCommands();
        
        verifyNoInteractions(srScan);
        verifyNoInteractions(lrScan);
        verifyNoInteractions(status);
        verifyNoInteractions(computer);
        verifyNoInteractions(chart);
        verifyNoInteractions(freeze);
        verifyNoInteractions(help);
        verifyNoInteractions(damages);
        verify(move, times(1)).ExecMOVE(new ArrayList<String>());
    }
}
