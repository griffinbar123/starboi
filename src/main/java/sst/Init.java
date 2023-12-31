package sst;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import Model.BlackHole;
import Model.Coordinate;
import Model.Device;
import Model.Enterprise;
import Model.Entity;
import Model.Game;
import Model.GameLength;
import Model.GameLevel;
import Model.GameType;
import Model.Klingon;
import Model.KlingonCommander;
import Model.KlingonSuperCommander;
import Model.Planet;
import Model.Position;
import Model.Romulan;
import Model.Star;
import Model.Starbase;
import Utils.Utils;
import static Utils.Utils.randDouble;
import static Utils.Utils.randInt;
import static Utils.Utils.turnEntityQuadrantsToStrings;
import static Utils.Utils.readCommands;

/**
 * Initializes a game. In future versions, this class will be able
 * to restore a saved game state
 * 
 * @author Griffin Barnard
 * @author Matthias Schrock
 * @author Fabrice Mpozenzi
 */
public class Init {
    private Game game;

    /**
     * Starts the game
     * 
     * @author Matthias Schrock
     */
    public void start(String[] args) {
        game = new Game();
        CommandHandler handler = new CommandHandler(this.game);
        double starDate;

        // game.setType(getGameParam(GameType.class));
        game.setLength(getGameParam(GameLength.class));
        game.setSkill(getGameParam(GameLevel.class));
        game.setType(GameType.REGULAR);
        // game.setLength(GameLength.SHORT);
        // game.setSkill(GameLevel.NOVICE);

        // TODO
        // System.out.print("Please type in a secret password (9 characters maximum)-");
        // System.out.println("changeit");

        game.setDamageFactor(game.getSkill().getSkillValue() * 0.5);

        starDate = randDouble(21, 39) * 100;
        game.setStarDate(starDate);
        game.setInitStarDate(starDate);
        game.getScore().setInitStarDate(starDate);
        game.setTime(this.game.getLength().getLengthValue() * 7);

        // TODO: initialize these romulans on game type, length, and skill
        int romulans = 4;

        initializeEnterprise();
        initializePlanets();
        initializeKlingons();
        initializeStarbases();
        initializeStars();
        initializeRomulans(romulans);
        initializeBlackHoles();

        if (args.length > 0 && args[0].equals("demo")) {
            demo();
        }

        game.updateMap();

        printStartupMessage();

        handler.getAndExecuteCommands();
    }

    private void demo() {
        game.con.printf("\n\n\n******************DEMO MODE******************");
        game.getEnterprise().setPosition(new Position(0, 1, 4, 4));
        game.getKlingons()[0].setPosition(new Position(0, 0, 0, 9));
        if (game.getKlingons().length > 1) {
            game.getKlingons()[1].setPosition(new Position(1, 0, 0, 1));
        }
        game.getBlackHoles()[0].setPosition(new Position(0, 1, 8, 1));
        game.getStarbases()[0].setPosition(new Position(0, 0, 0, 0));

        int cnt = 2;
        Coordinate quad = new Coordinate(0, 1);
        for (int i = 0; i < game.getStars().length; i++) {
            if (game.getStars()[i].getPosition().getQuadrant().isEqual(quad)) {
                if (cnt > 0) {
                    cnt--;
                    game.getStars()[i].setPosition(new Position(0, 1, cnt + 3, 0));
                } else {
                    game.getStars()[i] = null;
                }
            }
        }

        Coordinate quad2 = new Coordinate(0, 0);
        for (int i = 0; i < game.getRomulans().length; i++) {
            if (game.getRomulans()[i].getPosition().getQuadrant().isEqual(quad) ||
                    game.getRomulans()[i].getPosition().getQuadrant().isEqual(quad2)) {
                game.getRomulans()[i] = null;
            }
        }
    }

    /**
     * Prints the startup message
     * 
     * @author Matthias Schrock
     */
    private void printStartupMessage() {
        String initMessage = String.format("\n\n\nIt is stardate %.1f. The Federation is being attacked by " +
                "a deadly Klingon invasion force. As captain of the United Starship " +
                "U.S.S. Enterprise, it is your mission to seek out and destroy this " +
                "invasion force of %d battle cruisers. You have an initial allotment " +
                "of %.0f stardates to complete your mission. As you proceed you may be " +
                "given more time.\n\nYou will have %d supporting starbases. Starbase " +
                "locations- %s\n\nThe Enterprise is currently in Quadrant %d - %d " +
                "Sector %d - %d\n\nGood Luck!\n\n",
                game.getStarDate(), game.getKlingons().length, game.getTime(), game.getStarbases().length,
                turnEntityQuadrantsToStrings(this.game.getStarbases()),
                (game.getEnterprise().getPosition().getQuadrant().getY() + 1),
                (game.getEnterprise().getPosition().getQuadrant().getX() + 1),
                (game.getEnterprise().getPosition().getSector().getY() + 1),
                (game.getEnterprise().getPosition().getSector().getX() + 1));
        this.game.con.printf("%s", initMessage);
    }

    /**
     * Get a game parameter from the user
     * 
     * @param <T> Enum type
     * @param t   Enum class
     * @return Enum constant chosen by user
     * @author Matthias Schrock
     * @author Fabrice Mpozenzi
     */
    private <T extends Enum<T>> T getGameParam(Class<T> t) {
        T param;
        String in = "";
        String parsed = "";

        while (true) {
            if (t == GameType.class) {
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
     * @param e   Enum class
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

    private void initializeEnterprise() {
        Position pos = generateNewPosition(9, (Entity) null);
        Enterprise enterprise = new Enterprise(pos);
        Map<Device, Double> devices = new HashMap<>();

        for (Device d : Device.values()) {
            devices.put(d, 0.0);
        }
        enterprise.setDeviceDamage(devices);

        this.game.setEnterprise(enterprise);
    }

    private void initializeRomulans(int numberOfRomulans) {
        Position pos;
        Romulan rom[] = new Romulan[numberOfRomulans];
        for (int i = 0; i < numberOfRomulans; i++) {
            pos = generateNewPosition(9, rom);
            rom[i] = new Romulan(pos);
            rom[i].setPower(randDouble(0, 1) * 400.0 + 450.0 + 50.0 * this.game.getSkill().getSkillValue());
        }
        this.game.setRomulans(rom);
        this.game.getScore().setInitRomulans(numberOfRomulans);
    }

    private void initializeBlackHoles() {
        int numberOfBlackHoles = 0;
        for (int i = 1; i <= 8; i++)
            for (int j = 1; j <= 8; j++)
                numberOfBlackHoles += randDouble(0, 1) > 0.5 ? 1 : 0;
        Position pos;
        BlackHole blackHoles[] = new BlackHole[numberOfBlackHoles];
        for (int i = 0; i < numberOfBlackHoles; i++) {
            pos = generateNewPosition(3, blackHoles);
            blackHoles[i] = new BlackHole(pos);
        }
        this.game.setBlackHoles(blackHoles);
    }

    private void initializeStars() {
        int numberOfStars = 0;
        for (int i = 1; i <= 8; i++)
            for (int j = 1; j <= 8; j++)
                numberOfStars += ((int) (randDouble(0, 1) * 9.0)) + 1;
        Position pos;
        Star stars[] = new Star[numberOfStars];
        for (int i = 0; i < numberOfStars; i++) {
            pos = generateNewPosition(9, stars);
            stars[i] = new Star(pos);
        }
        this.game.setStars(stars);
    }

    private void initializeStarbases() {
        int numberOfStarbases = 3 * ((int) randDouble(0, 1)) + 2;
        Position pos;
        Starbase starBases[] = new Starbase[numberOfStarbases];
        for (int i = 0; i < numberOfStarbases; i++) {
            pos = generateNewPosition(9, starBases);
            starBases[i] = new Starbase(pos);
            this.game.addCoordinateString(pos.getQuadrant(), ".1.");
        }
        this.game.setStarbases(starBases);
    }

    private void initializePlanets() {
        int numberOfPlanets = (int) ((10 / 2) + (10 / 2 + 1) * randDouble(0, 1));
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

        // Original: 2.0*intime*((skill+1 - 2*Rand())*skill*0.1+.15)
        skill = this.game.getSkill().getSkillValue();
        initKling = (int) (2 * this.game.getTime() * ((skill + 1 - 2 * randInt(0, 1)) * skill * 0.1 + 0.15));
        cmd = (int) (skill + 0.0625 * initKling * randDouble(0, 1));
        sCmd = (this.game.getSkill() == GameLevel.GOOD ||
                this.game.getSkill() == GameLevel.EXPERT ||
                this.game.getSkill() == GameLevel.EMERITUS ? 1 : 0);
        ord = initKling - cmd - sCmd;

        Klingon[] klingons = new Klingon[ord];
        KlingonCommander[] klingonCommanders = new KlingonCommander[cmd];
        KlingonSuperCommander klingonSuperCommander = null;

        for (int i = 0; i < ord; i++) {
            pos = generateNewPosition(9, klingons);
            klingons[i] = new Klingon(pos);
            klingons[i].setPower(randDouble(0, 1) * 150.0 + 300.0 + 25.0 * skill);
        }

        for (int i = 0; i < cmd; i++) {
            pos = generateNewPosition(9, klingonCommanders);
            klingonCommanders[i] = new KlingonCommander(pos);
            klingonCommanders[i].setPower(950.0 + 400.0 * randDouble(0, 1) + 50.0 * skill);
        }

        if (sCmd == 1) {
            pos = generateNewPosition(9, klingonSuperCommander);
            klingonSuperCommander = new KlingonSuperCommander(pos);
            klingonSuperCommander.setPower(1175.0 + 400.0 * randDouble(0, 1) + 125.0 * skill);
            this.game.setKlingonSuperCommander(klingonSuperCommander);
        }

        this.game.setKlingons(klingons);
        this.game.setKlingonCommanders(klingonCommanders);

        game.getScore().setInitTotKlingons(initKling);
        game.getScore().setInitKlingons(ord);
        game.getScore().setInitKlingonCmds(cmd);
        game.getScore().setInitKlingonSupCmds(sCmd);
    }

    private Position generateNewPosition(int maxElementsInQuadrant, Entity... entities) {
        Coordinate quadrant = new Coordinate(Utils.randInt(0, 7), (Utils.randInt(0, 7)));
        Coordinate sector = new Coordinate(Utils.randInt(0, 9), (Utils.randInt(0, 9)));
        Position position = new Position(quadrant, sector);
        while (entities != null && (!game.isPositionEmpty(position)
                || getNumberOfEntiesBeforeMapUpdate(entities, position) > maxElementsInQuadrant)) {
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
            if (entities[i] != null && entities[i].getPosition().isEqualQuadrant(position)) {
                numberOfElements += 1;
            }
        }
        return numberOfElements;
    }
}
