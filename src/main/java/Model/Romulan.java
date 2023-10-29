package Model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Data
@EqualsAndHashCode(callSuper = true)
public class Romulan extends Entity {
    private double health;
    @JsonIgnore
    private final char symbol = 'R';

    public Romulan(@NonNull Position position) {
        super(position);
    }
}
