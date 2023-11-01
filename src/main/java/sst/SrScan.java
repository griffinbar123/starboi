package sst;

import Model.Enterprise;
import Model.Game;
import Model.Position;
import Model.Device;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Handles the short range scan(SRSCAN) command
 */
@RequiredArgsConstructor
public class SrScan {
    @NonNull
    private Game game;

    /**
     * Prints a short range scan report to console during the game
     * along with other status data
     */
    public void ExecSRSCAN() {
        StringBuilder sb = new StringBuilder("\n");
        Enterprise enterprise = game.getEnterprise();
        int row = enterprise.getPosition().getQuadrant().getY();
        int column = enterprise.getPosition().getQuadrant().getX();
        int r, c;
        boolean leftside = true;
        boolean rightside = true;

        // game.getEnterprise().getDeviceDamage().put(Enterprise.Device.SR_SENSORS, 1.0); // TODO: testing damaged sensors

        sb.append("    ");
        for (c = 1; c <= 10; c++)
            sb.append(String.format("%1d ", c));
        sb.append("\n");

        for (r = 1; r <= 10; r++) {
            if (leftside) {
                sb.append(String.format("%2d  ", r));

                for (c = 1; c <= 10; c++)
                    if (enterprise.getDeviceDamage().get(Device.SR_SENSORS) > 0
                            && !isAdjacent(r, c, enterprise.getPosition())) {
                        sb.append(String.format("%c ", '-'));
                    } else {
                        sb.append(String.format("%c ", this.game.getMap()[column][row][r - 1][c - 1]));
                    }
            }

            if (rightside) {
                sb.append(" ");

                switch (r) {
                    case 1:
                        sb.append(String.format("Stardate      %.1f", this.game.getStarDate()));
                        break;
                    case 2:
                        sb.append(String.format("Condition     %s", enterprise.getCondition()));
                        break;
                    case 3:
                        sb.append(String.format("Position      %d - %d, %d - %d",
                                (enterprise.getPosition().getQuadrant().getY() + 1),
                                (enterprise.getPosition().getQuadrant().getX() + 1),
                                (enterprise.getPosition().getSector().getY() + 1),
                                (enterprise.getPosition().getSector().getX() + 1)));
                        break;
                    case 4:
                        sb.append(String.format("Life Support  %s", "DAMAGED, Reserves = 2.30"));
                        break;
                    case 5:
                        sb.append(String.format("Warp Factor   %.1f", enterprise.getWarp()));
                        break;
                    case 6:
                        sb.append(String.format("Energy        %.2f", enterprise.getEnergy()));
                        break;
                    case 7:
                        sb.append(String.format("Torpedoes     %d", enterprise.getTorpedoes()));
                        break;
                    case 8:
                        sb.append(String.format("Shields       %s, %d%% %.1f units",
                                enterprise.getSheilds().getStatus().toString(),
                                enterprise.getSheilds().getLevel(),
                                enterprise.getSheilds().getUnits()));
                        break;
                    case 9:
                        sb.append(String.format("Klingons Left %d", game.getRemainingKlingonCount()));
                        break;
                    case 10:
                        sb.append(String.format("Time Left     %.2f", game.getTime()));
                        break;
                }
            }
            sb.append("\n");
        }

        this.game.con.printf("%s", sb.toString());
    }

    /**
     * used for determining whether a point on the map is close enough for damaged srsensors to detect
     * @param row
     * @param column
     * @param position position of the entity to check adjacency against
     * @return true if the point is adjacent to the entity
     * @author Matthias Schrock
     */
    private boolean isAdjacent(int row, int column, Position position) {
        int a = position.getSector().getX() + 1;
        int b = position.getSector().getY() + 1;
        return (Math.abs(row - b) <= 1 && Math.abs(column - a) <= 1);
    }
}
