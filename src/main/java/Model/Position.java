package Model;

import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Position {
    public Position (int qY, int qX, int sY, int sX) {
        this.quadrant = new Coordinate(qY, qX);
        this.sector = new Coordinate(sY, sX);
    }

    public Position (Coordinate quadrant, int sY, int sX) {
        this.quadrant = quadrant;
        this.sector = new Coordinate(sY, sX);
    }

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

    public Position[] getAdjecentPositions() {
        Position[] positions = {getTopLeftPosition(), getTopMiddlePosition(), getTopRightPosition(), getMiddleLeftPosition(), getMiddleRightPosition(), getBotLeftPosition(), getBotMiddlePosition(), getBotRightPosition()};
        return positions;
    }

    /**
     * calculate the distance bteween two positions
     * 
     * @param dest position to travel to
     * @return the distance between two points on the map
     * @author Griffin Barnard
     */
    public double calcDistance(Position dest) {
        int x1 = getXAsInt();
        int y1 = getYAsInt();

        int x2 = dest.getXAsInt();
        int y2 = dest.getYAsInt();
        
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

    public Position getClosestAdjecentPositionToDestination(Position destPos) {

        Map<Position, Double> scores = Map.of(
            getTopLeftPosition(), destPos.calcDistance(getTopLeftPosition()),
            getTopMiddlePosition(), destPos.calcDistance(getTopMiddlePosition()),
            getTopRightPosition(), destPos.calcDistance(getTopRightPosition()),
            getMiddleLeftPosition(), destPos.calcDistance(getMiddleLeftPosition()),
            getMiddleRightPosition(), destPos.calcDistance(getMiddleRightPosition()),
            getBotLeftPosition(), destPos.calcDistance(getBotLeftPosition()),
            getBotMiddlePosition(), destPos.calcDistance(getBotMiddlePosition()),
            getBotRightPosition(), destPos.calcDistance(getBotRightPosition())
        );
        Entry<Position, Double> minEntry = Collections.min(scores.entrySet(), Comparator.comparing(Entry::getValue));
        return minEntry.getKey(); 
    }
}
