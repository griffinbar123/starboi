package Model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Data
@EqualsAndHashCode(callSuper = true)
public class Klingon extends Entity {
    private double health;
    private double warp;
    private double power;

    private final char symbol = 'K';

    public Klingon(@NonNull Position position) {
        super(position);
    }
}
