package sst;

import Model.Game;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Commands {
    @NonNull
    private Game game;

    public void ExecCOMMANDS() {
        String out = "   SRSCAN    MOVE      PHASERS   CALL\n" +
                "   STATUS    IMPULSE   PHOTONS   ABANDON\n" +
                "   LRSCAN    WARP      SHIELDS   DESTRUCT\n" +
                "   CHART     REST      DOCK      QUIT\n" +
                "   DAMAGES   REPORT    SENSORS   ORBIT\n" +
                "   TRANSPORT MIHE      CRYSTALS  SHUTTLE\n" +
                "   PLANETS   REQUEST   DEATHRAY  FREEZE\n" +
                "   COMPUTER  EMEXIT    PROBE     COMMANDS\n" +
                "   SCORE     CLOAK     CAPTURE   HELP\n\n";
        this.game.con.printf("%s", out);
    }
}
