package Model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Data
@EqualsAndHashCode(callSuper = true)
public class KlingonCommander extends Klingon {
    @JsonIgnore
    private final char symbol = 'C';

    public KlingonCommander(@NonNull Position position) {
        super(position);
    }
}