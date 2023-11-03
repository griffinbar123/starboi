package Model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Data
@EqualsAndHashCode(callSuper = true)
public class Star extends Entity {
    public Star(@NonNull Position position) {
        super(EntityType.STAR, position);
    }
}
