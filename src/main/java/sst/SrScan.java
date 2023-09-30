package sst;

import java.io.Console;

import Model.Enterprise;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Handles the short range scan(SRSCAN) command
 */
@RequiredArgsConstructor
public class SrScan {
    @NonNull
    private Console con;
    @NonNull
    private Game game;

    /**
     * Prints a short range scan report to console during the game
     * along with other status data
     */
    public void ExecSRSCAN() {
        Enterprise enterprise = game.getEnterprise();
        int row = enterprise.getPosition().getQuadrant().getX();
        int column = enterprise.getPosition().getQuadrant().getY();
        int r, c;

        con.printf("\nShort-range scan:\n\n");
        boolean leftside = true;
        boolean rightside = true;

        // print column header
        con.printf("        ");
        for (c = 1; c <= 10; c++)
            con.printf("%1d ", c);
        con.printf("\n");

        for (r = 1; r <= 10; r++) {
            if (leftside) {
                con.printf("    %2d  ", r);

                for (c = 1; c <= 10; c++)
                    con.printf("%c ", this.game.getMap()[row][column][r - 1][c - 1]);
            }

            if (rightside) {
                con.printf(" ");

                switch (r) {
                    case 1:
                        con.printf("Stardate      %.1f", enterprise.getStarDate());
                        break;
                    case 2:
                        con.printf("Condition     %s", enterprise.getCondition());
                        break;
                    case 3:
                        con.printf("Position      %d - %d, %d - %d",
                                (enterprise.getPosition().getQuadrant().getX() + 1),
                                                (enterprise.getPosition().getQuadrant().getY() + 1), (enterprise.getPosition().getSector().getX() + 1),
                                                (enterprise.getPosition().getSector().getY() + 1));
                                        
                        break;
                    case 4:
                        con.printf("Life Support  %s", "DAMAGED, Reserves = 2.30");
                        break;
                    case 5:
                        con.printf("Warp Factor   %.1f", 5.0);
                        break;
                    case 6:
                        con.printf("Energy        %.2f", 2176.24);
                        break;
                    case 7:
                        con.printf("Torpedoes     %d", 3);
                        break;
                    case 8:
                        con.printf("Shields       %s, %d%% %.1f units", "UP", 42, 1050.0);
                        break;
                    case 9:
                        con.printf("Klingons Left %d", 12);
                        break;
                    case 10:
                        con.printf("Time Left     %.2f", 3.72);
                        break;
                }
            }

            con.printf("\n");
        }

        con.printf("\n");
    }
}
