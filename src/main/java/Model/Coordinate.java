package Model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Coordinate {
    private Integer y;
    private Integer x;

    public boolean isEqual(Coordinate coord) {
        return this.y == coord.y && this.x == coord.x;
    }
}