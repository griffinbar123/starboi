package Model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Data
@EqualsAndHashCode(callSuper = true)
public class Klingon extends Enemy {
    public Klingon(@NonNull Position position) {
        super(EntityType.KLINGON, position);
    }
}
