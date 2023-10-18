package sst;

import Model.Enterprise;
import Model.Game;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Handles the Chart command
 * 
 * @author Griffin Barnard
 */
@RequiredArgsConstructor
public class Chart {
    @NonNull
    private Game game;

    /**
     * Prints a long range scan report to console during the game
     */
    public void ExecCHART() {
        Enterprise enterprise = game.getEnterprise();
        String out = "\nSTAR CHART FOR THE KNOWN GALAXY\n\n" +
                "     1    2    3    4    5    6    7    8\n" +
                "   -----------------------------------------\n" +
                "  - \n" +
                "1 - %-3s  %-3s  %-3s  %-3s  %-3s  %-3s  %-3s  %-3s  -\n" +
                "2 - %-3s  %-3s  %-3s  %-3s  %-3s  %-3s  %-3s  %-3s  -\n" +
                "3 - %-3s  %-3s  %-3s  %-3s  %-3s  %-3s  %-3s  %-3s  -\n" +
                "4 - %-3s  %-3s  %-3s  %-3s  %-3s  %-3s  %-3s  %-3s  -\n" +
                "5 - %-3s  %-3s  %-3s  %-3s  %-3s  %-3s  %-3s  %-3s  -\n" +
                "6 - %-3s  %-3s  %-3s  %-3s  %-3s  %-3s  %-3s  %-3s  -\n" +
                "7 - %-3s  %-3s  %-3s  %-3s  %-3s  %-3s  %-3s  %-3s  -\n" +
                "8 - %-3s  %-3s  %-3s  %-3s  %-3s  %-3s  %-3s  %-3s  -\n" +
                "\nThe Enterprise is currently in Quadrant %d - %d\n";

        this.game.con.printf(out, this.game.getCoordinateString(0, 0), this.game.getCoordinateString(0, 1),
                this.game.getCoordinateString(0, 2), this.game.getCoordinateString(0, 3), this.game.getCoordinateString(0, 4),
                this.game.getCoordinateString(0, 5), this.game.getCoordinateString(0, 6), this.game.getCoordinateString(0, 7),
                this.game.getCoordinateString(1, 0), this.game.getCoordinateString(1, 1), this.game.getCoordinateString(1, 2),
                this.game.getCoordinateString(1, 3), this.game.getCoordinateString(1, 4), this.game.getCoordinateString(1, 5),
                this.game.getCoordinateString(1, 6), this.game.getCoordinateString(1, 7), this.game.getCoordinateString(2, 0),
                this.game.getCoordinateString(2, 1), this.game.getCoordinateString(2, 2), this.game.getCoordinateString(2, 3),
                this.game.getCoordinateString(2, 4), this.game.getCoordinateString(2, 5), this.game.getCoordinateString(2, 6),
                this.game.getCoordinateString(2, 7), this.game.getCoordinateString(3, 0), this.game.getCoordinateString(3, 1),
                this.game.getCoordinateString(3, 2), this.game.getCoordinateString(3, 3), this.game.getCoordinateString(3, 4),
                this.game.getCoordinateString(3, 5), this.game.getCoordinateString(3, 6), this.game.getCoordinateString(3, 7),
                this.game.getCoordinateString(4, 0), this.game.getCoordinateString(4, 1), this.game.getCoordinateString(4, 2),
                this.game.getCoordinateString(4, 3), this.game.getCoordinateString(4, 4), this.game.getCoordinateString(4, 5),
                this.game.getCoordinateString(4, 6), this.game.getCoordinateString(4, 7), this.game.getCoordinateString(5, 0),
                this.game.getCoordinateString(5, 1), this.game.getCoordinateString(5, 2), this.game.getCoordinateString(5, 3),
                this.game.getCoordinateString(5, 4), this.game.getCoordinateString(5, 5), this.game.getCoordinateString(5, 6),
                this.game.getCoordinateString(5, 7), this.game.getCoordinateString(6, 0), this.game.getCoordinateString(6, 1),
                this.game.getCoordinateString(6, 2), this.game.getCoordinateString(6, 3), this.game.getCoordinateString(6, 4),
                this.game.getCoordinateString(6, 5), this.game.getCoordinateString(6, 6), this.game.getCoordinateString(6, 7),
                this.game.getCoordinateString(7, 0), this.game.getCoordinateString(7, 1), this.game.getCoordinateString(7, 2),
                this.game.getCoordinateString(7, 3), this.game.getCoordinateString(7, 4), this.game.getCoordinateString(7, 5),
                this.game.getCoordinateString(7, 6), this.game.getCoordinateString(7, 7),
                enterprise.getPosition().getQuadrant().getX() + 1, enterprise.getPosition().getQuadrant().getY() + 1);

        // con.printf("\nSTAR CHART FOR THE KNOWN GALAXY\n\n");
        // con.printf(" 1 2 3 4 5 6 7 8\n");
        // con.printf(" -----------------------------------------\n");
        // con.printf(" - \n");
        // con.printf("1 - %-3s %-3s %-3s %-3s %-3s %-3s %-3s %-3s -\n",
        // game.getCoordinateString(0, 0), game.getCoordinateString(0, 1),
        // game.getCoordinateString(0, 2), game.getCoordinateString(0, 3),
        // game.getCoordinateString(0, 4), game.getCoordinateString(0, 5),
        // game.getCoordinateString(0, 6), game.getCoordinateString(0, 7));
        // con.printf("2 - %-3s %-3s %-3s %-3s %-3s %-3s %-3s %-3s -\n",
        // game.getCoordinateString(1, 0), game.getCoordinateString(1, 1),
        // game.getCoordinateString(1, 2), game.getCoordinateString(1, 3),
        // game.getCoordinateString(1, 4), game.getCoordinateString(1, 5),
        // game.getCoordinateString(1, 6), game.getCoordinateString(1, 7));
        // con.printf("3 - %-3s %-3s %-3s %-3s %-3s %-3s %-3s %-3s -\n",
        // game.getCoordinateString(2, 0), game.getCoordinateString(2, 1),
        // game.getCoordinateString(2, 2), game.getCoordinateString(2, 3),
        // game.getCoordinateString(2, 4), game.getCoordinateString(2, 5),
        // game.getCoordinateString(2, 6), game.getCoordinateString(2, 7));
        // con.printf("4 - %-3s %-3s %-3s %-3s %-3s %-3s %-3s %-3s -\n",
        // game.getCoordinateString(3, 0), game.getCoordinateString(3, 1),
        // game.getCoordinateString(3, 2), game.getCoordinateString(3, 3),
        // game.getCoordinateString(3, 4), game.getCoordinateString(3, 5),
        // game.getCoordinateString(3, 6), game.getCoordinateString(3, 7));
        // con.printf("5 - %-3s %-3s %-3s %-3s %-3s %-3s %-3s %-3s -\n",
        // game.getCoordinateString(4, 0), game.getCoordinateString(4, 1),
        // game.getCoordinateString(4, 2), game.getCoordinateString(4, 3),
        // game.getCoordinateString(4, 4), game.getCoordinateString(4, 5),
        // game.getCoordinateString(4, 6), game.getCoordinateString(4,7));
        // con.printf("6 - %-3s %-3s %-3s %-3s %-3s %-3s %-3s %-3s -\n",
        // game.getCoordinateString(5, 0), game.getCoordinateString(5, 1),
        // game.getCoordinateString(5, 2), game.getCoordinateString(5, 3),
        // game.getCoordinateString(5, 4), game.getCoordinateString(5, 5),
        // game.getCoordinateString(5, 6), game.getCoordinateString(5, 7));
        // con.printf("7 - %-3s %-3s %-3s %-3s %-3s %-3s %-3s %-3s -\n",
        // game.getCoordinateString(6, 0), game.getCoordinateString(6, 1),
        // game.getCoordinateString(6, 2), game.getCoordinateString(6, 3),
        // game.getCoordinateString(6, 4), game.getCoordinateString(6, 5),
        // game.getCoordinateString(6, 6), game.getCoordinateString(6, 7));
        // con.printf("8 - %-3s %-3s %-3s %-3s %-3s %-3s %-3s %-3s -\n",
        // game.getCoordinateString(7, 0), game.getCoordinateString(7, 1),
        // game.getCoordinateString(7, 2), game.getCoordinateString(7, 3),
        // game.getCoordinateString(7, 4), game.getCoordinateString(7, 5),
        // game.getCoordinateString(7, 6), game.getCoordinateString(7, 7));
        // con.printf("\nThe Enterprise is currently in Quadrant %d - %d\n",
        // enterprise.getPosition().getQuadrant().getX() + 1,
        // enterprise.getPosition().getQuadrant().getY()+1);

    }
}
