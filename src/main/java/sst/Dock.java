package sst;

import Model.Condition;
import Model.EntityType;
import Model.Game;
import Model.Position;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Handles docking to starbases
 */
@RequiredArgsConstructor
public class Dock {
    @NonNull
    private Game game;
    public void ExecDOCK() {
        StringBuilder sb = new StringBuilder();

        if (isAdjacentStarbase()) {
            sb.append("Docked.\n\n");
            this.game.getEnterprise().setCondition(Condition.DOCKED);
        } else {
            sb.append("Enterprise not adjacent to base.\n\n");
        }

        game.con.printf(sb.toString());
    }

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
