package Model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Enemy extends Entity {
    public Enemy(EntityType type, Position position) {
        super(type, position);
    }

    private double warp;
    private double power;
}
