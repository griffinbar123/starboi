package sst;

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
        Enterprise enterprise = this.game.getEnterprise();
        String stat = "Stardate      %.1f\n" +
                "Condition     %s\n" +
                "Position      %d - %d, %d - %d\n" +
                "Life Support  %s\n" +
                "Warp Factor   %.1f\n" +
                "Energy        %.2f\n" +
                "Torpedoes     %d\n" +
                "Shields       %s, %.0f%% %.1f units\n" +
                "Klingons Left %d\n" +
                "Time Left     %.2f\n";
        this.game.con.printf(stat,
                enterprise.getStarDate(),
                enterprise.getCondition(),
                enterprise.getPosition().getQuadrant().getX(),
                enterprise.getPosition().getQuadrant().getY(),
                enterprise.getPosition().getSector().getX(),
                enterprise.getPosition().getSector().getY(),
                (enterprise.getLifeSupport() == 1 ? "ACTIVE" : "RESERVES"),
                enterprise.getWarp(), enterprise.getEnergy(), enterprise.getTorpedoes(),
                (enterprise.getSheilds().getActive() == 1 ? "ACTIVE" : "DOWN"),
                enterprise.getSheilds().getLevel() / 100,
                enterprise.getSheilds().getUnits(),
                enterprise.getKlingons(),
                enterprise.getTime());
    }
}
