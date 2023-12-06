package sst;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.io.Console;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import Model.Coordinate;
import Model.Enterprise;
import Model.EntityType;
import Model.Game;
import Model.Klingon;
import Model.KlingonCommander;
import Model.KlingonSuperCommander;
import Model.Position;
import Model.Shield;
import Model.Device;

public class CommandTest {
    private Game game;
    private CommandHandler handler;
    private Enterprise enterprise;
    private Computer computer;
    private Chart chart;
    private Status status;
    private SrScan srScan;
    private LrScan lrScan;
    private Help help;
    private Damages damages;
    private Photon photon;
    private Shields shields;

    @Before
    public void setUp() {
        game = new Game();

        // Mocks
        this.game.con = mock(Console.class);
        this.enterprise = mock(Enterprise.class);
        this.handler = mock(CommandHandler.class);
        mockObjects();

        // Commands
        this.computer = new Computer(game);
        this.chart = new Chart(game);
        this.status = new Status(game);
        this.srScan = new SrScan(game);
        this.lrScan = new LrScan(game);
        this.help = new Help(game, handler, "invalid");
        this.damages = new Damages(game);
        this.photon = new Photon(game);
        this.shields = new Shields(game);
    }

    @Test
    public void computerReadCoordinatesShouldWorkAsExpected() {
        List<String> params = new ArrayList<String>();
        params.add("1");
        params.add("2");
        params.add("3");
        params.add("4");

        Optional<Position> position = computer.readCoordinates(params);
        assertNotNull(position);
        assert(position.get().getQuadrant().getX() == 1);
        assert(position.get().getQuadrant().getY() == 2);
        assert(position.get().getSector().getX() == 3);
        assert(position.get().getSector().getY() == 4);
    }

    @Test
    public void computerReadCoordinatesShouldHandleInvalidInputs() {
        List<String> params = new ArrayList<String>();
        params.add("1");
        params.add("2");
        params.add("3");

        Optional<Position> position = computer.readCoordinates(params);
        assertEquals(Optional.empty(), position);

        params = new ArrayList<String>();
        params.add("1");
        params.add("2");
        params.add("3");
        params.add("4");
        params.add("5");

        position = computer.readCoordinates(params);
        assertEquals(Optional.empty(), position);

        params = new ArrayList<String>();
        params.add("1");
        params.add("hello");

        position = computer.readCoordinates(params);
        assertEquals(Optional.empty(), position);
    }

    @Test
    public void computerReadCoordinatesShouldPromptForInputIfNoParams() {
        List<String> params = new ArrayList<String>();

        when(game.con.readLine(eq("Destination quadrant and/or sector? "))).thenReturn("1 2 3 4");

        Optional<Position> position = computer.readCoordinates(params);
        assertNotNull(position);
        assert(position.get().getQuadrant().getX() == 1);
        assert(position.get().getQuadrant().getY() == 2);
        assert(position.get().getSector().getX() == 3);
        assert(position.get().getSector().getY() == 4);
    }

    @Test
    public void positionCalcDistanceShouldWorkAsExpected() {
        Position position = new Position(new Coordinate(0, 0), new Coordinate(0, 0));
        Position position2 = new Position(new Coordinate(1, 1), new Coordinate(1, 1));
        double distance = position.calcDistance(position2);
        assertEquals(15.556349186104045, distance, 0.02);

        position = new Position(new Coordinate(0, 0), new Coordinate(0, 0));
        position2 = new Position(new Coordinate(1, 1), new Coordinate(2, 1));
        distance = position.calcDistance(position2);
        assertEquals(16.27882059609970, distance, 0.02);

        position = new Position(new Coordinate(0, 0), new Coordinate(0, 0));
        position2 = new Position(new Coordinate(1, 1), new Coordinate(1, 2));
        distance = position.calcDistance(position2);
        assertEquals(16.27882059609970, distance, 0.02);

        position = new Position(new Coordinate(0, 0), new Coordinate(0, 0));
        position2 = new Position(new Coordinate(1, 1), new Coordinate(2, 2));
        distance = position.calcDistance(position2);
        assertEquals(16.97056274847714, distance, 0.02);

        position = new Position(new Coordinate(0, 0), new Coordinate(0, 0));
        position2 = new Position(new Coordinate(8, 8), new Coordinate(8, 8));
        distance = position.calcDistance(position2);
        assertEquals(124.45079348883236, distance, 0.02);
    }

    @Test
    public void computerCalcTimeShouldWorkAsExpected() {
        // at warp 1, time is distance
        when(enterprise.getWarp()).thenReturn(1.0);
        Position position = new Position(new Coordinate(0, 0), new Coordinate(0, 0));
        Position position2 = new Position(new Coordinate(1, 1), new Coordinate(1, 1));
        double time = computer.calcTime(position, position2);
        assertEquals(15.556349186104045, time, 0.02);

        position = new Position(new Coordinate(0, 0), new Coordinate(0, 0));
        position2 = new Position(new Coordinate(1, 1), new Coordinate(2, 1));
        time = computer.calcTime(position, position2);
        assertEquals(16.27882059609970, time, 0.02);

        position = new Position(new Coordinate(0, 0), new Coordinate(0, 0));
        position2 = new Position(new Coordinate(1, 1), new Coordinate(1, 2));
        time = computer.calcTime(position, position2);
        assertEquals(16.27882059609970, time, 0.02);

        position = new Position(new Coordinate(0, 0), new Coordinate(0, 0));
        position2 = new Position(new Coordinate(1, 1), new Coordinate(2, 2));
        time = computer.calcTime(position, position2);
        assertEquals(16.97056274847714, time, 0.02);

        position = new Position(new Coordinate(0, 0), new Coordinate(0, 0));
        position2 = new Position(new Coordinate(8, 8), new Coordinate(8, 8));
        time = computer.calcTime(position, position2);
        assertEquals(124.45079348883236, time, 0.02);

        // at higher warp factors, time will decrease (at warp 5 time will be 1/25 of distance)
        when(enterprise.getWarp()).thenReturn(5.0);
        position = new Position(new Coordinate(0, 0), new Coordinate(0, 0));
        position2 = new Position(new Coordinate(1, 1), new Coordinate(1, 1));
        time = computer.calcTime(position, position2);
        assertEquals(0.6222539674441618, time, 0.02);

        position = new Position(new Coordinate(0, 0), new Coordinate(0, 0));
        position2 = new Position(new Coordinate(1, 1), new Coordinate(2, 1));
        time = computer.calcTime(position, position2);
        assertEquals(0.6511528238439882, time, 0.02);

        position = new Position(new Coordinate(0, 0), new Coordinate(0, 0));
        position2 = new Position(new Coordinate(1, 1), new Coordinate(1, 2));
        time = computer.calcTime(position, position2);
        assertEquals(0.6511528238439882, time, 0.02);

        position = new Position(new Coordinate(0, 0), new Coordinate(0, 0));
        position2 = new Position(new Coordinate(1, 1), new Coordinate(2, 2));
        time = computer.calcTime(position, position2);
        assertEquals(0.6788225099390855, time, 0.02);

        position = new Position(new Coordinate(0, 0), new Coordinate(0, 0));
        position2 = new Position(new Coordinate(8, 8), new Coordinate(8, 8));
        time = computer.calcTime(position, position2);
        assertEquals(4.978031739553295, time, 0.02);
    }

    @Test
    public void computerCalcPowerShouldWorkAsExpected() {
        // TODO finish when sheild power drain implemented
    }

    @Test
    public void computerCalcWarpDriveShouldWorkAsExpected() {
        // at time = 1, calculated warp is distance
        Position position = new Position(new Coordinate(0, 0), new Coordinate(0, 0));
        Position position2 = new Position(new Coordinate(1, 1), new Coordinate(1, 1));
        Double time = computer.calcWarpDrive(position, position2, 1.0);
        assertEquals(3.0, time, 0.02);

        position = new Position(new Coordinate(0, 0), new Coordinate(0, 0));
        position2 = new Position(new Coordinate(1, 1), new Coordinate(2, 1));
        time = computer.calcWarpDrive(position, position2, 1.0);
        assertEquals(4.0, time, 0.02);

        position = new Position(new Coordinate(0, 0), new Coordinate(0, 0));
        position2 = new Position(new Coordinate(1, 1), new Coordinate(1, 2));
        time = computer.calcWarpDrive(position, position2, 1.0);
        assertEquals(4.0, time, 0.02);

        position = new Position(new Coordinate(0, 0), new Coordinate(0, 0));
        position2 = new Position(new Coordinate(2, 2), new Coordinate(2, 2));
        time = computer.calcWarpDrive(position, position2, 7.0);
        assertEquals(2.0, time, 0.02);

        position = new Position(new Coordinate(0, 0), new Coordinate(0, 0));
        position2 = new Position(new Coordinate(8, 8), new Coordinate(8, 8));
        time = computer.calcWarpDrive(position, position2, 7.0);
        assertEquals(4.0, time, 0.02);
    }

    @Test
    public void computerCommandShouldWorkAsExpected() {
        // TODO
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
                "Shields       %s, %.0f%% %.1f units\n" +
                "Klingons Left %d\n" +
                "Time Left     %.2f\n";

        verify(game.con).printf(stat, 123.4, "GREEN", 6, 6, 5, 4, "ACTIVE", 1.0, 100.00, 10,
                "UP", 100.0, 1.0, 3, 10.00);
    }

    @Test
    public void commandsCommandShouldWorkAsExpected() {
        help.ExecCOMMANDS();

        String out = "\nValid commands:\n" +
                "SRSCAN     MOVE       STATUS     PHOTONS    \n" +
                "LRSCAN     SHIELDS    CHART      REST       \n" +
                "DOCK       QUIT       DAMAGES    COMPUTER   \n" +
                "COMMANDS   SCORE      HELP       \n\n";

        verify(game.con).printf(out);
    }

    @Test
    public void srScanShouldWorkAsExpected() {
        srScan.ExecSRSCAN();

        String scan = "\n    1 2 3 4 5 6 7 8 9 10 \n" +
                " 1  · · · · · · · · · ·  Stardate      123.4\n" +
                " 2  · · · · · · · · · ·  Condition     GREEN\n" +
                " 3  · · · · · · · · · ·  Position      6 - 6, 5 - 4\n" +
                " 4  · · · · · · · · · ·  Life Support  DAMAGED, Reserves = 2.30\n" +
                " 5  · · · E · · · · · ·  Warp Factor   1.0\n" +
                " 6  · · · · · · · · · ·  Energy        100.00\n" +
                " 7  · · · · · · · · · ·  Torpedoes     10\n" +
                " 8  · · · · · · · · · ·  Shields       UP, 100% 1.0 units\n" +
                " 9  · · · · · · · · · ·  Klingons Left 3\n" +
                "10  · · · · · · · · · ·  Time Left     10.00\n";

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
        EntityType map[][][][] = this.game.getMap();
        map[4][4][0][0] = EntityType.STARBASE;
        map[4][5][0][0] = EntityType.KLINGON;
        map[4][6][0][0] = EntityType.KLINGON;
        map[5][4][0][0] = EntityType.KLINGON;
        map[5][5][0][0] = EntityType.KLINGON;
        map[5][5][0][1] = EntityType.KLINGON;
        map[5][6][0][0] = EntityType.KLINGON;
        map[6][4][0][0] = EntityType.KLINGON;
        map[6][5][0][0] = EntityType.KLINGON;
        map[6][6][0][0] = EntityType.STARBASE;
        map[6][6][0][1] = EntityType.KLINGON;
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

    @Test
    public void helpShouldPrintAvailableCommandsWithInvalidInput() {
        List<String> params = new ArrayList<String>();
        params.add("INVALID");

        when(game.con.readLine(eq("Help on what command?"))).thenReturn("");
        when(handler.matchCommand(eq("INVALID"))).thenReturn(Command.undefined);

        help.ExecHELP(params);

        String expected = "\nValid commands:\n" +
                "SRSCAN     MOVE       STATUS     PHOTONS    \n" +
                "LRSCAN     SHIELDS    CHART      REST       \n" +
                "DOCK       QUIT       DAMAGES    COMPUTER   \n" +
                "COMMANDS   SCORE      HELP       \n\n";

        verify(game.con).printf(expected);
    }

    @Test
    public void helpShouldPrintAlertIfDocNotFound() {
        when(handler.matchCommand(eq("srscan"))).thenReturn(Command.SRSCAN);
        help.ExecHELP(Arrays.asList("srscan"));

        String expected = "Spock-  \"Captain, I'm sorry, but I can't find SST.DOC.\"\n" +
                "   computer. You need to find SST.DOC and put it in the\n" +
                "   current directory.\"\n";

        verify(game.con).printf("%s", expected);
    }

    @Test
    public void damagesShouldPrintOKifNoDamage() {
        damages.ExecDAMAGES();

        String expected = "\nAll devices functional.\n";

        verify(game.con).printf(expected);
    }

    @Test
    public void damagedDevicesShouldDisplayRepairTimes() {
        Map<Device, Double> dmg = new HashMap<Device, Double>();
        for (Device d : Device.values()) {
            dmg.put(d, 1.0);
        }
        when(game.getEnterprise().getDeviceDamage()).thenReturn(dmg);
        damages.ExecDAMAGES();

        String expected = "\nDevice            -REPAIR TIMES-\n" +
                "                IN FLIGHT   DOCKED\n" +
                "     S. R. Sensors     1.05      0.26\n" +
                "     L. R. Sensors     1.05      0.26\n" +
                "           Phasers     1.05      0.26\n" +
                "       ProtonTubes     1.05      0.26\n" +
                "      Life Support     1.05      0.26\n" +
                "      Warp Engines     1.05      0.26\n" +
                "   Impulse Engines     1.05      0.26\n" +
                "           Shields     1.05      0.26\n" +
                "    Subspace Radio     1.05      0.26\n" +
                "     Shuttle Craft     1.05      0.26\n" +
                "          Computer     1.05      0.26\n" +
                "       Transporter     1.05      0.26\n" +
                "    Shield Control     1.05      0.26\n" +
                "         Death Ray     1.01      0.25\n" +
                "       D. S. Probe     1.05      0.26\n" +
                "   Cloaking Device     1.05      0.26\n";

        verify(game.con).printf("%s", expected);
    }

    @Test
    public void damagedSrSensorsShouldLimitRange() {
        Map<Device, Double> dmg = new HashMap<Device, Double>();
        dmg.put(Device.SR_SENSORS, 1.0);
        when(game.getEnterprise().getDeviceDamage()).thenReturn(dmg);

        srScan.ExecSRSCAN();

        String scan = "\n    1 2 3 4 5 6 7 8 9 10 \n" +
                " 1  - - - - - - - - - -  Stardate      123.4\n" +
                " 2  - - - - - - - - - -  Condition     GREEN\n" +
                " 3  - - - - - - - - - -  Position      6 - 6, 5 - 4\n" +
                " 4  - - · · · - - - - -  Life Support  DAMAGED, Reserves = 2.30\n" +
                " 5  - - · E · - - - - -  Warp Factor   1.0\n" +
                " 6  - - · · · - - - - -  Energy        100.00\n" +
                " 7  - - - - - - - - - -  Torpedoes     10\n" +
                " 8  - - - - - - - - - -  Shields       UP, 100% 1.0 units\n" +
                " 9  - - - - - - - - - -  Klingons Left 3\n" +
                "10  - - - - - - - - - -  Time Left     10.00\n";

        verify(game.con).printf("%s", scan);
    }

    @Test
    public void damagedLrSensorsShouldPreventDeviceFunctioning() {
        Map<Device, Double> dmg = new HashMap<Device, Double>();
        dmg.put(Device.LR_SENSORS, 1.0);
        when(game.getEnterprise().getDeviceDamage()).thenReturn(dmg);
        lrScan.ExecLRSCAN();

        String error = "LONG-RANGE SENSORS DAMAGED.\n\n";

        verify(game.con).printf(error);
    }

    @Test
    public void damagedComputerShouldPreventDeviceFunctioning() {
        Map<Device, Double> dmg = new HashMap<Device, Double>();
        dmg.put(Device.COMPUTER, 1.0);
        when(game.getEnterprise().getDeviceDamage()).thenReturn(dmg);
        computer.ExecCOMPUTER(null);

        String error = "COMPUTER DAMAGED, USE A POCKET CALCULATOR.\n\n";

        verify(game.con).printf(error);
    }

    @Test
    public void getNumberOfTorpedoesToFireShouldReturnNullForNonInteger() {
        List<String> badInput = new ArrayList<String>();
        badInput.add("a");
        badInput.add("1");
        Integer numOfTorpedoes = photon.getNumberOfTorpedoesToFire(badInput);
        assertEquals(numOfTorpedoes, null);
    }

    @Test
    public void getNumberOfTorpedoesToFireShouldReturnNullForTooLittleTorps() {
        List<String> badInput = new ArrayList<String>();
        badInput.add("0");
        Integer numOfTorpedoes = photon.getNumberOfTorpedoesToFire(badInput);
        assertEquals(numOfTorpedoes, null);
    }

    @Test
    public void getNumberOfTorpedoesToFireShouldReturnCorrectValue() {
        List<String> input = new ArrayList<String>();
        input.add("1");
        Integer numOfTorpedoes = photon.getNumberOfTorpedoesToFire(input);
        assert(numOfTorpedoes == 1);
    }

    @Test
    public void getSectorsShouldReturnNullForNonInteger() {
        List<String> badInput = new ArrayList<String>();
        badInput.add("1");
        badInput.add("a");
        List<Double> sectors = photon.getSectors(1, badInput);
        assertEquals(sectors, null);
    }

    @Test
    public void getSectorsShouldReturnNullForWrongAmountOfInput() {
        List<String> badInput = new ArrayList<String>();
        badInput.add("1");
        badInput.add("2");
        badInput.add("3");
        List<Double> sectors = photon.getSectors(1, badInput);
        assertEquals(sectors, null);
    }

    @Test
    public void getSectorsShouldReturnCorrectValuesWithBasicParams() {
        List<String> input = new ArrayList<String>();
        input.add("1");
        input.add("2");
        List<Double> sectors = photon.getSectors(1, input);
        List<Double> output = new ArrayList<Double>();
        output.add(1.0);
        output.add(2.0);
        assertEquals(sectors, output);
    }

    @Test
    public void getSectorsShouldReturnCorrectValuesWithTwoParams() {
        List<String> input = new ArrayList<String>();
        input.add("1");
        input.add("2");
        List<Double> sectors = photon.getSectors(2, input);
        List<Double> output = new ArrayList<Double>();
        output.add(1.0);
        output.add(2.0);
        output.add(1.0);
        output.add(2.0);
        assertEquals(sectors, output);
    }

    @Test
    public void getSectorsShouldReturnCorrectValuesWithBaseParams() {
        List<String> input = new ArrayList<String>();
        input.add("1");
        input.add("2");
        input.add("3");
        input.add("4");
        List<Double> sectors = photon.getSectors(2, input);
        List<Double> output = new ArrayList<Double>();
        output.add(1.0);
        output.add(2.0);
        output.add(3.0);
        output.add(4.0);
        assertEquals(output, sectors);
    }

    private void mockObjects() {
        // Empty map
        EntityType map[][][][] = new EntityType[8][8][10][10];
        for (int a = 0; a < 8; a++) {
            for (int b = 0; b < 8; b++) {
                for (int c = 0; c < 10; c++) {
                    for (int d = 0; d < 10; d++) {
                        map[a][b][c][d] = EntityType.NOTHING;
                    }
                }
            }
        }
        map[5][5][4][3] = EntityType.ENTERPRISE;

        Map<Device, Double> dmg = new HashMap<Device, Double>();
        for (Device d : Device.values()) {
            dmg.put(d, 0.0);
        }

        // Game objects
        Position position = new Position(new Coordinate(0, 0), new Coordinate(0, 0));
        Klingon klingons[] = new Klingon[] { new Klingon(position) };
        KlingonCommander[] klingonCommanders = new KlingonCommander[] { new KlingonCommander(position) };
        KlingonSuperCommander klingonSuperCommander = new KlingonSuperCommander(position);
        game.setMap(map);
        game.setEnterprise(enterprise);
        game.setStarDate(123.4);
        game.setKlingons(klingons);
        game.setKlingonCommanders(klingonCommanders);
        game.setKlingonSuperCommander(klingonSuperCommander);
        game.setTime(10.0);
        
        // Mock objects
        when(enterprise.getCondition()).thenReturn(Model.Condition.GREEN);
        position = new Position(new Coordinate(5, 5), new Coordinate(4, 3));
        when(game.getEnterprise().getPosition()).thenReturn(position);
        when(enterprise.getLifeSupport()).thenReturn((byte) 1);
        when(enterprise.getWarp()).thenReturn(1.0);
        when(enterprise.getEnergy()).thenReturn(100.0);
        when(enterprise.getTorpedoes()).thenReturn(10);
        Shield shields = new Shield();
        shields.setStatus(Model.ShieldStatus.UP);
        shields.setLevel(1.0);
        shields.setMaxLevel(1.0);
        shields.setShieldIsChanging(false);
        when(enterprise.getSheilds()).thenReturn(shields);
        when(game.getEnterprise().getDeviceDamage()).thenReturn(dmg);
    }
}
