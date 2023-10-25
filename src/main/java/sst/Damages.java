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
    private final double docFac = 0.25;

    /**
     * DAMAGES command implementation
     * @author Fabrice Mpozenzi
     */
    public void ExecDAMAGES() {
        Map<Devices, Double> repairTimes = calcDamages();
        Devices devices = game.getEnterprise().getDevices();
        // game.con.printf("Device       -REPAIR TIMES-\n");
        // game.con.printf("             IN FLIGHT  DOCKED\n");
        // game.con.printf("     S. R. Sensors   %.2f    %.2f\n", 0.0, 0.0);
        // game.con.printf("     L. R. Sensors   %.2f    %.2f\n", 0.0, 0.0);
        // game.con.printf("           Phasers   %.2f    %.2f\n", 0.0, 0.0);
        // game.con.printf("      Photon Tubes   %.2f    %.2f\n", 0.0, 0.0);
        // game.con.printf("      Life Support   %.2f    %.2f\n", 0.0, 0.0);
        // game.con.printf("      Warp Engines   %.2f    %.2f\n", 0.0, 0.0);
        // game.con.printf("   Impulse Engines   %.2f    %.2f\n", 0.0, 0.0);
        // game.con.printf("           Shields   %.2f    %.2f\n", 0.0, 0.0);
        // game.con.printf("    Subspace Radio   %.2f    %.2f\n", 0.0, 0.0);
        // game.con.printf("     Shuttle Craft   %.2f    %.2f\n", 0.0, 0.0);
        // game.con.printf("          Computer   %.2f    %.2f\n", 0.0, 0.0);
        // game.con.printf("       Transporter   %.2f    %.2f\n", 0.0, 0.0);
        // game.con.printf("    Shield Control   %.2f    %.2f\n", 0.0, 0.0);
        // game.con.printf("       D. S. Probe   %.2f    %.2f\n", 0.0, 0.0);
        // game.con.printf("   Cloaking Device   %.2f    %.2f\n", 0.0, 0.0);

        game.con.printf("Device       -REPAIR TIMES-\n");
        game.con.printf("             IN FLIGHT  DOCKED\n");
        for (Devices device : Devices.values()) {
            game.con.printf("  %16s %.2f    %.2f\n", device.getDeviceName(), repairTimes.get(device), repairTimes.get(device) * docFac);
        }
    }

    private Map<Devices, Double> calcDamages() {
        Map<Devices, Double> repairTimes = new HashMap<>();
        Devices enterpriseDevices = game.getEnterprise().getDevices();

        // calculate in-flight repair times for all devices
        // TODO use the enterprise's devices rather than the device enum (which will always have damage of 0.0)
        for (Devices device : Devices.values()) {
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
        
    }
}
