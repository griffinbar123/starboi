package sst;

import java.util.HashMap;
import java.util.Map;

import Model.Enterprise;
import Model.Game;
import Model.Enterprise.Device;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * implements the damages command and functionality
 * @author Fabrice Mpozenzi
 */
@RequiredArgsConstructor
public class Damages {
    @NonNull
    private Game game;
    /**
     * Factor by which repair times are multiplied when docked
     */
    private final double docFac = 0.25;

    /**
     * DAMAGES command implementation
     * @author Fabrice Mpozenzi
     */
    public void ExecDAMAGES() {
        Map<Device, Double> repairTimes = calcDamages();
        StringBuilder sb = new StringBuilder();

        if (repairTimes.size() == 0) {
            game.con.printf("All devices functional.\n");
            return;
        }

        sb.append("Device       -REPAIR TIMES-\n");
        sb.append("             IN FLIGHT  DOCKED\n");
        for (Device device : Device.values()) {
            if (!repairTimes.containsKey(device)) {
                continue;
            }

            sb.append(String.format("  %16s %.2f    %.2f\n",
                    device.getDeviceName(),
                    repairTimes.get(device),
                    repairTimes.get(device) * docFac));
        }
        game.con.printf("%s", sb.toString());
    }

    /**
     * Calculates the repair times for each device
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
        for (Device device : Device.values()) {
            // if should be damaged ... else ... (no damage)
            if (game.getEnterprise().getDeviceDamage().get(device) > 0.0) {
                game.getEnterprise().getDeviceDamage().put(device,
                        game.getEnterprise().getDeviceDamage().get(device) + 0.05);
            }
        }

        // make sure to call print after this update damages
        ExecDAMAGES();
    }
}
