package sst;

import java.util.List;
import java.util.Optional;
import Model.Coordinate;
import Model.Game;
import Model.Position;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import static Utils.Utils.readCommands;
import static Utils.Utils.parseIntegers;
import static Utils.Utils.parseDoubles;

/**
 * implements the computer command.
 * Hopefully methods in the computer command can be
 * used for calculating time in the move command
 * 
 * @author Matthias Schrock
 * @author Griffin Barnard
 */
@RequiredArgsConstructor
public class Computer {
    @NonNull
    private Game game;
    public double aaitem = 0.5; //TODO: figure out what this is in original


    /**
     * COMPUTER command implementation
     * @author Matthias Schrock
     * @author Griffin Barnard
     */
    public void ExecCOMPUTER(List<String> params) {
        Position dest = new Position(null, null);
        Double tm;
        Double wf;
        Double warp;
        Double twarp = null;

        dest = readCorodinates(params).orElse(null);
        if (dest == null) {
            this.game.con.printf("\n\nBeg your pardon, Captain?\n\n");
            return;
        }

        this.game.con.printf("Answer \"no\" if you don't know the value:\n");
        while(true) {
            tm = readTime().orElse(null);
            if (tm != null) {
                wf = null;
                twarp = calcWarpDrive(this.game.getEnterprise().getPosition(), dest, tm);
                if(twarp > 10){
                    this.game.con.printf("We'll never make it, sir.\n");
                    continue;
                }
                break;
            }

            wf = readWarpFactor(true).orElse(this.game.getEnterprise().getWarp());
            if (wf != null) {
                break;
            };

            this.game.con.printf("\n\nBeg your pardon, Captain?\n\n");
        }

        warp = wf;
        double warp2 = this.game.getEnterprise().getWarp(); //save warp as we change it on Enterprise

        if(twarp != null) 
            warp = twarp;
            
        while(true){
            if(warp == null) break;
            this.game.getEnterprise().setWarp(warp);
            this.game.con.printf("Remaining energy will be %f", this.game.getEnterprise().getEnergy() - calcPower(this.game.getEnterprise().getPosition(), dest));
            if(twarp != null)
                this.game.con.printf("\nMinimum warp needed is %f", twarp);
            this.game.con.printf("\nAnd we will arrive at stardate %f", this.game.getTime() + calcTime(this.game.getEnterprise().getPosition(), dest));
    
            if(twarp != null)
                this.game.getEnterprise().setWarp(warp2);
            
            this.game.con.printf("\n");

            wf = readWarpFactor(false).orElse(null);

            if(wf == null) break;

            warp = wf;
        }

    }

    /**
     * calculate the minimum warp needed to travel a certain distance in a certain amount of time
     * 
     * @param pos starting position
     * @param dest position to travel to
     * @param time amount of time the distance needs to be travelled in
     * @return the power required to make the journey
     * @author Griffin Barnard
     */
    public double calcWarpDrive(Position pos, Position dest, Double time) {
        double distance = calcDistance(pos, dest);
        double calculatedWarp = Math.floor(Math.sqrt(distance/time));
        return calculatedWarp;
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
        double powerNeeded = (distance/10.0)*warpSpeed*warpSpeed*warpSpeed*(0+1); //TODO: implement draining power based off shields * (shldup+1)
        return powerNeeded;
    }

    /**
     * calculate the time required for travel between two positions in stardates
     * 
     * @param pos starting position
     * @param dest position to travel to
     * @return the time in stardates required to make the journey
     * @author Matthias Schrock
     * @author Griffin Barnard
     */
    public double calcTime(Position pos, Position dest) {
        double distance = calcDistance(pos, dest);
        double warpSpeed = game.getEnterprise().getWarp();
        double travelTime = (distance) / (warpSpeed * warpSpeed);
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

        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
        
    }

    /**
     * Read time from command line
     * @return Optional containing time or empty Optional if no time was found
     * @author Matthias Schrock
     */
    private Optional<Double> readTime() {
        String cmd = "";

        cmd = this.game.con.readLine("Time or arrival date? ").toUpperCase();
        if (cmd.contains("NO")) {
            return Optional.empty();
        }

        return Optional.of(parseDoubles(cmd).orElse(null).get(0));
    }

    /**
     * Read warp factor from command line
     * @param prompt true if prompt should be displayed, false otherwise
     * @return Optional containing warp factor or empty Optional if no warp factor was found or user entered "no"
     * @author Griffin Barnard
     * @author Matthias Schrock
     */
     private Optional<Double> readWarpFactor(Boolean prompt) {
        Double fact;
        String cmd = "";

        this.game.con.printf(prompt ? "Warp Factor? " : "New warp factor to try? ");
        cmd = this.game.con.readLine().toUpperCase();
        if (cmd.contains("NO")) {
            return Optional.empty();
        }

        fact = parseDoubles(cmd).orElse(null).get(0);

        if (fact >= 1.0 && fact <= 10.0) {
            return Optional.ofNullable(fact);
        }
        return Optional.empty();
    }

    /**
     * Establish destination coordinates for cacluations
     * @param params list of parameters from command line (may be empty)
     * @return Optional containing destination coordinates or empty Optional if no coordinates were found
     * @author Matthias Schrock
     */
    public Optional<Position> readCorodinates(List<String> params) {
        Coordinate sect;
        Coordinate quad;
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
}
