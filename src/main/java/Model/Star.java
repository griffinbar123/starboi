package Model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Data
@EqualsAndHashCode(callSuper = true)
public class Star extends Entity {
    @JsonIgnore
    private final char symbol = '*';

    public Star(@NonNull Position position) {
        super(position);
    }
}
