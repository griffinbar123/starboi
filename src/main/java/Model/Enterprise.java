package Model;

import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Data
@EqualsAndHashCode(callSuper = true)
public class Enterprise extends Entity {
    public Enterprise(@NonNull Position position) {
        super(EntityType.ENTERPRISE, position);
    }

    private Map<Device, Double> deviceDamage;

    private Condition condition = Condition.GREEN;

    private byte lifeSupport;

    private Boolean cloak = false;

    private double warp = 5.0;
    private double energy = 5000.0;

    private int torpedoes = 10;

    private Shield sheilds = new Shield();
}
