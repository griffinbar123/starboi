package Model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Enterprise extends Entity {
    private String condition;

    @NonNull
    private Position position;

    private final char symbol = 'E';

    private byte lifeSupport;

    private float warp = 5.0f;
    private float energy = 5000.0f;

    private int torpedoes;

    private Sheild sheilds = new Sheild();
}
