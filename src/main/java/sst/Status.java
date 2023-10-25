package sst;

import Model.Enterprise;
import Model.Game;
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
                "Shields       %s, %d%% %.1f units\n" +
                "Klingons Left %d\n" +
                "Time Left     %.2f\n";
        this.game.con.printf(stat,
                this.game.getStarDate(),
                enterprise.getCondition(),
                enterprise.getPosition().getQuadrant().getY() + 1,
                enterprise.getPosition().getQuadrant().getX() + 1,
                enterprise.getPosition().getSector().getY() + 1,
                enterprise.getPosition().getSector().getX() + 1,
                (enterprise.getLifeSupport() == 1 ? "ACTIVE" : "RESERVES"),
                enterprise.getWarp(), enterprise.getEnergy(), enterprise.getTorpedoes(),
                enterprise.getSheilds().getActive(),
                enterprise.getSheilds().getLevel(),
                enterprise.getSheilds().getUnits(),
                game.getKlingonCount(),
                game.getTime());
    }
}
