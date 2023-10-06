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
    @NonNull
    private Game game;

    /**
     * Prints the status of the Enterprise during the game
     */
    public void ExecSTATUS() {
        // print status
        Enterprise enterprise = this.game.getEnterprise();
        this.game.con.printf("Stardate      %.1f\n", enterprise.getStarDate());
        this.game.con.printf("Condition     %s\n", enterprise.getCondition());
        this.game.con.printf("Position      %d - %d, %d - %d\n",
                (enterprise.getPosition().getQuadrant().getX() + 1),
                (enterprise.getPosition().getQuadrant().getY() + 1),
                (enterprise.getPosition().getSector().getX() + 1),
                (enterprise.getPosition().getSector().getY() + 1));
        this.game.con.printf("Life Support  %s\n", (enterprise.getLifeSupport() == 1 ? "ACTIVE" : "RESERVES"));
        this.game.con.printf("Warp Factor   %.1f\n", enterprise.getWarp());
        this.game.con.printf("Energy        %.2f\n", enterprise.getEnergy());
        this.game.con.printf("Torpedoes     %d\n", enterprise.getTorpedoes());
        this.game.con.printf("Shields       %s, %.0f%% %.1f units\n",
                (enterprise.getSheilds().getActive() == 1 ? "ACTIVE" : "DOWN"),
                enterprise.getSheilds().getLevel() / 100,
                enterprise.getSheilds().getUnits());
        this.game.con.printf("Klingons Left %d\n", enterprise.getKlingons());
        this.game.con.printf("Time Left     %.2f\n", enterprise.getTime());
    }
}
