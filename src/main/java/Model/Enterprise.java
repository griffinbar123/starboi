package Model;

import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Data
@EqualsAndHashCode(callSuper = true)
public class Enterprise extends Entity {
    public enum Device {
        SR_SENSORS(1, "S. R. Sensors"),
        LR_SENSORS(2, "L. R. Sensors"),
        PHASERS(3, "Phasers"),
        PHOTON_TUBES(4, "ProtonTubes"),
        LIFE_SUPPORT(5, "Life Support"),
        WARP_ENGINES(6, "Warp Engines"),
        IMPULSE_ENGINES(7, "Impulse Engines"),
        SHIELDS(8, "Shields"),
        SUBSPACE_RADIO(9, "Subspace Radio"),
        SHUTTLE_CRAFT(10, "Shuttle Craft"),
        COMPUTER(11, "Computer"),
        TRANSPORTER(12, "Transporter"),
        SHIELD_CONTROL(13, "Shield Control"),
        DEATHRAY(14, "Death Ray"),
        DS_PROBE(15, "D. S. Probe"),
        CLOAKING_DEVICE(16, "Cloaking Device");

        private final int deviceNum;
        private final String deviceName;

        private Device(int deviceNum, String deviceName) {
            this.deviceNum = deviceNum;
            this.deviceName = deviceName;
        }

        public int getDeviceNum() {
            return deviceNum;
        }

        public String getDeviceName() {
            return deviceName;
        }
    }
    private Map<Device, Double> deviceDamage = new HashMap<Device, Double>();

    private String condition;

    private byte lifeSupport;

    private double warp = 5.0f;
    private double energy = 5000.0f;

    private int torpedoes;

    private Sheild sheilds = new Sheild();

    private final char symbol = 'E';

    public Enterprise(@NonNull Position position) {
        super(position);
    }
}
