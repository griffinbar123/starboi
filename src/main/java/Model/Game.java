package Model;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.Console;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import static Utils.Utils.checkEntityAgainstPosition;
import static Utils.Utils.checkEntityListAgainstPosition;
import static Utils.Utils.checkEntityAgainstQuadrant;
import static Utils.Utils.positionsAreEqual;
import static Utils.Utils.outputDestroy;


/**
 * Game entity container
 */
@Data
public class Game {
    public int getKlingonCount() {
        return klingons.length + klingonCommanders.length + (klingonSuperCommander != null ? 1 : 0);
    }

    @JsonIgnore
    public Console con = System.console();

    @JsonIgnore
    public static final char NOTHING = '\u00B7';

    private double starDate;

    private char[][][][] map = new char[8][8][10][10];

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

    private Score score = new Score();


    @JsonIgnore
    public void begPardon(){
        con.printf("\nBeg your pardon, Captain?\n");
    }

    @JsonIgnore
    public void addCoordinateString(Coordinate coord, String s){
        for (Map.Entry<Coordinate, String> entry : ScannedQuadrants.entrySet()) {
            Coordinate key = entry.getKey();
            if(coordinatesAreEqual(key, coord)){
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
            if(coordinatesAreEqual(key, new Coordinate(row, col))){
                return value;
            }
        }
        return  "...";
    }

    @JsonIgnore
    private Boolean coordinatesAreEqual(Coordinate x, Coordinate y) {
        return x.getX() == y.getX() && x.getY() == y.getY();
    }

    @JsonIgnore
    public void updateMap() {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                for (int k = 0; k < map[i][j].length; k++) {
                    for (int l = 0; l < map[i][j][k].length; l++) {
                        Position position = new Position(new Coordinate(i, j), new Coordinate(k, l));
                        if (checkEntityListAgainstPosition(position, klingons)) {
                            map[i][j][k][l] = klingons[0].getSymbol();
                        } else if (checkEntityListAgainstPosition(position, planets)) {
                            map[i][j][k][l] = planets[0].getSymbol();
                        } else if (checkEntityAgainstPosition(position, enterprise)) {
                            map[i][j][k][l] = enterprise.getSymbol();
                        } else if (checkEntityListAgainstPosition(position, starbases)) {
                            map[i][j][k][l] = starbases[0].getSymbol();
                        } else if (checkEntityListAgainstPosition(position, stars)) {
                            map[i][j][k][l] = stars[0].getSymbol();
                        } else if (checkEntityListAgainstPosition(position, romulans)) {
                            map[i][j][k][l] = romulans[0].getSymbol();
                        } else {
                            map[i][j][k][l] = NOTHING;
                        }
                    }
                }
            }
        }
    }

    @JsonIgnore
    public Boolean checkPositionForEntity(Position position, char entity) {
        return getPositionChar(position) == entity;
    }

    @JsonIgnore
    public char getPositionChar(Position position) {
        return map[position.getQuadrant().getY()][position.getQuadrant().getX()][position.getSector().getY()][position.getSector().getX()];
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
    public Klingon getEnemyAtPosition(Position pos) {
        for(Klingon k: klingons)
            if(checkEntityAgainstPosition(enterprise.getPosition(), k))
                return k;

        for(Romulan r: romulans)
            if(checkEntityAgainstPosition(enterprise.getPosition(), r))
                return r;

        for(KlingonCommander c: klingonCommanders)
            if(checkEntityAgainstPosition(enterprise.getPosition(), c))
                return c;

        if(checkEntityAgainstPosition(enterprise.getPosition(), klingonSuperCommander))
            return klingonSuperCommander;
            
        return null;
    }

     @JsonIgnore
    public void destroyStarbase(Position pos) {
        con.printf("***STARBASE DESTROYED..\n");
        starbases = (Starbase[]) Arrays.stream(starbases).filter(starbase -> !positionsAreEqual(starbase.getPosition(), pos)).toArray();
        updateMap();

        // TODO: adjust game score and states
        destroyedBases += 1;
        // TODO: Adjust condition
    }

    @JsonIgnore
    public void destroyPlanet(Position pos) {
        con.printf("%s\n", outputDestroy(pos.getSector().getY(), pos.getSector().getY(), 'P'));
        planets = (Planet[]) Arrays.stream(planets).filter(planet -> !positionsAreEqual(planet.getPosition(), pos)).toArray();
        updateMap();

        // TODO: adjust game score and states
        destroyedPlanets += 1;
        // TODO: check if enterprise was landed. if so, end game
    }
}
