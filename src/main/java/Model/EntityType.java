package Model;

public enum EntityType {
    ENTERPRISE('E', "Enterprise"),
    PLANET('P', "Planet"),
    FAERIE_QUEEN('F', "Faerie Queen"),
    COMMANDER('C', "Commander"),
    STAR('*', "Star"),
    STARBASE('B', "Starbase"),
    BLACK_HOLE(' ', "Black Hole"),
    THOLIAN('T', "Tholian"),
    THOLIAN_WEB('#', "Tholian Web"),
    UNKNOWN('?', "Unknown");

    private final char symbol;
    private final String name;

    EntityType(char symbol, String name) {
        this.symbol = symbol;
        this.name = name;
    }

    public char getSymbol() {
        return symbol;
    }

    public String getName() {
        return name;
    }
}
