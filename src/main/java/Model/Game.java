package Model;
import java.util.ArrayList;
import java.util.List;
import java.io.Console;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import static Utils.Utils.randDouble;
import static Utils.Utils.checkEntityAgainstPosition;
import static Utils.Utils.checkEntityListAgainstPosition;
import static Utils.Utils.checkEntityAgainstQuadrant;

/**
 * Game entity container. In the future, this should help
 * when we try to save and restore saved games
 */
@Data
public class Game {
    public enum GameType {
        REGULAR,
        // TOURNAMENT,
        // FROZEN,
        UNDEFINED;
    }

    public enum GameLength {
        SHORT(1),
        MEDIUM(2),
        LONG(3),
        UNDEFINED(0);

        private final int lengthVal;

        private GameLength(int lengthVal) {
            this.lengthVal = lengthVal;
        }

        public int getLengthValue() {
            return lengthVal;
        }
    }

    public enum GameLevel {
        NOVICE(1),
        FAIR(2),
        GOOD(3),
        EXPERT(4),
        EMERITUS(5),
        UNDEFINED(0);

        private final int skillVal;

        private GameLevel(int skillVal) {
            this.skillVal = skillVal;
        }

        public int getSkillValue() {
            return skillVal;
        }
    }

    public int getKlingonCount() {
        return klingons.length + klingonCommanders.length + (klingonSuperCommander != null ? 1 : 0);
    }

    @JsonIgnore
    public Console con = System.console();

    @JsonIgnore
    public static final char NOTHING = '\u00B7';

    private double starDate = randDouble(21, 39) * 100;

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
    public String getEntityStringFromChar(char c) {
        switch (c) {
            case 'E':
                return "Enterprise";
            case 'P':
                return "Planet";
            case 'F':
                return "Faerie Queen";
            case 'C':
                return "Commander";
            case '*':
                return "Star";
            case 'B':
                return "Starbase";
            case ' ':
                return "Black hole";
            case 'T':
                return "Tholian";
            case '#':
                return "Tholian web";
            default:
                return "Unknown??";
        }
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
}
