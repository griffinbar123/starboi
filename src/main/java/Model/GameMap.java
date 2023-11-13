package Model;

import static Utils.Utils.checkEntityAgainstPosition;
import static Utils.Utils.checkEntityListAgainstPosition;
import static Utils.Utils.randDouble;
import static Utils.Utils.randInt;
import static Utils.Utils.outputEntity;
import static Utils.Utils.isEqual;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

import Utils.Utils;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import sst.Finish;
import sst.Finish.GameOverReason;

@RequiredArgsConstructor
@Data
public class GameMap {
    @NonNull
    private Game game;
    private List<List<List<List<EntityType>>>> map = new ArrayList<List<List<List<EntityType>>>>();
    private List<List<Integer>> entityMap = new ArrayList<List<Integer>>();
    private final int QUADRANT_SIZE = 8;
    private final int SECTOR_SIZE = 10;

    public Integer getQuadrantNumber(int y, int x) {
        return entityMap.get(x).get(y);
    }

    public Integer getQuadrantNumber(Position pos) {
        return entityMap.get(pos.getQuadrant().getX()).get(pos.getQuadrant().getY());
    }

    public void init() {
        initializeMap();
        initializeEnterprise();
        initializePlanets();
        initializeKlingons();
        initializeStarbases();
        initializeStars();
        initializeRomulans(4);
        initializeBlackHoles();
        updateMap();
    }

    public void updateMap() {
        for (int i = 0; i < QUADRANT_SIZE; i++) {
            for (int j = 0; j < QUADRANT_SIZE; j++) {
                for (int k = 0; k < SECTOR_SIZE; k++) {
                    for (int l = 0; l < SECTOR_SIZE; l++) {
                        Position position = new Position(j, i, l, k);
                        if (checkEntityListAgainstPosition(position, game.getKlingons())) {
                            map.get(j).get(i).get(k).set(l, EntityType.KLINGON);
                            entityMap.get(j).set(j, entityMap.get(j).get(i) + 100);
                        } else if (checkEntityListAgainstPosition(position, game.getPlanets())) {
                            map.get(j).get(i).get(k).set(l, EntityType.PLANET);
                        } else if (checkEntityAgainstPosition(position, game.getEnterprise())) {
                            map.get(j).get(i).get(k).set(l, EntityType.ENTERPRISE);
                        } else if (checkEntityAgainstPosition(position, game.getKlingonSuperCommander())) {
                            map.get(j).get(i).get(k).set(l, EntityType.SUPER_COMMANDER);
                            entityMap.get(j).set(j, entityMap.get(j).get(i) + 100);
                        } else if (checkEntityListAgainstPosition(position, game.getStarbases())) {
                            map.get(j).get(i).get(k).set(l, EntityType.STARBASE);
                            entityMap.get(i).set(j, entityMap.get(j).get(i) + 10);
                        } else if (checkEntityListAgainstPosition(position, game.getStars())) {
                            map.get(j).get(i).get(k).set(l, EntityType.STAR);
                            entityMap.get(i).set(j, entityMap.get(j).get(i) + 1);
                        } else if (checkEntityListAgainstPosition(position, game.getRomulans())) {
                            map.get(j).get(i).get(k).set(l, EntityType.ROMULAN);
                        } else if (checkEntityListAgainstPosition(position, game.getBlackHoles())) {
                            map.get(j).get(i).get(k).set(l, EntityType.BLACK_HOLE);
                        } else if (checkEntityListAgainstPosition(position, game.getKlingonCommanders())) {
                            map.get(j).get(i).get(k).set(l, EntityType.COMMANDER);
                            entityMap.get(j).set(j, entityMap.get(j).get(i) + 100);
                        } else {
                            map.get(j).get(i).get(k).set(l, EntityType.NOTHING);
                            entityMap.get(j).set(i, 0);
                        }
                    }
                }
            }
        }
    }

    private void initializeMap() {
        for (int i = 0; i < QUADRANT_SIZE; i++) {
            map.add(new ArrayList<List<List<EntityType>>>());
            entityMap.add(new ArrayList<Integer>());
            for (int j = 0; j < QUADRANT_SIZE; j++) {
                map.get(i).add(new ArrayList<List<EntityType>>());
                entityMap.get(i).add(0);
                for (int k = 0; k < SECTOR_SIZE; k++) {
                    map.get(i).get(j).add(new ArrayList<EntityType>());
                    for (int l = 0; l < SECTOR_SIZE; l++) {
                        map.get(i).get(j).get(k).add(EntityType.NOTHING);
                    }
                }
            }
        }
    }

    @JsonIgnore
    private List<Entity> getEntitiesInQuadrant(Coordinate quad){
        List<Entity> entities = new ArrayList<Entity>();

        for(int i = 0; i < SECTOR_SIZE; i++) {
            for(int j = 0; j < SECTOR_SIZE; j++) {
                Position position = new Position(quad, new Coordinate(i, j));
                Entity entity = getEntityAtPosition(position);
                if(entity != null){
                    entities.add(entity);
                }
            }
        }

        return entities;
    }

    @SuppressWarnings("unchecked")
    public <T extends Entity> T getEntityAtPosition(Position pos) {
        for(Klingon k: getGame().getKlingons()) {
            if(checkEntityAgainstPosition(pos, k)) {
                return (T) k;
            }
        }
        for(KlingonCommander c: getGame().getKlingonCommanders()) {
            if(checkEntityAgainstPosition(pos, c)) {
                return (T) c;
            }
        }
        for(Romulan r: getGame().getRomulans()) {
            if(checkEntityAgainstPosition(pos, r)) {
                return (T) r;
            }
        }
        if(checkEntityAgainstPosition(pos, getGame().getKlingonSuperCommander())) {
            return (T) game.getKlingonSuperCommander();
        }
        for(Star s: getGame().getStars()) {
            if(checkEntityAgainstPosition(pos, s)) {
                return (T) s;
            }
        }
        for(Starbase b: getGame().getStarbases()) {
            if(checkEntityAgainstPosition(pos, b)) {
                return (T) b;
            }
        }
        for(Planet p: getGame().getPlanets()) {
            if(checkEntityAgainstPosition(pos, p)) {
                return (T) p;
            }
        }
        for(BlackHole bl: getGame().getBlackHoles()) {
            if(checkEntityAgainstPosition(pos, bl)) {
                return (T) bl;
            }
        }

        return null;
    }


    public void randomizeQuadrant(Coordinate quad){
        List<Entity> entitiesInQuadrant = getEntitiesInQuadrant(quad);
        for(Entity entity : entitiesInQuadrant) {
            entity.setPosition(generateRandomPositionWithinQuadrant(quad));
        }
        updateMap();
    }

    private Position generateRandomPositionWithinQuadrant(Coordinate quad) {
        Coordinate sector = new Coordinate(randInt(0, 9), (randInt(0, 9)));
        Position position = new Position(quad, sector);
        while (!isPositionEmpty(position)) {
            position = generateRandomPositionWithinQuadrant(quad);
        }
        return position;
    }

    public Boolean isPositionEmpty(Position position) {
        // use this to check if a position is empty, but can't check the map because it
        // might not be updated
        return !(checkEntityAgainstPosition(position, getGame().getEnterprise())
                || checkEntityAgainstPosition(position, getGame().getKlingonSuperCommander())
                || checkEntityListAgainstPosition(position, getGame().getKlingons())
                || checkEntityListAgainstPosition(position, getGame().getPlanets())
                || checkEntityListAgainstPosition(position, getGame().getStars())
                || checkEntityListAgainstPosition(position, getGame().getStarbases())
                || checkEntityListAgainstPosition(position, getGame().getRomulans())
                || checkEntityListAgainstPosition(position, getGame().getKlingonCommanders()));
    }

    public Boolean checkPositionForEntity(Position position, EntityType entityType) {
        return getEntityTypeAtPosition(position) == entityType;
    }

    public EntityType getEntityTypeAtPosition(Position position) {
        return map.get(position.getQuadrant().getX()).get(position.getQuadrant().getY()).get(position.getSector().getY()).get(position.getSector().getX());
    }

    private void initializeEnterprise() {
        Position pos = generateNewPosition(9, (Entity) null);
        Enterprise enterprise = new Enterprise(pos);
        Map<Device, Double> devices = new HashMap<>();

        for (Device d : Device.values()) {
            devices.put(d, 0.0);
        }
        enterprise.setDeviceDamage(devices);

        this.game.setEnterprise(enterprise);
    }

    private void initializeRomulans(int numberOfRomulans) {
        Position pos;
        Romulan rom[] = new Romulan[numberOfRomulans];
        for (int i = 0; i < numberOfRomulans; i++) {
            pos = generateNewPosition(9, rom);
            rom[i] = new Romulan(pos);
            rom[i].setPower(randDouble(0, 1)*400.0 + 450.0 + 50.0*this.game.getSkill().getSkillValue());
        }
        this.game.setRomulans(rom);
        this.game.getScore().setInitRomulans(numberOfRomulans);
    }

    private void initializeBlackHoles() {
        int numberOfBlackHoles = 0;
        for (int i=1; i<=8; i++)
            for (int j=1; j<=8; j++) 
                numberOfBlackHoles += randDouble(0, 1) > 0.5 ? 1 : 0;
        Position pos;
        BlackHole blackHoles[] = new BlackHole[numberOfBlackHoles];
        for (int i = 0; i < numberOfBlackHoles; i++) {
            pos = generateNewPosition(3, blackHoles);
            blackHoles[i] = new BlackHole(pos);
        }
        this.game.setBlackHoles(blackHoles);
    }

    private void initializeStars() {
        int numberOfStars = 0;
        for (int i=1; i<=8; i++)
            for (int j=1; j<=8; j++) 
                numberOfStars += ((int)(randDouble(0, 1)*9.0)) + 1;
        Position pos;
        Star stars[] = new Star[numberOfStars];
        for (int i = 0; i < numberOfStars; i++) {
            pos = generateNewPosition(9, stars);
            stars[i] = new Star(pos);
        }
        this.game.setStars(stars);
    }

    private void initializeStarbases() {
        int numberOfStarbases = 3 * ((int) randDouble(0, 1))+2;
        Position pos;
        Starbase starBases[] = new Starbase[numberOfStarbases];
        for (int i = 0; i < numberOfStarbases; i++) {
            pos = generateNewPosition(9, starBases);
            starBases[i] = new Starbase(pos);
            this.game.addCoordinateString(pos.getQuadrant(), ".1.");
        }
        this.game.setStarbases(starBases);
    }

    private void initializePlanets() {
        int numberOfPlanets =  (int) ((10/2) + (10/2+1)*randDouble(0, 1));
        Position pos;
        Planet planets[] = new Planet[numberOfPlanets];
        for (int i = 0; i < numberOfPlanets; i++) {
            pos = generateNewPosition(9, planets);
            planets[i] = new Planet(pos);
        }
        this.game.setPlanets(planets);
    }

    private void initializeKlingons() {
        Position pos;
        int skill, initKling, sCmd, cmd, ord;

        // Original: 2.0*intime*((skill+1 - 2*Rand())*skill*0.1+.15)
        skill = this.game.getSkill().getSkillValue();
        initKling = (int) (2 * this.game.getTime() * ((skill + 1 - 2 * randInt(0, 1)) * skill * 0.1 + 0.15));
        cmd = (int) (skill + 0.0625 * initKling * randDouble(0, 1));
        sCmd = (this.game.getSkill() == GameLevel.GOOD ||
                this.game.getSkill() == GameLevel.EXPERT ||
                this.game.getSkill() == GameLevel.EMERITUS ? 1 : 0);
        ord = initKling - cmd - sCmd;

        Klingon[] klingons = new Klingon[ord];
        KlingonCommander[] klingonCommanders = new KlingonCommander[cmd];
        KlingonSuperCommander klingonSuperCommander = null;

        for (int i = 0; i < ord; i++) {
            pos = generateNewPosition(9, klingons);
            klingons[i] = new Klingon(pos);
            klingons[i].setPower(randDouble(0, 1)*150.0 +300.0 +25.0*skill);
        }

        for (int i = 0; i < cmd; i++) {
            pos = generateNewPosition(9, klingonCommanders);
            klingonCommanders[i] = new KlingonCommander(pos);
            klingonCommanders[i].setPower(950.0+400.0*randDouble(0, 1)+50.0*skill);
        }

        if (sCmd == 1) {
            pos = generateNewPosition(9, klingonSuperCommander);
            klingonSuperCommander = new KlingonSuperCommander(pos);
            klingonSuperCommander.setPower(1175.0 + 400.0*randDouble(0, 1) + 125.0*skill);
            this.game.setKlingonSuperCommander(klingonSuperCommander);
        }

        this.game.setKlingons(klingons);
        this.game.setKlingonCommanders(klingonCommanders);

        game.getScore().setInitTotKlingons(initKling);
        game.getScore().setInitKlingons(ord);
        game.getScore().setInitKlingonCmds(cmd);
        game.getScore().setInitKlingonSupCmds(sCmd);
    }

    private Position generateNewPosition(int maxElementsInQuadrant, Entity... entities) {
        Coordinate quadrant = new Coordinate(Utils.randInt(0, 7), (Utils.randInt(0, 7)));
        Coordinate sector = new Coordinate(Utils.randInt(0, 9), (Utils.randInt(0, 9)));
        Position position = new Position(quadrant, sector);
        while (!isPositionEmpty(position) && entities != null
                || getNumberOfEntiesBeforeMapUpdate(entities, position) > maxElementsInQuadrant) {
            position = generateNewPosition(maxElementsInQuadrant, entities);
        }
        return position;
    }

    private int getNumberOfEntiesBeforeMapUpdate(Entity[] entities, Position position) {
        // function to get number of entities in a qudrant before a map update, used in
        // iniitialzation to make
        // sure a quadrant doesn't get too many of an entity type
        int numberOfElements = 0;
        for (int i = 0; i < entities.length; i++) {
            if (entities[i] != null && entities[i].getPosition().getQuadrant().getX() == position.getQuadrant().getX()
                    && entities[i].getPosition().getQuadrant().getY() == position.getQuadrant().getY()) {
                numberOfElements += 1;
            }
        }
        return numberOfElements;
    }

    // private int getNumberOfEntiesInMapQuadrant(int y, int x, EntityType entity) {
    //     int numberOfElements = 0;
    //     for (int i = 0; i < SECTOR_SIZE; i++) {
    //         for (int j = 0; j < SECTOR_SIZE; j++) {
    //             if (map.get(x).get(y).get(j).get(i) == entity) {
    //                 numberOfElements += 1;
    //             }
    //         }
    //     }
    //     return numberOfElements;
    // }

    public void destroyEntityAtPosition(Position pos){
        EntityType entityType = getEntityTypeAtPosition(pos);
        switch(entityType){
            case STARBASE:
                game.con.printf("***STARBASE DESTROYED..\n");
                for(int i=0; i < game.getStarbases().length; i++)
                    if(game.getStarbases()[i] != null && isEqual(game.getStarbases()[i].getPosition(), pos)){
                        game.getStarbases()[i] = null;
                        break;
                    }
            case PLANET:
                game.con.printf("%s destroyed.\n", outputEntity(pos.getSector().getY()+1, pos.getSector().getX()+1, entityType));
                for(int i=0; i < game.getPlanets().length; i++)
                    if(game.getPlanets()[i] != null && isEqual(game.getPlanets()[i].getPosition(), pos)){
                        game.getPlanets()[i] = null;
                        game.setDestroyedPlanets(game.getDestroyedPlanets() + 1);
                        break;
                    }
                break;
            case KLINGON:
                game.con.printf("%s destroyed.\n", outputEntity(pos.getSector().getY()+1, pos.getSector().getX()+1, entityType));
                for(int i=0; i < game.getKlingons().length; i++)
                    if(game.getKlingons()[i] != null && isEqual(game.getKlingons()[i].getPosition(), pos)) {
                        game.getKlingons()[i] = null;
                        game.setKlingonsKilled(game.getKlingonsKilled() + 1);
                    }
                break;
            case COMMANDER:
                game.con.printf("%s destroyed.\n", outputEntity(pos.getSector().getY()+1, pos.getSector().getX()+1, entityType));
                for(int i=0; i < game.getKlingonCommanders().length; i++)
                    if(game.getKlingonCommanders()[i] != null && isEqual(game.getKlingonCommanders()[i].getPosition(), pos)){
                        game.getKlingonCommanders()[i] = null;
                        game.setCommandersKilled(game.getCommandersKilled() + 1);
                    }
                break;
            case ROMULAN:
                game.con.printf("%s destroyed.\n", outputEntity(pos.getSector().getY()+1, pos.getSector().getX()+1, entityType));
                for(int i=0; i < game.getRomulans().length; i++)
                    if(game.getRomulans()[i] != null && isEqual(game.getRomulans()[i].getPosition(), pos)) {
                        game.getRomulans()[i] = null;
                        game.setRomulansKilled(game.getRomulansKilled() + 1);
                    }
                break;
            case SUPER_COMMANDER:
                game.con.printf("%s destroyed.\n", outputEntity(pos.getSector().getY()+1, pos.getSector().getX()+1, entityType));
                game.setKlingonSuperCommander(null);
                game.setSuperCommandersKilled(game.getSuperCommandersKilled()+1);
                break;
            case STAR:
                // con.printf("%s destroyed.\n", outputEntity(pos.getSector().getY()+1, pos.getSector().getX()+1, entityType));
                game.nova((Star) getEntityAtPosition(pos));
                break;
            default:
                game.con.printf("not implemented yet\n");
        }

        if(game.getRemainingKlingonCount() == 0) {
            Finish finish = new Finish(game);
            finish.finish(GameOverReason.WON);
        }
        updateMap();
    }

}
