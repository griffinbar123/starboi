package Model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Data
@EqualsAndHashCode(callSuper = true)
public class Starbase extends Entity {
    @JsonIgnore
    private final char symbol = 'B';

    public Starbase(@NonNull Position position) {
        super(position);
    }
}
