package sst;

import Model.Enterprise;
import Model.Klingon;
import Model.Planet;
import Model.Romulan;
import Model.Star;
import Model.Starbase;
import lombok.Data;

/**
 * Game entity container. In the future, this should help
 * when we try to save and restore saved games
 */
@Data
public class Game {
    private char[][][][] map = new char[8][8][10][10];
    private Klingon[] klingons;
    private Enterprise enterprise;
    private Planet[] planets;
    private Starbase[] starbases;
    private Star[] stars;
    private Romulan[] romulans;
}
