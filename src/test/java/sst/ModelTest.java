package sst;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import java.io.Console;
import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import Model.BlackHole;
import Model.Condition;
import Model.Coordinate;
import Model.Device;
import Model.Enterprise;
import Model.Entity;
import Model.EntityType;
import Model.Game;
import Model.Klingon;
import Model.KlingonCommander;
import Model.KlingonSuperCommander;
import Model.Planet;
import Model.Position;
import Model.Romulan;
import Model.Star;
import Model.Starbase;

public class ModelTest {
    private Game game;

    @Before
    public void setUp() {
        game = new Game();
        game.con = mock(Console.class);
        Enterprise enterprise = new Enterprise(new Position(1, 1, 1, 1));
        
        Map<Device, Double> deviceDamage = new HashMap<Device, Double>();
        for (Device device : Device.values()) {
            deviceDamage.put(device, 0.0);
        }
        enterprise.setDeviceDamage(deviceDamage);
        enterprise.setCondition(Condition.GREEN);
        
        EntityType map[][][][] = new EntityType[8][8][10][10];
        for (int a = 0; a < 8; a++) {
            for (int b = 0; b < 8; b++) {
                for (int c = 0; c < 10; c++) {
                    for (int d = 0; d < 10; d++) {
                        map[a][b][c][d] = EntityType.NOTHING;
                    }
                }
            }
        }
        map[1][1][1][1] = EntityType.ENTERPRISE;
        game.setEnterprise(enterprise);
        game.setMap(map);
    }

    @Test
    public void passTimeShouldPassStardate() {
        game.setTime(7);
        game.setStarDate(3000);
        game.passTime(1.0);
        assert (game.getStarDate() == 3001.0);
        assert (game.getTime() == 6);
    }

    @Test
    public void passTimeShouldPerformInFlightRepairs() {
        // TODO
    }

    @Test
    public void passTimeShouldPeformDockedFlightRepairs() {
        // TODO
    }

    @Test
    public void refreshConditionShouldWorkAsExpected() {
        game.getMap()[1][1][1][2] = EntityType.NOTHING;
        game.getMap()[1][1][2][2] = EntityType.NOTHING;
        game.getMap()[1][1][3][3] = EntityType.NOTHING;
        game.refreshCondition();
        assert (game.getEnterprise().getCondition() == Condition.GREEN);

        game.getEnterprise().setEnergy(100.0);
        game.refreshCondition();
        assert (game.getEnterprise().getCondition() == Condition.YELLOW);

        game.getMap()[1][1][1][2] = EntityType.KLINGON;
        game.getMap()[1][1][2][2] = EntityType.KLINGON;
        game.getMap()[1][1][3][3] = EntityType.COMMANDER;
        game.refreshCondition();
        assert (game.getEnterprise().getCondition() == Condition.RED);
    }

    @Test
    public void invalidInputShouldPrintError() {
        game.begPardon();
        verify(game.con).printf("\nBeg your pardon, Captain?\n");
    }

    @Test
    public void getKlingonCountShouldWorkAsExpected() {
        game.setKlingons(new Klingon[] {
            new Klingon(new Position(1, 1, 1, 2)),
            new Klingon(new Position(1, 1, 2, 2)),
            null
        });
        game.setKlingonCommanders(new KlingonCommander[] {
            new KlingonCommander(new Position(1, 1, 3, 3)),
            null
        });

        assert (game.getRemainingKlingonCount() == 3);

        game.setKlingonSuperCommander(new KlingonSuperCommander(new Position(1, 1, 4, 4)));
        assert (game.getRemainingKlingonCount() == 4);
    }

    @Test
    public void checkEntityAgainstQuadrantShouldWorkAsExpected() {
        Coordinate quadrant = new Coordinate(1, 1);
        Coordinate quadrant2 = new Coordinate(2, 2);
        Coordinate quadrant3 = new Coordinate(2, 1);
        Coordinate quadrant4 = new Coordinate(1, 2);
        Entity entity = new Entity(EntityType.undefined, new Position(1, 1, 1, 1));
        assertFalse(game.checkEntityAgainstQuadrant(quadrant, null));
        assertTrue(game.checkEntityAgainstQuadrant(quadrant, entity));
        assertFalse(game.checkEntityAgainstQuadrant(quadrant2, entity));
        assertFalse(game.checkEntityAgainstQuadrant(quadrant3, entity));
        assertFalse(game.checkEntityAgainstQuadrant(quadrant4, entity));
    }

    @Test
    public void checkEntityAgainstPositionShouldWorkAsExpected() {
        Position position = new Position(1, 1, 1, 1);
        Position position2 = new Position(1, 1, 2, 2);
        Position position3 = new Position(1, 1, 2, 1);
        Position position4 = new Position(1, 1, 1, 2);
        Entity entity = new Entity(EntityType.undefined, new Position(1, 1, 1, 1));
        assertFalse(game.checkEntityAgainstPosition(position, null));
        assertTrue(game.checkEntityAgainstPosition(position, entity));
        assertFalse(game.checkEntityAgainstPosition(position2, entity));
        assertFalse(game.checkEntityAgainstPosition(position3, entity));
        assertFalse(game.checkEntityAgainstPosition(position4, entity));
    }

    @Test
    public void checkEntityListAgainstPositionShouldWorkAsExpected() {
        Position position = new Position(1, 1, 1, 1);
        Position position2 = new Position(1, 1, 2, 1);
        Entity[] entities = new Entity[]{
            new Entity(EntityType.undefined, new Position(1, 1, 1, 1)),
            new Entity(EntityType.undefined, new Position(1, 1, 1, 2)),
        };
        assertFalse(game.checkEntityListAgainstPosition(position, null));
        assertTrue(game.checkEntityListAgainstPosition(position, entities));
        assertFalse(game.checkEntityListAgainstPosition(position2, entities));
    }

    @Test
    public void getQuadrantNumberShouldWorkAsExpected() {
        game.getMap()[1][1][1][1] = EntityType.ENTERPRISE;
        game.getMap()[1][1][1][2] = EntityType.KLINGON;
        game.getMap()[1][1][2][2] = EntityType.KLINGON;
        game.getMap()[1][1][3][3] = EntityType.COMMANDER;

        assert(game.getQuadrantNumber(1, 1) == 300);
        assert(game.getQuadrantNumber(9, 9) == -1);
        assert(game.getQuadrantNumber(9, 0) == -1);
        assert(game.getQuadrantNumber(0, 9) == -1);
        assert(game.getQuadrantNumber(-1, -1) == -1);
        assert(game.getQuadrantNumber(4, 4) == 0);
    }

    @Test
    public void updateMapShouldWorkAsExpected() {
        // setup
        game.getMap()[1][1][1][1] = EntityType.ENTERPRISE;
        game.getMap()[1][1][1][2] = EntityType.KLINGON;
        game.getMap()[1][1][2][2] = EntityType.KLINGON;
        game.getMap()[1][1][3][3] = EntityType.COMMANDER;
        game.getMap()[7][7][1][1] = EntityType.PLANET;
        game.getMap()[7][7][1][1] = EntityType.PLANET;
        game.getMap()[7][7][2][2] = EntityType.STARBASE;
        game.getMap()[7][7][3][3] = EntityType.STAR;
        game.getMap()[7][7][4][4] = EntityType.ROMULAN;
        game.getMap()[7][7][5][5] = EntityType.BLACK_HOLE;
        game.getMap()[7][7][6][6] = EntityType.SUPER_COMMANDER;

        game.setKlingons(new Klingon[] {
            new Klingon(new Position(1, 1, 1, 2)),
            new Klingon(new Position(1, 1, 2, 2)),
            null
        });
        game.setKlingonCommanders(new KlingonCommander[] {
            new KlingonCommander(new Position(1, 1, 3, 3)),
            null
        });
        game.setPlanets(new Planet[] {
            new Planet(new Position(7, 7, 1, 1))
        });
        game.setStarbases(new Starbase[] {
            new Starbase(new Position(7, 7, 2, 2))
        });
        game.setStars(new Star[] {
            new Star(new Position(7, 7, 3, 3))
        });
        game.setRomulans(new Romulan[] {
            new Romulan(new Position(7, 7, 4, 4))
        });
        game.setBlackHoles(new BlackHole[] {
            new BlackHole(new Position(7, 7, 5, 5))
        });
        game.setKlingonSuperCommander(new KlingonSuperCommander(new Position(7, 7, 6, 6)));
        
        assert(game.getMap()[1][1][1][1] == EntityType.ENTERPRISE);
        assert(game.getMap()[1][1][1][2] == EntityType.KLINGON);
        assert(game.getMap()[1][1][2][2] == EntityType.KLINGON);
        assert(game.getMap()[1][1][3][3] == EntityType.COMMANDER);
        assert(game.getMap()[7][7][1][1] == EntityType.PLANET);
        assert(game.getMap()[7][7][2][2] == EntityType.STARBASE);
        assert(game.getMap()[7][7][3][3] == EntityType.STAR);
        assert(game.getMap()[7][7][4][4] == EntityType.ROMULAN);
        assert(game.getMap()[7][7][5][5] == EntityType.BLACK_HOLE);
        assert(game.getMap()[7][7][6][6] == EntityType.SUPER_COMMANDER);
        
        // test
        game.setKlingons(new Klingon[] {
            new Klingon(new Position(1, 1, 1, 2)),
            null,
            null
        });
        game.setKlingonCommanders(new KlingonCommander[] {
            null,
            null
        });
        game.setPlanets(null);
        game.setStarbases(null);
        game.setStars(null);
        game.setRomulans(null);
        game.setBlackHoles(null);
        game.setKlingonSuperCommander(null);
        
        game.updateMap();
        assert(game.getMap()[1][1][1][1] == EntityType.ENTERPRISE);
        assert(game.getMap()[1][1][1][2] == EntityType.KLINGON);
        assert(game.getMap()[1][1][2][2] == EntityType.NOTHING);
        assert(game.getMap()[1][1][3][3] == EntityType.NOTHING);
        assert(game.getMap()[7][7][1][1] == EntityType.NOTHING);
        assert(game.getMap()[7][7][2][2] == EntityType.NOTHING);
        assert(game.getMap()[7][7][3][3] == EntityType.NOTHING);
        assert(game.getMap()[7][7][4][4] == EntityType.NOTHING);
        assert(game.getMap()[7][7][5][5] == EntityType.NOTHING);
        assert(game.getMap()[7][7][6][6] == EntityType.NOTHING);
    }

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
        assert (position1.isEqual(position2));

        position2 = new Position(1, 1, 1, 2);
        assert (!position1.isEqual(position2));
    }

    @Test
    public void positionQuadrantEqualityShouldBehaveAsExpected() {
        Position position1 = new Position(1, 1, 1, 1);
        Position position2 = new Position(1, 1, 5, 5);
        assert (position1.isEqualQuadrant(position2));

        position2 = new Position(1, 2, 1, 1);
        assert (!position1.isEqualQuadrant(position2));
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