package sst;

import java.util.List;

import Model.Coordinate;
import Model.EntityType;
import Model.Game;
import Model.Position;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import sst.Finish.GameOverReason;

import static Utils.Utils.readCommands;
import static Utils.Utils.parseIntegers;
import static Utils.Utils.isEqual;
import static Utils.Utils.parseDoubles;
import static Utils.Utils.positionsHaveSameQuadrant;

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

        wrapperMove(this.game.getEnterprise().getPosition(), newPosition);
    }

    private void manualMove(List<String> params) {
        if (parseDoubles(params.get(0)).isEmpty())
            params.remove(0);
        else 
            this.game.con.printf("(Manual movenemnt assumed.)\n");


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
        
        wrapperMove(this.game.getEnterprise().getPosition(), newPosition);

    }

    private void adjustStats(Position dest) {
        Computer slay = new Computer(this.game);

        double powerNeeded = slay.calcPower(this.game.getEnterprise().getPosition(), dest);
        double timeNeeded = slay.calcTime(this.game.getEnterprise().getPosition(), dest);
        this.game.setStarDate(this.game.getStarDate() + timeNeeded);
        game.passTime(timeNeeded);
        this.game.getEnterprise().setEnergy(this.game.getEnterprise().getEnergy() - powerNeeded);
    }

    public void wrapperMove(Position curPos, Position destPos) {
        Position movedPos = moveToPosition(this.game.getEnterprise().getPosition(), destPos);

        if(movedPos == null)
            return;
        if(!positionsHaveSameQuadrant(this.game.getEnterprise().getPosition(), movedPos)) {
            game.getEnterprise().refreshCondition(game.getGameMap().getEntityMap());
            new Attack(game).klingonAttack(0);
            this.game.con.printf("\nEntering %d - %d\n", movedPos.getQuadrant().getY()+1, movedPos.getQuadrant().getX()+1);
            game.setJustEnteredQuadrant(true);
        } else {
            new Attack(game).klingonAttack(0);
            game.getEnterprise().refreshCondition(game.getGameMap().getEntityMap());
            game.setJustEnteredQuadrant(false);
        }

        adjustStats(movedPos);
        this.game.getEnterprise().setPosition(movedPos);
        this.game.getGameMap().updateMap();
        this.game.getEnterprise().refreshCondition(this.game.getGameMap().getEntityMap());
    }

    private Position moveToPosition(Position curPos, Position destPos) {
        Computer computer = new Computer(this.game);
        Position nextPos = curPos.getClosestAdjecentPositionToDestination(destPos);

        Coordinate nq = nextPos.getQuadrant();
        Coordinate ns = nextPos.getSector();

        if (nq.getY() >= 8 || nq.getY() < 0 || nq.getX() >= 8 || nq.getX() < 0 || ns.getX() < 0 || ns.getY() < 0) {
            this.game.con.printf(
                    "\nYOU HAVE ATTEMPTED TO CROSS THE NEGATIVE ENERGY BARRIER\nAT THE EDGE OF THE GALAXY.  THE THIRD TIME YOU TRY THIS,\nYOU WILL BE DESTROYED.\n");
            return curPos;
        }

        if(!positionsHaveSameQuadrant(curPos, nextPos)){
            game.randomizeQuadrant(nq);
        }

        EntityType entityType = this.game.getEntityTypeAtPosition(nextPos);

        switch(entityType) {
            case NOTHING: // do nothing
                break;
            case THOLIAN:
            case KLINGON:
            case COMMANDER:
            case SUPER_COMMANDER:
            case ROMULAN: // ram an enemy
                game.pause(2);
                new Ram(game).ram(nextPos, false);
                return nextPos;
            case BLACK_HOLE:
                // game.clearScreen();
                game.con.printf("\n***RED ALERT!  RED ALERT!\n***%s pulled into black hole at %d - %d\n", "Enterprise", ns.getY()+1, ns.getX()+1);
                new Finish(game).finish(GameOverReason.HOLE);
                return null;
            default:
                Double stopEnergy = 5.0*game.getEnterprise().getPosition().calcDistance(nextPos)/Math.max(computer.calcTime(game.getEnterprise().getPosition(), nextPos), 0.0001);
                game.con.printf("\n%s %s %d - %d;\nEmergency stop required %.2f unit of energy.\n", "Enterprise", entityType == EntityType.THOLIAN_WEB ? "encounters Tholian web at" : "blocked by object at", ns.getY()+1, ns.getX()+1, stopEnergy);
                this.game.getEnterprise().setEnergy(game.getEnterprise().getEnergy() - stopEnergy);
                if(game.getEnterprise().getEnergy() <= 0) {
                    // game.clearScreen();
                    new Finish(game).finish(GameOverReason.ENERGY);
                    return null;
                }
                return curPos;
        }
        if (isEqual(destPos, nextPos)) {
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
