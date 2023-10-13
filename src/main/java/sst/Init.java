package sst;

import java.util.HashMap;
import java.util.Map;
import Model.Coordinate;
import Model.Enterprise;
import Model.Entity;
import Model.Game;
import Model.Klingon;
import Model.Planet;
import Model.Position;
import Model.Romulan;
import Model.Star;
import Model.Starbase;
import Utils.Utils;

import static Utils.Utils.randInt;

/**
 * Initializes a game. In future versions, this class will be able
 * to restore a saved game state
 */
public class Init {
    private Game game;
    public static final char NOTHING = '\u00B7';

    private void gameStyle() {
        CommandHandler handler = new CommandHandler(this.game);
        String in = "";

        this.game.con.printf("Would you like a regular, tournament, or frozen game?");
        in = this.game.con.readLine();
        String styl = handler.readCommands(in).get().get(0);

        while (true) {
            switch (styl) {
                case "REGULAR":
                    this.game.setStyle(Game.GameStyle.REGULAR);
                    return;
                case "TOURNAMENT":
                    this.game.setStyle(Game.GameStyle.TOURNAMENT);
                    return;
                case "FROZEN":
                    this.game.setStyle(Game.GameStyle.FROZEN);
                    return;
                default:
                    this.game.con.printf("Invalid choice. Please choose regular, tournament, or frozen.\n");
            }
        }
    }

    /**
     * Allows the user to select a game level
     * 
     * @param levelChoice
     * @author Fabrice Mpozenzi
     */
    private void gameLevel() {
        CommandHandler handler = new CommandHandler(this.game);
        String in = "";

        this.game.con.printf("Would you like a Short, Medium, or Long game?");
        in = this.game.con.readLine();
        String len = handler.readCommands(in).get().get(0);

        while (true) {
            switch (len) {
                case "SHORT":
                    this.game.setType(Game.GameType.SHORT);
                    return;
                case "MEDIUM":
                    this.game.setType(Game.GameType.MEDIUM);
                    return;
                case "LONG":
                    this.game.setType(Game.GameType.LONG);
                    return;
                default:
                    this.game.con.printf("Invalid choice. Please choose short, medium, or long.\n");
                    break;
            }
        }
        
        // System.out.print("Please type in a secret password (9 characters maximum)- ");
    }
    
    /**
     * Allows the user to select a game type
     * 
     * @param gameChoice
     * @author Fabrice Mpozenzi
     */
    private void gameType() {
        CommandHandler handler = new CommandHandler(this.game);
        String in = "";

        this.game.con.printf("Are you a Novice, Fair, Good, Expert, or Emeritus player?");
        in = this.game.con.readLine();
        String lvl = handler.readCommands(in).get().get(0);

        while (true) {
            switch (lvl) {
                case "NOVICE":
                    this.game.setSkill(Game.GameLevel.NOVICE);
                    return;
                case "FAIR":
                    this.game.setSkill(Game.GameLevel.FAIR);
                    return;
                case "GOOD":
                    this.game.setSkill(Game.GameLevel.GOOD);
                    return;
                case "EXPERT":
                    this.game.setSkill(Game.GameLevel.EXPERT);
                    return;
                case "EMERITUS":
                    this.game.setSkill(Game.GameLevel.EMERITUS);
                    return;
                default:
                    this.game.con.printf("Invalid choice. Please choose Novice, Fair, Good, Expert, or Emeritus\n");
                    return;
                }
        }
    }
    
    private Map<String, Integer> gameDifficulty() {
        Map<String, Integer> params = new HashMap<>(); // entities

        gameStyle();
        gameLevel();
        gameType();
        int skill = this.game.getSkill().getValue();
        params.put("klingons", randInt(0.15, 0.15 + (skill + 1) * skill * 0.1)); // All Klingons (any type)

        return params;
    }

    /**
     * Starts the game
     * 
     * @author Matthias Schrock
     */
    public void start() {
        // TODO: be able to load game
        // TODO: implement original code for initializing stardate: d.date = indate = 100.0*(int)(31.0*Rand()+20.0)
        this.game = new Game();

        Map<String, Integer> params = gameDifficulty();
        initializeEnterprise();
        // TODO: get entity numbers from Fabrice
        initializePlanets(params.get("planets"));
        initializeKlingons(params.get("klingons"));
        initializeStarbases(params.get("starbases"));
        initializeStars(params.get("stars"));
        initializeRomulans(params.get("romulans"));
        updateMap();
        CommandHandler handler = new CommandHandler(this.game);

        handler.getAndExecuteCommands();
    }

    // private void initializeGame() {
    // System.out.println("-SUPER- STAR TREK (JAVA EDITION)\n");
    // System.out.println("Latest update-prolly today\n");
    // System.out.print("Would you like a regular, tournament, or frozen game?");
    // System.out.println(" I'm going to assume regular\n");
    // System.out.print("Would you like a Short, Medium, or Long Game?");
    // System.out.println(" I'm going to assume short");
    // System.out.print("Are you a Novice, Fair, Good, Expert, or Emeritus
    // player?");
    // System.out.println(" Def a novice");
    // System.out.print("Please type in a secret password (9 characters maximum)-");
    // System.out.println("changeit");

    // System.out.println("\n\n\nIt is stardate " + Enterprise.getStarDate()
    // + ". The Federation is being attacked by a deadly Klingon invasion force. As
    // captain of the United Starship "
    // + "U.S.S. Enterprise, it is your mission to seek out and destroy this
    // invasion force of "
    // + Klingons.length + " battle cruisers. You have an initial allotment of " +
    // "7"
    // + " stardates to complete your mission. As you proceed you may be given more
    // time.\n");
    // System.out.println("You will have " + "x"
    // + "supporting starbases. Starbase locations- " +
    // turnEntityQuadrantsToStrings(Starbases) + "\nThe Enterprise is currently in
    // Quadrant "
    // + (Enterprise.getPosition().getQuadrant().getX() + 1) + " - " +
    // (Enterprise.getPosition().getQuadrant().getY() + 1)
    // + " Sector " + (Enterprise.getPosition().getSector().getX() + 1) + " - "
    // + (Enterprise.getPosition().getSector().getY() + 1) + "\n\nGood Luck!\n\n");
    // }

    /**
     * Updates the map
     * 
     * @author Griffin Barnard
     */
    public void updateMap() {
        for (int i = 0; i < this.game.getMap().length; i++) {
            for (int j = 0; j < this.game.getMap()[i].length; j++) {
                for (int k = 0; k < this.game.getMap()[i][j].length; k++) {
                    for (int l = 0; l < this.game.getMap()[i][j][k].length; l++) {
                        Position position = new Position(new Coordinate(i, j), new Coordinate(k, l));
                        // check if positions is a
                        if (checkEntityListAgainstPosition(position, this.game.getKlingons())) {
                            this.game.getMap()[j][i][l][k] = this.game.getKlingons()[0].getSymbol();
                        } else if (checkEntityListAgainstPosition(position, this.game.getPlanets())) {
                            this.game.getMap()[j][i][l][k] = this.game.getPlanets()[0].getSymbol();
                        } else if (checkEntityAgainstPosition(position, this.game.getEnterprise())) {
                            this.game.getMap()[j][i][l][k] = this.game.getEnterprise().getSymbol();
                        } else if (checkEntityListAgainstPosition(position, this.game.getStarbases())) {
                            this.game.getMap()[j][i][l][k] = this.game.getStarbases()[0].getSymbol();
                        } else if (checkEntityListAgainstPosition(position, this.game.getStars())) {
                            this.game.getMap()[j][i][l][k] = this.game.getStars()[0].getSymbol();
                        } else if (checkEntityListAgainstPosition(position, this.game.getRomulans())) {
                            this.game.getMap()[j][i][l][k] = this.game.getRomulans()[0].getSymbol();
                        } else {
                            this.game.getMap()[j][i][l][k] = NOTHING;
                        }
                    }
                }
            }
        }
    }

    private void initializeEnterprise() {
        Position pos = generateNewPosition(null, 9);
        
        this.game.setEnterprise(new Enterprise(pos));
    }

    private void initializeRomulans(int numberOfRomulans) {
        Position pos;
        Romulan rom[] = new Romulan[numberOfRomulans];
        for (int i = 0; i < numberOfRomulans; i++) {
            pos = generateNewPosition(rom, 9);
            rom[i] = new Romulan(pos);
        }
        this.game.setRomulans(rom);
    }

    private void initializeStars(int numberOfStars) {
        Position pos;
        Star stars[] = new Star[numberOfStars];
        for (int i = 0; i < numberOfStars; i++) {
            pos = generateNewPosition(stars, 9);
            stars[i] = new Star(pos);
        }
        this.game.setStars(stars);
    }

    private void initializeStarbases(int numberOfStarbases) {
        Position pos;
        Starbase starBases[] = new Starbase[numberOfStarbases];
        for (int i = 0; i < numberOfStarbases; i++) {
            pos = generateNewPosition(starBases, 9);
            starBases[i] = new Starbase(pos);
            this.game.addCoordinateString(pos.getQuadrant(), ".1.");
        }
        this.game.setStarbases(starBases);
    }

    private void initializePlanets(int numberOfPlanets) {
        Position pos;
        Planet planets[] = new Planet[numberOfPlanets];
        for (int i = 0; i < numberOfPlanets; i++) {
            pos = generateNewPosition(planets, 9);
            planets[i] = new Planet(pos);
        }
        this.game.setPlanets(planets);
    }

    private void initializeKlingons(int numberOfKlingons) {
        Position pos;
        Klingon klingons[] = new Klingon[numberOfKlingons];
        for (int i = 0; i < numberOfKlingons; i++) {
            pos = generateNewPosition(klingons, 9);
            klingons[i] = new Klingon(pos);
        }
        this.game.setKlingons(klingons);
    }

    private Position generateNewPosition(Entity[] entities, int maxElementsInQuadrant) {
        Coordinate quadrant = new Coordinate(Utils.randInt(0, 7), (Utils.randInt(0, 7)));
        Coordinate sector = new Coordinate(Utils.randInt(0, 9), (Utils.randInt(0, 9)));
        Position position = new Position(quadrant, sector);
        while (!isPositionEmpty(position) && entities != null
                && getNumberOfEntiesBeforeMapUpdate(entities, position) <= maxElementsInQuadrant) {
            position = generateNewPosition(entities, maxElementsInQuadrant);
        }
        return position;
    }

    private int getNumberOfEntiesBeforeMapUpdate(Entity[] entities, Position position) {
        // function to get number of entities in a qudrant before a map update, used in
        // iniitialzation to make
        // sure a quadrant doesn't get too many of an entity type
        int numberOfElements = 0;
        for (int i = 0; i < entities.length; i++) {
            if (entities[i] != null && entities[i].getPosition().getQuadrant().getX() == position.getQuadrant().getX()
                    &&
                    entities[i].getPosition().getQuadrant().getY() == position.getQuadrant().getY()) {
                numberOfElements += 1;
            }
        }
        return numberOfElements;
    }

    private Boolean isPositionEmpty(Position position) {
        // use this to check if a position is empty, but can't check the map because it
        // might not be updated
        return !(checkEntityAgainstPosition(position, this.game.getEnterprise())
                || checkEntityListAgainstPosition(position, this.game.getKlingons())
                || checkEntityListAgainstPosition(position, this.game.getPlanets()));
    }

    private Boolean checkEntityListAgainstPosition(Position position, Entity[] entities) {
        // checks if any entity in the list provided is is in the provided position
        if (entities == null)
            return false;

        for (int i = 0; i < entities.length; i++) {
            if (checkEntityAgainstPosition(position, entities[i])) {
                return true;
            }
        }
        return false;
    }

    private Boolean checkEntityAgainstPosition(Position position, Entity entity) {
        // checks if entity is in a position
        return entity != null && entity.getPosition().getQuadrant().getX() == position.getQuadrant().getX()
                && entity.getPosition().getQuadrant().getY() == position.getQuadrant().getY() &&
                entity.getPosition().getSector().getX() == position.getSector().getX()
                && entity.getPosition().getSector().getY() == position.getSector().getY();
    }
}
