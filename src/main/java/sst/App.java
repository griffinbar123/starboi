package sst;
import java.io.Console;
import java.util.Random;

import main.java.sst.Coordinate;
import main.java.sst.Planet;
import main.java.sst.Klingon;
import main.java.sst.Enterprise;
import main.java.sst.Position;
import main.java.sst.Entity;

/**
 * Hello world!
 *
 */


public class App 
{

    // public static final char STAR = '*';
    // public static final char ENTERPRISE = 'E';
    // public static final char KLINGON = 'K';
    // public static final char STARBASE = 'B';
    // public static final char KLINGON_COMMANDER = 'C';
    // public static final char KLINGON_SUPER_COMMNANDER = 'S';
    // public static final char ROMULAN = 'R';
    // public static final char PLANET = 'P';
    public static final char NOTHING = '\u00B7';
    // public static final char BLACK_HOLE = ' ';

    private static String cmdstr;
    private static Console con;
    private static char[][][][] Map;
    private static Klingon[] Klingons;
    private static Enterprise Enterprise;
    private static Planet[] Planets;

    enum Command
    {
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

        Command()
            {
            canAbbrev = true;
            }

        Command(boolean abrOk)
            {
            canAbbrev = abrOk;   
            }

        public boolean CanAbbrev() { return canAbbrev; }
    }

    public static void main(String[] args)   
    {

        con = System.console();
        if (con == null)
            return;

        // getAndExecuteCommands();
        initalizeMap();
    }

    static void initalizeMap(){
        Map = new char[8][8][10][10];

        initializeEnterprise();
        initializeKlingons(3);
    

        updateMap();
        ExecSRSCAN();

    }

    static void updateMap(){
        for(int i = 0; i < Map.length; i++) {
            for(int j = 0; j < Map[i].length; j++) {
                for(int k = 0; k < Map[i][j].length; k++) {
                    for(int l = 0; l < Map[i][j][k].length; l++) {
                        Position position = new Position(new Coordinate(i, j), new Coordinate(k, l));
                        // check if positions is a 
                        if(checkEntityListAgainstPosition(position, Klingons)) {
                            Map[i][j][k][l] = Klingons[0].Symbol;
                        } else if (checkEntityAgainstPosition(position, Enterprise)) {
                            Map[i][j][k][l] = Enterprise.Symbol;
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

        System.out.println("Enterprise Position: " + pos.Quadrant.X + ", " + pos.Quadrant.Y + " - " + pos.Sector.X + ", " + pos.Sector.Y); 

        Enterprise = new Enterprise(pos);
        System.out.println(" ");
    }

    static void initializePlanets(int numberOfPlanets){
        Planets = new Planet[numberOfPlanets];
        for(int i = 0; i < numberOfPlanets; i++){
            Position pos = generateNewPosition();

            System.out.println("Planets Positions: " + pos.Quadrant.X + ", " + pos.Quadrant.Y + " - " + pos.Sector.X + ", " + pos.Sector.Y); 
            // make sure position is not being used by another klingon
            Planets[i] = new Planets(pos);
        }
        System.out.println(" ");
    }

    static void initializeKlingons(int numberOfKlingons){
        Klingons = new Klingon[numberOfKlingons];
        for(int i = 0; i < numberOfKlingons; i++){
            Position pos = generateNewPosition();
            
            System.out.println("Klingon Positions: " + pos.Quadrant.X + ", " + pos.Quadrant.Y + " - " + pos.Sector.X + ", " + pos.Sector.Y); 
            // make sure position is not being used by another klingon
            Klingons[i] = new Klingon(pos);
        }
        System.out.println(" ");
    }

    static Position generateNewPosition(){
        Coordinate quadrant = new Coordinate(generateRandomNumber(8), (generateRandomNumber(8)));
        Coordinate sector = new Coordinate(generateRandomNumber(10), (generateRandomNumber(10)));
        Position position = new Position(quadrant, sector);
        while(!isPositionEmpty(position)) {
            position = generateNewPosition();
        }
        return position;
    }

    static Boolean isPositionEmpty(Position position) {
        // use this to check if a position is empty, but can't check the map because it might not be updated
        return !(checkEntityAgainstPosition(position, Enterprise) || checkEntityListAgainstPosition(position, Klingons));
    }

    static Boolean checkEntityListAgainstPosition(Position position, Entity[] entities){
        // checks if any entity in the list provided is is in the provided position

        for(int i = 0; i < entities.length; i++){
            if(checkEntityAgainstPosition(position, entities[i])){
                return true;
            }
        }
        return false;
    }

    static Boolean checkEntityAgainstPosition(Position position, Entity entity) {
        // checks if entity is in a position
        return entity != null && entity.Position.Quadrant.X == position.Quadrant.X && entity.Position.Quadrant.Y == position.Quadrant.Y && 
            entity.Position.Sector.X == position.Sector.X && entity.Position.Sector.Y == position.Sector.Y;
    }

    static void getAndExecuteCommands() {
        // main polling loop
        String str;

        con.printf("\n            SUPER STAR TREK (Java Edition)\n");
        con.printf("\n *** Welcome aboard the USS Enterprise (NCC 1701) *** \n\n");

        while (true)
            {
            str = con.readLine("COMMAND> ");
            cmdstr = str.toUpperCase().trim();

            Command c = Command.undefined;

            for (Command cx : Command.values())
                {
                boolean Matched;

                if (cx.CanAbbrev())
                    {
                    String cmd = cx.toString();

                    int cmdlen = cmd.length();
                    int tstlen = cmdstr.length();
                
                    String abrcheck = cmd.substring(0,Math.min(cmdlen,tstlen));

                    Matched = cmdstr.compareTo(abrcheck) == 0;
                    }
                else
                    Matched = cmdstr.compareTo(cx.toString()) == 0;

                if (Matched)
                    {
                    c = cx;
                    break;                    
                    }
                }

            switch(c)
                {
                case SRSCAN:
                    ExecSRSCAN();
                    break;

                case COMMANDS:
                    ExecCOMMANDS();
                    break;

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

    static void ExecCOMMANDS()
        {
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

    static void ExecSRSCAN()
        {

            int row = Enterprise.Position.Quadrant.X;
            int column = Enterprise.Position.Quadrant.Y;
            int r, c;

            con.printf("\nShort-range scan (MOCK):\n\n");
            boolean leftside = true;
            boolean rightside = true;

            // print column header
            con.printf("        ");
            for (c = 1; c <= 10; c++)
                con.printf("%1d ", c);
            con.printf("\n");

            for (r = 1; r <= 10; r++)
                {
                if (leftside)
                    {
                    con.printf("    %2d  ", r);
                    
                    for (c = 1; c <= 10; c++)
                        con.printf("%c ", Map[row][column][r-1][c-1]);
                    }

                if (rightside)
                    {
                    con.printf(" ");

                    switch (r)   
                        {
                        case  1:  con.printf("Stardate      %.1f", 2516.3);                          break;
                        case  2:  con.printf("Condition     %s", "RED");                             break;
                        case  3:  con.printf("Position      %d - %d, %d - %d", 5, 1, 2, 4);          break;
                        case  4:  con.printf("Life Support  %s", "DAMAGED, Reserves = 2.30");        break;
                        case  5:  con.printf("Warp Factor   %.1f", 5.0);                             break;
                        case  6:  con.printf("Energy        %.2f", 2176.24);                         break;
                        case  7:  con.printf("Torpedoes     %d", 3);                                 break;
                        case  8:  con.printf("Shields       %s, %d%% %.1f units", "UP", 42, 1050.0); break;
                        case  9:  con.printf("Klingons Left %d", 12);                                break;
                        case 10:  con.printf("Time Left     %.2f", 3.72);                            break;
                        }
                    }

                con.printf("\n");
                }

            con.printf("\n");
        }

        private static int generateRandomNumber(int large) {
            Random rand = new Random();
    
            // Generate random integers in range 0 to 999
            return rand.nextInt(large);
        }

    }
