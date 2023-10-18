package Model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Data
@EqualsAndHashCode(callSuper = true)
public class Enterprise extends Entity {
    private String condition;

    private byte lifeSupport;
    
    private double warp = 5.0f;
    private double energy = 5000.0f;

    private int torpedoes;
    
    private Sheild sheilds = new Sheild();
    
    private final char symbol = 'E';
    
    public Enterprise(@NonNull Position position) {
        super(position);
    }
}
