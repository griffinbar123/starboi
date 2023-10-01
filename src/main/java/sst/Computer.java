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

    /**
     * COMPUTER command implementation
     */
    public void ExecCOMPUTER() {
        Optional<Integer> tm;
        Position dest;
        Integer time;
        // Initialize console
        con = System.console();
        if (con == null)
            return;

        dest = readCorodinates().orElse(null);
        tm = readTime();
        if (tm == null) {
            return;
        }

        time = tm.orElse(null);

        calc(dest, time);
    }

    /**
     * calculate the time required for travel in stardates
     * @param pos position to travel to
     * @param time desired time to reach destination
     * @return the time in stardates required to make the journey
     * @author Matthias Schrock
     */
    public Integer calc(Position pos, Integer time) {
        return -1;
    }

    /**
     * calculate the time required for travel in stardates
     * @param pos position to travel to
     * @return the time in stardates required to make the journey
     * @author Matthias Schrock
     */
    public Integer calc(Position pos) {
        return -1;
    }

    private Optional<Integer> readTime() {
        Pattern pattern = Pattern.compile("\\d");
        Matcher matcher;
        String cmd = "";

        con.printf("Answer \"no\" if you don't know the value:\nTime or arrival date? ");
        cmd = con.readLine().toUpperCase().trim();
        if (cmd.contains("NO")) {
            return Optional.empty();
        }

        matcher = pattern.matcher(cmd);
        if (matcher.find()) {
            return Optional.ofNullable(Integer.valueOf(matcher.group()));
        } else {
            con.printf("\n\nBeg your pardon, Captain?\n\n");
        }

        return null;
    }

    /**
     * Reads destination input from console
     * @return the targeted destination
     */
    private Optional<Position> readCorodinates() {
        Coordinate sect = new Coordinate(null, null);
        Coordinate quad = new Coordinate(null, null);
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
                quad.setX(cord.get(2));
                quad.setY(cord.get(3));
                // waterfall
            case 2:
                sect.setX(cord.get(0));
                sect.setY(cord.get(1));
                break;
            default:
                con.printf("\n\nBeg your pardon, Captain?\n\n");
                return Optional.empty();
        }

        return Optional.ofNullable(new Position(quad, sect));
    }
}
