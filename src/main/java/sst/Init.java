package sst;

import java.util.List;
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
import static Utils.Utils.turnEntityQuadrantsToStrings;
import static Utils.Utils.readCommands;

/**
 * Initializes a game. In future versions, this class will be able
 * to restore a saved game state
 */
public class Init {
    private Game game;
    public static final char NOTHING = '\u00B7';

    /**
     * Starts the game
     * 
     * @author Matthias Schrock
     */
    public void start() {
        this.game = new Game();
        CommandHandler handler = new CommandHandler(this.game);
        String initMessage;
        int skill, planets, klingons, starbases, stars, romulans;

        setGameType();
        setGameLength();
        setGameLevel();

        // TODO
        System.out.print("Please type in a secret password (9 characters maximum)-");
        System.out.println("changeit");

        skill = this.game.getSkill().getSkillValue();
        // TODO: initialize these numbers based on game type, length, and skill
        planets = 30;
        klingons = randInt(0.15, 0.15 + (skill + 1) * skill * 0.1); // TODO: All Klingons (any type). The commanders and
                                                                    // super commanders should be subtracted from this
                                                                    // value
        starbases = 4;
        stars = 300;
        romulans = 4;

        initializeEnterprise();
        initializePlanets(planets);
        initializeKlingons(klingons);
        initializeStarbases(starbases);
        initializeStars(stars);
        initializeRomulans(romulans);
        updateMap();

        initMessage = String.format("\n\n\nIt is stardate %.1f. The Federation is being attacked by " +
                "a deadly Klingon invasion force. As captain of the United Starship " +
                "U.S.S. Enterprise, it is your mission to seek out and destroy this " +
                "invasion force of %d battle cruisers. You have an initial allotment " +
                "of %d stardates to complete your mission. As you proceed you may be " +
                "given more time.\n\nYou will have %d supporting starbases. Starbase " +
                "locations- %s\n\nThe Enterprise is currently in Quadrant %d - %d " +
                "Sector %d - %d\n\nGood Luck!\n\n",
                this.game.getStarDate(), klingons, -1, -1,
                turnEntityQuadrantsToStrings(this.game.getStarbases()),
                (this.game.getEnterprise().getPosition().getQuadrant().getX() + 1),
                (this.game.getEnterprise().getPosition().getQuadrant().getY() + 1),
                (this.game.getEnterprise().getPosition().getSector().getX() + 1),
                (this.game.getEnterprise().getPosition().getSector().getY() + 1));
        this.game.con.printf("%s", initMessage);

        handler.getAndExecuteCommands();
    }

    /**
     * Allows the user to select a game style
     * 
     * @aurhor Fabrice Mpozenzi
     * @author Matthias Schrock
     */
    private void setGameType() {
        CommandHandler handler = new CommandHandler(this.game);
        String in = "";
        String typ = "";

        while (true) {
            in = this.game.con.readLine("Would you like a regular, tournament, or frozen game?");
            typ = readCommands(in).orElse(List.of("")).get(0);

            Game.GameType type = handler.matcher(typ, Game.GameType.class).orElse(Game.GameType.UNDEFINED);

            if (type == Game.GameType.UNDEFINED) {
                this.game.con.printf("Invalid choice. Please choose regular, tournament, or frozen.\n");
            } else {
                this.game.setType(type);
                return;
            }
        }
    }

    /**
     * Allows the user to select a game level
     * 
     * @author Fabrice Mpozenzi
     * @author Matthias Schrock
     */
    private void setGameLength() {
        CommandHandler handler = new CommandHandler(this.game);
        String in = "";
        String len = "";

        while (true) {
            in = this.game.con.readLine("Would you like a Short, Medium, or Long game?");
            len = readCommands(in).orElse(List.of("")).get(0);

            Game.GameLength length = handler.matcher(len, Game.GameLength.class).orElse(Game.GameLength.UNDEFINED);

            if (length == Game.GameLength.UNDEFINED) {
                this.game.con.printf("Invalid choice. Please choose short, medium, or long.\n");
            } else {
                this.game.setLength(length);
                return;
            }
        }

        // System.out.print("Please type in a secret password (9 characters maximum)-
        // ");
    }

    /**
     * Allows the user to select a game type
     * 
     * @author Fabrice Mpozenzi
     * @author Matthias Schrock
     */
    private void setGameLevel() {
        CommandHandler handler = new CommandHandler(this.game);
        String in = "";
        String lvl = "";

        while (true) {
            in = this.game.con.readLine("Are you a Novice, Fair, Good, Expert, or Emeritus player?");
            lvl = readCommands(in).orElse(List.of("")).get(0);

            Game.GameLevel level = handler.matcher(lvl, Game.GameLevel.class).orElse(Game.GameLevel.UNDEFINED);

            if (level == Game.GameLevel.UNDEFINED) {
                this.game.con.printf("Invalid choice. Please choose Novice, Fair, Good, Expert, or Emeritus\n");
            } else {
                this.game.setSkill(level);
                return;
            }
        }
    }

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
