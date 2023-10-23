package sst;

import java.util.List;
import Model.Game;
import lombok.AllArgsConstructor;
import static Utils.Utils.readCommands;

/**
 * This class contains the main game loop in getAndExecuteCommands(). Also
 * contains functionality for parsing the command line
 */
@AllArgsConstructor // used for testing
public class CommandHandler {
    private Game game;
    private SrScan srScan;
    private LrScan lrScan;
    private Status status;
    private Computer computer;
    private Chart chart;
    private Freeze freeze;
    private Help help;
    private Damages damages;

    public CommandHandler(Game game) {
        this.game = game;
        this.srScan = new SrScan(game);
        this.lrScan = new LrScan(game);
        this.status = new Status(game);
        this.computer = new Computer(game);
        this.chart = new Chart(game);
        this.freeze = new Freeze(game);
        this.help = new Help(game, this);
        this.damages = new Damages(game);
    }

    public enum Command {
        SRSCAN,
        MOVE,
        PHASERS,
        CALL(false),
        STATUS,
        IMPULSE,
        PHOTONS,
        ABANDON(false),
        LRSCAN,
        WARP,
        SHIELDS,
        DESTRUCT(false),
        CHART,
        REST,
        DOCK,
        QUIT(false),
        DAMAGES,
        REPORT,
        SENSORS,
        ORBIT,
        TRANSPORT,
        MINE,
        CRYSTALS,
        SHUTTLE,
        PLANETS,
        REQUEST,
        DEATHRAY(false),
        FREEZE(false),
        COMPUTER,
        EMEXIT,
        PROBE,
        COMMANDS,
        SCORE,
        CLOAK,
        CAPTURE,
        HELP(false),
        // DEBUG(false),
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

    /**
     * Contains the main game loop. Reads commands from console and directs them
     * (and their parameters) to their handler methods.
     */
    public void getAndExecuteCommands() {
        Command c = Command.undefined;
        List<String> params;
        String cmdstr, cmd;

        this.game.con.printf("\n            SUPER STAR TREK (Java Edition)\n");
        this.game.con.printf("\n *** Welcome aboard the USS Enterprise (NCC 1701) *** \n\n");

        while (true) {
            this.game.con.printf("\n");
            cmdstr = this.game.con.readLine("COMMAND> ");
            params = readCommands(cmdstr).orElse(null);

            if (params != null && params.size() > 0) {
                cmd = params.remove(0);
                c = matchCommand(cmd);
            } else {
                c = Command.undefined;
            }

<<<<<<< HEAD
                switch (c) {
                    case SRSCAN:
                        new SrScan(this.game).ExecSRSCAN();
                        break;
                    case COMMANDS:
                        new Commands(this.game).ExecCOMMANDS();
                        break;
                    case STATUS:
                        new Status(this.game).ExecSTATUS();
                        break;
                    case LRSCAN:
                        new LrScan(this.game).ExecLRSCAN();
                        break;
                    case COMPUTER:
                        new Computer(this.game).ExecCOMPUTER(params);
                        break;
                    case CHART:
                        new Chart(this.game).ExecCHART();
                        break;
                    case MOVE:
                        new Move(this.game).ExecMOVE(params);
                        break;
                    case QUIT:
                        return;
                    case FREEZE:
                        new Freeze(this.game).ExecFREEZE();
                        return;
                    case undefined:
                        this.game.con.printf("'%s' is not a valid command.\n\n", cmdstr);
                        break;
                    default:
                        this.game.con.printf("Lt. Cmdr. Scott: \"Captain, '%s' is nae yet operational.\"\n\n",
                                c.toString());
                        break;
                }
=======
            switch (c) {
                case SRSCAN:
                    srScan.ExecSRSCAN();
                    break;
                case COMMANDS:
                    // COMMANDS functionality now handled in Help.java
                    help.printValidCommands();
                    break;
                case STATUS:
                    status.ExecSTATUS();
                    break;
                case LRSCAN:
                    lrScan.ExecLRSCAN();
                    break;
                case COMPUTER:
                    computer.ExecCOMPUTER(params);
                    break;
                case CHART:
                    chart.ExecCHART();
                    break;
                case QUIT:
                    return;
                case FREEZE:
                    freeze.ExecFREEZE();
                    return;
                case HELP:
                    help.ExecHELP(params);
                    break;
                case DAMAGES:
                    damages.ExecDAMAGES();
                    break;
                case undefined:
                    help.printValidCommands();
                    break;
                default:
                    this.game.con.printf("Lt. Cmdr. Scott: \"Captain, '%s' is nae yet operational.\"\n\n",
                            c.toString());
                    break;
>>>>>>> 19c0ebdbb522ea301dc02c8c4b5a65151523f024
            }
        }
    }

    public Command matchCommand(String cmdstr) {
        Command c = Command.undefined;
        boolean matched = false;
        int cmdlen = 0;
        int tstlen = 0;

        for (Command cx : Command.values()) {

            if (cx.CanAbbrev()) {
                String cmd = cx.toString();

                cmdlen = cmd.length();
                tstlen = cmdstr.length();

                String abrcheck = cmd.substring(0, Math.min(cmdlen, tstlen));

                matched = cmdstr.compareTo(abrcheck) == 0;
            } else
                matched = cmdstr.compareTo(cx.toString()) == 0;

            if (matched) {
                c = cx;
                break;
            }
        }

        return c;
    }
}
