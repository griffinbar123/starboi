package Model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Data
@EqualsAndHashCode(callSuper = true)
public class KlingonSuperCommander extends Klingon {
    @JsonIgnore
    private final char symbol = 'S';

    public KlingonSuperCommander(@NonNull Position position) {
        super(position);
    }
}
