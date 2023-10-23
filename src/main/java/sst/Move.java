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
     * Moves player in the map
     */
    public void ExecMOVE(List<String> params) {
        Enterprise enterprise = game.getEnterprise();
        Computer computer = new Computer(this.game);
        MoveType moveType = MoveType.undefined;
        String cmdstr;

        System.out.println(Arrays.toString(params.toArray()));

        if(params.size() == 0) {
            //promp for movement type
            cmdstr = this.game.con.readLine("Manual or automatic- ");
            params = readCommands(cmdstr).orElse(null);
        }

        if (params.size() > 0) {
            String moveTypeT = params.get(0);
            moveType = matchMoveType(moveTypeT);

            switch (moveType) {
                case MANUAL:
                    manualMove(params);
                    break;
                case AUTOMATIC:
                    automaticMove(params);
                    break;
                default:
                    this.game.con.printf("\nBeg your pardon, Captain?\n");
                    break;
            }
        }

    }

    private void manualMove(List<String> params){
        this.game.con.printf("manual\n");
        Double xOffset = 0.0;
        Double yOffset = 0.0;

        List<Double> offsets;

        if(!parseDoubles(params.get(0)).isEmpty()){
            this.game.con.printf("assumed manual\n");
        }
        
    }
    
    private void automaticMove(List<String> params){
        this.game.con.printf("automatic");
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
        if(mt.equals(MoveType.undefined) && !parseDoubles(cmdstr).isEmpty()) {
            return MoveType.MANUAL;
        }
        return mt;
    }

}
