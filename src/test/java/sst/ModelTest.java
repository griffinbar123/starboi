package sst;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import Model.Coordinate;
import Model.Enterprise;
import Model.Entity;
import Model.EntityType;
import Model.Position;

public class ModelTest {
    @Test
    public void getEntitySymbolShouldReturnCorrectSymbol() {
        Entity entity = null;
        
        for (EntityType e : EntityType.values()) {
            entity = new Entity(e, new Position(0, 0, 0, 0));
            assert entity.getSymbol() == e.getSymbol();
        }
    }

    @Test
    public void getEntityNameShouldReturnCorrectName() {
        Entity entity = null;
        
        for (EntityType e : EntityType.values()) {
            entity = new Entity(e, new Position(0, 0, 0, 0));
            assert entity.getName() == e.getName();
        }
    }

    @Test
    public void positionShouldInitializeProperCoordinates() {
        Position position = new Position(1, 2, 3, 4);
        assert position.getQuadrant().getY() == 1;
        assert position.getQuadrant().getX() == 2;
        assert position.getSector().getY() == 3;
        assert position.getSector().getX() == 4;

        Coordinate coordinate = new Coordinate(1, 2);
        position = new Position(coordinate, 3, 4);
        assert position.getQuadrant().getY() == 1;
        assert position.getQuadrant().getX() == 2;
        assert position.getSector().getY() == 3;
        assert position.getSector().getX() == 4;
    }

    @Test
    public void positionEqualityShouldBehaveAsExpected() {
        Position position1 = new Position(1, 1, 1, 1);
        Position position2 = new Position(1, 1, 1, 1);
        assert(position1.isEqual(position2));

        position2 = new Position(1, 1, 1, 2);
        assert(!position1.isEqual(position2));
    }

    @Test
    public void positionQuadrantEqualityShouldBehaveAsExpected() {
        Position position1 = new Position(1, 1, 1, 1);
        Position position2 = new Position(1, 1, 5, 5);
        assert(position1.isEqualQuadrant(position2));

        position2 = new Position(1, 2, 1, 1);
        assert(!position1.isEqualQuadrant(position2));
    }

    @Test
    public void positionAdjacencyShouldWorkAsExpected() {
        Position position = new Position(1, 1, 5, 5);

        assertEquals(new Position(1, 1, 4, 4), position.getTopLeftPosition());
        assertEquals(new Position(1, 1, 5, 4), position.getTopMiddlePosition());
        assertEquals(new Position(1, 1, 6, 4), position.getTopRightPosition());
        assertEquals(new Position(1, 1, 4, 5), position.getMiddleLeftPosition());
        assertEquals(new Position(1, 1, 6, 5), position.getMiddleRightPosition());
        assertEquals(new Position(1, 1, 4, 6), position.getBotLeftPosition());
        assertEquals(new Position(1, 1, 5, 6), position.getBotMiddlePosition());
        assertEquals(new Position(1, 1, 6, 6), position.getBotRightPosition());

        Position[] positions = position.getAdjecentPositions();
        assertEquals(new Position(1, 1, 4, 4), positions[0]);
        assertEquals(new Position(1, 1, 5, 4), positions[1]);
        assertEquals(new Position(1, 1, 6, 4), positions[2]);
        assertEquals(new Position(1, 1, 4, 5), positions[3]);
        assertEquals(new Position(1, 1, 6, 5), positions[4]);
        assertEquals(new Position(1, 1, 4, 6), positions[5]);
        assertEquals(new Position(1, 1, 5, 6), positions[6]);
        assertEquals(new Position(1, 1, 6, 6), positions[7]);
    }

    @Test
    public void positionsShouldNotCollideWithInterquadrantMovement() {
        Position position = new Position(1, 1, 5, 5);
        Position destination = new Position(2, 2, 5, 5);

        assertEquals(position.getClosestAdjecentPositionToDestination(destination), new Position(1, 1, 6, 6));
    }

    @Test
    public void enterpriseShouldInitializeWithCorrectParameters() {
        Position position = new Position(1, 1, 1, 1);
        Enterprise enterprise = new Enterprise(position);
        assertEquals(enterprise.getType(), EntityType.ENTERPRISE);
        assertEquals(enterprise.getPosition(), position);
    }
}
