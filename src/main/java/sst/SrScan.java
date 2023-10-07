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
        int row = enterprise.getPosition().getQuadrant().getX();
        int column = enterprise.getPosition().getQuadrant().getY();
        int r, c;
        String out = "\nShort-range scan:\n\n";
        boolean leftside = true;
        boolean rightside = true;

        // print column header
        out += "        ";
        for (c = 1; c <= 10; c++)
            out += String.format("%1d ", c);
        out += "\n";

        for (r = 1; r <= 10; r++) {
            if (leftside) {
                out += String.format("    %2d  ", r);

                for (c = 1; c <= 10; c++)
                    out += String.format("%c ", this.game.getMap()[row][column][r - 1][c - 1]);
            }

            if (rightside) {
                out += " ";

                switch (r) {
                    case 1:
                        out += String.format("Stardate      %.1f", enterprise.getStarDate());
                        break;
                    case 2:
                        out += String.format("Condition     %s", enterprise.getCondition());
                        break;
                    case 3:
                        out += String.format("Position      %d - %d, %d - %d",
                                (enterprise.getPosition().getQuadrant().getX() + 1),
                                (enterprise.getPosition().getQuadrant().getY() + 1),
                                (enterprise.getPosition().getSector().getX() + 1),
                                (enterprise.getPosition().getSector().getY() + 1));
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
                        out += String.format("Klingons Left %d", game.getKlingons().length);
                        break;
                    case 10:
                        out += String.format("Time Left     %.2f", game.getTime());
                        break;
                }
            }
            out += "\n";
        }
        out += "\n";

        this.game.con.printf("%s", out);
    }
    // public void ExecSRSCAN() {
    //     Enterprise enterprise = game.getEnterprise();
    //     int row = enterprise.getPosition().getQuadrant().getX();
    //     int column = enterprise.getPosition().getQuadrant().getY();
    //     int r, c;

    //     this.game.con.printf("\nShort-range scan:\n\n");
    //     boolean leftside = true;
    //     boolean rightside = true;

    //     // print column header
    //     this.game.con.printf("        ");
    //     for (c = 1; c <= 10; c++)
    //         this.game.con.printf("%1d ", c);
    //     this.game.con.printf("\n");

    //     for (r = 1; r <= 10; r++) {
    //         if (leftside) {
    //             this.game.con.printf("    %2d  ", r);

    //             for (c = 1; c <= 10; c++)
    //                 this.game.con.printf("%c ", this.game.getMap()[row][column][r - 1][c - 1]);
    //         }

    //         if (rightside) {
    //             this.game.con.printf(" ");

    //             switch (r) {
    //                 case 1:
    //                     this.game.con.printf("Stardate      %.1f", enterprise.getStarDate());
    //                     break;
    //                 case 2:
    //                     this.game.con.printf("Condition     %s", enterprise.getCondition());
    //                     break;
    //                 case 3:
    //                     this.game.con.printf("Position      %d - %d, %d - %d",
    //                             (enterprise.getPosition().getQuadrant().getX() + 1),
    //                                             (enterprise.getPosition().getQuadrant().getY() + 1), (enterprise.getPosition().getSector().getX() + 1),
    //                                             (enterprise.getPosition().getSector().getY() + 1));
                                        
    //                     break;
    //                 case 4:
    //                     this.game.con.printf("Life Support  %s", "DAMAGED, Reserves = 2.30");
    //                     break;
    //                 case 5:
    //                     this.game.con.printf("Warp Factor   %.1f", 5.0);
    //                     break;
    //                 case 6:
    //                     this.game.con.printf("Energy        %.2f", 2176.24);
    //                     break;
    //                 case 7:
    //                     this.game.con.printf("Torpedoes     %d", 3);
    //                     break;
    //                 case 8:
    //                     this.game.con.printf("Shields       %s, %d%% %.1f units", "UP", 42, 1050.0);
    //                     break;
    //                 case 9:
    //                     this.game.con.printf("Klingons Left %d", 12);
    //                     break;
    //                 case 10:
    //                     this.game.con.printf("Time Left     %.2f", 3.72);
    //                     break;
    //             }
    //         }

    //         this.game.con.printf("\n");
    //     }

    //     this.game.con.printf("\n");
    // }
}
