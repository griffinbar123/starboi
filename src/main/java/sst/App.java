package sst;

public class App {
    public static void main(String[] args) {
        Init newGame = new Init();
        newGame.start();
    }

    // static String turnEntityQuadrantsToStrings(Entity[] entities) {
    //     String locs = "";
    //     for (Entity entity : entities) {
    //         locs += (entity.getPosition().getQuadrant().getX() + 1) + " - "
    //                 + (entity.getPosition().getQuadrant().getY() + 1) + "  ";
    //     }
    //     return locs;
    // }
}
