package Utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Utils {
    /**
     * 
     * @param min
     * @param max
     * @return a random integer between the specified minimum and maximum
     */
    public static int randInt(int min, int max) {
        return (int) Math.floor(Math.random() * (max - min + 1) + min);
    }

    /**
     * 
     * @param min
     * @param max
     * @return a random double between the specified minimum and maximum
     */
    public static double randDouble(double min, double max) {
        return Math.random() * (max - min + 1) + min;
    }

    /**
     * 
     * @param obj to be serialized
     * @return json string with current game session data
     */
    public static String serialize(Object obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
    }

    /**
     * 
     * @param <T> object return type
     * @param json string to be deserialized into object
     * @param obj type of object to be returned
     * @return a java objected of type T with data from json
     * @throws JsonProcessingException
     */
    public static <T> Object deserialize(String json, T obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, obj.getClass());
    }
}
