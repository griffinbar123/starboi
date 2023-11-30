package sst;

public enum Command {
    SRSCAN(true),
    MOVE(true),
    PHASERS(false),
    CALL(false, false),
    STATUS(true),
    IMPULSE(false),
    PHOTONS(true),
    ABANDON(false, false),
    LRSCAN(true),
    WARP(false),
    SHIELDS(false),
    DESTRUCT(false, false),
    CHART(true),
    REST(true),
    DOCK(true),
    QUIT(false, true),
    DAMAGES(true),
    REPORT(false),
    SENSORS(false),
    ORBIT(false),
    TRANSPORT(false),
    MINE(false),
    CRYSTALS(false),
    SHUTTLE(false),
    PLANETS(false),
    REQUEST(false),
    DEATHRAY(false, false),
    FREEZE(false, false),
    COMPUTER(true),
    EMEXIT(false),
    PROBE(false),
    COMMANDS(true),
    SCORE(true),
    CLOAK(false),
    CAPTURE(false),
    HELP(false, true),
    DEBUG(false, false),
    undefined(true);


    private boolean canAbbrev;
    private boolean implemented;

    Command(boolean impl) {
        canAbbrev = true;
        implemented = impl;
    }

    Command(boolean abrOk, boolean impl) {
        canAbbrev = abrOk;
        implemented = impl;
    }

    public boolean CanAbbrev() {
        return canAbbrev;
    }

    public boolean Implemented() {
        return implemented;
    }
}