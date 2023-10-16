package Model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Klingon extends Entity {
    // TODO: extend klingon for different types
    public enum KlingonType {
        REGULAR,
        SUPER,
        COMMANDER;
    }

    @NonNull
    private Position position;

    private KlingonType type;

    private double health;
    private final char symbol = 'K';
}
