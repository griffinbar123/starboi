package sst;
import java.io.Console;

/**
 * Hello world!
 *
 */
public class App 
{
   private static String cmdstr;
    private static Console con;

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
        // main polling loop
        String str;

        con = System.console();
        if (con == null)
            return;

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
        int r,c;

        char[][] chScan = new char[10][10]; // minor inefficiency here

        for (r = 1; r <= 10; r++)
            for (c = 1; c <= 10; c++)
                {
//                chScan[r-1][c-1] = '.';
                chScan[r-1][c-1] = '\u00B7';
                }

        // MOCK
        chScan[0][0] = '*';
        chScan[0][5] = 'R';
        chScan[1][3] = 'E';
        chScan[2][5] = '*';
        chScan[2][7] = 'B';
        chScan[3][3] = 'S';
        chScan[4][7] = 'K';
        chScan[5][1] = 'K';
        chScan[5][3] = ' ';
        chScan[5][8] = '*';
        chScan[6][5] = 'P';
        chScan[7][4] = '*';
        chScan[8][1] = '*';
        chScan[8][4] = '*';
        chScan[8][8] = 'C';

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
                    con.printf("%c ", chScan[r-1][c-1]);
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

    }
