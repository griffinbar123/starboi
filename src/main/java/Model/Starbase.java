package Model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Data
@EqualsAndHashCode(callSuper = true)
public class Starbase extends Entity {
    private final char symbol = 'B';

    public Starbase(@NonNull Position position) {
        super(position);
    }
}
