package sst;

import java.util.List;
import java.util.Optional;
import Model.Game;
import Model.GameLength;
import Model.GameLevel;
import Model.GameType;
import Utils.Utils;
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

    /**
     * Starts the game
     * @author Matthias Schrock
     */
    public void start() {
        game = new Game();
        CommandHandler handler = new CommandHandler(this.game);
        double starDate;

        // TODO: for testing so we don't have to type in the same thing every time
        // game.setType(getGameParam(GameType.class));
        // game.setLength(getGameParam(GameLength.class));
        // game.setSkill(getGameParam(GameLevel.class));
        game.setType(GameType.REGULAR);
        game.setLength(GameLength.SHORT);
        game.setSkill(GameLevel.NOVICE);

        // TODO
        System.out.print("Please type in a secret password (9 characters maximum)-");
        System.out.println("changeit");

        game.setDamageFactor(game.getSkill().getSkillValue() * 0.5);

        starDate = randDouble(21, 39) * 100;
        game.setStarDate(starDate);
        game.getScore().setInitStarDate(starDate);
        game.setTime(this.game.getLength().getLengthValue() * 7);

        game.getGameMap().init();

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
                (this.game.getEnterprise().getPosition().getQuadrant().getY() + 1),
                (this.game.getEnterprise().getPosition().getQuadrant().getX() + 1),
                (this.game.getEnterprise().getPosition().getSector().getY() + 1),
                (this.game.getEnterprise().getPosition().getSector().getX() + 1));
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
}
