package Model;

public enum EntityType {
    ENTERPRISE('E', "Enterprise"),
    PLANET('P', "Planet"),
    FAERIE_QUEEN('F', "Faerie Queen"),
    KLINGON('K', "Klingon"),
    COMMANDER('C', "Commander"),
    ROMULAN('R', "Romulan"),
    STAR('*', "Star"),
    STARBASE('B', "Starbase"),
    BLACK_HOLE(' ', "Black Hole"),
    THOLIAN('T', "Tholian"),
    THOLIAN_WEB('#', "Tholian Web"),
    undefined('?', "Undefined");

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
