package sst;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import Model.Coordinate;
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
    private Console con;
    @NonNull
    private Game game;
    public double aaitem = 0.5; //TODO: figure out what this is in original


    /**
     * COMPUTER command implementation
     */
    public void ExecCOMPUTER() {
        Optional<Integer> tm;
        Position dest = new Position(null, null);
        Integer time;
        // Initialize console
        con = System.console();
        if (con == null)
            return;

        dest = readCorodinates().orElse(null);
        if (dest == null) return;

        tm = readTime();
        if (tm == null) return;

        time = tm.orElse(null);

        if (time == null) {
            calcDistance(this.game.getEnterprise().getPosition(), dest);
        } else {
            calcTime(this.game.getEnterprise().getPosition(), dest, time);
        }
    }

    /**
     * calculate the time required for travel between two positions in stardates
     * 
     * @param pos starting position
     * @param dest position to travel to
     * @param time desired time to reach destination
     * @return the time in stardates required to make the journey
     * @author Matthias Schrock
     */
    public double calcTime(Position pos, Position dest, Integer time) {
        double distance = calcDistance(pos, dest);
        return -1;
    }

    /**
     * calculate the distance bteween two positions
     * 
     * @param pos starting position
     * @param dest position to travel to
     * @return the distance between two points on the map
     * @author Matthias Schrock
     */
    public double calcDistance(Position pos, Position dest) {
        double ix2 = dest.getQuadrant().getX();
        double iy2 = dest.getQuadrant().getY();
        double ix1 = aaitem + 0.5;
        double iy1 = aaitem + 0.5;
        double quadX = pos.getQuadrant().getY();
        double quadY = pos.getQuadrant().getX();
        double sectX = pos.getSector().getX();
        double sectY = pos.getSector().getY();

        if (dest.getQuadrant() == pos.getQuadrant()) {
            ix2 = ix1;
            iy2 = iy1;
            ix1 = quadX;
            iy1 = quadY;
        }

        if (ix1 > 8 || ix1 < 1 || iy1 > 8 || iy1 < 1 ||
                ix2 > 10 || ix2 < 1 || iy2 > 10 || iy2 < 1) {
            return -1;
        }

        return Math.sqrt(Math.pow(iy1 - quadX + 0.1 * (iy2 - sectY), 2) +
                Math.pow(ix1 - quadY + 0.1 * (ix2 - sectX), 2));
    }

    private Optional<Integer> readTime() {
        Pattern pattern = Pattern.compile("\\d");
        Matcher matcher;
        String cmd = "";

        con.printf("Answer \"no\" if you don't know the value:\n");
        con.printf("Time or arrival date? ");
        cmd = con.readLine().toUpperCase().trim();
        if (cmd.contains("NO")) {
            return Optional.empty();
        }

        matcher = pattern.matcher(cmd);
        if (matcher.find()) {
            return Optional.ofNullable(Integer.valueOf(matcher.group()));
        }

        con.printf("\n\nBeg your pardon, Captain?\n\n");
        return null;
    }

    private Optional<Position> readCorodinates() {
        Coordinate sect = null;
        Coordinate quad = null;
        List<Integer> cord = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\d");
        Matcher matcher;
        String cmd = "";

        con.printf("Destination quadrant and/or sector? ");
        cmd = con.readLine().trim();
        matcher = pattern.matcher(cmd);

        while (matcher.find()) {
            cord.add(Integer.valueOf(matcher.group()));
        }

        switch (cord.size()) {
            case 4:
                quad = new Coordinate(cord.get(3), cord.get(2));
                // waterfall
            case 2:
                if (quad == null) {
                    quad = new Coordinate(this.game.getEnterprise().getPosition().getQuadrant().getY(),
                            this.game.getEnterprise().getPosition().getQuadrant().getX());
                }
                sect = new Coordinate(cord.get(1), cord.get(0));
                break;
            default:
                con.printf("\n\nBeg your pardon, Captain?\n\n");
                return Optional.empty();
        }

        return Optional.ofNullable(new Position(quad, sect));
    }
}
