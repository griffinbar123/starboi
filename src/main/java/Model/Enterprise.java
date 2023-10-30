package Model;

import java.util.Map;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Data
@EqualsAndHashCode(callSuper = true)
public class Enterprise extends Entity {
    private Map<Device, Double> deviceDamage;

    private Condition condition = Condition.GREEN;

    private byte lifeSupport;

    private Boolean cloak = false;

    private double warp = 5.0f;
    private double energy = 5000.0f;

    private int torpedoes = 10;

    private Shield sheilds = new Shield();

    @JsonIgnore
    private final char symbol = 'E';

    public Enterprise(@NonNull Position position) {
        super(position);
    }
}
