package Model;

import java.io.Console;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

/**
 * Game entity container. In the future, this should help
 * when we try to save and restore saved games
 */
@Data
public class Game {
    public enum GameLevel {
        NOVICE(1),
        FAIR(2),
        GOOD(3),
        EXPERT(4),
        EMERITUS(5);

        private final int value;

        private GameLevel(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum GameType {
        SHORT,
        MEDIUM,
        LONG;
    }

    public enum GameStyle {
        REGULAR,
        TOURNAMENT,
        FROZEN;
    }

    @JsonIgnore
    public Console con = System.console();
    private char[][][][] map = new char[8][8][10][10];
    private Klingon[] klingons;
    private Enterprise enterprise;
    private Planet[] planets;
    private Starbase[] starbases;
    private Star[] stars;
    private Romulan[] romulans;
    private HashMap<Coordinate, String> ScannedQuadrants = new HashMap<Coordinate, String>();
    private float time;
    private GameType type;
    private GameLevel skill;
    private GameStyle style;

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
