package Model;

import static Utils.Utils.checkEntityAgainstPosition;
import static Utils.Utils.checkEntityListAgainstPosition;
import static Utils.Utils.randDouble;
import static Utils.Utils.randInt;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import Utils.Utils;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class GameMap {
    @NonNull
    private Game game;
    private List<List<List<List<EntityType>>>> map = new ArrayList<List<List<List<EntityType>>>>();
    private List<List<Integer>> entityMap = new ArrayList<List<Integer>>();
    private final int QUADRANT_SIZE = 8;
    private final int SECTOR_SIZE = 10;

    public Integer getSectorNumber(int y, int x) {
        return entityMap.get(x).get(y);
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
                        } //"hihihii"
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
        while (!game.isPositionEmpty(position) && entities != null
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
}
