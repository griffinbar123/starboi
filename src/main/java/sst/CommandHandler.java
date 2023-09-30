package sst;

import java.io.Console;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CommandHandler {
    private static String cmdstr;
    @NonNull
    private Console con;
    @NonNull
    private Game game;

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

    public void getAndExecuteCommands() {
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
                    new SrScan(this.game).ExecSRSCAN();;
                    break;
                case COMMANDS:
                    new Commands().ExecCOMMANDS();
                    break;
                case STATUS:
                    new Status(this.game).ExecSTATUS();
                    break;
                case LRSCAN:
                    new LrScan(this.game).ExecLRSCAN();
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
}
