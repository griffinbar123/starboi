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
    private Photon photon;
    private Score score;
    private Dock dock;
    private Attack attack;

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
        photon = mock(Photon.class);
        score = mock(Score.class);
        dock = mock(Dock.class);
        attack = mock(Attack.class);

        handler = new CommandHandler(game, srScan, lrScan, status, computer,
                chart, freeze, help, damages, move, photon, score, dock, attack);
    }

    @Test
    public void matchCommandShouldWorkAsExpected() {
        assert(handler.matchCommand("SRSCAN") == Command.SRSCAN);
        assert(handler.matchCommand("MOVE") == Command.MOVE);
        assert(handler.matchCommand("PHASERS") == Command.PHASERS);
        assert(handler.matchCommand("CALL") == Command.CALL);
        assert(handler.matchCommand("STATUS") == Command.STATUS);
        assert(handler.matchCommand("IMPULSE") == Command.IMPULSE);
        assert(handler.matchCommand("PHOTONS") == Command.PHOTONS);
        assert(handler.matchCommand("ABANDON") == Command.ABANDON);
        assert(handler.matchCommand("LRSCAN") == Command.LRSCAN);
        assert(handler.matchCommand("WARP") == Command.WARP);
        assert(handler.matchCommand("SHIELDS") == Command.SHIELDS);
        assert(handler.matchCommand("DESTRUCT") == Command.DESTRUCT);
        assert(handler.matchCommand("CHART") == Command.CHART);
        assert(handler.matchCommand("REST") == Command.REST);
        assert(handler.matchCommand("DOCK") == Command.DOCK);
        assert(handler.matchCommand("QUIT") == Command.QUIT);
        assert(handler.matchCommand("DAMAGES") == Command.DAMAGES);
        assert(handler.matchCommand("REPORT") == Command.REPORT);
        assert(handler.matchCommand("SENSORS") == Command.SENSORS);
        assert(handler.matchCommand("ORBIT") == Command.ORBIT);
        assert(handler.matchCommand("TRANSPORT") == Command.TRANSPORT);
        assert(handler.matchCommand("MINE") == Command.MINE);
        assert(handler.matchCommand("CRYSTALS") == Command.CRYSTALS);
        assert(handler.matchCommand("SHUTTLE") == Command.SHUTTLE);
        assert(handler.matchCommand("PLANETS") == Command.PLANETS);
        assert(handler.matchCommand("REQUEST") == Command.REQUEST);
        assert(handler.matchCommand("DEATHRAY") == Command.DEATHRAY);
        assert(handler.matchCommand("FREEZE") == Command.FREEZE);
        assert(handler.matchCommand("COMPUTER") == Command.COMPUTER);
        assert(handler.matchCommand("EMEXIT") == Command.EMEXIT);
        assert(handler.matchCommand("PROBE") == Command.PROBE);
        assert(handler.matchCommand("COMMANDS") == Command.COMMANDS);
        assert(handler.matchCommand("SCORE") == Command.SCORE);
        assert(handler.matchCommand("CLOAK") == Command.CLOAK);
        assert(handler.matchCommand("CAPTURE") == Command.CAPTURE);
        assert(handler.matchCommand("HELP") == Command.HELP);
        assert(handler.matchCommand("invalid") == Command.undefined);
    }

    @Test
    public void matchCommandShouldWorkOnAbbreviations() {
        assert(handler.matchCommand("SR") == Command.SRSCAN);
        assert(handler.matchCommand("MO") == Command.MOVE);
        assert(handler.matchCommand("PH") == Command.PHASERS);
        assert(handler.matchCommand("ST") == Command.STATUS);
        assert(handler.matchCommand("IM") == Command.IMPULSE);
        assert(handler.matchCommand("PHO") == Command.PHOTONS);
        assert(handler.matchCommand("LR") == Command.LRSCAN);
        assert(handler.matchCommand("WA") == Command.WARP);
        assert(handler.matchCommand("SH") == Command.SHIELDS);
        assert(handler.matchCommand("CH") == Command.CHART);
        assert(handler.matchCommand("RE") == Command.REST);
        assert(handler.matchCommand("DO") == Command.DOCK);
        assert(handler.matchCommand("DA") == Command.DAMAGES);
        assert(handler.matchCommand("REP") == Command.REPORT);
        assert(handler.matchCommand("SE") == Command.SENSORS);
        assert(handler.matchCommand("OR") == Command.ORBIT);
        assert(handler.matchCommand("TR") == Command.TRANSPORT);
        assert(handler.matchCommand("MI") == Command.MINE);
        assert(handler.matchCommand("CR") == Command.CRYSTALS);
        assert(handler.matchCommand("SHU") == Command.SHUTTLE);
        assert(handler.matchCommand("PL") == Command.PLANETS);
        assert(handler.matchCommand("REQ") == Command.REQUEST);
        assert(handler.matchCommand("CO") == Command.COMPUTER);
        assert(handler.matchCommand("EM") == Command.EMEXIT);
        assert(handler.matchCommand("PR") == Command.PROBE);
        assert(handler.matchCommand("COMM") == Command.COMMANDS);
        assert(handler.matchCommand("SC") == Command.SCORE);
        assert(handler.matchCommand("CL") == Command.CLOAK);
        assert(handler.matchCommand("CA") == Command.CAPTURE);

        // cannot abbreviate
        assert(handler.matchCommand("CAL") == Command.undefined);
        assert(handler.matchCommand("AB") == Command.undefined);
        assert(handler.matchCommand("DE") == Command.undefined);
        assert(handler.matchCommand("QU") == Command.undefined);
        assert(handler.matchCommand("DE") == Command.undefined);
        assert(handler.matchCommand("FRE") == Command.undefined);
        assert(handler.matchCommand("HE") == Command.undefined);
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
        verify(help, times(1)).ExecCOMMANDS();
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
