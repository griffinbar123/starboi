package sst;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
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

public class CommandTest {
    private Game game;
    private Enterprise enterprise;
    private Chart chart;
    private Status status;
    private Commands commands;
    private SrScan srScan;
    private LrScan lrScan;

    @Before
    public void setUp() {
        game = new Game();

        // Mocks
        game.con = mock(Console.class);
        enterprise = mock(Enterprise.class);
        mockObjects();

        // Commands
        chart = new Chart(game);
        status = new Status(game);
        commands = new Commands(game);
        srScan = new SrScan(game);
        lrScan = new LrScan(game);
    }

    @Test
    public void chartCommandShouldWorkAsExpected() {
        chart.ExecCHART();
        String chart = "\nSTAR CHART FOR THE KNOWN GALAXY\n\n" +
                "     1    2    3    4    5    6    7    8\n" +
                "   -----------------------------------------\n" +
                "  - \n" +
                "1 - %-3s  %-3s  %-3s  %-3s  %-3s  %-3s  %-3s  %-3s  -\n" +
                "2 - %-3s  %-3s  %-3s  %-3s  %-3s  %-3s  %-3s  %-3s  -\n" +
                "3 - %-3s  %-3s  %-3s  %-3s  %-3s  %-3s  %-3s  %-3s  -\n" +
                "4 - %-3s  %-3s  %-3s  %-3s  %-3s  %-3s  %-3s  %-3s  -\n" +
                "5 - %-3s  %-3s  %-3s  %-3s  %-3s  %-3s  %-3s  %-3s  -\n" +
                "6 - %-3s  %-3s  %-3s  %-3s  %-3s  %-3s  %-3s  %-3s  -\n" +
                "7 - %-3s  %-3s  %-3s  %-3s  %-3s  %-3s  %-3s  %-3s  -\n" +
                "8 - %-3s  %-3s  %-3s  %-3s  %-3s  %-3s  %-3s  %-3s  -\n" +
                "\nThe Enterprise is currently in Quadrant %d - %d\n";

        verify(game.con).printf(chart, "...", "...", "...", "...", "...", "...", "...", "...", "...", "...", "...",
                "...", "...", "...", "...", "...", "...", "...", "...", "...", "...", "...", "...", "...", "...", "...",
                "...", "...", "...", "...", "...", "...", "...", "...", "...", "...", "...", "...", "...", "...", "...",
                "...", "...", "...", "...", "...", "...", "...", "...", "...", "...", "...", "...", "...", "...", "...",
                "...", "...", "...", "...", "...", "...", "...", "...", 6, 6);
    }

    @Test
    public void statusCommandShouldWorkAsExpected() {
        status.ExecSTATUS();
        String stat = "Stardate      %.1f\n" +
                "Condition     %s\n" +
                "Position      %d - %d, %d - %d\n" +
                "Life Support  %s\n" +
                "Warp Factor   %.1f\n" +
                "Energy        %.2f\n" +
                "Torpedoes     %d\n" +
                "Shields       %s, %d%% %.1f units\n" +
                "Klingons Left %d\n" +
                "Time Left     %.2f\n";

        verify(game.con).printf(stat, 123.4, "GREEN", 6, 6, 4, 5, "ACTIVE", 1.0, 100.00, 10,
                "UP", 100, 1.0, 1, 10.00);
    }

    @Test
    public void commandsCommandShouldWorkAsExpected() {
        commands.ExecCOMMANDS();

        String out = "   SRSCAN    MOVE      PHASERS   CALL\n" +
                "   STATUS    IMPULSE   PHOTONS   ABANDON\n" +
                "   LRSCAN    WARP      SHIELDS   DESTRUCT\n" +
                "   CHART     REST      DOCK      QUIT\n" +
                "   DAMAGES   REPORT    SENSORS   ORBIT\n" +
                "   TRANSPORT MIHE      CRYSTALS  SHUTTLE\n" +
                "   PLANETS   REQUEST   DEATHRAY  FREEZE\n" +
                "   COMPUTER  EMEXIT    PROBE     COMMANDS\n" +
                "   SCORE     CLOAK     CAPTURE   HELP\n\n";

        verify(game.con).printf("%s", out);
    }

    @Test
    public void srScanShouldWorkAsExpected() {
        srScan.ExecSRSCAN();

        String scan = "\n    1 2 3 4 5 6 7 8 9 10 \n" +
                " 1  . . . . . . . . . .  Stardate      123.4\n" +
                " 2  . . . . . . . . . .  Condition     GREEN\n" +
                " 3  . . . . . . . . . .  Position      6 - 6, 4 - 5\n" +
                " 4  . . . . . . . . . .  Life Support  DAMAGED, Reserves = 2.30\n" +
                " 5  . . . . . . . . . .  Warp Factor   1.0\n" +
                " 6  . . . . . . . . . .  Energy        100.00\n" +
                " 7  . . . . . . . . . .  Torpedoes     10\n" +
                " 8  . . . . . . . . . .  Shields       UP, 100% 1.0 units\n" +
                " 9  . . . . . . . . . .  Klingons Left 1\n" +
                "10  . . . . . . . . . .  Time Left     10.00\n";

        verify(game.con).printf("%s", scan);
    }

    @Test
    public void lrSacnShouldWorkAsExpected() {
        lrScan.ExecLRSCAN();

        String scan = "\nLong-range scan for Quadrant %d - %d:\n" +
                "%-5s%-5s%-5s\n" +
                "%-5s%-5s%-5s\n" +
                "%-5s%-5s%-5s\n";

        verify(game.con).printf(scan, 6, 6, "0", "0", "0", "0", "0", "0", "0", "0", "0");
    }

    @Test
    public void lrScanShouldStoreScanForChart() {
        char map[][][][] = this.game.getMap();
        map[4][4][0][0] = 'B';
        map[4][5][0][0] = 'K';
        map[4][6][0][0] = 'K';
        map[5][4][0][0] = 'K';
        map[5][5][0][0] = 'K';
        map[5][5][0][1] = 'K';
        map[5][6][0][0] = 'K';
        map[6][4][0][0] = 'K';
        map[6][5][0][0] = 'K';
        map[6][6][0][0] = 'B';
        map[6][6][0][1] = 'K';
        game.setMap(map);

        // LrScan hasn't run, so chart should be empty
        chartCommandShouldWorkAsExpected();

        // Run LrScan to update chart
        lrScan.ExecLRSCAN();
        String expected = "\nLong-range scan for Quadrant %d - %d:\n" +
                "%-5s%-5s%-5s\n" +
                "%-5s%-5s%-5s\n" +
                "%-5s%-5s%-5s\n";
        verify(game.con).printf(expected, 6, 6, "10", "100", "100", "100", "200", "100", "100", "100", "110");

        // Verify chart
        chart.ExecCHART();
        expected = "\nSTAR CHART FOR THE KNOWN GALAXY\n\n" +
                "     1    2    3    4    5    6    7    8\n" +
                "   -----------------------------------------\n" +
                "  - \n" +
                "1 - %-3s  %-3s  %-3s  %-3s  %-3s  %-3s  %-3s  %-3s  -\n" +
                "2 - %-3s  %-3s  %-3s  %-3s  %-3s  %-3s  %-3s  %-3s  -\n" +
                "3 - %-3s  %-3s  %-3s  %-3s  %-3s  %-3s  %-3s  %-3s  -\n" +
                "4 - %-3s  %-3s  %-3s  %-3s  %-3s  %-3s  %-3s  %-3s  -\n" +
                "5 - %-3s  %-3s  %-3s  %-3s  %-3s  %-3s  %-3s  %-3s  -\n" +
                "6 - %-3s  %-3s  %-3s  %-3s  %-3s  %-3s  %-3s  %-3s  -\n" +
                "7 - %-3s  %-3s  %-3s  %-3s  %-3s  %-3s  %-3s  %-3s  -\n" +
                "8 - %-3s  %-3s  %-3s  %-3s  %-3s  %-3s  %-3s  %-3s  -\n" +
                "\nThe Enterprise is currently in Quadrant %d - %d\n";

        verify(game.con).printf(expected, "...", "...", "...", "...", "...", "...", "...", "...", "...", "...",
                "...", "...", "...", "...", "...", "...", "...", "...", "...", "...", "...", "...", "...", "...", "...",
                "...", "...", "...", "...", "...", "...", "...", "...", "...", "...", "...", "10", "100", "100", "...",
                "...", "...", "...", "...", "100", "200", "100", "...", "...", "...", "...", "...", "100", "100", "110",
                "...", "...", "...", "...", "...", "...", "...", "...", "...", 6, 6);
    }

    private void mockObjects() {
        // Empty map
        char map[][][][] = new char[8][8][10][10];
        for (int a = 0; a < 8; a++) {
            for (int b = 0; b < 8; b++) {
                for (int c = 0; c < 10; c++) {
                    for (int d = 0; d < 10; d++) {
                        map[a][b][c][d] = '.';
                    }
                }
            }
        }

        // Game objects
        Position position = new Position(new Coordinate(0, 0), new Coordinate(0, 0));
        Klingon klingons[] = new Klingon[] { new Klingon(position) };
        game.setMap(map);
        game.setEnterprise(enterprise);
        game.setStarDate(123.4);
        game.setKlingons(klingons);
        game.setTime(10.0);

        // Mock objects
        when(enterprise.getCondition()).thenReturn("GREEN");
        position = new Position(new Coordinate(5, 5), new Coordinate(4, 3));
        when(game.getEnterprise().getPosition()).thenReturn(position);
        when(enterprise.getLifeSupport()).thenReturn((byte) 1);
        when(enterprise.getWarp()).thenReturn(1.0);
        when(enterprise.getEnergy()).thenReturn(100.0);
        when(enterprise.getTorpedoes()).thenReturn(10);
        Sheild shields = new Sheild();
        shields.setActive("UP");
        shields.setLevel(100);
        shields.setUnits(1.0);
        when(enterprise.getSheilds()).thenReturn(shields);
    }
}
