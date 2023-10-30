package Model;

/**
 * Enterprise's devices with their corresponding numbers and names
 */
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
