package main.java.sst;


import lombok.Data;

@Data
public class Entity {
    public Position position;
    public char symbol;

    public Entity(Position position, char symbol) {
        this.position = position;
        this.symbol = symbol;
    }
}
