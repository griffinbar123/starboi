package Model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Data
@EqualsAndHashCode(callSuper = true)
public class Romulan extends Entity {
    private double health;
    private final char symbol = 'R';

    public Romulan(@NonNull Position position) {
        super(position);
    }
}
