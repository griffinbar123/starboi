package sst;

import static Utils.Utils.*;
import java.util.HashMap;
import java.util.Map;
import Model.Device;
import Model.Enterprise;
import Model.Entity;
import Model.EntityType;
import Model.Game;
import Model.ShieldStatus;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import sst.Finish.GameOverReason;

/**
 * implements the damages command and functionality
 * 
 * @author Fabrice Mpozenzi
 */
@RequiredArgsConstructor
public class Damages {
    @NonNull
    private Game game;
    public Damages typeDamageScore;
    /**
     * Factor by which repair times are multiplied when docked
     */
    private final double docFac = 0.25;

    /**
     * DAMAGES command implementation
     * 
     * @author Fabrice Mpozenzi
     */
    public void ExecDAMAGES() {
        Map<Device, Double> repairTimes = calcDamages();
        StringBuilder sb = new StringBuilder();

        if (repairTimes.size() == 0) {
            game.con.printf("\nAll devices functional.\n");
            return;
        }

        sb.append("\nDevice            -REPAIR TIMES-\n");
        sb.append("                IN FLIGHT   DOCKED\n");
        for (Device device : Device.values()) {
            if (!repairTimes.containsKey(device)) {
                continue;
            }

            sb.append(String.format("  %16s %8.2f  %8.2f\n",
                    device.getDeviceName(),
                    repairTimes.get(device),
                    repairTimes.get(device) * docFac));
        }
        game.con.printf("%s", sb.toString());
    }

    /**
     * Calculates the repair times for each device
     * 
     * @return a map of devices and their (in-flight) repair times
     * @author Matthias Schrock
     * @author Fabrice Mpozenzi
     */
    private Map<Device, Double> calcDamages() {
        Map<Device, Double> repairTimes = new HashMap<>();
        Enterprise enterprise = game.getEnterprise();

        for (Device device : Device.values()) {
            if (enterprise.getDeviceDamage().get(device) > 0.0) {
                if (device == Device.DEATHRAY) {
                    repairTimes.put(device,
                            enterprise.getDeviceDamage().get(device) + 0.005);
                } else {
                    repairTimes.put(device,
                            enterprise.getDeviceDamage().get(device) + 0.05);
                }
            }
        }

        return repairTimes;
    }

    /**
     * Public facing method for calculating and updating damages throughout
     * the game as damage is incurred by any entitity
     */

    public void assessDamages() {
        // update devices with damage incurred
        /*
         * for (Device device : Device.values()) {
         * // if should be damaged ... else ... (no damage)
         * if (game.getEnterprise().getDeviceDamage().get(device) > 0.0) {
         * game.getEnterprise().getDeviceDamage().put(device,
         * game.getEnterprise().getDeviceDamage().get(device) + 0.05);
         * }
         * }
         */
        double typeDamageScore = 1.0;
        Entity entity = game.getGameMap().getEntityAtPosition(game.getEnterprise().getPosition());
        EntityType entityType = entity.getType();

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

        for (Map.Entry<Device, Double> entry : game.getEnterprise().getDeviceDamage().entrySet()) {
            if (entry.getKey() == Device.DEATHRAY)
                continue;
            if (entry.getValue() < 0)
                continue;
            double damage = (10.0 * typeDamageScore * randDouble(0, 1) + 1.0) * game.getDamageFactor();
            game.getEnterprise().getDeviceDamage().put(entry.getKey(), damage);

        }
        game.getEnterprise().getSheilds().setStatus(ShieldStatus.DOWN);

        if (game.getRemainingKlingonCount() > 0) {
            // pause
            new Damages(game).ExecDAMAGES();
        } else {
            new Finish(game).finish(GameOverReason.WON);
        }

        // make sure to call print after this update damages
        ExecDAMAGES();
    }
}