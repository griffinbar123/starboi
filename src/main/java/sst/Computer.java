package sst;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import Model.Coordinate;
import Model.Game;
import Model.Position;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * implements the computer command.
 * Hopefully methods in the computer command can be
 * used for calculating time in the move command
 * 
 * @author Matthias Schrock
 */
@RequiredArgsConstructor
public class Computer {
    @NonNull
    private Game game;
    public double aaitem = 0.5; //TODO: figure out what this is in original


    /**
     * COMPUTER command implementation
     */
    public void ExecCOMPUTER() {
        Optional<Integer> tm;
        Optional<Float> wf;
        Position dest = new Position(null, null);
        Integer time;
        Float warp;

        dest = readCorodinates().orElse(null);
        if (dest == null) return;

        this.game.con.printf("Answer \"no\" if you don't know the value:\n");
        while(true) {
            tm = readTime();
            if (tm != null) break;
            wf = readWarpFactor();
            if (wf != null) break;
            this.game.con.printf("\n\nBeg your pardon, Captain?\n\n");
        }

        time = tm.orElse(null);
        warp = wf.orElse(null);

        if(warp != null) {
            this.game.getEnterprise().setWarp(warp);
            this.game.con.printf("\nRemaining energy will be %d", this.game.getEnterprise().getEnergy() - calcPower(this.game.getEnterprise().getPosition(), dest));
            if(time != null)
                this.game.con.printf("\nMinimum warp needed is %d", calcWarpDrive(this.game.getEnterprise().getPosition(), dest, time));
            this.game.con.printf("\nAnd we will arrive at stardate %d", this.game.getTime() + calcTime(this.game.getEnterprise().getPosition(), dest));
        }

    }

    /**
     * calculate the minimum warp needed to travel a certain distance in a certain amount of time
     * 
     * @param pos starting position
     * @param dest position to travel to
     * @return the power required to make the journey
     * @author Griffin Barnard
     */
    public double calcWarpDrive(Position pos, Position dest, Integer Time) {
        //TODO: This doesnt work yet
        double distance = calcDistance(pos, dest);
        double warpSpeed = game.getEnterprise().getWarp();
        // tpower = dist*twarp*twarp*twarp*(shldup+1);
        double powerNeeded = 0.0;
        System.out.println(powerNeeded);
        return powerNeeded;
    }

     /**
     * calculate the power required for travel between two positions
     * 
     * @param pos starting position
     * @param dest position to travel to
     * @return the power required to make the journey
     * @author Griffin Barnard
     */
    public double calcPower(Position pos, Position dest) {
        double distance = calcDistance(pos, dest);
        double warpSpeed = game.getEnterprise().getWarp();
        double powerNeeded = distance*warpSpeed*warpSpeed*warpSpeed*(0+1); //TODO: implement draining power based off shields * (shldup+1)
        System.out.println(powerNeeded);
        return powerNeeded;
    }

    /**
     * calculate the time required for travel between two positions in stardates
     * 
     * @param pos starting position
     * @param dest position to travel to
     * @return the time in stardates required to make the journey
     * @author Matthias Schrock & Griffin Barnard
     */
    public double calcTime(Position pos, Position dest) {
        double distance = calcDistance(pos, dest);
        double warpSpeed = game.getEnterprise().getWarp();
        double travelTime = (distance) / (warpSpeed * warpSpeed);
        System.out.println(travelTime);
        return travelTime;
    }

    /**
     * calculate the distance bteween two positions
     * 
     * @param pos starting position
     * @param dest position to travel to
     * @return the distance between two points on the map
     * @author Griffin Barnard
     */
    public double calcDistance(Position pos, Position dest) {
        double destQuadX = dest.getQuadrant().getX();
        double destQuadY = dest.getQuadrant().getY();
        double destSectX = dest.getSector().getX();
        double destSectY = dest.getSector().getY();

        double posQuadX = pos.getQuadrant().getX() + 1;
        double posQuadY = pos.getQuadrant().getY() + 1;
        double posSectX = pos.getSector().getX() + 1;
        double posSectY = pos.getSector().getY() + 1;

        double x1 = (posQuadX * 10) +  posSectX;
        double y1 = (posQuadY * 10) + posSectY;

        double x2 = (destQuadX * 10) + destSectX;
        double y2 = (destQuadY * 10) + destSectY;

        System.out.println("x1: " + x1 + ", y1: " + y1 + ", x2: " + x2 + ", y2: " + y2);

        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
        
    }


    private Optional<Integer> readTime() {
        Pattern pattern = Pattern.compile("[1-9]|10");
        Matcher matcher;
        String cmd = "";

        this.game.con.printf("Time or arrival date? ");
        cmd = this.game.con.readLine().toUpperCase().trim();
        if (cmd.contains("NO")) {
            return Optional.empty();
        }

        matcher = pattern.matcher(cmd);
        if (matcher.find()) {
            return Optional.ofNullable(Integer.valueOf(matcher.group()));
        }
        return null;
    }

     private Optional<Float> readWarpFactor() {
        Pattern pattern = Pattern.compile("([1-9]|10)?([0-9]+)");
        Matcher matcher;
        String cmd = "";

        this.game.con.printf("Warp Factor? ");
        cmd = this.game.con.readLine().toUpperCase().trim();
        if (cmd.contains("NO")) {
            return Optional.empty();
        }

        matcher = pattern.matcher(cmd);
        if (matcher.find()) {
            return Optional.ofNullable(Float.valueOf(matcher.group()));
        }
        return null;
    }

    private Optional<Position> readCorodinates() {
        Coordinate sect = null;
        Coordinate quad = null;
        List<Integer> cord = new ArrayList<>();
        Pattern pattern = Pattern.compile("[1-9]|10");
        Matcher matcher;
        String cmd = "";

        this.game.con.printf("Destination quadrant and/or sector? ");
        cmd = this.game.con.readLine().trim();
        matcher = pattern.matcher(cmd);

        while (matcher.find()) {
            cord.add(Integer.valueOf(matcher.group()));
        }

        switch (cord.size()) {
            case 4:
                quad = new Coordinate(cord.get(1), cord.get(0));
                // waterfall
            case 2:
                if (quad == null) {
                    quad = new Coordinate(this.game.getEnterprise().getPosition().getQuadrant().getY(),
                            this.game.getEnterprise().getPosition().getQuadrant().getX());
                }
                sect = new Coordinate(cord.get(3), cord.get(2));
                break;
            default:
                this.game.con.printf("\n\nBeg your pardon, Captain?\n\n");
                return Optional.empty();
        }

        return Optional.ofNullable(new Position(quad, sect));
    }
}
