package Model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Data
@EqualsAndHashCode(callSuper = true)
public class Enterprise extends Entity {
    public enum Devices {
        SR_SENSORS(1, "S. R. Sensors", 0.0),
        LR_SENSORS(2, "L. R. Sensors", 0.0),
        PHASERS(3, "Phasers", 0.0),
        PHOTON_TUBES(4, "ProtonTubes", 0.0),
        LIFE_SUPPORT(5, "Life Support", 0.0),
        WARP_ENGINES(6, "Warp Engines", 0.0),
        IMPULSE_ENGINES(7, "Impulse Engines", 0.0),
        SHIELDS(8, "Shields", 0.0),
        SUBSPACE_RADIO(9, "Subspace Radio", 0.0),
        SHUTTLE_CRAFT(10, "Shuttle Craft", 0.0),
        COMPUTER(11, "Computer", 0.0),
        TRANSPORTER(12, "Transporter", 0.0),
        SHIELD_CONTROL(13, "Shield Control", 0.0),
        DEATHRAY(14, "Death Ray", 0.0),
        DS_PROBE(15, "D. S. Probe", 0.0),
        CLOAKING_DEVICE(16, "Cloaking Device", 0.0);

        private final int deviceNum;
        private final String deviceName;
        private final double damage;

        private Devices(int deviceNum, String deviceName, double damage) {
            this.deviceNum = deviceNum;
            this.deviceName = deviceName;
            this.damage = damage;
        }

        public int getDeviceNum() {
            return deviceNum;
        }

        public String getDeviceName() {
            return deviceName;
        }

        public double getDamage() {
            return damage;
        }
    }
    private Devices devices;

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
