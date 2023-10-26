package Model;

import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;

import lombok.AllArgsConstructor;
import lombok.Data;
import sst.Computer;

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

    public Position getPositionFromOffset(int xOffset, int yOffset) { //
        return Position.turnIntToPosition(getYAsInt() + yOffset, getXAsInt() + xOffset);
    }

    // functions to get adjecent sectors
    public Position getTopLeftPosition() {
        return getPositionFromOffset(-1, -1);
    }
    public Position getTopMiddlePosition() {
        return getPositionFromOffset(-1, 0);
    }
    public Position getTopRightPosition() {
        return getPositionFromOffset(-1, 1);
    }
    public Position getMiddleLeftPosition() {
        return getPositionFromOffset(0, -1);
    }
    public Position getMiddleRightPosition() {
        return getPositionFromOffset(0, 1);
    }
    public Position getBotLeftPosition() {
        return getPositionFromOffset(1, -1);
    }
    public Position getBotMiddlePosition() {
        return getPositionFromOffset(1, 0);
    }
    public Position getBotRightPosition() {
        return getPositionFromOffset(1, 1);
    }

    public Position getClosestAdjecentPositionToDestination(Position destPos, Computer computer){

        Map<Position, Double> scores = Map.of(
            getTopLeftPosition(), computer.calcDistance(destPos, getTopLeftPosition()),
            getTopMiddlePosition(), computer.calcDistance(destPos, getTopMiddlePosition()),
            getTopRightPosition(), computer.calcDistance(destPos, getTopRightPosition()),
            getMiddleLeftPosition(), computer.calcDistance(destPos, getMiddleLeftPosition()),
            getMiddleRightPosition(), computer.calcDistance(destPos, getMiddleRightPosition()),
            getBotLeftPosition(), computer.calcDistance(destPos, getBotLeftPosition()),
            getBotMiddlePosition(), computer.calcDistance(destPos, getBotMiddlePosition()),
            getBotRightPosition(), computer.calcDistance(destPos, getBotRightPosition())
        );
        Entry<Position, Double> minEntry = Collections.min(scores.entrySet(), Comparator.comparing(Entry::getValue));
        return minEntry.getKey(); 
    }
}
