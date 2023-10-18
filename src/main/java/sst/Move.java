package sst;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import Model.Coordinate;
import Model.Enterprise;
import Model.Game;
import Model.Position;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import sst.Chart;
import sst.Commands;
import sst.Computer;
import sst.Freeze;
import sst.LrScan;
import sst.SrScan;
import sst.Status;
import static Utils.Utils.readCommands;
import static Utils.Utils.parseIntegers;
import static Utils.Utils.parseDoubles;

/**
 * Handles the move command
 * 
 * @author Griffin Barnard
 */
@RequiredArgsConstructor
public class Move {
    @NonNull
    private Game game;

    private enum MoveType {
        MANUAL,
        AUTOMATIC,
        undefined;
    }

    /**
     * Moves playernin the map
     */
    public void ExecMOVE(List<String> params) {
        Enterprise enterprise = game.getEnterprise();
        Computer computer = new Computer(this.game);
        MoveType moveType = MoveType.undefined;
        String cmdstr;
        List<String> paramst;

        //promp for movement type
        cmdstr = this.game.con.readLine("Manual or automatic- ");
        paramst = readCommands(cmdstr).orElse(null);

        System.out.println(Arrays.toString(params.toArray()));

        if (paramst.size() > 0) {
                String moveTypeT = paramst.get(0);
                moveType = matchMoveType(moveTypeT);

                switch (moveType) {
                    case MANUAL:
                        new SrScan(this.game).ExecSRSCAN();
                        break;
                    case AUTOMATIC:
                        new Commands(this.game).ExecCOMMANDS();
                        break;
                    default:
                        this.game.con.printf("\n\nBeg your pardon, Captain?\n\n");
                        break;
                }
            }

        Position dest = new Position(null, null);
        dest = computer.readCorodinates(paramst).orElse(null);
        if (dest == null) {
            this.game.con.printf("\n\nBeg your pardon, Captain?\n\n");
            return;
        }
    }

     /**
     * Establish destination coordinates for cacluations
     * @param params list of parameters from command line (may be empty)
     * @return Optional containing destination coordinates or empty Optional if no coordinates were found
     * @author Matthias Schrock
     */
    private Optional<Position> readMoveInputs(List<String> params) {
        Coordinate sect = null;
        Coordinate quad = null;
        List<String> cmd;
        List<Integer> cord;
        String cmdStr;

        if (params.size() < 2) {
            cmdStr = this.game.con.readLine("Destination quadrant and/or sector? ");
            cmd = readCommands(cmdStr).orElse(null);

            cord = parseIntegers(cmd);
        } else {
            cord = parseIntegers(params);
        }

        switch (cord.size()) {
            case 4:
                quad = new Coordinate(cord.get(1), cord.get(0));
                sect = new Coordinate(cord.get(3), cord.get(2));
                break;
            case 2:
                quad = new Coordinate(this.game.getEnterprise().getPosition().getQuadrant().getY(),
                        this.game.getEnterprise().getPosition().getQuadrant().getX());
                sect = new Coordinate(cord.get(1), cord.get(0));
                break;
            default:
                this.game.con.printf("\n\nBeg your pardon, Captain?\n\n");
                return Optional.empty();
        }

        return Optional.ofNullable(new Position(quad, sect));
    }

    private MoveType matchMoveType(String cmdstr) {
        MoveType mt = MoveType.undefined;
        boolean matched = false;
        int cmdlen = 0;
        int tstlen = 0;

        for (MoveType ml : MoveType.values()) {

            String cmd = ml.toString();

            cmdlen = cmd.length();
            tstlen = cmdstr.length();

            String abrcheck = cmd.substring(0, Math.min(cmdlen, tstlen));

            matched = cmdstr.compareTo(abrcheck) == 0;
            

            if (matched) {
                mt = ml;
                break;
            }
        }
        return mt;
    }

}
