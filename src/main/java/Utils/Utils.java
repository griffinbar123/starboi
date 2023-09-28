package Utils;

public class Utils {
    public static int randInt(int min, int max) {
        return (int) Math.floor(Math.random() * (max - min + 1) + min);
    }

    public static double randDouble(double min, double max) {
        return Math.random() * (max - min + 1) + min;
    }
}
