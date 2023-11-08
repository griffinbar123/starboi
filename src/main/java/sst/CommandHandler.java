package sst;

import java.util.ArrayList;
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
    private Move move;
    private Photon photon;
    private Score score;
    private Dock dock;

    public CommandHandler(Game game) {
        this.game = game;
        this.srScan = new SrScan(game);
        this.lrScan = new LrScan(game);
        this.status = new Status(game);
        this.computer = new Computer(game);
        this.chart = new Chart(game);
        this.freeze = new Freeze(game);
        this.help = new Help(game, this, "sst.doc");
        this.damages = new Damages(game);
        this.move = new Move(game);
        this.photon = new Photon(game);
        this.score = new Score(game);
        this.dock = new Dock(game);
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
            cmdstr = this.game.con.readLine("\nCOMMAND> ");
            params = readCommands(cmdstr).orElse(new ArrayList<>());

            if (params.size() > 0) {
                cmd = params.remove(0);
                c = matchCommand(cmd);
            } else {
                c = Command.undefined;
            }

            switch (c) {
                case SRSCAN:
                    srScan.ExecSRSCAN();
                    break;
                case COMMANDS:
                    // COMMANDS functionality now handled in Help.java
                    help.printValidCommands();
                    break;
                case DOCK:
                    dock.ExecDOCK();
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
                case MOVE:
                    move.ExecMOVE(params);
                    break;
                case PHOTONS:
                    photon.ExecPHOTON(params);
                    break;
                case SCORE:
                    score.ExecSCORE(false, false);
                    break;
                case undefined:
                    help.printValidCommands();
                    break;
                default:
                    this.game.con.printf("Lt. Cmdr. Scott: \"Captain, '%s' is nae yet operational.\"\n\n",
                            c.toString());
                    break;
            }
        }
    }

    /**
     * Matches a string to a Command enum
     * @param cmdstr
     * @return the command indicated from the cmdstr
     */
    public Command matchCommand(String cmdstr) {
        Command c = Command.undefined;
        boolean matched = false;
        int cmdlen = 0;
        int tstlen = 0;
        cmdstr = cmdstr.trim();

        if (cmdstr.length() == 0)
            return c;

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
