package Model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Data
@EqualsAndHashCode(callSuper = true)
public class Enterprise extends Entity {
    private boolean srSensors = true;
    private boolean lrSensors = true;
    private boolean phasers = true;
    private boolean photonTubes = true;
    private boolean lifeSupportStutus = true;
    private boolean warpEngines = true;
    private boolean impulseEngines = true;
    private boolean shields = true;
    private boolean subspaceRadio = true;
    private boolean shuttleCraft = true;
    private boolean computer = true;
    private boolean trasnporter = true;
    private boolean shieldControl = true;
    private boolean dsProbe = true;
    private boolean cloakingDevice = true;

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
