package sst;

import java.util.List;
import Model.Coordinate;
import Model.Enterprise;
import Model.Entity;
import Model.Game;
import Model.Klingon;
import Model.KlingonCommander;
import Model.KlingonSuperCommander;
import Model.Planet;
import Model.Position;
import Model.Romulan;
import Model.Star;
import Model.Starbase;
import Utils.Utils;
import static Utils.Utils.randInt;
import static Utils.Utils.randDouble;
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
        int planets, starbases, stars, romulans;

        setGameType();
        setGameLength();
        setGameLevel();

        // TODO
        System.out.print("Please type in a secret password (9 characters maximum)-");
        System.out.println("changeit");

        // TODO: initialize these numbers based on game type, length, and skill
        planets = 30;
        starbases = 4;
        stars = 300;
        romulans = 4;

        initializeEnterprise();
        initializePlanets(planets);
        initializeKlingons();
        initializeStarbases(starbases);
        initializeStars(stars);
        initializeRomulans(romulans);
        updateMap();

        printStartupMessage();

        handler.getAndExecuteCommands();
    }

    private void printStartupMessage() {
        String initMessage = String.format("\n\n\nIt is stardate %.1f. The Federation is being attacked by " +
                "a deadly Klingon invasion force. As captain of the United Starship " +
                "U.S.S. Enterprise, it is your mission to seek out and destroy this " +
                "invasion force of %d battle cruisers. You have an initial allotment " +
                "of %d stardates to complete your mission. As you proceed you may be " +
                "given more time.\n\nYou will have %d supporting starbases. Starbase " +
                "locations- %s\n\nThe Enterprise is currently in Quadrant %d - %d " +
                "Sector %d - %d\n\nGood Luck!\n\n",
                this.game.getStarDate(), this.game.getKlingons().length, -1, -1,
                turnEntityQuadrantsToStrings(this.game.getStarbases()),
                (this.game.getEnterprise().getPosition().getQuadrant().getX() + 1),
                (this.game.getEnterprise().getPosition().getQuadrant().getY() + 1),
                (this.game.getEnterprise().getPosition().getSector().getX() + 1),
                (this.game.getEnterprise().getPosition().getSector().getY() + 1));
        this.game.con.printf("%s", initMessage);
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
        Position pos = generateNewPosition(9, (Entity) null);

        this.game.setEnterprise(new Enterprise(pos));
    }

    private void initializeRomulans(int numberOfRomulans) {
        Position pos;
        Romulan rom[] = new Romulan[numberOfRomulans];
        for (int i = 0; i < numberOfRomulans; i++) {
            pos = generateNewPosition(9, rom);
            rom[i] = new Romulan(pos);
        }
        this.game.setRomulans(rom);
    }

    private void initializeStars(int numberOfStars) {
        Position pos;
        Star stars[] = new Star[numberOfStars];
        for (int i = 0; i < numberOfStars; i++) {
            pos = generateNewPosition(9, stars);
            stars[i] = new Star(pos);
        }
        this.game.setStars(stars);
    }

    private void initializeStarbases(int numberOfStarbases) {
        Position pos;
        Starbase starBases[] = new Starbase[numberOfStarbases];
        for (int i = 0; i < numberOfStarbases; i++) {
            pos = generateNewPosition(9, starBases);
            starBases[i] = new Starbase(pos);
            this.game.addCoordinateString(pos.getQuadrant(), ".1.");
        }
        this.game.setStarbases(starBases);
    }

    private void initializePlanets(int numberOfPlanets) {
        Position pos;
        Planet planets[] = new Planet[numberOfPlanets];
        for (int i = 0; i < numberOfPlanets; i++) {
            pos = generateNewPosition(9, planets);
            planets[i] = new Planet(pos);
        }
        this.game.setPlanets(planets);
    }

    private void initializeKlingons() {
        int skill = this.game.getSkill().getSkillValue();
        int initKling = randInt(0, 1 + (skill + 1) * skill * 0.1);
        int sCmd = (this.game.getSkill() == Game.GameLevel.GOOD ||
                this.game.getSkill() == Game.GameLevel.EXPERT ||
                this.game.getSkill() == Game.GameLevel.EMERITUS ? 1 : 0);
        int cmd = (int) (skill + 0.0625 * initKling * randDouble(0, 1));
        int ord = initKling - cmd - sCmd;
        Position pos;
        Klingon klingons[] = new Klingon[ord];
        KlingonCommander klingonCommanders[] = new KlingonCommander[cmd];
        KlingonSuperCommander klingonSuperCommander = null;

        for (int i = 0; i < ord; i++) {
            pos = generateNewPosition(9, klingons);
            klingons[i] = new Klingon(pos);
        }

        for (int i = 0; i < cmd; i++) {
            pos = generateNewPosition(9, klingonCommanders);
            klingonCommanders[i] = new KlingonCommander(pos);
        }

        if (sCmd == 1) {
            pos = generateNewPosition(9, klingonSuperCommander);
            this.game.setKlingonSuperCommander(new KlingonSuperCommander(pos));
        }

        this.game.setKlingons(klingons);
        this.game.setKlingonCommanders(klingonCommanders);
        this.game.setKlingonSuperCommander(klingonSuperCommander);
    }

    private Position generateNewPosition(int maxElementsInQuadrant, Entity... entities) {
        Coordinate quadrant = new Coordinate(Utils.randInt(0, 7), (Utils.randInt(0, 7)));
        Coordinate sector = new Coordinate(Utils.randInt(0, 9), (Utils.randInt(0, 9)));
        Position position = new Position(quadrant, sector);
        while (!isPositionEmpty(position) && entities != null
                && getNumberOfEntiesBeforeMapUpdate(entities, position) <= maxElementsInQuadrant) {
            position = generateNewPosition(maxElementsInQuadrant, entities);
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
