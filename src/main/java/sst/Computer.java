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
        Position dest = new Position(null, null);
        Optional<Position> res;
        // Initialize console
        con = System.console();
        if (con == null)
            return;

        res = readCorodinates();
        if (!res.isPresent()) {
            return;
        }

        dest = res.get();

    }

    private Optional<Integer> readTime() {
        return Optional.ofNullable(null);
    }

    /**
     * Reads destination input from console
     * @return the targeted destination
     */
    private Optional<Position> readCorodinates() {
        Coordinate sect = new Coordinate(-1, -1);
        Coordinate quad = new Coordinate(-1, -1);
        List<Integer> cord = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\d");
        Matcher matcher;
        String cmd = "";
        int num = 0;
        boolean accepted = false;

        con.printf("Destination quadrant and/or sector? ");
        while (!accepted) {
            cmd = con.readLine().trim();

            matcher = pattern.matcher(cmd);

            while (matcher.find()) {
                cord.add(Integer.valueOf(matcher.group()));
                num += 1;
            }

            switch (num) {
                case 2:
                    sect.setX(cord.get(0));
                    sect.setY(cord.get(1));
                    accepted = true;
                    break;
                case 4:
                    sect.setX(cord.get(0));
                    sect.setY(cord.get(1));
                    quad.setX(cord.get(2));
                    quad.setY(cord.get(3));
                    accepted = true;
                    break;
                default:
                    con.printf("\n\nBeg your pardon, Captain?\n\n");
                    return Optional.empty();
            }
        }

        return Optional.ofNullable(new Position(quad, sect));
    }
}
