package sst;

import Model.Condition;
import Model.Coordinate;
import Model.Device;
import Model.Enemy;
import Model.EntityType;
import Model.Game;
import Model.GameLevel;
import Model.Position;
import Model.ShieldStatus;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import sst.Finish.GameOverReason;

import static Utils.Utils.randDouble;

import java.util.List;

/**
 * Handles the move command
 * 
 * @author Griffin Barnard
 */
@RequiredArgsConstructor
public class Attack {
    @NonNull
    private Game game;


    public void klingonAttack(int k){
        // game.con.printf("\nklingon would attack\n");

        /* k == 0 forces use of phasers in an attack */
        int percent, ihurt=0, i=0;
        Boolean usePhasers;
        int atackd = 0, attempt = 0;
        double hit;
        double powerFactor, dustFactor, hitmax=0.0, hittot=0.0, chargeFactor=1.0, randomFactor;
        if(game.getEnterprise().getCloak()) return;

        if(game.getIsOver()) return;

        //TODO: if tholian is here move it

        //TODO: check if in romulan neutral zone. if in it, change it to not be neutral and return without attacking player
        
        if(game.getSkill() == GameLevel.EMERITUS || (!game.getJustEnteredQuadrant() && (game.checkIfCommanderInCurrentQuadrant() || game.checkIfSuperInCurrentQuadrant()))) {
            // TODO: move commander
        }
        if(game.getEnemiesInQuadrant().size() == 0) return;

        powerFactor = 1.0/game.getEnterprise().getSheilds().getMaxLevel();
        if(game.getEnterprise().getSheilds().getShieldIsChanging()) chargeFactor = 0.25+0.5*randDouble(0, 1);

        game.con.printf("\n");
        if(game.getSkill().getSkillValue() <= GameLevel.FAIR.getSkillValue()) i = 2;

        for(Enemy e: game.getEnemiesInQuadrant()) {
            // game.con.printf("ENTITY TYPE: %s", e.getName());
            if(e.getPower() < 0) continue; // too weak to attack
            /* compute hit strength and diminsh shield power */
            randomFactor = randDouble(0, 1);
            /* Increase chance of photon torpedos if docked or enemy energy low */
            if(game.getEnterprise().getCondition().equals(Condition.DOCKED)) randomFactor *= 0.25;
            if (e.getPower() < 500) randomFactor *= 0.25;

            // game.con.printf("r: %f", randomFactor);
            usePhasers = (e.getType() == EntityType.KLINGON && randomFactor > 0.0005) || k == 0 ||
			(e.getType() == EntityType.COMMANDER && randomFactor > 0.015) ||
			(e.getType() == EntityType.ROMULAN && randomFactor > 0.3) ||
			(e.getType() == EntityType.SUPER_COMMANDER && randomFactor > 0.07);

            if(usePhasers) {
                /* Enemy uses phasers */
                if (game.getEnterprise().getCondition().equals(Condition.DOCKED)) continue; /* Don't waste the effort! */
                attempt = 1;
                dustFactor = 0.8+0.05*Math.random();
                hit = e.getPower()*Math.pow(dustFactor,e.getPosition().calcDistance(game.getEnterprise().getPosition())/10.0);
                e.setPower(e.getPower() * 0.75);
            } else {
                /* Enemy used photon torpedo */
                Coordinate sect = game.getEnterprise().getPosition().getSector();
                double course = 1.90985*Math.atan2((double)(sect.getX()+1)-e.getPosition().getSector().getX()+1, (double)e.getPosition().getSector().getY()+1-(sect.getY()));
                hit = 0;
                game.con.printf("***TORPEDO INCOMING");
                if (game.getEnterprise().getDeviceDamage().get(Device.SR_SENSORS) <= 0.0) {
                    game.con.printf(" From %s at %d - %d",e.getName(), e.getPosition().getSector().getY()+1, e.getPosition().getSector().getX()+1);
                }
                attempt = 1;
                game.con.printf("--");
                randomFactor = (randDouble(0, 1)+randDouble(0, 1))*0.5 -0.5;
                randomFactor += 0.002*e.getPower()*randomFactor;
                hit = new Photon(game).fireTorpedo(course, randomFactor, (Double) ((double)e.getPosition().getSector().getY()), ((Double)(double)e.getPosition().getSector().getX()));
                if (game.getRemainingKlingonCount() == 0) new Finish(game).finish(GameOverReason.WON); /* Klingons did themselves in! */
                if (game.getIsOver()) return; /* Supernova or finished. TODO: check for supernova */ 
                if (hit == 0) continue;
            }
            if (game.getEnterprise().getSheilds().getStatus().equals(ShieldStatus.UP) || game.getEnterprise().getSheilds().getShieldIsChanging()) {
                /* shields will take hits */
                double absorb, hitsh, propor = powerFactor*game.getEnterprise().getSheilds().getLevel();
                if(propor < 0.1) propor = 0.1;
                hitsh = propor*chargeFactor*hit+1.0;
                atackd=1;
                absorb = 0.8*hitsh;
                if (absorb > game.getEnterprise().getSheilds().getLevel()) absorb = game.getEnterprise().getSheilds().getLevel();
                game.getEnterprise().getSheilds().setLevel(game.getEnterprise().getSheilds().getLevel() - absorb);
                hit -= hitsh;
                if (propor > 0.1 && hit < 0.005*game.getEnterprise().getEnergy()) continue;
            }

            atackd = 1;
            ihurt = 1;

            game.con.printf("%.2f unit hit", hit);

            if ((game.getEnterprise().getDeviceDamage().get(Device.SR_SENSORS) > 0 && usePhasers) || game.getSkill().getSkillValue() <= GameLevel.FAIR.getSkillValue()) {
                game.con.printf(" on the %s", "Enterprise");
            }
            if(game.getEnterprise().getDeviceDamage().get(Device.SR_SENSORS) <= 0.0 && usePhasers) {
                game.con.printf(" from %s at %d - %d", e.getName(), e.getPosition().getSector().getY()+1, e.getPosition().getSector().getX()+1);
            }
            game.con.printf("\n");
            /* Decide if hit is critical */
            if (hit > hitmax) hitmax = hit;
            hittot += hit;
            // fry(hit);
            game.con.printf("Hit %.2f energy %g\n", hit, game.getEnterprise().getEnergy());
            game.getEnterprise().setEnergy(game.getEnterprise().getEnergy() - hit);
        }

        if (game.getEnterprise().getEnergy() <= 0) {
            /* Returning home upon your shield, not with it... */
            new Finish(game).finish(GameOverReason.BATTLE);
            return;
        }
        if (attempt == 0 && game.getEnterprise().getCondition().equals(Condition.DOCKED))
            game.con.printf("***Enemies decide against attacking your ship.\n");
        if (atackd == 0) return;
        percent = (int) (100.0*powerFactor*game.getEnterprise().getSheilds().getLevel()+0.5);
        if (ihurt==0) {
            /* Shields fully protect ship */
            game.con.printf("Enemy attack reduces shield strength to ");
        } else {
            /* Print message if starship suffered hit(s) */
            game.con.printf("\nEnergy left %.2f    shields ", game.getEnterprise().getEnergy());
            if (game.getEnterprise().getSheilds().getStatus().equals(ShieldStatus.UP)) game.con.printf("up, ");
            else if (game.getEnterprise().getDeviceDamage().get(Device.SHIELDS) == 0) game.con.printf("down, ");
            else game.con.printf("damaged, ");
        }
        game.con.printf("%d%%   torpedoes left %d\n", percent, game.getEnterprise().getTorpedoes());

        if (hitmax >= 200 || hittot >= 500) {
            int icas= (int) (hittot*randDouble(0, 1)*0.015);
            if (icas >= 2) {
                game.con.printf("\nMc Coy-  \"Sickbay to bridge.  We suffered %1d casualties\n   in that last attack.\"", icas);
                game.setCasualties(game.getCasualties() + icas);
            }
        }

    }

}