package Model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Entity {
    public char getSymbol() {
        return type.getSymbol();
    }

    public String getName() {
        return type.getName();
    }

    private EntityType type;
    private Position position;
}
