package sst;

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
        StringBuilder sb = new StringBuilder();
        if (this.game.getEnterprise().getCondition() == Condition.DOCKED) {
            sb.append("\nAlready docked.\n\n");
            return;
        } else if (!isAdjacentStarbase()) {
            sb.append("Enterprise not adjacent to base.\n\n");
            return;
        }

        sb.append("Docked.\n\n");
        this.game.getEnterprise().setCondition(Condition.DOCKED);
        // Undocking/condition reevaluation is handled in Move.java when successful move
        // is made

        game.con.printf(sb.toString());
    }

    /**
     * Checks if the Enterprise is adjacent to a starbase for docking
     * 
     * @return true if adjacent to starbase, false otherwise
     */
    private boolean isAdjacentStarbase() {
        Position ncc = game.getEnterprise().getPosition();
        if (game.checkPositionForEntity(ncc.getTopRightPosition(), EntityType.STARBASE) ||
                game.checkPositionForEntity(ncc.getTopMiddlePosition(), EntityType.STARBASE) ||
                game.checkPositionForEntity(ncc.getTopLeftPosition(), EntityType.STARBASE) ||
                game.checkPositionForEntity(ncc.getMiddleRightPosition(), EntityType.STARBASE) ||
                game.checkPositionForEntity(ncc.getMiddleLeftPosition(), EntityType.STARBASE) ||
                game.checkPositionForEntity(ncc.getBotRightPosition(), EntityType.STARBASE) ||
                game.checkPositionForEntity(ncc.getBotMiddlePosition(), EntityType.STARBASE) ||
                game.checkPositionForEntity(ncc.getBotLeftPosition(), EntityType.STARBASE)) {
            return true;
        }
        return false;
    }
}
