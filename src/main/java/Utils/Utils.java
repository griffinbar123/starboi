package Utils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import Model.Entity;

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
        List<String> params = Stream.of(cmd.split("[\\s\\p{Punct}]"))
                .filter(s -> !s.equals(""))
                .filter(s -> !s.equals(" "))
                .filter(s -> !s.equals("\n"))
                .map(String::trim)
                .map(String::toUpperCase)
                .toList();
        return (params.size() > 0 ? Optional.ofNullable(params) : Optional.empty());
    }

    /**
     * Parse Integers from string
     * 
     * @param str input string
     * @return list of Integers in user command
     * @author Matthias Schrock
     */
    public static Optional<List<Integer>> parseIntegers(String str) {
        List<Integer> integers = Stream.of(str.split("[\\s\\p{Punct}]"))
                .filter(s -> s.matches("\\d+"))
                .map(Integer::valueOf)
                .toList();
        return (integers.size() > 0 ? Optional.ofNullable(integers) : Optional.empty());
    }

    /**
     * Parse Doubles from string
     * 
     * @param str input string
     * @return list of Doubles in the input string
     * @author Matthias Schrock
     */
    public static Optional<List<Double>> parseDoubles(String str) {
        List<Double> doubles = Stream.of(str.split("[\\s\\p{Punct}&&[^\\.]]"))
                .filter(s -> s.matches("\\d+\\.?\\d*"))
                .map(Double::valueOf)
                .toList();
        return (doubles.size() > 0 ? Optional.ofNullable(doubles) : Optional.empty());
    }

    /**
     * Parse Integers from list of string
     * 
     * @param params list of String
     * @return list of Integers in original String list
     * @author Matthias Schrock
     */
    public static List<Integer> parseIntegers(List<String> params) {
        return params.stream()
                .filter(s -> s.matches("\\d+"))
                .map(Integer::valueOf)
                .toList();
    }

    /**
     * Parse Doubles from list of string
     * 
     * @param params list of String
     * @return list of Doubles in original String list
     * @author Matthias Schrock
     */
    public static List<Double> parseDoubles(List<String> params) {
        return params.stream()
                .filter(s -> s.matches("\\d+\\.?\\d*"))
                .map(Double::valueOf)
                .toList();
    }

    /**
     * 
     * @param entities array of entities to be turned into a string
     * @return a string of the entities' locations
     */
    public static String turnEntityQuadrantsToStrings(Entity[] entities) {
        String locs = "";
        for (Entity entity : entities) {
            locs += (entity.getPosition().getQuadrant().getX() + 1) + " - "
                    + (entity.getPosition().getQuadrant().getY() + 1) + "  ";
        }
        return locs;
    }
}
