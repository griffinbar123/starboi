package Model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Data
@EqualsAndHashCode(callSuper = true)
public class Star extends Entity {
    private final char symbol = '*';

    public Star(@NonNull Position position) {
        super(position);
    }
}
