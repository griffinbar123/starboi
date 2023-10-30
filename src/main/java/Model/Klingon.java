package Model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Data
@EqualsAndHashCode(callSuper = true)
public class Klingon extends Entity {
    private double warp;
    private double power;

    @JsonIgnore
    private final char symbol = 'K';

    public Klingon(@NonNull Position position) {
        super(position);
    }
}
