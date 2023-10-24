package sst;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
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
        Enterprise enterprise = game.getEnterprise();
        Computer computer = new Computer(this.game);
        MoveType moveType = MoveType.undefined;
        String cmdstr;

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

    private void automaticMove(List<String> params) {
        params.remove(0);

        while(params == null || params.size() == 0) {
            String displacements = this.game.con.readLine("Destination sector or quadrant&sector- ");
            params = readCommands(displacements).orElse(null);
        }

        List<Integer> offsets = parseIntegers(params);
        // System.out.println(Arrays.toString(offsets.toArray()));
        if(offsets.size() != 2 && offsets.size() != 4) {
            this.game.con.printf("\nBeg your pardon, Captain?\n");
            return;
        }

        Coordinate quad = offsets.size() == 2 ? this.game.getEnterprise().getPosition().getQuadrant() : new Coordinate(offsets.get(0)-1, offsets.get(1)-1);
        Coordinate sect = offsets.size() == 2 ? new Coordinate(offsets.get(0)-1, offsets.get(1)-1) : new Coordinate(offsets.get(2)-1, offsets.get(3)-1);

        this.game.con.printf("\nEnsign Chekov- \"Course laid in, Captain.\"\n");

        Position newPosition = new Position(quad, sect);
        Position movedPos = moveToPosition(this.game.getEnterprise().getPosition(), newPosition);

        this.game.getEnterprise().setPosition(movedPos);
        this.game.updateMap();
    }

    private void manualMove(List<String> params){
        if(parseDoubles(params.get(0)).isEmpty()){
            params.remove(0);
        }

        while(params == null || params.size() == 0) {
            String displacements = this.game.con.readLine("X and Y displacements- ");
            params = readCommands(displacements).orElse(null);
        }

        List<Double> offsets = parseDoubles(params);
        if(offsets.size() == 0) {
            this.game.con.printf("\nBeg your pardon, Captain?\n");
            return;
        }

        int xOffset = (int) ((double)offsets.get(0) * 10);
        int yOffset =  offsets.size() == 1 ? 0 : (int) ((double)offsets.get(1) * 10);

        this.game.con.printf("\nHelmsman Sulu- \"Aye, Sir.\"\n");

        Position newPosition = getDesiredPosition(xOffset, yOffset);
        Position movedPos = moveToPosition(this.game.getEnterprise().getPosition(), newPosition);

        this.game.getEnterprise().setPosition(movedPos);
        this.game.updateMap();

    }
    
    private Position moveToPosition(Position curPos, Position destPos) {
        Position nextPos = getClosestAdjecentPositionToDestination(curPos, destPos);

        Coordinate nq = nextPos.getQuadrant();
        Coordinate ns = nextPos.getSector();

        if(this.game.getMap()[nq.getY()][nq.getX()][ns.getY()][ns.getX()] != Game.NOTHING){
            this.game.con.printf("\nEnterprise blocked by object at Sector %d - %d\nEmergency stop required 125.00 units of energy.\n", ns.getY(), ns.getX());
            this.game.getEnterprise().setEnergy(this.game.getEnterprise().getEnergy()-125.00);
            return curPos;
        }
        if(positionsAreEqual(destPos, nextPos)) {
            return nextPos;
        }
        return moveToPosition(nextPos, destPos);
    }

    private Position getClosestAdjecentPositionToDestination(Position curPos, Position destPos){
        Computer computer = new Computer(this.game);

        int x = curPos.getXAsInt();
        int y = curPos.getYAsInt();

        Position topLeft = Position.turnIntToPosition(y-1, x-1);
        Position topMiddle = Position.turnIntToPosition(y-1, x);
        Position topRight = Position.turnIntToPosition(y-1, x+1);
        Position midLeft = Position.turnIntToPosition(y, x-1);
        Position midRight = Position.turnIntToPosition(y, x+1);
        Position botLeft = Position.turnIntToPosition(y+1, x-1);
        Position botMiddle = Position.turnIntToPosition(y+1, x);
        Position botRight = Position.turnIntToPosition(y+1, x+1);
        Double topLeftScore = computer.calcDistance(destPos, topLeft);
        Double topMiddleScore = computer.calcDistance(destPos, topMiddle);
        Double topRightScore = computer.calcDistance(destPos, topRight);
        Double midLeftScore = computer.calcDistance(destPos, midLeft);
        Double midRightScore = computer.calcDistance(destPos, midRight);
        Double botLeftScore = computer.calcDistance(destPos, botLeft);
        Double botMiddleScore = computer.calcDistance(destPos, botMiddle);
        Double botRightScore = computer.calcDistance(destPos, botRight);



        Double[] scores = {topLeftScore, topMiddleScore, topRightScore, botLeftScore, botMiddleScore, botRightScore, midLeftScore, midRightScore};

        if(topLeftScore == Collections.min(Arrays.asList(scores))) {
            return topLeft;
        } else if(topMiddleScore == Collections.min(Arrays.asList(scores))) {
            return topMiddle;
        } else if(topRightScore == Collections.min(Arrays.asList(scores))) {
            return topRight;
        } else if(botLeftScore == Collections.min(Arrays.asList(scores))) {
            return botLeft;
        } else if(botMiddleScore == Collections.min(Arrays.asList(scores))) {
            return botMiddle;
        } else if(botRightScore == Collections.min(Arrays.asList(scores))) {
            return botRight;
        } else if(midLeftScore == Collections.min(Arrays.asList(scores))) {
            return midLeft;
        } else if(midRightScore == Collections.min(Arrays.asList(scores))) {
            return midRight;
        }
        return curPos;
    }

    private Position getDesiredPosition(int xOffset, int yOffset) {
        Position enterPos = this.game.getEnterprise().getPosition();
        int newX = enterPos.getXAsInt() + xOffset;
        int newY = enterPos.getYAsInt() - yOffset;

        return Position.turnIntToPosition(newY, newX);
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
