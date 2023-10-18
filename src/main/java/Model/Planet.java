package Model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Data
@EqualsAndHashCode(callSuper = true)
public class Planet extends Entity {
    private final char symbol = 'P';

    public Planet(@NonNull Position position) {
        super(position);
    }
}
