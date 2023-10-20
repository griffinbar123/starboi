package sst;

import java.util.List;
import java.util.Optional;

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
 * @author Griffin Barnard
 * @author Matthias Schrock
 * @author Fabrice Mpozenzi
 */
public class Init {
    private Game game;
    public static final char NOTHING = '\u00B7';

    /**
     * Starts the game
     * @author Matthias Schrock
     */
    public void start() {
        this.game = new Game();
        CommandHandler handler = new CommandHandler(this.game);
        int planets, starbases, stars, romulans;

        // TODO: for testing so we don't have to type in the same thing every time
        // this.game.setType(getGameParam(Game.GameType.class));
        // this.game.setLength(getGameParam(Game.GameLength.class));
        // this.game.setSkill(getGameParam(Game.GameLevel.class));
        this.game.setType(Game.GameType.REGULAR);
        this.game.setLength(Game.GameLength.SHORT);
        this.game.setSkill(Game.GameLevel.NOVICE);

        // TODO
        System.out.print("Please type in a secret password (9 characters maximum)-");
        System.out.println("changeit");

        this.game.setTime(this.game.getLength().getLengthValue() * 7);

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

    /**
     * Prints the startup message
     * @author Matthias Schrock
     */
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
     * Get a game parameter from the user
     * @param <T> Enum type
     * @param t Enum class
     * @return Enum constant chosen by user
     * @author Matthias Schrock
     * @author Fabrice Mpozenzi
     */
    private <T extends Enum<T>> T getGameParam(Class<T> t) {
        T param;
        String in = "";
        String parsed = "";

        while (true) {
            if (t == Game.GameType.class) {
                in = this.game.con.readLine("Are you a " + getEnumString(t) + " player?");
            } else {
                in = this.game.con.readLine("Would you like a " + getEnumString(t) + " game?");
            }

            parsed = readCommands(in).orElse(List.of("")).get(0);
            param = matcher(parsed, t).orElse(null);

            if (param == null) {
                this.game.con.printf("What is " + in + "?\n");
                continue;
            }

            return param;
        }
    }

    /**
     * Match a string to an enum constant. Assumes abbreviation is allowed
     * but will only match if the abbreviation is unique.
     * 
     * @param <T> Enum type
     * @param str String to match
     * @param e Enum class
     * @return Enum constant if match, null otherwise
     * @author Matthias Schrock
     */
    private <T extends Enum<T>> Optional<T> matcher(String str, Class<T> e) {
        boolean prevMatch = false;
        T match = null;

        // e.getDeclaringClass().getEnumConstants()
        for (T t : e.getEnumConstants()) {
            String tStr = t.toString();

            String abrCheck = tStr.substring(0, 
                    Math.min(tStr.length(), str.length()));

            if (str.compareTo(abrCheck) == 0) {
                if (prevMatch) {
                    return Optional.empty();
                }

                match = t;
                prevMatch = true;
            }
        }
        return Optional.ofNullable(match);
    }

    /**
     * Get a comma-separated list of enum values
     * 
     * @param <T> Enum type
     * @param e   Enum class
     * @return comma-separated list of enum values
     * @author Matthias Schrock
     */
    private <T extends Enum<T>> String getEnumString(Class<T> e) {
        int len = e.getEnumConstants().length - 1;
        String values = "";
        if (len == 1) {
            return e.getEnumConstants()[0].toString().toLowerCase();
        }
        if (len == 2) {
            return e.getEnumConstants()[0].toString().toLowerCase() + " or " +
                    e.getEnumConstants()[1].toString().toLowerCase();
        }
        for (int i = 0; i < len; i++) {
            if (i == len - 1) {
                values += "or " + e.getEnumConstants()[i].toString().toLowerCase();
            } else {
                values += e.getEnumConstants()[i].toString().toLowerCase() + ", ";
            }
        }
        return values;
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
        Position pos;
        int skill, initKling, sCmd, cmd, ord;

        skill = this.game.getSkill().getSkillValue();
        initKling = (int) (2 * this.game.getTime() * ((skill + 1 - 2 * randInt(0, 1)) * skill * 0.1 + 0.15)); // 2.0*intime*((skill+1 - 2*Rand())*skill*0.1+.15)
        sCmd = (this.game.getSkill() == Game.GameLevel.GOOD ||
                this.game.getSkill() == Game.GameLevel.EXPERT ||
                this.game.getSkill() == Game.GameLevel.EMERITUS ? 1 : 0);
        cmd = (int) (skill + 0.0625 * initKling * randDouble(0, 1));
        ord = initKling - cmd - sCmd;

        // TODO: not best practice to initialize arrays like this
        Klingon[] klingons = new Klingon[ord];
        KlingonCommander[] klingonCommanders = new KlingonCommander[cmd];
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
                    && entities[i].getPosition().getQuadrant().getY() == position.getQuadrant().getY()) {
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
