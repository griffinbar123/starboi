package Model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Data
@EqualsAndHashCode(callSuper = true)
public class Romulan extends Enemy {
    public Romulan(@NonNull Position position) {
        super(EntityType.ROMULAN, position);
    }
}
