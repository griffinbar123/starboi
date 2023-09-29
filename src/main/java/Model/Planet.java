package Model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;


@Data
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Planet extends Entity {
    @NonNull
    private Position position;

    private char symbol = 'P';
}
