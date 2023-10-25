package sst;

import Model.Enterprise;
import Model.Game;
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
        Enterprise enterprise = game.getEnterprise();
        int row = enterprise.getPosition().getQuadrant().getY();
        int column = enterprise.getPosition().getQuadrant().getX();
        int r, c;
        String out = "\n"; //Short-range scan:\n
        boolean leftside = true;
        boolean rightside = true;

        // print column header
        out += "    ";
        for (c = 1; c <= 10; c++)
            out += String.format("%1d ", c);
        out += "\n";

        for (r = 1; r <= 10; r++) {
            if (leftside) {
                out += String.format("%2d  ", r);

                for (c = 1; c <= 10; c++)
                    out += String.format("%c ", this.game.getMap()[row][column][r - 1][c - 1]);
            }

            if (rightside) {
                out += " ";

                switch (r) {
                    case 1:
                        out += String.format("Stardate      %.1f", this.game.getStarDate());
                        break;
                    case 2:
                        out += String.format("Condition     %s", enterprise.getCondition());
                        break;
                    case 3:
                        out += String.format("Position      %d - %d, %d - %d",
                                (enterprise.getPosition().getQuadrant().getY() + 1),
                                (enterprise.getPosition().getQuadrant().getX() + 1),
                                (enterprise.getPosition().getSector().getY() + 1),
                                (enterprise.getPosition().getSector().getX() + 1));
                        break;
                    case 4:
                        out += String.format("Life Support  %s", "DAMAGED, Reserves = 2.30");
                        break;
                    case 5:
                        out += String.format("Warp Factor   %.1f", enterprise.getWarp());
                        break;
                    case 6:
                        out += String.format("Energy        %.2f", enterprise.getEnergy());
                        break;
                    case 7:
                        out += String.format("Torpedoes     %d", enterprise.getTorpedoes());
                        break;
                    case 8:
                        out += String.format("Shields       %s, %d%% %.1f units",
                                enterprise.getSheilds().getActive(),
                                enterprise.getSheilds().getLevel(),
                                enterprise.getSheilds().getUnits());
                        break;
                    case 9:
                        out += String.format("Klingons Left %d", game.getKlingonCount());
                        break;
                    case 10:
                        out += String.format("Time Left     %.2f", game.getTime());
                        break;
                }
            }
            out += "\n";
        }

        this.game.con.printf("%s", out);
    }
}
