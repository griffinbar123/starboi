package Model;

/**
 * Game lengths and their corresponding numbers,
 * which are used later in scoring calculations
 */
public enum GameLength {
    SHORT(1),
    MEDIUM(2),
    LONG(3),
    UNDEFINED(0);

    private final int lengthVal;

    private GameLength(int lengthVal) {
        this.lengthVal = lengthVal;
    }

    public int getLengthValue() {
        return lengthVal;
    }
}
