package Utils;

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
}
