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

    public static double roundN(double num, int n) {
        return Math.round(num * Math.pow(10, n)) / Math.pow(10, n);
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
     * Parse Doubles from string
     * 
     * @param str input string
     * @return list of Doubles in the input string
     * @author Matthias Schrock
     */
    public static Optional<List<Double>> parseDoubles(String str) {
        if(str == null)
            return Optional.empty();
        List<Double> doubles = new ArrayList<Double>(
                Stream.of(str.split("[\\s\\p{Punct}&&[^\\.]&&[^\\-]]"))
                        .filter(s -> s.matches("\\-?\\d+\\.?\\d*"))
                        .map(Double::valueOf)
                        .toList());
        return (doubles.size() > 0 ? Optional.ofNullable(doubles) : Optional.empty());
    }

    /**
     * Parse Integers from list of string
     * 
     * @param params list of String
     * @return list of Integers in original String list
     * @author Matthias Schrock
     */
    public static Optional<List<Integer>> parseIntegers(List<String> params) {
        if(params == null)
            return Optional.empty();
        List<Integer> integers = new ArrayList<Integer>(
                params.stream()
                        .filter(s -> s.matches("\\-?\\d+\\.?\\d*"))
                        .map(Double::valueOf)
                        .map(Double::intValue)
                        .toList());
        return (integers.size() > 0 ? Optional.ofNullable(integers) : Optional.empty());
    }

    /**
     * Parse Doubles from list of string
     * 
     * @param params list of String
     * @return list of Doubles in original String list
     * @author Matthias Schrock
     */
    public static Optional<List<Double>> parseDoubles(List<String> params) {
        if(params == null)
            return Optional.empty();
        List<Double> doubles = new ArrayList<Double>(
                params.stream()
                        .filter(s -> s.matches("\\-?\\d+\\.?\\d*"))
                        .map(Double::valueOf)
                        .toList());
        return (doubles.size() > 0 ? Optional.ofNullable(doubles) : Optional.empty());
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
     * @param quad quadrant to be compared
     * @param entity entity to be compared
     * @return a boolean inticating wether a quad on a map contains the symbol of an entity
     */
    public static Boolean checkEntityAgainstQuadrant(Coordinate quad, Entity entity) {
        if (entity == null)
            return false;
        return entity.getPosition().getQuadrant().getX() == quad.getX()
                && entity.getPosition().getQuadrant().getY() == quad.getY();
    }

    public static String outputEntity(Integer iy, Integer ix, EntityType type) {
        return "***" + type.getName() + " at " + iy + " - " + ix;
    }

    public static String outputEntity(Integer iy, Integer ix, char symbol) {
        return "***" + symbol + " at " + iy + " - " + ix;
    }
}
