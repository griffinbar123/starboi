package sst;

import java.util.Arrays;

import Model.Condition;
import Model.EntityType;
import Model.Game;
import Model.Position;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Handles docking to starbases
 * 
 * @author Matthias Schrock
 */
@RequiredArgsConstructor
public class Dock {
    @NonNull
    private Game game;

    /**
     * executes the DOCK comamnd. Docking will allow the player to repair
     * the ship and refuel more quickly
     * 
     * @author Matthias Schrock
     * @see sst.Rest
     */
    public void ExecDOCK() {
        if (this.game.getEnterprise().getCondition() == Condition.DOCKED) {
            game.con.printf("Already docked.\n\n");
            return;
        } else if (!isAdjacentStarbase()) {
            game.con.printf("Enterprise not adjacent to base.\n\n");
            return;
        }

        this.game.getEnterprise().setCondition(Condition.DOCKED);
        game.con.printf("Docked.\n\n");
        // Undocking/condition reevaluation is handled in Move.java when successful move
        // is made
    }

    /**
     * Checks if the Enterprise is adjacent to a starbase for docking
     * 
     * @return true if adjacent to starbase, false otherwise
     */
    private boolean isAdjacentStarbase() {
        Position ncc = game.getEnterprise().getPosition();
        return Arrays.stream(ncc.getAdjecentPositions())
                .anyMatch(p -> game.checkPositionForEntity(p, EntityType.STARBASE));
    }
}
