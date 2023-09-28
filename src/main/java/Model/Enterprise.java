package Model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Enterprise extends Entity {
    private float starDate;
    private String condition;

    @NonNull
    private Position position;

    private byte lifeSupport;

    private float warp;
    private float energy;

    private int torpedoes;

    private Sheild sheilds;

    private int klingons;

    private float time;
}
