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
    private Console con;
    @NonNull
    private Game game;

    /**
     * Prints the status of the Enterprise during the game
     */
    public void ExecSTATUS() {
        Enterprise enterprise = this.game.getEnterprise();
        System.out.println("Stardate\t" + enterprise.getStarDate());
        System.out.println("Condition\t" + enterprise.getCondition());
        System.out.println("Position\t" + (enterprise.getPosition().getQuadrant().getX() + 1) + " - "
                + (enterprise.getPosition().getQuadrant().getY() + 1) + ", "
                + (enterprise.getPosition().getSector().getX() + 1) + " - "
                + (enterprise.getPosition().getSector().getY() + 1));
        System.out.println("Life Support\t" + (enterprise.getLifeSupport() == 1 ? "ACTIVE" : "RESERVES"));
        System.out.print("Warp Factor\t");
        System.out.printf("%.1f\n", enterprise.getWarp());
        System.out.print("Energy\t\t");
        System.out.printf("%.2f\n", enterprise.getEnergy());
        System.out.println("Torpedoes\t" + enterprise.getTorpedoes());
        System.out.print("Sheilds\t\t");
        System.out.printf("%s, %.0f%% %.1f units\n", (enterprise.getSheilds().getActive() == 1 ? "ACTIVE" : "DOWN"),
                enterprise.getSheilds().getLevel() / 100, enterprise.getSheilds().getUnits());
        System.out.println("Klingons Left\t" + enterprise.getKlingons());
        System.out.print("Time Left\t");
        System.out.printf("%.2f\n", enterprise.getTime());
    }
}
