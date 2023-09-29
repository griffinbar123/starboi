package sst;

import java.io.Console;

import Model.Coordinate;
import Model.Enterprise;
import Model.Entity;
import Model.Klingon;
import Model.Planet;
import Model.Position;
import Utils.Utils;

public class App {
    private static String cmdstr;
    private static Console con;
    private static char[][][][] Map;
    private static Klingon[] Klingons;
    private static Enterprise Enterprise;
    private static Planet[] Planets;

    public static final char NOTHING = '\u00B7';

    enum Command {
        SRSCAN,
        LRSCAN,
        PHASERS,
        PHOTONS,
        MOVE,
        SHIELDS,
        DOCK,
        DAMAGES,
        CHART,
        IMPULSE,
        REST,
        WARP,
        STATUS,
        SENSORS,
        ORBIT,
        TRANSPORT,
        MINE,
        CRYSTALS,
        SHUTTLE,
        PLANETS,
        REQUEST,
        REPORT,
        COMPUTER,
        COMMANDS,
        EMEXIT,
        PROBE,
        CLOAK,
        CAPTURE,
        SCORE,
        ABANDON(false),
        DESTRUCT(false),
        FREEZE(false),
        DEATHRAY(false),
        DEBUG(false),
        CALL(false),
        QUIT(false),
        HELP(false),
        undefined;

        private boolean canAbbrev;

        Command() {
            canAbbrev = true;
        }

        Command(boolean abrOk) {
            canAbbrev = abrOk;
        }

        public boolean CanAbbrev() {
            return canAbbrev;
        }
    }

    public static void main(String[] args) {

        con = System.console();
        if (con == null)
            return;

        initializeGame(); // TODO: for testing

        // getAndExecuteCommands(); // TODO: uncomment
        ExecSTATUS(); // TODO: for testing
        ExecSRSCAN();
    }

    static void initializeGame() {
        Map = new char[8][8][10][10];

        System.out.println("-SUPER- STAR TREK (JAVA EDITION)\n");
        System.out.println("Latest update-prolly today\n");
        System.out.print("Would you like a regular, tournament, or frozen game?");
        System.out.println(" I'm going to assume regular\n");
        System.out.print("Would you like a Short, Medium, or Long Game?");
        System.out.println(" I'm going to assume short");
        System.out.print("Are you a Novice, Fair, Good, Expert, or Emeritus player?");
        System.out.println(" Def a novice");
        System.out.print("Please type in a secret password (9 characters maximum)-");
        System.out.println("changeit");

        initializeEnterprise();
        initializePlanets(30);
        initializeKlingons(3);

        System.out.println("\n\n\nIt is stardate " + Enterprise.getStarDate()
                + ". The Federation is being attacked by a deadly Klingon invasion force. As captain of the United Starship "
                + "U.S.S. Enterprise, it is your mission to seek out and destroy this invasion force of "
                + Klingons.length + " battle cruisers. You have an initial allotment of " + "7"
                + " stardates to complete your mission. As you proceed you may be given more time.\n");
        System.out.println("You will have " + "x"
                + "supporting starbases. Starbase locations-   3 - 7   7 - 7   2 - 7   8 - 3\nThe Enterprise is currently in Quadrant "
                + Enterprise.getPosition().getQuadrant().getX() + " - " + Enterprise.getPosition().getQuadrant().getY()
                + " Sector " + Enterprise.getPosition().getSector().getX() + " - "
                + Enterprise.getPosition().getSector().getY() + "\n\nGood Luck!\n\n");

        updateMap();
    }

    /**
     * Updates the map
     * 
     * @author Griffin Barnard
     */
    static void updateMap() {
        for (int i = 0; i < Map.length; i++) {
            for (int j = 0; j < Map[i].length; j++) {
                for (int k = 0; k < Map[i][j].length; k++) {
                    for (int l = 0; l < Map[i][j][k].length; l++) {
                        Position position = new Position(new Coordinate(i, j), new Coordinate(k, l));
                        // check if positions is a
                        if (checkEntityListAgainstPosition(position, Klingons)) {
                            Map[i][j][k][l] = Klingons[0].getSymbol();
                        } else if (checkEntityListAgainstPosition(position, Planets)) {
                            Map[i][j][k][l] = Planets[0].getSymbol();
                        } else if (checkEntityAgainstPosition(position, Enterprise)) {
                            Map[i][j][k][l] = Enterprise.getSymbol();
                        } else {
                            Map[i][j][k][l] = NOTHING;
                        }
                    }
                }
            }
        }
    }

    static void initializeEnterprise() {
        Position pos = generateNewPosition();
        Enterprise = new Enterprise(pos);
    }

    static void initializePlanets(int numberOfPlanets) {
        Planets = new Planet[numberOfPlanets];
        for (int i = 0; i < numberOfPlanets; i++) {
            Position pos = generateNewPosition();

            // System.out.println("Planets Positions: " + pos.Quadrant.x + ", " +
            // pos.Quadrant.y + " - " + pos.Sector.x + ", " + pos.Sector.y);
            // make sure position is not being used by another klingon
            Planets[i] = new Planet(pos);
        }
    }

    static void initializeKlingons(int numberOfKlingons) {
        Klingons = new Klingon[numberOfKlingons];
        for (int i = 0; i < numberOfKlingons; i++) {
            Position pos = generateNewPosition();

            // System.out.println("Klingon Positions: " + pos.Quadrant.x + ", " +
            // pos.Quadrant.y + " - " + pos.Sector.x + ", " + pos.Sector.y);
            // make sure position is not being used by another klingon
            Klingons[i] = new Klingon(pos);
        }
    }

    static Position generateNewPosition() {
        Coordinate quadrant = new Coordinate(Utils.randInt(0, 7), (Utils.randInt(0, 7)));
        Coordinate sector = new Coordinate(Utils.randInt(0, 9), (Utils.randInt(0, 9)));
        Position position = new Position(quadrant, sector);
        while (!isPositionEmpty(position)) {
            position = generateNewPosition();
        }
        return position;
    }

    static Boolean isPositionEmpty(Position position) {
        // use this to check if a position is empty, but can't check the map because it
        // might not be updated
        return !(checkEntityAgainstPosition(position, Enterprise) || checkEntityListAgainstPosition(position, Klingons)
                ||
                checkEntityListAgainstPosition(position, Planets));
    }

    static Boolean checkEntityListAgainstPosition(Position position, Entity[] entities) {
        // checks if any entity in the list provided is is in the provided position
        if (entities == null)
            return false;

        for (int i = 0; i < entities.length; i++) {
            if (checkEntityAgainstPosition(position, entities[i])) {
                return true;
            }
        }
        return false;
    }

    static Boolean checkEntityAgainstPosition(Position position, Entity entity) {
        // checks if entity is in a position
        return entity != null && entity.getPosition().getQuadrant().getX() == position.getQuadrant().getX()
                && entity.getPosition().getQuadrant().getY() == position.getQuadrant().getY() &&
                entity.getPosition().getSector().getX() == position.getSector().getX()
                && entity.getPosition().getSector().getY() == position.getSector().getY();
        // return entity != null && entity.position.Quadrant.x == position.Quadrant.x &&
        // entity.position.Quadrant.y == position.Quadrant.y &&
        // entity.position.Sector.x == position.Sector.x && entity.position.Sector.y ==
        // position.Sector.y;
    }

    static void getAndExecuteCommands() {
        // main polling loop
        String str;

        con.printf("\n            SUPER STAR TREK (Java Edition)\n");
        con.printf("\n *** Welcome aboard the USS Enterprise (NCC 1701) *** \n\n");

        while (true) {
            str = con.readLine("COMMAND> ");
            cmdstr = str.toUpperCase().trim();

            Command c = Command.undefined;

            for (Command cx : Command.values()) {
                boolean Matched;

                if (cx.CanAbbrev()) {
                    String cmd = cx.toString();

                    int cmdlen = cmd.length();
                    int tstlen = cmdstr.length();

                    String abrcheck = cmd.substring(0, Math.min(cmdlen, tstlen));

                    Matched = cmdstr.compareTo(abrcheck) == 0;
                } else
                    Matched = cmdstr.compareTo(cx.toString()) == 0;

                if (Matched) {
                    c = cx;
                    break;
                }
            }

            switch (c) {
                case SRSCAN:
                    ExecSRSCAN();
                    break;
                case COMMANDS:
                    ExecCOMMANDS();
                    break;
                case STATUS:
                    ExecSTATUS();
                case QUIT:
                    return;
                case undefined:
                    con.printf("'%s' is not a valid command.\n\n", cmdstr);
                    break;
                default:
                    con.printf("Lt. Cmdr. Scott: \"Captain, '%s' is nae yet operational.\"\n\n", c.toString());
                    break;
            }
        }
    }

    static void ExecCOMMANDS() {
        con.printf("   SRSCAN    MOVE      PHASERS   CALL\n");
        con.printf("   STATUS    IMPULSE   PHOTONS   ABANDON\n");
        con.printf("   LRSCAN    WARP      SHIELDS   DESTRUCT\n");
        con.printf("   CHART     REST      DOCK      QUIT\n");
        con.printf("   DAMAGES   REPORT    SENSORS   ORBIT\n");
        con.printf("   TRANSPORT MIHE      CRYSTALS  SHUTTLE\n");
        con.printf("   PLANETS   REQUEST   DEATHRAY  FREEZE\n");
        con.printf("   COMPUTER  EMEXIT    PROBE     COMMANDS\n");
        con.printf("   SCORE     CLOAK     CAPTURE   HELP\n");
        con.printf("\n");
    }

    static void ExecSRSCAN() {

        int row = Enterprise.getPosition().getQuadrant().getX();
        int column = Enterprise.getPosition().getQuadrant().getY();
        int r, c;

        con.printf("\nShort-range scan (MOCK):\n\n");
        boolean leftside = true;
        boolean rightside = true;

        // print column header
        con.printf("        ");
        for (c = 1; c <= 10; c++)
            con.printf("%1d ", c);
        con.printf("\n");

        for (r = 1; r <= 10; r++) {
            if (leftside) {
                con.printf("    %2d  ", r);

                for (c = 1; c <= 10; c++)
                    con.printf("%c ", Map[row][column][r - 1][c - 1]);
            }

            if (rightside) {
                con.printf(" ");

                switch (r) {
                    case 1:
                        con.printf("Stardate      %.1f", 2516.3);
                        break;
                    case 2:
                        con.printf("Condition     %s", "RED");
                        break;
                    case 3:
                        con.printf("Position      %d - %d, %d - %d", 5, 1, 2, 4);
                        break;
                    case 4:
                        con.printf("Life Support  %s", "DAMAGED, Reserves = 2.30");
                        break;
                    case 5:
                        con.printf("Warp Factor   %.1f", 5.0);
                        break;
                    case 6:
                        con.printf("Energy        %.2f", 2176.24);
                        break;
                    case 7:
                        con.printf("Torpedoes     %d", 3);
                        break;
                    case 8:
                        con.printf("Shields       %s, %d%% %.1f units", "UP", 42, 1050.0);
                        break;
                    case 9:
                        con.printf("Klingons Left %d", 12);
                        break;
                    case 10:
                        con.printf("Time Left     %.2f", 3.72);
                        break;
                }
            }

            con.printf("\n");
        }

        con.printf("\n");
    }

    /**
     * 
     * Prints the status of the Enterprise during the game
     * 
     * @author Matthias Schrock
     * @see Enterprise
     */
    static void ExecSTATUS() {
        System.out.println("Stardate\t" + Enterprise.getStarDate());
        System.out.println("Condition\t" + Enterprise.getCondition());
        System.out.println("Position\t" + Enterprise.getPosition().getQuadrant().getX() + " - "
                + Enterprise.getPosition().getQuadrant().getY() + ", "
                + Enterprise.getPosition().getSector().getX() + " - " + Enterprise.getPosition().getSector().getY());
        System.out.println("Life Support\t" + (Enterprise.getLifeSupport() == 1 ? "ACTIVE" : "RESERVES"));
        System.out.print("Warp Factor\t");
        System.out.printf("%.1f\n", Enterprise.getWarp());
        System.out.print("Energy\t\t");
        System.out.printf("%.2f\n", Enterprise.getEnergy());
        System.out.println("Torpedoes\t" + Enterprise.getTorpedoes());
        System.out.print("Sheilds\t\t");
        System.out.printf("%s, %.0f%% %.1f units\n", (Enterprise.getSheilds().getActive() == 1 ? "ACTIVE" : "DOWN"),
                Enterprise.getSheilds().getLevel() / 100, Enterprise.getSheilds().getUnits());
        System.out.println("Klingons Left\t" + Enterprise.getKlingons());
        System.out.print("Time Left\t");
        System.out.printf("%.2f\n", Enterprise.getTime());
    }
}
