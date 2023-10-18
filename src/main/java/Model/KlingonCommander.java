package Model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Data
@EqualsAndHashCode(callSuper = true)
public class KlingonCommander extends Klingon {
    private final char symbol = 'C';

    public KlingonCommander(@NonNull Position position) {
        super(position);
    }
}