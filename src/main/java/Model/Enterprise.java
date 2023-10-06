package Model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Enterprise extends Entity {
    private float starDate;
    private String condition;

    @NonNull
    private Position position;

    private final char symbol = 'E';

    private byte lifeSupport;

    private float warp = 1.0f;
    private float energy;

    private int torpedoes;

    private Sheild sheilds = new Sheild();
}
