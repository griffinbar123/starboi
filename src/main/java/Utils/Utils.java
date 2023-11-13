package Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Coordinate;
import Model.Entity;
import Model.EntityType;
import Model.Position;

public class Utils {
    /**
     * 
     * @param min
     * @param max
     * @return a random integer between the specified minimum and maximum
     */
    public static int randInt(double min, double max) {
        return (int) Math.floor(Math.random() * (max - min + 1) + min);
    }

    /**
     * 
     * @param min
     * @param max
     * @return a random double between the specified minimum and maximum
     */
    public static double randDouble(double min, double max) {
        java.util.Random r = new java.util.Random();
        return min + (max - min) * r.nextDouble();
    }

    /**
     * 
     * @param obj to be serialized
     * @return json string with current game session data
     */
    public static <T> String serialize(T obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(obj);
    }

    /**
     * 
     * @param <T>  object return type
     * @param json string to be deserialized into object
     * @param obj  type of object to be returned
     * @return a java objected of type T with data from json
     * @throws JsonProcessingException
     */
    public static <T> T deserialize(String json, Class<T> obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, obj);
    }

    /**
     * Parse command and parameters from string. The command is assumed to be at
     * index 0
     * 
     * @param cmd
     * @return list containing command at index 0 and parameters at index 1+
     * @author Matthias Schrock
     */
    public static Optional<List<String>> readCommands(String cmd) {
        List<String> params = new ArrayList<String>(
                Stream.of(cmd.split("[\\s\\p{Punct}&&[^\\.]&&[^\\-]]"))
                        .filter(s -> !s.equals(""))
                        .filter(s -> !s.equals(" "))
                        .filter(s -> !s.equals("\n"))
                        .map(String::trim)
                        .map(String::toUpperCase)
                        .toList());
        return (params.size() > 0 ? Optional.ofNullable(params) : Optional.empty());
    }

    /**
     * Parse Integers from string
     * 
     * @param str input string
     * @return list of Integers in user command
     * @author Matthias Schrock
     */
    public static List<Integer> parseIntegers(String str) {
        if(str == null)
            return new ArrayList<Integer>();
        List<Integer> integers = new ArrayList<Integer>(
                Stream.of(str.split("[\\s\\p{Punct}&&[^\\.]&&[^\\-]]"))
                        .filter(s -> s.matches("\\-?\\d+\\.?\\d*"))
                        .map(Double::valueOf)
                        .map(Double::intValue)
                        .toList());
        return integers;
    }

    /**
     * Parse Doubles from string
     * 
     * @param str input string
     * @return list of Doubles in the input string
     * @author Matthias Schrock
     */
    public static List<Double> parseDoubles(String str) {
        if(str == null)
            return new ArrayList<Double>();
        List<Double> doubles = new ArrayList<Double>(
                Stream.of(str.split("[\\s\\p{Punct}&&[^\\.]&&[^\\-]]"))
                        .filter(s -> s.matches("\\-?\\d+\\.?\\d*"))
                        .map(Double::valueOf)
                        .toList());
        return doubles;
    }

    /**
     * Parse Integers from list of string
     * 
     * @param params list of String
     * @return list of Integers in original String list
     * @author Matthias Schrock
     */
    public static List<Integer> parseIntegers(List<String> params) {
        if(params == null)
            return new ArrayList<Integer>();
        return new ArrayList<Integer>(
                params.stream()
                        .filter(s -> s.matches("\\-?\\d+\\.?\\d*"))
                        .map(Double::valueOf)
                        .map(Double::intValue)
                        .toList());
    }

    /**
     * Parse Doubles from list of string
     * 
     * @param params list of String
     * @return list of Doubles in original String list
     * @author Matthias Schrock
     */
    public static List<Double> parseDoubles(List<String> params) {
        if(params == null)
            return new ArrayList<Double>();
        return new ArrayList<Double>(
                params.stream()
                        .filter(s -> s.matches("\\-?\\d+\\.?\\d*"))
                        .map(Double::valueOf)
                        .toList());
    }

    /**
     * 
     * @param entities array of entities to be turned into a string
     * @return a string of the entities' locations
     */
    public static String turnEntityQuadrantsToStrings(Entity[] entities) {
        String locs = "";
        for (Entity entity : entities) {
            locs += (entity.getPosition().getQuadrant().getY() + 1) + " - "
                    + (entity.getPosition().getQuadrant().getX() + 1) + "  ";
        }
        return locs;
    }

    /**
     * 
     * @param pos1 first position to be compared
     * @param pos2 second position to be compared
     * @return a boolean inticating wether two positions are equal
     */
    public static boolean isEqual(Position pos1, Position pos2) {
        return pos1.getQuadrant().getX() == pos2.getQuadrant().getX() && pos1.getQuadrant().getY() == pos2.getQuadrant().getY()
        && pos1.getSector().getX() == pos2.getSector().getX() && pos1.getSector().getY() == pos2.getSector().getY();
    }

    /**
     * 
     * @param quad quadrant to be compared
     * @param entity entity to be compared
     * @return a boolean inticating wether a quad on a map contains the symbol of an entity
     */
    public static Boolean checkEntityAgainstQuadrant(Coordinate quad, Entity entity) {
        // checks if entity is in a position, used mainly if map may not be updated
        return entity != null && entity.getPosition().getQuadrant().getX() == quad.getX()
                && entity.getPosition().getQuadrant().getY() == quad.getY();
    }

    /**
     * 
     * @param pos1 position to be compared
     * @param entity entity to be compared
     * @return a boolean inticating wether a pos on a map contains the symbol of an entity
     */
    public static Boolean checkEntityAgainstPosition(Position position, Entity entity) {
        // checks if entity is in a position, used mainly if map may not be updated
        return entity != null && checkEntityAgainstQuadrant(position.getQuadrant(), entity) &&
                entity.getPosition().getSector().getX() == position.getSector().getX()
                && entity.getPosition().getSector().getY() == position.getSector().getY();
    }

    /**
     * 
     * @param pos1 position to be compared
     * @param entities entities to be compared
     * @return a boolean inticating wether a pos on a map contains the symbol of an entity
     */
    public static Boolean checkEntityListAgainstPosition(Position position, Entity[] entities) {
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

    /**
     * 
     * @param pos1 pos1 to be compared
     * @param pos2 pos2 to be compared
     * @return a boolean inticating wether 2 positions are in the same Quadrant
     */
    public static Boolean positionsHaveSameQuadrant(Position position, Position position2) {
        // checks if any entity in the list provided is is in the provided position
        return position.getQuadrant().getX() == position2.getQuadrant().getX()
                && position.getQuadrant().getY() == position2.getQuadrant().getY();
    }

    public static String outputEntity(Integer iy, Integer ix, EntityType type) {
        return "***" + type.getName() + " at " + iy + " - " + ix;
    }

    public static String outputEntity(Integer iy, Integer ix, char symbol) {
        return "***" + symbol + " at " + iy + " - " + ix;
    }
}
