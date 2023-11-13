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
    private Rest rest;
    // private Finish finish;
    private Attack attack;

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
        this.rest = new Rest(game);
        // this.finish = new Finish(game);
        this.attack = new Attack(game);
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

        Double originalStarDate; // used to see if stardate changes by a command

        while (true) {
            game.setReadyForHit(false);
            game.setJustEnteredQuadrant(false);
            originalStarDate = game.getStarDate();
            cmdstr = this.game.con.readLine("\nCOMMAND> ");
            params = readCommands(cmdstr).orElse(new ArrayList<>());

            if (params.size() > 0) {
                cmd = params.remove(0);
                c = matchCommand(cmd);
            } else {
                c = Command.undefined;
            }
            if(!game.getIsOver()) {

                switch (c) {
                    case SRSCAN:
                        srScan.ExecSRSCAN();
                        break;
                    case COMMANDS:
                        help.ExecCOMMANDS();
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
                    case REST:
                        rest.ExecREST(params);
                        break;
                    case SCORE:
                        score.ExecSCORE(false, false);
                        break;
                    case undefined:
                        help.ExecCOMMANDS();
                        break;
                    default:
                        this.game.con.printf("Lt. Cmdr. Scott: \"Captain, '%s' is nae yet operational.\"\n\n",
                                c.toString());
                        break;
                }
            }
            while(true) { // this is how the original game does it. look at sst.c
                if(game.getIsOver()) break;

                if(game.getStarDate() > originalStarDate) {
                    //TODO: do events, and check if anyof them caused the game to end
                    if(game.getIsOver()) break;
                }
                /* og game code i havent implemented
                if (d.galaxy[quadx][quady] == 1000) { // Galaxy went Nova!
                    atover(0);
                    continue;
                }
                if (nenhere == 0) movetho();
                */
                // game.con.printf("\nhit: %b, NotJustEntered: %b\n", game.getReadyForHit(), !game.getJustEnteredQuadrant());
                if(game.getReadyForHit() && !game.getJustEnteredQuadrant()) {
                    // game.con.printf("\nattacking\n");
                    //attack player
                    attack.klingonAttack(2);
                    if(game.getIsOver()) break;
                    //TODO: check if galaxy nova'd and attack player if so and continue loop
                }
                break;
            }
            if(game.getIsOver()) break;
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
