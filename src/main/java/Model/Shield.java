package Model;

import lombok.Data;

@Data
public class Shield {
    private ShieldStatus status = ShieldStatus.UP;
    private int level;
    private double units;
}
