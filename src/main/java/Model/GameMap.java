// package Model;

// import java.util.HashMap;
// import java.util.Map;
// import com.fasterxml.jackson.annotation.JsonIgnore;
// import lombok.Data;
// import lombok.EqualsAndHashCode;

// /**
//  * Game map
//  */
// @Data
// public class GameMap {
//     private char[][][][] map = new char[8][8][10][10];
//     private HashMap<Coordinate, String> ScannedQuadrants = new HashMap<Coordinate, String>();


//     @JsonIgnore
//     public void addCoordinateString(Coordinate coord, String s){
//         for (Map.Entry<Coordinate, String> entry : ScannedQuadrants.entrySet()) {
//             Coordinate key = entry.getKey();
//             if(coordinatesAreEqual(key, coord)){
//                 ScannedQuadrants.remove(key);
//                 break;
//             }
//         }
//         ScannedQuadrants.put(coord, s);
//     }

//     @JsonIgnore
//     public String getCoordinateString(int row, int col){
//         for (Map.Entry<Coordinate, String> entry : ScannedQuadrants.entrySet()) {
//             Coordinate key = entry.getKey();
//             String value = entry.getValue();
//             if(coordinatesAreEqual(key, new Coordinate(row, col))){
//                 return value;
//             }
//         }
//         return  "...";
//     }

//     @JsonIgnore
//     private Boolean coordinatesAreEqual(Coordinate x, Coordinate y) {
//         return x.getX() == y.getX() && x.getY() == y.getY();
//     }

//     @JsonIgnore
//     public void updateMap() {
//         for (int i = 0; i < map.length; i++) {
//             for (int j = 0; j < map[i].length; j++) {
//                 for (int k = 0; k < map[i][j].length; k++) {
//                     for (int l = 0; l < map[i][j][k].length; l++) {
//                         Position position = new Position(new Coordinate(i, j), new Coordinate(k, l));
//                         if (checkEntityListAgainstPosition(position, klingons)) {
//                             map[i][j][k][l] = klingons[0].getSymbol();
//                         } else if (checkEntityListAgainstPosition(position, planets)) {
//                             map[i][j][k][l] = planets[0].getSymbol();
//                         } else if (checkEntityAgainstPosition(position, enterprise)) {
//                             map[i][j][k][l] = enterprise.getSymbol();
//                         } else if (checkEntityListAgainstPosition(position, starbases)) {
//                             map[i][j][k][l] = starbases[0].getSymbol();
//                         } else if (checkEntityListAgainstPosition(position, stars)) {
//                             map[i][j][k][l] = stars[0].getSymbol();
//                         } else if (checkEntityListAgainstPosition(position, romulans)) {
//                             map[i][j][k][l] = romulans[0].getSymbol();
//                         } else {
//                             map[i][j][k][l] = NOTHING;
//                         }
//                     }
//                 }
//             }
//         }
//     }
// }
