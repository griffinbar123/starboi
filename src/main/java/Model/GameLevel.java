package Model;

/**
 * Game levels and their corresponding skill value used in
 * scoring and initialization calculations
 */
public enum GameLevel {
    NOVICE(1),
    FAIR(2),
    GOOD(3),
    EXPERT(4),
    EMERITUS(5),
    UNDEFINED(0);

    private final int skillVal;

    private GameLevel(int skillVal) {
        this.skillVal = skillVal;
    }

    public int getSkillValue() {
        return skillVal;
    }
}
