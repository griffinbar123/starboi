package sst;

import java.util.HashMap;
import java.util.Map;

import Model.Game;
import Model.Enterprise.Devices;
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
        Map<Devices, Double> repairTimes = calcDamages();

        if (repairTimes.size() == 0) {
            game.con.printf("All devices functional.\n");
            return;
        }

        game.con.printf("Device       -REPAIR TIMES-\n");
        game.con.printf("             IN FLIGHT  DOCKED\n");
        for (Devices device : Devices.values()) {
            game.con.printf("  %16s %.2f    %.2f\n", device.getDeviceName(), repairTimes.get(device), repairTimes.get(device) * docFac);
        }
    }

    /**
     * Calculates the repair times for each device
     * @return a map of devices and their (in-flight) repair times
     * @author Matthias Schrock
     * @author Fabrice Mpozenzi
     */
    private Map<Devices, Double> calcDamages() {
        Map<Devices, Double> repairTimes = new HashMap<>();

        for (Devices device : game.getEnterprise().getDevices()) {
            if (device.getDamage() > 0.0) {
                if (device == Devices.DEATHRAY) {
                    repairTimes.put(device, device.getDamage() + 0.005);
                } else {
                    repairTimes.put(device, device.getDamage() + 0.05);
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
        Devices[] devices = game.getEnterprise().getDevices();
        for (Devices device : devices) {
            // if should be damaged ... else ... (no damage)
            if (device.getDamage() > 0.0) {
                device.setDamage(device.getDamage() + 0.05);
            }
        }

        // make sure to call print after this update damages
        ExecDAMAGES();
    }
}
