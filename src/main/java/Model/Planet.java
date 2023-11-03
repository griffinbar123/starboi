package Model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Data
@EqualsAndHashCode(callSuper = true)
public class Planet extends Entity {
    public Planet(@NonNull Position position) {
        super(EntityType.PLANET, position);
    }
}
