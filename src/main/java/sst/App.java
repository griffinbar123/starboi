package sst;

import java.io.Console;
import java.util.Scanner;

import Model.Coordinate;
import Model.Enterprise;
import Model.Entity;
import Model.Klingon;
import Model.Planet;
import Model.Position;
import Model.Romulan;
import Model.Star;
import Model.Starbase;
import Utils.Utils;

public class App {
    private Scanner scanner = new Scanner(System.in);
    private static String cmdstr;
    private static Console con;
    private static char[][][][] Map;
    private static Klingon[] Klingons;
    private static Enterprise Enterprise;
    private static Planet[] Planets;
    private static Starbase[] Starbases;
    private static Star[] Stars;
    private static Romulan[] Romulans;

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
        ExecLRSCAN();
    }

    public void gameLevel(String levelChoice) {
        // System.out.println("Here is the player choice after passed in:
        // "+levelChoice);
        switch (levelChoice) {
            case "novice":
            case "fair":
            case "good":
            case "expert":
            case "emeritus":
                System.out.print("Please type in a secret password (9 characters maximum)- ");
                break;
            default:
                System.out.println("Invalid choice. Please choose Novice, Fair, Good, Expert, or Emeritus");
                break;
        }
    }

    public void gameType(String gameChoice) {
        switch (gameChoice) {
            case "short":
            case "medium":
            case "long":
                System.out.print("Are you a Novice, Fair, Good, Expert, or Emeritus player? ");
                String playerChoiceOne = scanner.nextLine().trim().toLowerCase();
                System.out.println();
                // System.out.println("Here is the player choice before passed in:
                // "+playerChoice);
                gameLevel(playerChoiceOne);
                break;
            default:
                System.out.println("Invalid choice. Please choose short, medium, or long.");
                break;
        }
    }

    static void initializeGame() {
        Map = new char[8][8][10][10];

        // System.out.println("-SUPER- STAR TREK (JAVA EDITION)\n");
        // System.out.println("Latest update-prolly today\n");
        // System.out.print("Would you like a regular, tournament, or frozen game?");
        // System.out.println(" I'm going to assume regular\n");
        // System.out.print("Would you like a Short, Medium, or Long Game?");
        // System.out.println(" I'm going to assume short");
        // System.out.print("Are you a Novice, Fair, Good, Expert, or Emeritus
        // player?");
        // System.out.println(" Def a novice");
        // System.out.print("Please type in a secret password (9 characters maximum)-");
        // System.out.println("changeit");

        initializeEnterprise();
        initializePlanets(30);
        initializeKlingons(3);
        initializeStarbases(4);
        initializeStars(300);
        initializeRomulans(4);

        // System.out.println("\n\n\nIt is stardate " + Enterprise.getStarDate()
        // + ". The Federation is being attacked by a deadly Klingon invasion force. As
        // captain of the United Starship "
        // + "U.S.S. Enterprise, it is your mission to seek out and destroy this
        // invasion force of "
        // + Klingons.length + " battle cruisers. You have an initial allotment of " +
        // "7"
        // + " stardates to complete your mission. As you proceed you may be given more
        // time.\n");
        // System.out.println("You will have " + "x"
        // + "supporting starbases. Starbase locations- " +
        // turnEntityQuadrantsToStrings(Starbases) + "\nThe Enterprise is currently in
        // Quadrant "
        // + (Enterprise.getPosition().getQuadrant().getX() + 1) + " - " +
        // (Enterprise.getPosition().getQuadrant().getY() + 1)
        // + " Sector " + (Enterprise.getPosition().getSector().getX() + 1) + " - "
        // + (Enterprise.getPosition().getSector().getY() + 1) + "\n\nGood Luck!\n\n");

        updateMap();
    }

    static String turnEntityQuadrantsToStrings(Entity[] entities) {
        String locs = "";
        for (Entity entity : entities) {
            locs += (entity.getPosition().getQuadrant().getX() + 1) + " - "
                    + (entity.getPosition().getQuadrant().getY() + 1) + "  ";
        }
        return locs;
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
                            Map[j][i][l][k] = Klingons[0].getSymbol();
                        } else if (checkEntityListAgainstPosition(position, Planets)) {
                            Map[j][i][l][k] = Planets[0].getSymbol();
                        } else if (checkEntityAgainstPosition(position, Enterprise)) {
                            Map[j][i][l][k] = Enterprise.getSymbol();
                        } else if (checkEntityListAgainstPosition(position, Starbases)) {
                            Map[j][i][l][k] = Starbases[0].getSymbol();
                        } else if (checkEntityListAgainstPosition(position, Stars)) {
                            Map[j][i][l][k] = Stars[0].getSymbol();
                        } else if (checkEntityListAgainstPosition(position, Romulans)) {
                            Map[j][i][l][k] = Romulans[0].getSymbol();
                        } else {
                            Map[j][i][l][k] = NOTHING;
                        }
                    }
                }
            }
        }
    }

    static void initializeEnterprise() {
        Position pos = generateNewPosition(null, 9);
        Enterprise = new Enterprise(pos);
    }

    static void initializeRomulans(int numberOfRomulans) {
        Romulans = new Romulan[numberOfRomulans];
        for (int i = 0; i < numberOfRomulans; i++) {
            Position pos = generateNewPosition(Romulans, 9);
            Romulans[i] = new Romulan(pos);
        }
    }

    static void initializeStars(int numberOfStars) {
        Stars = new Star[numberOfStars];
        for (int i = 0; i < numberOfStars; i++) {
            Position pos = generateNewPosition(Stars, 9);

            Stars[i] = new Star(pos);
        }
    }

    static void initializeStarbases(int numberOfStarbases) {
        Starbases = new Starbase[numberOfStarbases];
        for (int i = 0; i < numberOfStarbases; i++) {
            Position pos = generateNewPosition(Starbases, 9);

            Starbases[i] = new Starbase(pos);
        }
    }

    static void initializePlanets(int numberOfPlanets) {
        Planets = new Planet[numberOfPlanets];
        for (int i = 0; i < numberOfPlanets; i++) {
            Position pos = generateNewPosition(Planets, 9);
            Planets[i] = new Planet(pos);
        }
    }

    static void initializeKlingons(int numberOfKlingons) {
        Klingons = new Klingon[numberOfKlingons];
        for (int i = 0; i < numberOfKlingons; i++) {
            Position pos = generateNewPosition(Klingons, 9);
            Klingons[i] = new Klingon(pos);
            System.out.println(pos.getQuadrant().getX() +" - " + pos.getQuadrant().getY() + ", " + pos.getSector().getX() + " - " + pos.getSector().getY());
        }
    }

    static Position generateNewPosition(Entity[] entities, int maxElementsInQuadrant) {
        Coordinate quadrant = new Coordinate(Utils.randInt(0, 7), (Utils.randInt(0, 7)));
        Coordinate sector = new Coordinate(Utils.randInt(0, 9), (Utils.randInt(0, 9)));
        Position position = new Position(quadrant, sector);
        while (!isPositionEmpty(position) && entities != null && getNumberOfEntiesBeforeMapUpdate(entities, position) <= maxElementsInQuadrant) {
            position = generateNewPosition(entities, maxElementsInQuadrant);
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
                case LRSCAN:
                    ExecLRSCAN();
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

    static void ExecLRSCAN() {
        int row = Enterprise.getPosition().getQuadrant().getX();
        int column = Enterprise.getPosition().getQuadrant().getY();
        con.printf("\nLong-range scan for Quadrant %d - %d:\n", row + 1, column + 1);
        con.printf("%-5d%-5d%-5d\n", getQuadrantNumber(row-1, column-1), getQuadrantNumber(row, column-1), getQuadrantNumber(row+1, column-1));
        con.printf("%-5d%-5d%-5d\n", getQuadrantNumber(row-1, column), getQuadrantNumber(row, column), getQuadrantNumber(row+1, column));
        con.printf("%-5d%-5d%-5d\n", getQuadrantNumber(row-1, column+1), getQuadrantNumber(row, column+1), getQuadrantNumber(row+1, column+1));

    }

    static int getQuadrantNumber(int row, int column) {
        // int thousands = getNumberOfEntiesInMapQuadrant(row, column, Supernova); TODO: add supernova
        if(row < 0 || row >= 8 || column < 0 || column >= 8) {
            return -1;
        }
        int thousands = 0;
        // System.out.println(row + " " + column);
        int hundreds  = getNumberOfEntiesInMapQuadrant(row, column, 'K')*100;
        int tens = getNumberOfEntiesInMapQuadrant(row, column, 'B')*10;
        int ones = getNumberOfEntiesInMapQuadrant(row, column, '*');

        return  thousands + hundreds + tens + ones;
    }

    static int getNumberOfEntiesInMapQuadrant(int row, int column, char entity) {
        int numberOfElements = 0;
        for(int i = 0; i < Map[row][column].length; i++){
            for(int j = 0; j < Map[row][column][i].length; j++){
                if(Map[row][column][i][j] == entity) {
                    numberOfElements += 1;
                }
            }
        }
        return numberOfElements;
    }

    static int getNumberOfEntiesBeforeMapUpdate(Entity[] entities, Position position) {
        // function to get number of entities in a qudrant before a map update, used in iniitialzation to make
        // sure a quadrant doesn't get too many of an entity type
        int numberOfElements = 0;
        for(int i = 0; i < entities.length; i++){
            if(entities[i] != null && entities[i].getPosition().getQuadrant().getX() == position.getQuadrant().getX() &&
            entities[i].getPosition().getQuadrant().getY() == position.getQuadrant().getY()) {
                numberOfElements += 1;
            }
        }
        return numberOfElements;
    }

    static void ExecSRSCAN() {

        int row = Enterprise.getPosition().getQuadrant().getX();
        int column = Enterprise.getPosition().getQuadrant().getY();
        int r, c;

        con.printf("\nShort-range scan:\n\n");
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
                        con.printf("Stardate      %.1f", Enterprise.getStarDate());
                        break;
                    case 2:
                        con.printf("Condition     %s", Enterprise.getCondition());
                        break;
                    case 3:
                        con.printf("Position      %d - %d, %d - %d",
                                (Enterprise.getPosition().getQuadrant().getX() + 1),
                                                (Enterprise.getPosition().getQuadrant().getY() + 1), (Enterprise.getPosition().getSector().getX() + 1),
                                                (Enterprise.getPosition().getSector().getY() + 1));
                                        
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
    public static void ExecSTATUS() {
        System.out.println("Stardate\t" + Enterprise.getStarDate());
        System.out.println("Condition\t" + Enterprise.getCondition());
        System.out.println("Position\t" + (Enterprise.getPosition().getQuadrant().getX() + 1) + " - "
                + (Enterprise.getPosition().getQuadrant().getY() + 1) + ", "
                + (Enterprise.getPosition().getSector().getX() + 1) + " - "
                + (Enterprise.getPosition().getSector().getY() + 1));
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
