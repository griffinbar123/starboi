package sst;

import java.util.ArrayList;
import java.util.List;

import Model.Condition;
import Model.Device;
import Model.Game;
import Model.ShieldStatus;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import sst.Finish.GameOverReason;

import static Utils.Utils.parseDoubles;
import static Utils.Utils.readCommands;


/**
 * Handles the move command
 * 
 * @author Griffin Barnard
 */
@RequiredArgsConstructor
public class Shields {
    @NonNull
    private Game game;

    private enum ShieldType {
        UP,
        DOWN,
        TRANSFER,
        undefined
    }

    /**
     * Moves player in the map
     */
    public void ExecSHIELDS(List<String> params) {
        game.setReadyForHit(false);
        ShieldType shieldType = ShieldType.undefined;
        String cmdstr;
        ShieldStatus currentStatus = game.getEnterprise().getSheilds().getStatus();

        if (params.size() == 0) {
            if(getIfTransfer())
                shieldType = ShieldType.TRANSFER;
            if(!shieldType.equals(ShieldType.TRANSFER)) {
                if(game.getEnterprise().getDeviceDamage().get(Device.SHIELDS) > 0) {
                    game.con.printf("Shields damaged and down.\n");
                    return;
                }
                shieldType = currentStatus.equals(ShieldStatus.DOWN) ? ShieldType.UP : ShieldType.DOWN;
                cmdstr = currentStatus.equals(ShieldStatus.DOWN) ?
                 this.game.con.readLine("Shields are down. Do you want them up? ") :
                 this.game.con.readLine("Shields are up. Do you want them down? ");
                params = readCommands(cmdstr).orElse(new ArrayList<String>());
                while(params.size() == 0 || (!params.get(0).equals("N") && !params.get(0).equals("Y"))) {
                    // System.out.println(params);
                    cmdstr = this.game.con.readLine("Please answer with \"Y\" or \"N\":");
                    params = readCommands(cmdstr).orElse(null);
                }

                if(params.get(0).equals("N")) return;
            }
        } else {
            String shieldTypeT = params.get(0);
            shieldType = matchShieldType(shieldTypeT);
            if(!shieldType.equals(ShieldType.TRANSFER)) {
                if(game.getEnterprise().getDeviceDamage().get(Device.SHIELDS) > 0) {
                    game.con.printf("Shields damaged and down.\n");
                    return;
                }
            }
        }

        switch (shieldType) {
            case TRANSFER:
                transferEnergy(params);
                break;
            case UP:
                raiseShields();
                break;
            case DOWN:
                lowerShields();
                break;
            default:
            System.err.println("hereee");
                this.game.begPardon();
                break;
        }

    }
    
    private void transferEnergy(List<String> params) {
        Double energyToTransfer;
        if(params.size() == 2) {
            energyToTransfer = Double.parseDouble(params.get(1));
        } else {
            String cmdstr;
            cmdstr = this.game.con.readLine("Energy to transfer to shields- ");
            List<Double> params2 = parseDoubles(cmdstr).orElse(null);
            while(params2 == null || params2.size() == 0) {
                cmdstr = this.game.con.readLine("Energy to transfer to shields- ");
                params2 = parseDoubles(cmdstr).orElse(null);
            }
            energyToTransfer = params2.get(0);
        }

        if (energyToTransfer==0) return;
        if (energyToTransfer > game.getEnterprise().getEnergy()) {
            game.con.printf("Insufficient ship energy.\n");
            return;
        }
        game.setReadyForHit(true);
        if (game.getEnterprise().getSheilds().getLevel()+energyToTransfer >= game.getEnterprise().getSheilds().getMaxLevel()) {
            game.con.printf("Shield energy maximized.\n");
            game.con.printf("Excess energy requested returned to ship energy\n");
            
            game.getEnterprise().setEnergy(game.getEnterprise().getEnergy() - (game.getEnterprise().getSheilds().getMaxLevel() - game.getEnterprise().getSheilds().getLevel()));
            game.getEnterprise().getSheilds().setLevel(game.getEnterprise().getSheilds().getMaxLevel());
            return;
        }
        if (energyToTransfer < 0.0 && game.getEnterprise().getEnergy()-energyToTransfer > 5000) {
            /* Prevent shield drain loophole */
            game.con.printf("\nEngineering to bridge--\n");
            game.con.printf("  Scott here. Power circuit problem, Captain.\n");
            game.con.printf("  I can't drain the shields.\n");
            game.setReadyForHit(false);
            return;
        }
        if (game.getEnterprise().getSheilds().getLevel()+energyToTransfer < 0) {
            game.con.printf("All shield energy transferred to ship.\n");
            game.getEnterprise().setEnergy(game.getEnterprise().getEnergy() + game.getEnterprise().getSheilds().getLevel());
            game.getEnterprise().getSheilds().setLevel(0.0);
            return;
        }
        game.con.printf("Scotty- \"");
        if (energyToTransfer > 0)
            game.con.printf("Transferring energy to shields.\"\n");
        else
            game.con.printf("Draining energy from shields.\"\n");
        game.getEnterprise().getSheilds().setLevel(game.getEnterprise().getSheilds().getLevel() + energyToTransfer);
        game.getEnterprise().setEnergy(game.getEnterprise().getEnergy() - energyToTransfer);
        return;
    }

    private void raiseShields() {
        if (game.getEnterprise().getSheilds().getStatus().equals(ShieldStatus.UP)) {
            game.con.printf("Shields already up.\n");
            return;
        }
        game.getEnterprise().getSheilds().setStatus(ShieldStatus.UP);
        game.getEnterprise().getSheilds().setShieldIsChanging(true);
        
        if (!game.getEnterprise().getCondition().equals(Condition.DOCKED)) 
            game.getEnterprise().setEnergy(game.getEnterprise().getEnergy()-50.0);
        game.con.printf("Shields raised.\n");
        if (game.getEnterprise().getEnergy() <= 0) {
            game.con.printf("\nShields raising uses up last of energy.\n");
            new Finish(game).finish(GameOverReason.ENERGY);
            return;
        }
        game.setReadyForHit(true);
        return;
    }

    private void lowerShields() {
        if (game.getEnterprise().getSheilds().getStatus().equals(ShieldStatus.DOWN)) {
            game.con.printf("Shields already down.\n");
            return;
        }
        game.getEnterprise().getSheilds().setStatus(ShieldStatus.DOWN);
        game.getEnterprise().getSheilds().setShieldIsChanging(true);
        
        game.con.printf("Shields lowered.\n");
        game.setReadyForHit(true);
        return;
    }

    private Boolean getIfTransfer() {
        String cmdstr = this.game.con.readLine("Do you wish to change shield energy? ");
        List<String> params = readCommands(cmdstr).orElse(new ArrayList<String>());

        while(params.size() == 0 || (!params.get(0).equals("N") && !params.get(0).equals("Y"))) {
            cmdstr = this.game.con.readLine("Please answer with \"Y\" or \"N\":");
            params = readCommands(cmdstr).orElse(null);
        }
        return params.get(0).equals("Y");
    }


    private ShieldType matchShieldType(String cmdstr) {

        ShieldType mt = ShieldType.undefined;
        boolean matched = false;
        int cmdlen = 0;
        int tstlen = 0;

        for (ShieldType ml : ShieldType.values()) {

            String cmd = ml.toString();

            cmdlen = cmd.length();
            tstlen = cmdstr.length();

            String abrcheck = cmd.substring(0, Math.min(cmdlen, tstlen));

            matched = cmdstr.compareTo(abrcheck) == 0;

            if (matched) {
                mt = ml;
                break;
            }
        }
        return mt;
    }

}
