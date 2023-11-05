package Model;
import java.util.ArrayList;
import java.util.List;
import java.io.Console;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonIgnore;

import Utils.Utils;
import lombok.Data;
import sst.Finish;
import sst.Finish.GameOverReason;

import static Utils.Utils.checkEntityAgainstPosition;
import static Utils.Utils.checkEntityListAgainstPosition;
import static Utils.Utils.checkEntityAgainstQuadrant;
import static Utils.Utils.positionsAreEqual;
import static Utils.Utils.outputEntity;
import static Utils.Utils.randInt;;


/**
 * Game entity container
 */
@Data
public class Game {
    public int getRemainingKlingonCount() {
        return getRegularKlingonCount() + getKlingonCommanderCount()+ (klingonSuperCommander != null ? 1 : 0);
    }

    @JsonIgnore
    public Console con = System.console();

    private double starDate;

    private EntityType[][][][] map = new EntityType[8][8][10][10];

    private Klingon[] klingons;
    private KlingonCommander[] klingonCommanders;
    private KlingonSuperCommander klingonSuperCommander = null;
    private Enterprise enterprise;
    private Planet[] planets;
    private Starbase[] starbases;
    private Star[] stars;
    private Romulan[] romulans;
    private HashMap<Coordinate, String> ScannedQuadrants = new HashMap<Coordinate, String>();
    private double time;
    private GameLevel skill = GameLevel.UNDEFINED;
    private GameLength length = GameLength.UNDEFINED;
    private GameType type = GameType.UNDEFINED;
    
    private Integer destroyedPlanets = 0;
    private Integer destroyedBases = 0;
    private Integer romulansKilled = 0;
    private Integer superCommandersKilled = 0;
    private Integer klingonsKilled = 0;
    private Integer commandersKilled = 0;

    private Score score = new Score();


    @JsonIgnore
    public void begPardon(){
        con.printf("\nBeg your pardon, Captain?\n");
    }

    @JsonIgnore
    public void addCoordinateString(Coordinate coord, String s){
        for (Map.Entry<Coordinate, String> entry : ScannedQuadrants.entrySet()) {
            Coordinate key = entry.getKey();
            if(key.isEqual(coord)){
                ScannedQuadrants.remove(key);
                break;
            }
        }
        ScannedQuadrants.put(coord, s);
    }

    @JsonIgnore
    public String getCoordinateString(int row, int col){
        for (Map.Entry<Coordinate, String> entry : ScannedQuadrants.entrySet()) {
            Coordinate key = entry.getKey();
            String value = entry.getValue();
            if(key.isEqual(new Coordinate(row, col))){
                return value;
            }
        }
        return  "...";
    }

    @JsonIgnore
    public void updateMap() {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                for (int k = 0; k < map[i][j].length; k++) {
                    for (int l = 0; l < map[i][j][k].length; l++) {
                        Position position = new Position(new Coordinate(i, j), new Coordinate(k, l));
                        if (checkEntityListAgainstPosition(position, klingons)) {
                            map[j][i][k][l] = EntityType.KLINGON;
                        } else if (checkEntityListAgainstPosition(position, planets)) {
                            map[j][i][k][l] = EntityType.PLANET;
                        } else if (checkEntityAgainstPosition(position, enterprise)) {
                            map[j][i][k][l] = EntityType.ENTERPRISE;
                        } else if (checkEntityAgainstPosition(position, klingonSuperCommander)) {
                            map[j][i][k][l] = EntityType.ENTERPRISE;
                        } else if (checkEntityListAgainstPosition(position, starbases)) {
                            map[j][i][k][l] = EntityType.STARBASE;
                        } else if (checkEntityListAgainstPosition(position, stars)) {
                            map[j][i][k][l] = EntityType.STAR;
                        } else if (checkEntityListAgainstPosition(position, romulans)) {
                            map[j][i][k][l] = EntityType.ROMULAN;
                        } else if (checkEntityListAgainstPosition(position, klingonCommanders)) {
                            map[j][i][k][l] = EntityType.COMMANDER;
                        } else {
                            map[j][i][k][l] = EntityType.NOTHING;
                        }
                    }
                }
            }
        }
    }

    @JsonIgnore
    private List<Entity> getEntitiesInQuadrant(Coordinate quad){
        List<Entity> entities = new ArrayList<Entity>();

        for(int i = 0; i < map[quad.getX()][quad.getY()].length; i++) {
            for(int j = 0; j < map[quad.getX()][quad.getY()].length; j++) {
                Position position = new Position(quad, new Coordinate(i, j));
                Entity entity = getEntityAtPosition(position);
                if(entity != null){
                    entities.add(entity);
                }
            }
        }

        return entities;
    }

    @JsonIgnore
    public void randomizeQuadrant(Coordinate quad){
        List<Entity> entitiesInQuadrant = getEntitiesInQuadrant(quad);
        for(Entity entity : entitiesInQuadrant) {
            entity.setPosition(generateRandomPositionWithinQuadrant(quad));
        }
        updateMap();
    }

    private Position generateRandomPositionWithinQuadrant(Coordinate quad) {
        Coordinate sector = new Coordinate(randInt(0, 9), (randInt(0, 9)));
        Position position = new Position(quad, sector);
        while (!isPositionEmpty(position)) {
            position = generateRandomPositionWithinQuadrant(quad);
        }
        return position;
    }

    @JsonIgnore
    public Boolean isPositionEmpty(Position position) {
        // use this to check if a position is empty, but can't check the map because it
        // might not be updated
        return !(checkEntityAgainstPosition(position, getEnterprise())
                || checkEntityAgainstPosition(position, getKlingonSuperCommander())
                || checkEntityListAgainstPosition(position, getKlingons())
                || checkEntityListAgainstPosition(position, getPlanets())
                || checkEntityListAgainstPosition(position, getStars())
                || checkEntityListAgainstPosition(position, getStarbases())
                || checkEntityListAgainstPosition(position, getRomulans())
                || checkEntityListAgainstPosition(position, getKlingonCommanders()));
    }

    @JsonIgnore
    public Boolean checkPositionForEntity(Position position, EntityType entityType) {
        return getPositionEntityType(position) == entityType;
    }

    @JsonIgnore
    public EntityType getPositionEntityType(Position position) {
        return map[position.getQuadrant().getX()][position.getQuadrant().getY()][position.getSector().getY()][position.getSector().getX()];
    }

    @JsonIgnore
    public List<Entity> getEnemiesInAQuadrant(Coordinate quad) {
        List<Entity> enemeies = new ArrayList<Entity>();

        for(Klingon k: klingons)
            if(checkEntityAgainstQuadrant(enterprise.getPosition().getQuadrant(), k))
                enemeies.add(k);

        for(Romulan r: romulans)
            if(checkEntityAgainstQuadrant(enterprise.getPosition().getQuadrant(), r))
                enemeies.add(r);

        for(KlingonCommander c: klingonCommanders)
            if(checkEntityAgainstQuadrant(enterprise.getPosition().getQuadrant(), c))
                enemeies.add(c);

        if(checkEntityAgainstQuadrant(enterprise.getPosition().getQuadrant(), klingonSuperCommander))
            enemeies.add(klingonSuperCommander);
            
        return enemeies;
    }

    @JsonIgnore
    @SuppressWarnings("unchecked")
    public <T extends Entity> T getEntityAtPosition(Position pos) {
        for(Klingon k: klingons) {
            if(checkEntityAgainstPosition(pos, k)) {
                return (T) k;
            }
        }
        for(KlingonCommander c: klingonCommanders) {
            if(checkEntityAgainstPosition(pos, c)) {
                return (T) c;
            }
        }
        for(Romulan r: romulans) {
            if(checkEntityAgainstPosition(pos, r)) {
                return (T) r;
            }
        }
        if(checkEntityAgainstPosition(pos, klingonSuperCommander)) {
            return (T) klingonSuperCommander;
        }
        for(Star s: stars) {
            if(checkEntityAgainstPosition(pos, s)) {
                return (T) s;
            }
        }
        for(Starbase b: starbases) {
            if(checkEntityAgainstPosition(pos, b)) {
                return (T) b;
            }
        }
        for(Planet p: planets) {
            if(checkEntityAgainstPosition(pos, p)) {
                return (T) p;
            }
        }

        return null;
    }

    @JsonIgnore
    public int getRegularKlingonCount() {
        int total = 0;
        for(int i = 0; i < klingons.length; i++)
            if(klingons[i] != null) 
                total++;
        return total;
    }

    @JsonIgnore
    public int getKlingonCommanderCount() {
        int total = 0;
        for(int i = 0; i < klingonCommanders.length; i++)
            if(klingonCommanders[i] != null) 
                total++;
        return total;
    }

    @JsonIgnore
    public int getRomulansCount() {
        int total = 0;
        for(int i = 0; i < romulans.length; i++)
            if(romulans[i] != null) 
                total++;
        return total;
    }

     @JsonIgnore
    public void destroyStarbase(Position pos) {
        con.printf("***STARBASE DESTROYED..\n");
        for(int i=0; i < starbases.length; i++)
            if(starbases[i] != null && positionsAreEqual(starbases[i].getPosition(), pos))
                starbases[i] = null;
        updateMap();

        // TODO: adjust game score and states
        destroyedBases += 1;
        // TODO: Adjust condition
    }

    @JsonIgnore
    public void destroyPlanet(Position pos) {
        con.printf("%s destroyed.\n", outputEntity(pos.getSector().getY()+1, pos.getSector().getX()+1, EntityType.PLANET));
        for(int i=0; i < planets.length; i++)
            if(planets[i] != null && positionsAreEqual(planets[i].getPosition(), pos))
                planets[i] = null;
        updateMap();

        // TODO: adjust game score and states
        destroyedPlanets += 1;
        // TODO: check if enterprise was landed. if so, end game
    }

    @JsonIgnore
    public void destroyEnemy(Enemy k) {
        EntityType type = k.getType();
        Position pos = k.getPosition();
        con.printf("%s destroyed.\n", outputEntity(pos.getSector().getY()+1, pos.getSector().getX()+1, type));

        switch (type) {
            case KLINGON:
                for(int i=0; i < klingons.length; i++)
                    if(klingons[i] != null && positionsAreEqual(klingons[i].getPosition(), pos)) {
                        klingons[i] = null;
                        klingonsKilled += 1;
                    }
                break;
            case COMMANDER:
                for(int i=0; i < klingonCommanders.length; i++)
                    if(klingonCommanders[i] != null && positionsAreEqual(klingonCommanders[i].getPosition(), pos)){
                        klingonCommanders[i] = null;
                        commandersKilled += 1;
                    }
                break;
            case ROMULAN:
                for(int i=0; i < romulans.length; i++)
                    if(romulans[i] != null && positionsAreEqual(romulans[i].getPosition(), pos)) {
                        romulans[i] = null;
                        romulansKilled += 1;
                    }
                break;
            case SUPER_COMMANDER:
                klingonSuperCommander = null;
                superCommandersKilled += 1; 
                break;
        }

        if(getRemainingKlingonCount() == 0) {
            Finish finish = new Finish(this);
            finish.finish(GameOverReason.WON);
        }
        updateMap();

        // TODO: adjust game score and states dependent on entity
    }
}
