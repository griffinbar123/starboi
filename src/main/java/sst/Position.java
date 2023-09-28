package sst;

import lombok.Data;

@Data
public class Position {

    public Coordinate Quadrant;
    public Coordinate Sector;

    public Position(Coordinate quadrant, Coordinate sector) {
        Quadrant = quadrant;
        Sector = sector;
    }

}
