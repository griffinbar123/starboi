package sst;

public enum Command {
        SRSCAN,
        MOVE,
        PHASERS,
        CALL(false),
        STATUS,
        IMPULSE,
        PHOTONS,
        ABANDON(false),
        LRSCAN,
        WARP,
        SHIELDS,
        DESTRUCT(false),
        CHART,
        REST,
        DOCK,
        QUIT(false),
        DAMAGES,
        REPORT,
        SENSORS,
        ORBIT,
        TRANSPORT,
        MINE,
        CRYSTALS,
        SHUTTLE,
        PLANETS,
        REQUEST,
        DEATHRAY(false),
        FREEZE(false),
        COMPUTER,
        EMEXIT,
        PROBE,
        COMMANDS,
        SCORE,
        CLOAK,
        CAPTURE,
        HELP(false),
        // DEBUG(false),
        undefined;
        
        private boolean canAbbrev;
        
        Command() {
            canAbbrev = true;
        }

        Command(boolean abrOk) {
            canAbbrev = abrOk;
        }

        public boolean CanAbbrev() {
            return canAbbrev;
        }
    }
