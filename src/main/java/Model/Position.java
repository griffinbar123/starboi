package Model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Position {
    private Coordinate quadrant;
    private Coordinate sector;

    public int getXAsInt() {
        return (quadrant.getX() * 10) + sector.getX();
    }

    public int getYAsInt() {
        return (quadrant.getY() * 10) + sector.getY();
    }

    public static Position turnIntToPosition(int y, int x) {
        return new Position(
            new Coordinate((int) (y/10), (int) (x/10)),
            new Coordinate(y%10, x%10)
        );
    }
}
