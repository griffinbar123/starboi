package sst;

import Model.Enterprise;
import Model.Game;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Damages {
    @NonNull
    private Game game;

    public void ExecDAMAGES() {
        Enterprise enterprise = this.game.getEnterprise();
        game.con.printf("Device       -REPAIR TIMES-\n");
        game.con.printf("             IN FLIGHT  DOCKED\n");
        game.con.printf("     s. R. Sensors   %.2f    %.2f\n", 0.0, 0.0);
        game.con.printf("     L. R. Sensors   %.2f    %.2f\n", 0.0, 0.0);
        game.con.printf("           Phasers   %.2f    %.2f\n", 0.0, 0.0);
        game.con.printf("      Photon Tubes   %.2f    %.2f\n", 0.0, 0.0);
        game.con.printf("      Life Support   %.2f    %.2f\n", 0.0, 0.0);
        game.con.printf("      Warp Engines   %.2f    %.2f\n", 0.0, 0.0);
        game.con.printf("   Impulse Engines   %.2f    %.2f\n", 0.0, 0.0);
        game.con.printf("           Shields   %.2f    %.2f\n", 0.0, 0.0);
        game.con.printf("    Subspace Radio   %.2f    %.2f\n", 0.0, 0.0);
        game.con.printf("     Shuttle Craft   %.2f    %.2f\n", 0.0, 0.0);
        game.con.printf("          Computer   %.2f    %.2f\n", 0.0, 0.0);
        game.con.printf("       Transporter   %.2f    %.2f\n", 0.0, 0.0);
        game.con.printf("    Shield Control   %.2f    %.2f\n", 0.0, 0.0);
        game.con.printf("       D. S. Probe   %.2f    %.2f\n", 0.0, 0.0);
        game.con.printf("   Cloaking Device   %.2f    %.2f\n", 0.0, 0.0);
    }

}
