package Model;

import lombok.Data;

@Data
public class Sheild {
    public static enum Status {
        UP,
        DOWN,
        RESERVES;
    }

    private Status status = Status.UP;
    private int level;
    private double units;
}
