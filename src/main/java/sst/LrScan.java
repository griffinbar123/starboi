package sst;

import Model.Coordinate;
import Model.Enterprise;
import Model.Game;
import Model.Device;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Handles the long range scan (LRSCAN) command
 * 
 * @author Griffin Barnard
 */
@RequiredArgsConstructor
public class LrScan {
    @NonNull
    private Game game;

    /**
     * Prints a long range scan report to console during the game
     */
    public void ExecLRSCAN() {
        Enterprise enterprise = game.getEnterprise();
        int row = enterprise.getPosition().getQuadrant().getY();
        int column = enterprise.getPosition().getQuadrant().getX();
        String out = "";

        // game.getEnterprise().getDeviceDamage().put(Enterprise.Device.LR_SENSORS, 1.0); // TODO: testing damaged sensors

        if (game.getEnterprise().getDeviceDamage().get(Device.LR_SENSORS) > 0) {
            this.game.con.printf("LONG-RANGE SENSORS DAMAGED.\n\n");
            return;
        }

        game.addCoordinateString(new Coordinate(row - 1, column - 1), getQuadrantNumber(row - 1, column - 1));
        game.addCoordinateString(new Coordinate(row, column - 1), getQuadrantNumber(row, column - 1));
        game.addCoordinateString(new Coordinate(row + 1, column - 1), getQuadrantNumber(row + 1, column - 1));
        game.addCoordinateString(new Coordinate(row - 1, column), getQuadrantNumber(row - 1, column));
        game.addCoordinateString(new Coordinate(row, column), getQuadrantNumber(row, column));
        game.addCoordinateString(new Coordinate(row + 1, column), getQuadrantNumber(row + 1, column));
        game.addCoordinateString(new Coordinate(row - 1, column + 1), getQuadrantNumber(row - 1, column + 1));
        game.addCoordinateString(new Coordinate(row, column + 1), getQuadrantNumber(row, column + 1));
        game.addCoordinateString(new Coordinate(row + 1, column + 1), getQuadrantNumber(row + 1, column + 1));

        out = "\nLong-range scan for Quadrant %d - %d:\n" +
                "%-5s%-5s%-5s\n" +
                "%-5s%-5s%-5s\n" +
                "%-5s%-5s%-5s\n";

        this.game.con.printf(out, row + 1, column + 1,
                game.getCoordinateString(row - 1, column - 1),
                game.getCoordinateString(row - 1, column),
                game.getCoordinateString(row - 1, column + 1),
                game.getCoordinateString(row, column - 1),
                game.getCoordinateString(row, column),
                game.getCoordinateString(row, column + 1),
                game.getCoordinateString(row + 1, column - 1),
                game.getCoordinateString(row + 1, column),
                game.getCoordinateString(row + 1, column + 1));
    }

    private String getQuadrantNumber(int row, int column) {
        // int thousands = getNumberOfEntiesInMapQuadrant(row, column, Supernova); TODO:
        // add supernova
        if (row < 0 || row >= 8 || column < 0 || column >= 8) {
            return "-1";
        }
        int thousands = 0;
        // System.out.println(row + " " + column);
        int hundreds = (getNumberOfEntiesInMapQuadrant(row, column, 'K') + getNumberOfEntiesInMapQuadrant(row, column, 'C') + getNumberOfEntiesInMapQuadrant(row, column, 'S')) * 100;
        int tens = getNumberOfEntiesInMapQuadrant(row, column, 'B') * 10;
        int ones = getNumberOfEntiesInMapQuadrant(row, column, '*');

        return Integer.toString(thousands + hundreds + tens + ones);
    }

    private int getNumberOfEntiesInMapQuadrant(int row, int column, char entity) {
        int numberOfElements = 0;
        for (int i = 0; i < this.game.getMap()[column][row].length; i++) {
            for (int j = 0; j < this.game.getMap()[column][row][i].length; j++) {
                if (this.game.getMap()[column][row][j][i] == entity) {
                    numberOfElements += 1;
                }
            }
        }
        return numberOfElements;
    }
}
