package sst;

import lombok.Data;

@Data
public class Klingon extends Entity {
    
    public Klingon(Position position) {
        super(position, 'K');
    }
}
