package sst;

import java.io.Console;

import Model.Enterprise;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Handles the STATUS command
 * 
 * @author Matthias Schrock
 * @see Enterprise
 */
@RequiredArgsConstructor
public class Status {
    private Console con;
    @NonNull
    private Game game;

    /**
     * Prints the status of the Enterprise during the game
     */
    public void ExecSTATUS() {
        // Initialize console
        con = System.console();
        if (con == null)
            return;

        // print status
        Enterprise enterprise = this.game.getEnterprise();
        con.printf("Stardate      %.1f\n", enterprise.getStarDate());
        con.printf("Condition     %s\n", enterprise.getCondition());
        con.printf("Position      %d - %d, %d - %d\n",
                (enterprise.getPosition().getQuadrant().getX() + 1),
                (enterprise.getPosition().getQuadrant().getY() + 1),
                (enterprise.getPosition().getSector().getX() + 1),
                (enterprise.getPosition().getSector().getY() + 1));
        con.printf("Life Support  %s\n", (enterprise.getLifeSupport() == 1 ? "ACTIVE" : "RESERVES"));
        con.printf("Warp Factor   %.1f\n", enterprise.getWarp());
        con.printf("Energy        %.2f\n", enterprise.getEnergy());
        con.printf("Torpedoes     %d\n", enterprise.getTorpedoes());
        con.printf("Shields       %s, %.0f%% %.1f units\n",
                (enterprise.getSheilds().getActive() == 1 ? "ACTIVE" : "DOWN"),
                enterprise.getSheilds().getLevel() / 100,
                enterprise.getSheilds().getUnits());
        con.printf("Klingons Left %d\n", enterprise.getKlingons());
        con.printf("Time Left     %.2f\n", enterprise.getTime());
    }
}
