package sst;

import Model.Game;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Commands {
    @NonNull
    private Game game;

    public void ExecCOMMANDS() {
        // print commands
        this.game.con.printf("   SRSCAN    MOVE      PHASERS   CALL\n");
        this.game.con.printf("   STATUS    IMPULSE   PHOTONS   ABANDON\n");
        this.game.con.printf("   LRSCAN    WARP      SHIELDS   DESTRUCT\n");
        this.game.con.printf("   CHART     REST      DOCK      QUIT\n");
        this.game.con.printf("   DAMAGES   REPORT    SENSORS   ORBIT\n");
        this.game.con.printf("   TRANSPORT MIHE      CRYSTALS  SHUTTLE\n");
        this.game.con.printf("   PLANETS   REQUEST   DEATHRAY  FREEZE\n");
        this.game.con.printf("   COMPUTER  EMEXIT    PROBE     COMMANDS\n");
        this.game.con.printf("   SCORE     CLOAK     CAPTURE   HELP\n");
        this.game.con.printf("\n");
    }
}
