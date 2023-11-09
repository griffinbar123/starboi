package Model;

public enum EntityType {
    ENTERPRISE('E', "Enterprise"),
    PLANET('P', "Planet"),
    FAERIE_QUEEN('F', "Faerie Queen"),
    KLINGON('K', "Klingon"),
    COMMANDER('C', "Commander"),
    SUPER_COMMANDER('S', "Super Commander"),
    ROMULAN('R', "Romulan"),
    STAR('*', "Star"),
    STARBASE('B', "Starbase"),
    BLACK_HOLE(' ', "Black Hole"),
    THOLIAN('T', "Tholian"),
    THOLIAN_WEB('#', "Tholian Web"),
    NOTHING('\u00B7', "Nothing"),
    // NOTHING('\u2665', "Nothing"),
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
