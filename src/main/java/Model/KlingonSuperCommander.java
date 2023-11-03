package Model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Data
@EqualsAndHashCode(callSuper = true)
public class KlingonSuperCommander extends Enemy {
    public KlingonSuperCommander(@NonNull Position position) {
        super(EntityType.SUPER_COMMANDER, position);
    }
}
