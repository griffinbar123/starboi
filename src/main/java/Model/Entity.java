package Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@AllArgsConstructor
@EqualsAndHashCode
@ToString(includeFieldNames = true)
public class Entity {
    public char getSymbol() {
        return type.getSymbol();
    }

    public String getName() {
        return type.getName();
    }

    private EntityType type = EntityType.undefined;
    private Position position;
}
