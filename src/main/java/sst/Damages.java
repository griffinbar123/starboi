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
    @Getter
    private final double DOCFAC = 0.25;

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
                    repairTimes.get(device) * DOCFAC));
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
    public Map<Device, Double> calcDamages() {
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

}
