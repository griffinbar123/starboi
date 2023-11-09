package Model;

import lombok.Data;

@Data
public class Shield {
    private ShieldStatus status = ShieldStatus.UP;
    private double level = 2500.0;
    private double maxLevel = 2500.0;
}
