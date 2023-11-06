package Model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Data
@EqualsAndHashCode(callSuper = true)
public class BlackHole extends Entity {
    public BlackHole(@NonNull Position position) {
        super(EntityType.BLACK_HOLE, position);
    }
}
