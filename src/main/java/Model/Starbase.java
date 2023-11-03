package Model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Data
@EqualsAndHashCode(callSuper = true)
public class Starbase extends Entity {
    public Starbase(@NonNull Position position) {
        super(EntityType.STARBASE, position);
    }
}
