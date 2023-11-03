package Model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Data
@EqualsAndHashCode(callSuper = true)
public class KlingonCommander extends Enemy {
    public KlingonCommander(@NonNull Position position) {
        super(EntityType.COMMANDER, position);
    }
}