package sst;

import java.util.List;

import Model.Coordinate;
import Model.Game;
import Model.Position;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import static Utils.Utils.readCommands;
import static Utils.Utils.parseIntegers;
import static Utils.Utils.parseDoubles;
import static Utils.Utils.positionsAreEqual;

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
        MoveType moveType = MoveType.undefined;
        String cmdstr;

        if (params.size() == 0) {
            // prompt for movement type
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
                    this.game.begPardon();
                    break;
            }
        }

    }

    private void automaticMove(List<String> params) {
        params.remove(0);

        while (params == null || params.size() == 0) {
            String displacements = this.game.con.readLine("Destination sector or quadrant&sector- ");
            params = readCommands(displacements).orElse(null);
        }

        List<Integer> offsets = parseIntegers(params);

        if ((offsets.size() != 2 && offsets.size() != 4) ||
                (offsets.size() == 2
                        && ((offsets.get(0) > 10) || offsets.get(0) < 1 || offsets.get(1) > 10 || offsets.get(1) < 1))
                ||
                (offsets.size() == 4 && ((offsets.get(0) > 8) || offsets.get(0) < 1 || offsets.get(1) > 8
                        || offsets.get(1) < 1 || offsets.get(2) > 10 || offsets.get(2) < 1 || offsets.get(3) > 10
                        || offsets.get(3) < 1))) {
            this.game.begPardon();
            return;
        }

        Coordinate quad = offsets.size() == 2 ? this.game.getEnterprise().getPosition().getQuadrant()
                : new Coordinate(offsets.get(0) - 1, offsets.get(1) - 1);
        Coordinate sect = offsets.size() == 2 ? new Coordinate(offsets.get(0) - 1, offsets.get(1) - 1)
                : new Coordinate(offsets.get(2) - 1, offsets.get(3) - 1);

        if(offsets.size() == 4)
            this.game.con.printf("\nEnsign Chekov- \"Course laid in, Captain.\"\n");

        Position newPosition = new Position(quad, sect);
        Position movedPos = moveToPosition(this.game.getEnterprise().getPosition(), newPosition);

        adjustStats(movedPos);
        this.game.getEnterprise().setPosition(movedPos);
        this.game.updateMap();
    }

    private void manualMove(List<String> params) {
        if (parseDoubles(params.get(0)).isEmpty())
            params.remove(0);

        while (params == null || params.size() == 0) {
            String displacements = this.game.con.readLine("X and Y displacements- ");
            params = readCommands(displacements).orElse(null);
        }

        List<Double> offsets = parseDoubles(params);
        if (offsets.size() == 0) {
            this.game.begPardon();
            return;
        }

        int yOffset = offsets.size() == 1 ? 0 : (int) ((double) offsets.get(1) * 10);
        int xOffset = (int) ((double) offsets.get(0) * 10);

        this.game.con.printf("\nHelmsman Sulu- \"Aye, Sir.\"\n");


        Position newPosition = this.game.getEnterprise().getPosition().getPositionFromOffset(xOffset, yOffset * -1);
        Position movedPos = moveToPosition(this.game.getEnterprise().getPosition(), newPosition);

        adjustStats(movedPos);
        this.game.getEnterprise().setPosition(movedPos);
        this.game.updateMap();

    }

    private void adjustStats(Position dest) {
        Computer computer = new Computer(this.game);

        double powerNeeded = computer.calcPower(this.game.getEnterprise().getPosition(), dest);
        double timeNeeded = computer.calcTime(this.game.getEnterprise().getPosition(), dest);
        this.game.setStarDate(this.game.getStarDate() - timeNeeded);
        this.game.setTime(this.game.getTime() - timeNeeded);
        this.game.getEnterprise().setEnergy(this.game.getEnterprise().getEnergy() - powerNeeded);
    }

    private Position moveToPosition(Position curPos, Position destPos) {
        Position nextPos = curPos.getClosestAdjecentPositionToDestination(destPos);

        Coordinate nq = nextPos.getQuadrant();
        Coordinate ns = nextPos.getSector();

        if (nq.getY() >= 8 || nq.getY() < 0 || nq.getX() >= 8 || nq.getX() < 0) {
            this.game.con.printf(
                    "\nYOU HAVE ATTEMPTED TO CROSS THE NEGATIVE ENERGY BARRIER\nAT THE EDGE OF THE GALAXY.  THE THIRD TIME YOU TRY THIS,\nYOU WILL BE DESTROYED.\n");

            return curPos;
        }

        if (this.game.getMap()[nq.getY()][nq.getX()][ns.getY()][ns.getX()] != Game.NOTHING) {
            this.game.con.printf(
                    "\nEnterprise blocked by object at Sector %d - %d\nEmergency stop required 125.00 units of energy.\n",
                    ns.getY(), ns.getX());
            this.game.getEnterprise().setEnergy(this.game.getEnterprise().getEnergy() - 125.00);
            return curPos;
        }
        if (positionsAreEqual(destPos, nextPos)) {
            return nextPos;
        }
        return moveToPosition(nextPos, destPos);
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
        if (mt.equals(MoveType.undefined) && !parseDoubles(cmdstr).isEmpty()) {
            return MoveType.MANUAL;
        }
        return mt;
    }

}
