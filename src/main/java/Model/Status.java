package Model;

import lombok.Data;

@Data
public class Status {
    private float starDate;
    private String condition;

    private int sectorX;
    private int sectorY;
    private int quadrantX;
    private int quadrantY;

    private byte lifeSupport;

    private float warp;
    private float energy;

    private int torpedoes;

    private Sheild sheilds = new Sheild();

    private int klingons;

    private float time;
}
