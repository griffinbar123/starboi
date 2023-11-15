package sst;

import static Utils.Utils.*;

import Model.Entity;
import Model.EntityType;
import Model.Game;
import Model.Position;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Ram {
    @NonNull
    private Game game;
    public Ram typeDamageScore;

    public void ram(Position position, Boolean gotRammed) {
        double typeDamageScore = 1.0;
        Integer casualties = (int) (10.0 + 20.0 * randDouble(0, 1));

        Entity entity = game.getEntityAtPosition(position);
        EntityType entityType = entity.getType();

        game.clearScreen();
        game.con.printf("***RED ALERT!  RED ALERT!\n***COLLISION IMMINENT.\n\n\n***%s %s %s at Sector %d - %d %s\n",
                "Enterprise", gotRammed ? "rammed by" : "rams", entity.getType().getName(),
                position.getSector().getY() + 1, position.getSector().getX() + 1,
                gotRammed ? "(original position)" : "");

        switch (entityType) {
            case ROMULAN:
                typeDamageScore = 1.5;
                break;
            case COMMANDER:
                typeDamageScore = 2.0;
                break;
            case SUPER_COMMANDER:
                typeDamageScore = 2.5;
                break;
            case THOLIAN:
                typeDamageScore = 0.5;
                break;
            default:
                break;
        }

        game.destroyEntityAtPosition(position);
        game.con.printf("***%s heavily damaged.\n***Sickbay reports %d casualties.\n", "Enterprise", casualties);
        game.setCasualties(game.getCasualties() + casualties);

        /*
         * // TODO: damage ship.
         * for (Map.Entry<Device, Double> entry :
         * game.getEnterprise().getDeviceDamage().entrySet()) {
         * if (entry.getKey() == Device.DEATHRAY)
         * continue;
         * if (entry.getValue() < 0)
         * continue;
         * double damage = (10.0 * typeDamageScore * randDouble(0, 1) + 1.0) *
         * game.getDamageFactor();
         * game.getEnterprise().getDeviceDamage().put(entry.getKey(), damage);
         * }
         * game.getEnterprise().getSheilds().setStatus(ShieldStatus.DOWN);
         * 
         * if (game.getRemainingKlingonCount() > 0) {
         * // pause
         * new Damages(game).ExecDAMAGES();
         * } else {
         * new Finish(game).finish(GameOverReason.WON);
         * }
         */
    }

}
