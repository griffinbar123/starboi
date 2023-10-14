package Model;

import java.io.Console;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import static Utils.Utils.randInt;

/**
 * Game entity container. In the future, this should help
 * when we try to save and restore saved games
 */
@Data
public class Game {
    public enum GameType {
        REGULAR,
        TOURNAMENT,
        FROZEN,
        UNDEFINED;
    }

    public enum GameLength {
        SHORT,
        MEDIUM,
        LONG,
        UNDEFINED;
    }

    public enum GameLevel {
        NOVICE(1),
        FAIR(2),
        GOOD(3),
        EXPERT(4),
        EMERITUS(5),
        UNDEFINED(0);

        private final int skill;

        private GameLevel(int skill) {
            this.skill = skill;
        }

        public int getSkill() {
            return skill;
        }
    }

    @JsonIgnore
    public Console con = System.console();

    private float starDate = randInt(21, 39) * 100;
    private char[][][][] map = new char[8][8][10][10];
    private Klingon[] klingons;
    private Enterprise enterprise;
    private Planet[] planets;
    private Starbase[] starbases;
    private Star[] stars;
    private Romulan[] romulans;
    private HashMap<Coordinate, String> ScannedQuadrants = new HashMap<Coordinate, String>();
    private float time;
    private GameLevel skill;
    private GameLength length;
    private GameType type;

    @JsonIgnore
    public void addCoordinateString(Coordinate coord, String s){
        for (Map.Entry<Coordinate, String> entry : ScannedQuadrants.entrySet()) {
            Coordinate key = entry.getKey();
            String value = entry.getValue();
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
}
