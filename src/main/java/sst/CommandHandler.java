package sst;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import Model.Game;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CommandHandler {
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
        Command c = Command.undefined;
        List<String> params;
        String cmdstr, cmd;

        this.game.con.printf("\n            SUPER STAR TREK (Java Edition)\n");
        this.game.con.printf("\n *** Welcome aboard the USS Enterprise (NCC 1701) *** \n\n");

        while (true) {
            this.game.con.printf("\n");
            cmdstr = this.game.con.readLine("COMMAND> ");
            params = readCommands(cmdstr).orElse(null);

            if (params.size() > 0) {
                cmd = params.get(0);
                c = matchCommand(cmd);

                executeCommand(cmdstr, c, params.subList(1, params.size()));
            }
        }
    }

    private void executeCommand(String cmdstr, Command c, List<String> params) {
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
    }

    private Command matchCommand(String cmdstr) {
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

        return c;
    }

    public Optional<List<String>> readCommands(String cmd) {
        return Optional.ofNullable(Stream.of(cmd.split("[\\s\\p{Punct}]"))
                .filter(s -> !s.isEmpty())
                .map(String::trim)
                .map(String::toUpperCase)
                .toList());
    }

    public Optional<List<Integer>> readIntegers(String cmd) {
        return Optional.ofNullable(Stream.of(cmd.split("[\\s\\p{Punct}]"))
                .filter(s -> s.matches("\\d+"))
                .map(Integer::valueOf)
                .toList());
    }
}
