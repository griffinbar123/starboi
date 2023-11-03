package sst;

import java.util.ArrayList;
import java.util.List;

import Model.Coordinate;
import Model.Device;
import Model.EntityType;
import Model.Game;
import Model.Klingon;
import Model.Position;
import Model.Condition;
import Model.ShieldStatus;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import sst.Finish.GameOverReason;

import static Utils.Utils.readCommands;
import static Utils.Utils.parseIntegers;
import static Utils.Utils.parseDoubles;
import static Utils.Utils.buildPosFromQuad;
import static Utils.Utils.outputEntity;
/**
 * Handles the move command
 * 
 * @author Griffin Barnard
 */
@RequiredArgsConstructor
public class Photon {
    @NonNull
    private Game game;

    /**
     * Moves player in the map
     */
    public void ExecPHOTON(List<String> params) {
        if(game.getEnterprise().getDeviceDamage().get(Device.PHOTON_TUBES) > 0.0){
            game.con.printf("Photon tubes damaged.\n");
            return;
        }

        Integer numOfTorpedoesToFire = getNumberOfTorpedoesToFire(params);

        if(numOfTorpedoesToFire == null){
            game.begPardon();
            return;
        }

        if(params.size() % 2 == 1)
            params.remove(0);

        fireTorpedos(numOfTorpedoesToFire, params);

        // this.game.con.printf("\nNumber of torps: %d\n", numOfTorpedoesToFire);
    }


    private void fireTorpedos(Integer numOfTorpedoesToFire, List<String> params) {

        List<Double> sects = parseDoubles(params);
        List<Double> courses = new ArrayList<Double>();

        if(params.size() != sects.size() || (sects.size() / 2 != numOfTorpedoesToFire && sects.size() != 2)) {
            this.game.begPardon();
            return;
        }

        for(int i = 0; i < numOfTorpedoesToFire; i+=2) {
            Double course = getInitialCourse(sects.get(i), sects.get(i+1));
            if(course == null)
                return;
            courses.add(course);
        }

        Boolean breakFlag = false;

        for(int i = 1; i <= numOfTorpedoesToFire && !breakFlag; i+=2) {
            Double r = (Math.random()+Math.random())*0.5 -0.5;
            if(Math.abs(r) >= 0.47) {
                // misfire
                r = (Math.random()+1.2) * r;
                if(numOfTorpedoesToFire > 1) 
                    this.game.con.printf("\n***TORPEDO NUMBER %d MISFIRES.", i);
                else 
                    this.game.con.printf("\n***TORPEDO MISFIRES.\n");
                
                if(i < numOfTorpedoesToFire)
                    this.game.con.printf("\n  Remainder of burst aborted.");
                
                breakFlag = true;

                if(Math.random() <= 0.2) {
                    this.game.con.printf("\n***Photon tubes damaged by misfire.");
                    game.getEnterprise().getDeviceDamage().put(Device.PHOTON_TUBES, 0.2);
                    break;
                }
            }

            if(this.game.getEnterprise().getCloak())
                r *= 1.2;
            if(this.game.getEnterprise().getSheilds().getStatus() == ShieldStatus.DOWN || this.game.getEnterprise().getCondition() == Condition.DOCKED) 
                r *= 1.0 + 0.0001*this.game.getEnterprise().getSheilds().getUnits(); 

            if(numOfTorpedoesToFire > 1) 
                this.game.con.printf("\nTrack for torpedo number %d-   ", i);
            else
                this.game.con.printf("\nTorpedo track- ");
            

            fireTorpedo(courses.get(i-1), r, (Double) (double)this.game.getEnterprise().getPosition().getSector().getY(), (Double) (double)this.game.getEnterprise().getPosition().getSector().getX());
        }
    }

    private void fireTorpedo(Double course, Double r, Double initialY, Double initialX){

        int ix, iy,  jx = -1, jy = -1, shoved=0;
        Double ac=course + 0.25*r;
        Double angle = (15.0-ac)*0.5235988;
        Double bullseye = (15.0 - course)*0.5235988;
        Double deltay=-Math.sin(angle), deltax=Math.cos(angle), x=initialX, y=initialY, bigger;
        Double ang, temp, xx, yy, h1;

        bigger = Math.abs(deltax);
        if (Math.abs(deltay) > bigger) 
            bigger = Math.abs(deltay);
        deltax /= bigger;
        deltay /= bigger;

        for(int i=1; i<=15; i++){
            x += deltax;
            ix = (int) ( x + 0.5);
            if (ix < 0 || ix > 9) 
                break;
            y += deltay;
            iy = (int) (y + 0.5);
            if (iy < 0 || iy > 9) 
                break;

            if (i==4 || i==9)
                this.game.con.printf("\n");
            this.game.con.printf("%.1f - %.1f   ", y+1.0, x+1.0);

            Coordinate quad = this.game.getEnterprise().getPosition().getQuadrant();

            // this.game.con.printf("iy: %.1f - ix: %.1f   ", iy+1.0, ix+1.0);
            char symbol = this.game.getPositionChar(new Position(quad, new Coordinate(iy, ix)));

            if(symbol == EntityType.NOTHING.getSymbol()) continue;

            this.game.con.printf("\n");

            Position pos = buildPosFromQuad(quad, iy, ix);
            switch(symbol) {
                case 'E': //hits our ship
                case 'F': //hits Faire Queene
                    break;
                case 'C': //hit a commander
                case 'S': //hit a super commander
                if (Math.random() <= 0.05) {
                    this.game.con.printf("%s\nuses anti-photon device;\n   torpedo neutralized.",outputEntity(iy+1, ix+1, symbol));
					return;
				}
                case 'R': //hit a romulan
                case 'K': //hit a klingon
                    Klingon k = this.game.getEnemyAtPosition(pos);
                    Double power = Math.abs(k.getPower());
                    //not sure whath1 stands for rn
                    h1 = Math.abs(700.0 + 100.0*Math.random() - 1000.0*Math.sqrt(Math.pow(ix-initialX, 2)+Math.pow(iy-initialY, 2))*Math.abs(Math.sin(bullseye-angle)));
                    if (power < h1) 
                        h1 = power;
                    k.setPower(k.getPower() - (k.getPower() < 0 ? -h1: h1));
                    if(k.getPower() == 0) {
                        game.destroyKlingon(k);
                        return;
                    }
                    this.game.con.printf("%s", outputEntity(iy+1, ix+1, symbol));
                    // move enemy
                    ang = angle + 2.5*(Math.random()-0.5);
                    temp = Math.abs(Math.sin(ang));
                    if (Math.abs(Math.cos(ang)) > temp) 
                        temp = Math.abs(Math.cos(ang));
                    yy = -Math.sin(ang)/temp;
                    xx = Math.cos(ang)/temp;
                    jx= (int) (ix+xx+0.5);
                    jy= (int) (iy+yy+0.5);
                    Position klingonPos = buildPosFromQuad(quad, jy, jx);
                    if (jx<0 || jx>9 || jy<0 ||jy > 9) {
                        this.game.con.printf(" damaged but not destroyed.\n");
                        return;
                    }
                    if (this.game.getPositionChar(klingonPos)==' ') {
                        this.game.con.printf(" buffeted into black hole\n");
                        game.destroyKlingon(k);
                        return;
                    }
                    if (this.game.getPositionChar(klingonPos) != EntityType.NOTHING.getSymbol()) {
                        /* can't move into object */
                        this.game.con.printf(" damaged but not destroyed.\n");
                        return;
                    }
                    this.game.con.printf(" damaged--\n");
                    k.setPosition(klingonPos);
                    shoved = 1;
                    break;
                case 'B': // Hit a base
                    this.game.destroyStarbase(pos);
                    return;
                case 'P': // Hit a planet
                    this.game.destroyPlanet(pos);
                    return;
                case '*': // Hit a star 
                    if (Math.random() > 0.10) {
                        // TODO: create supernova
                        this.game.con.printf("NOVA not yet implemented\n");
                        return;
                    }
                    this.game.con.printf("%s unaffected by photon blast.\n", outputEntity(iy+1, ix+1, symbol));
                    return;
                case '?': // Hit a thingy 
                    this.game.con.printf("\nAAAAIIIIEEEEEEEEAAAAAAAAUUUUUGGGGGHHHHHHHHHHHH!!!\n    HACK!     HACK!    HACK!        *CHOKE!*  \nMr. Spock-\n  \"Fascinating!\"\n");
                    // TODO: remove thingy
                    return;
                case ' ': // Black hole
                    this.game.con.printf("\n%s swallows torpedo.\n", outputEntity(iy+1, ix+1, symbol));
                    return;
                case '#': // hit the web 
                    this.game.con.printf("\n***Torpedo absorbed by Tholian web.\"\n");
                    return;
                case 'T': // Hit a Tholian 
                    // TODO: handle hitting a Tholian
                    this.game.con.printf("\n%s hitting tholians not implemented.\n", outputEntity(iy+1, ix+1, symbol));
                default: // Problem! 
                    this.game.con.printf("\nDon't know how to handle collision with %s\n", outputEntity(iy+1, ix+1, symbol));
                    return;
            }
            break;
        }
        if (shoved == 1) {
            this.game.updateMap();
            this.game.con.printf(" displaced by blast to %d - %d\n", jy+1, jx+1);
            return;
        }
        this.game.con.printf("\nTorpedo missed.\n");
        return;
    }

    private Double getInitialCourse(Double y, Double x) {

        if(y < 1 || y > 10 || x < 1 || x > 10) {
            this.game.begPardon();
            return null;
        }

        Double deltx = 0.1*(x - (this.game.getEnterprise().getPosition().getSector().getX() + 1));
        Double delty = 0.1*((this.game.getEnterprise().getPosition().getSector().getY()+1) - y);

        // this.game.con.printf("\ndelty: %f, deltx: %f\n", delty, deltx);

        if(deltx == 0 && delty == 0) {
            this.game.con.printf("\nSpock-  \"Bridge to sickbay.  Dr. McCoy,\n  I recommend an immediate review of\n  the Captain's psychological profile.\"");
            return null;
        }

        return 1.90985932*Math.atan2(deltx, delty);
    }

    private Integer getNumberOfTorpedoesToFire(List<String> params){

         List<Integer> numOfTorpedoesList = parseIntegers(params);

        if(params!= null && numOfTorpedoesList.size() != params.size()) {
            this.game.begPardon();
            return null;
        }

        if(params == null || numOfTorpedoesList.isEmpty()) {
            this.game.con.printf("%d torpedoes left.\n", this.game.getEnterprise().getTorpedoes());
            String torpsStr = this.game.con.readLine("Number of torpedoes to fire- ");
            params = readCommands(torpsStr).orElse(null);
            numOfTorpedoesList = parseIntegers(params);
            if(params  == null || params.size() == 0){
                return getNumberOfTorpedoesToFire(null);
            } else if(params.size() != 1 && numOfTorpedoesList.isEmpty()) {
                this.game.begPardon();
                return null;
            } 
        }
        
        int numOfTorpedoes = numOfTorpedoesList.get(0);

        if(numOfTorpedoes > 3){
            this.game.con.printf("Maximum of 3 torpedoes per burst.\n");
            return getNumberOfTorpedoesToFire(null);
        } else if(numOfTorpedoes < 0 || numOfTorpedoes == 0){
            return null;
        } else if(numOfTorpedoes > this.game.getEnterprise().getTorpedoes()){
            return getNumberOfTorpedoesToFire(params);
        }

        return numOfTorpedoes;
    }



}
