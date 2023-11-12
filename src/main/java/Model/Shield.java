package Model;

import lombok.Data;

@Data
public class Shield {
    private ShieldStatus status = ShieldStatus.DOWN;
    private double level = 2500.0;
    private double maxLevel = 2500.0;
    private Boolean shieldIsChanging = false;
}
