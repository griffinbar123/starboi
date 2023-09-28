package sst;

import lombok.Data;

@Data
public class Planet extends Entity {
    
    public Planet(Position position) {
        super(position, 'P');
    }
}
