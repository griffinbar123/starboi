package sst;

import Model.Condition;
import Model.Coordinate;
import Model.Enterprise;
import Model.EntityType;
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

        // game.getEnterprise().getDeviceDamage().put(Enterprise.Device.LR_SENSORS,
        // 1.0); // TODO: testing damaged sensors

        if (game.getEnterprise().getDeviceDamage().get(Device.LR_SENSORS) > 0 &&
                game.getEnterprise().getCondition() != Condition.DOCKED) {
            this.game.con.printf("LONG-RANGE SENSORS DAMAGED.\n\n");
            return;
        }

        game.addCoordinateString(new Coordinate(row - 1, column - 1), getQuadrantNumberAsString(row - 1, column - 1));
        game.addCoordinateString(new Coordinate(row, column - 1), getQuadrantNumberAsString(row, column - 1));
        game.addCoordinateString(new Coordinate(row + 1, column - 1), getQuadrantNumberAsString(row + 1, column - 1));
        game.addCoordinateString(new Coordinate(row - 1, column), getQuadrantNumberAsString(row - 1, column));
        game.addCoordinateString(new Coordinate(row, column), getQuadrantNumberAsString(row, column));
        game.addCoordinateString(new Coordinate(row + 1, column), getQuadrantNumberAsString(row + 1, column));
        game.addCoordinateString(new Coordinate(row - 1, column + 1), getQuadrantNumberAsString(row - 1, column + 1));
        game.addCoordinateString(new Coordinate(row, column + 1), getQuadrantNumberAsString(row, column + 1));
        game.addCoordinateString(new Coordinate(row + 1, column + 1), getQuadrantNumberAsString(row + 1, column + 1));

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

    private String getQuadrantNumberAsString(int row, int column) {
        return Integer.toString(game.getQuadrantNumber(row, column));
    }
}
