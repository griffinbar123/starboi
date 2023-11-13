package Model;
import java.util.ArrayList;
import java.util.List;
import java.io.Console;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonIgnore;
import Utils.Utils;
import lombok.Data;
import sst.Finish;
import sst.Move;
import sst.Finish.GameOverReason;
import static Utils.Utils.checkEntityAgainstPosition;
import static Utils.Utils.checkEntityListAgainstPosition;
import static Utils.Utils.isEqual;
import static Utils.Utils.randDouble;
import static Utils.Utils.outputEntity;
import static Utils.Utils.randInt;
import static Utils.Utils.positionsHaveSameQuadrant;


/**
 * Game entity container
 */
@Data
public class Game {
    public int getRemainingKlingonCount() {
        return getRegularKlingonCount() + getKlingonCommanderCount()+ (klingonSuperCommander != null ? 1 : 0);
    }

    @JsonIgnore
    public Console con = System.console();

    private double starDate;

    private EntityType[][][][] map = new EntityType[8][8][10][10];
    private HashMap<Coordinate, String> ScannedQuadrants = new HashMap<Coordinate, String>();

    // TODO: Testing abstraction of map
    private GameMap gameMap = new GameMap(this);

    private Klingon[] klingons;
    private KlingonCommander[] klingonCommanders;
    private KlingonSuperCommander klingonSuperCommander = null;
    private Enterprise enterprise;
    private Planet[] planets;
    private Starbase[] starbases;
    private BlackHole[] blackHoles;
    private Star[] stars;
    private Romulan[] romulans;
    private double time;
    private GameLevel skill = GameLevel.UNDEFINED;
    private GameLength length = GameLength.UNDEFINED;
    private GameType type = GameType.UNDEFINED;
    
    private Integer destroyedPlanets = 0;
    private Integer destroyedBases = 0;
    private Integer destroyedStars = 0;
    private Integer romulansKilled = 0;
    private Integer superCommandersKilled = 0;
    private Integer klingonsKilled = 0;
    private Integer commandersKilled = 0;
    private Integer casualties = 0;

    private Score score = new Score();
    private Boolean IsOver = false;

    private double damageFactor;

    private Boolean readyForHit = false;
    private Boolean justEnteredQuadrant = true;

    /**
     * Handles passing time
     * @author Matthias Schrock
     */
    @JsonIgnore
    public void passTime(Double time) {
        this.starDate -= time;
    }

    @JsonIgnore
    public void begPardon(){
        con.printf("\nBeg your pardon, Captain?\n");
    }

    @JsonIgnore
    public void addCoordinateString(Coordinate coord, String s){
        for (Map.Entry<Coordinate, String> entry : ScannedQuadrants.entrySet()) {
            Coordinate key = entry.getKey();
            if(key.isEqual(coord)){
                ScannedQuadrants.remove(key);
                break;
            }
        }
        ScannedQuadrants.put(coord, s);
    }

    @JsonIgnore
    public String getCoordinateString(int row, int col){
        for (Map.Entry<Coordinate, String> entry : ScannedQuadrants.entrySet()) {
            Coordinate key = entry.getKey();
            String value = entry.getValue();
            if(key.isEqual(new Coordinate(row, col))){
                return value;
            }
        }
        return  "...";
    }

    @JsonIgnore
    private int getNumberOfEntiesInMapQuadrant(int y, int x, EntityType entity) {
        int numberOfElements = 0;
        for (int i = 0; i < getMap()[x][y].length; i++) {
            for (int j = 0; j < getMap()[x][y][i].length; j++) {
                if (getMap()[x][y][j][i] == entity) {
                    numberOfElements += 1;
                }
            }
        }
        return numberOfElements;
    }

    @JsonIgnore
    public void updateMap() {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                for (int k = 0; k < map[i][j].length; k++) {
                    for (int l = 0; l < map[i][j][k].length; l++) {
                        Position position = new Position(new Coordinate(i, j), new Coordinate(k, l));
                        if (checkEntityListAgainstPosition(position, klingons)) {
                            map[j][i][k][l] = EntityType.KLINGON;
                        } else if (checkEntityListAgainstPosition(position, planets)) {
                            map[j][i][k][l] = EntityType.PLANET;
                        } else if (checkEntityAgainstPosition(position, enterprise)) {
                            map[j][i][k][l] = EntityType.ENTERPRISE;
                        } else if (checkEntityAgainstPosition(position, klingonSuperCommander)) {
                            map[j][i][k][l] = EntityType.ENTERPRISE;
                        } else if (checkEntityListAgainstPosition(position, starbases)) {
                            map[j][i][k][l] = EntityType.STARBASE;
                        } else if (checkEntityListAgainstPosition(position, stars)) {
                            map[j][i][k][l] = EntityType.STAR;
                        } else if (checkEntityListAgainstPosition(position, romulans)) {
                            map[j][i][k][l] = EntityType.ROMULAN;
                        } else if (checkEntityListAgainstPosition(position, blackHoles)) {
                            map[j][i][k][l] = EntityType.BLACK_HOLE;
                        } else if (checkEntityListAgainstPosition(position, klingonCommanders)) {
                            map[j][i][k][l] = EntityType.COMMANDER;
                        } else {
                            map[j][i][k][l] = EntityType.NOTHING;
                        }
                    }
                }
            }
        }
    }

    @JsonIgnore
    private List<Entity> getEntitiesInQuadrant(Coordinate quad){
        List<Entity> entities = new ArrayList<Entity>();

        for(int i = 0; i < map[quad.getX()][quad.getY()].length; i++) {
            for(int j = 0; j < map[quad.getX()][quad.getY()].length; j++) {
                Position position = new Position(quad, new Coordinate(i, j));
                Entity entity = getEntityAtPosition(position);
                if(entity != null){
                    entities.add(entity);
                }
            }
        }

        return entities;
    }

    @JsonIgnore
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

    @JsonIgnore
    public Boolean isPositionEmpty(Position position) {
        // use this to check if a position is empty, but can't check the map because it
        // might not be updated
        return !(checkEntityAgainstPosition(position, getEnterprise())
                || checkEntityAgainstPosition(position, getKlingonSuperCommander())
                || checkEntityListAgainstPosition(position, getKlingons())
                || checkEntityListAgainstPosition(position, getPlanets())
                || checkEntityListAgainstPosition(position, getStars())
                || checkEntityListAgainstPosition(position, getStarbases())
                || checkEntityListAgainstPosition(position, getRomulans())
                || checkEntityListAgainstPosition(position, getKlingonCommanders()));
    }

    @JsonIgnore
    public Boolean checkPositionForEntity(Position position, EntityType entityType) {
        return getEntityTypeAtPosition(position) == entityType;
    }

    @JsonIgnore
    public EntityType getEntityTypeAtPosition(Position position) {
        return map[position.getQuadrant().getX()][position.getQuadrant().getY()][position.getSector().getY()][position.getSector().getX()];
    }

    @JsonIgnore
    @SuppressWarnings("unchecked")
    public <T extends Entity> T getEntityAtPosition(Position pos) {
        for(Klingon k: klingons) {
            if(checkEntityAgainstPosition(pos, k)) {
                return (T) k;
            }
        }
        for(KlingonCommander c: klingonCommanders) {
            if(checkEntityAgainstPosition(pos, c)) {
                return (T) c;
            }
        }
        for(Romulan r: romulans) {
            if(checkEntityAgainstPosition(pos, r)) {
                return (T) r;
            }
        }
        if(checkEntityAgainstPosition(pos, klingonSuperCommander)) {
            return (T) klingonSuperCommander;
        }
        for(Star s: stars) {
            if(checkEntityAgainstPosition(pos, s)) {
                return (T) s;
            }
        }
        for(Starbase b: starbases) {
            if(checkEntityAgainstPosition(pos, b)) {
                return (T) b;
            }
        }
        for(Planet p: planets) {
            if(checkEntityAgainstPosition(pos, p)) {
                return (T) p;
            }
        }
        for(BlackHole bl: blackHoles) {
            if(checkEntityAgainstPosition(pos, bl)) {
                return (T) bl;
            }
        }

        return null;
    }

    @JsonIgnore
    public int getRegularKlingonCount() {
        int total = 0;
        for(int i = 0; i < klingons.length; i++)
            if(klingons[i] != null) 
                total++;
        return total;
    }

    @JsonIgnore
    public int getKlingonCommanderCount() {
        int total = 0;
        for(int i = 0; i < klingonCommanders.length; i++)
            if(klingonCommanders[i] != null) 
                total++;
        return total;
    }

    @JsonIgnore
    public int getRomulansCount() {
        int total = 0;
        for(int i = 0; i < romulans.length; i++)
            if(romulans[i] != null) 
                total++;
        return total;
    }

    public void damage(Device device, double damage) {
        enterprise.getDeviceDamage().put(device, damage);
    }

    public void destroyEntityAtPosition(Position pos){
        EntityType entityType = getEntityTypeAtPosition(pos);
        switch(entityType){
            case STARBASE:
                con.printf("***STARBASE DESTROYED..\n");
                for(int i=0; i < starbases.length; i++)
                    if(starbases[i] != null && isEqual(starbases[i].getPosition(), pos)){
                        starbases[i] = null;
                        break;
                    }
            case PLANET:
                con.printf("%s destroyed.\n", outputEntity(pos.getSector().getY()+1, pos.getSector().getX()+1, entityType));
                for(int i=0; i < planets.length; i++)
                    if(planets[i] != null && isEqual(planets[i].getPosition(), pos)){
                        planets[i] = null;
                        destroyedPlanets += 1;
                        break;
                    }
                break;
            case KLINGON:
                con.printf("%s destroyed.\n", outputEntity(pos.getSector().getY()+1, pos.getSector().getX()+1, entityType));
                for(int i=0; i < klingons.length; i++)
                    if(klingons[i] != null && isEqual(klingons[i].getPosition(), pos)) {
                        klingons[i] = null;
                        klingonsKilled += 1;
                    }
                break;
            case COMMANDER:
                con.printf("%s destroyed.\n", outputEntity(pos.getSector().getY()+1, pos.getSector().getX()+1, entityType));
                for(int i=0; i < klingonCommanders.length; i++)
                    if(klingonCommanders[i] != null && isEqual(klingonCommanders[i].getPosition(), pos)){
                        klingonCommanders[i] = null;
                        commandersKilled += 1;
                    }
                break;
            case ROMULAN:
                con.printf("%s destroyed.\n", outputEntity(pos.getSector().getY()+1, pos.getSector().getX()+1, entityType));
                for(int i=0; i < romulans.length; i++)
                    if(romulans[i] != null && isEqual(romulans[i].getPosition(), pos)) {
                        romulans[i] = null;
                        romulansKilled += 1;
                    }
                break;
            case SUPER_COMMANDER:
                con.printf("%s destroyed.\n", outputEntity(pos.getSector().getY()+1, pos.getSector().getX()+1, entityType));
                klingonSuperCommander = null;
                superCommandersKilled += 1; 
                break;
            case STAR:
                // con.printf("%s destroyed.\n", outputEntity(pos.getSector().getY()+1, pos.getSector().getX()+1, entityType));
                nova((Star) getEntityAtPosition(pos));
                break;
            default:
                con.printf("not implemented yet\n");
        }

        if(getRemainingKlingonCount() == 0) {
            Finish finish = new Finish(this);
            finish.finish(GameOverReason.WON);
        }
        updateMap();
    }

    @JsonIgnore
    public void clearScreen(){
        con.printf( "\033[2J\033[0;0H");	/* Hope for an ANSI display */
    }

    @JsonIgnore 
    public void pause(int i){
        //pause, implemented like original game almost verbatim
        con.printf("\n");
        if(i == 1){
            if(skill.getSkillValue() > GameLevel.FAIR.getSkillValue())
                con.printf("[ANNOUNCEMENT ARRIVING...]\n");
            else 
                con.printf("[IMPORTANT ANNOUNCEMENT ARRIVING -- HIT SPACE BAR TO CONTINUE]");
            getSpaceBar();
        } else {
            if (skill.getSkillValue() > GameLevel.FAIR.getSkillValue())
                con.printf("[CONTINUE?]\n");
            else
                con.printf("[HIT SPACE BAR TO CONTINUE]");
            getSpaceBar();
            con.printf("\r                           \r");
        }
        if (i != 0) {
            clearScreen();
        }
    }

    @JsonIgnore
    public void getSpaceBar(){
        // TODO: actually get input without enter key press
        con.readLine("");
        con.printf("\n");
    }

    @JsonIgnore
    public Boolean checkIfCommanderInCurrentQuadrant(){
        for(KlingonCommander c: klingonCommanders)
            if(c != null && positionsHaveSameQuadrant(c.getPosition(), enterprise.getPosition())) return true;
        return false;
    }

    @JsonIgnore
    public Boolean checkIfSuperInCurrentQuadrant(){
        return (klingonSuperCommander != null && positionsHaveSameQuadrant(klingonSuperCommander.getPosition(), enterprise.getPosition()));
    }

    @JsonIgnore
    public List<Enemy> getEnemiesInQuadrant(){
        List<Enemy> totalEnemies = new ArrayList<Enemy>();
        for(Klingon k: klingons) 
            if(k!= null && positionsHaveSameQuadrant(k.getPosition(), enterprise.getPosition()))
                totalEnemies.add(k);
        for(KlingonCommander c: klingonCommanders) 
            if(c!= null && positionsHaveSameQuadrant(c.getPosition(), enterprise.getPosition()))
               totalEnemies.add(c);
        for(Romulan r: romulans) 
            if(r!= null && positionsHaveSameQuadrant(r.getPosition(), enterprise.getPosition()))
                totalEnemies.add(r);
        if(klingonSuperCommander!= null && positionsHaveSameQuadrant(klingonSuperCommander.getPosition(), enterprise.getPosition()))
            totalEnemies.add(klingonSuperCommander);

        return totalEnemies;
    }

    @JsonIgnore
    public void refreshCondition(){
        enterprise.setCondition(Condition.GREEN);
        if (enterprise.getEnergy() < 1000.0) enterprise.setCondition(Condition.YELLOW);
        if (gameMap.getQuadrantNumber(enterprise.getPosition()) > 99) {
                    enterprise.setCondition(Condition.RED);
        }
    }

    @JsonIgnore
    public void nova(Star star){

        Coordinate sector;
        Coordinate starSector = star != null ? star.getPosition().getSector() : null;
        int newY;
        int newX;
        Position newPosition;

        if(star == null) {
            return;
        }

        if (randDouble(0, 1) < 0.05) {
            con.printf("supernova would happen, not implemented yet\n");
            /* Wow! We've supernova'ed */
            // snova(ix, iy);
            return;
        }

        destroyedStars += 1;

        con.printf("***%s at %d - %d novas.\n", EntityType.STAR.getName(), starSector.getY()+1, starSector.getX()+1);

        Position[] positions = star.getPosition().getAdjecentPositions();
        for(int i = 0; i < stars.length; i++) {
            if(stars[i] != null && isEqual(stars[i].getPosition(), star.getPosition())){
                stars[i] = null;
            }
        }
        for(int i = 0; i < positions.length; i++) {
            if(!positionsHaveSameQuadrant(positions[i], star.getPosition())) {
                continue;
            }
            EntityType entityType = getEntityTypeAtPosition(positions[i]);
        
            switch (entityType) {
                case FAERIE_QUEEN:
                case ENTERPRISE:
                    con.printf("***Starship buffeted by nova.\n");
                    if(enterprise.getSheilds().getStatus() == ShieldStatus.UP) {
                        if(enterprise.getSheilds().getLevel() >= 2000.0) {
                            enterprise.getSheilds().setLevel(enterprise.getSheilds().getLevel() - 2000.0);
                        } else {
                            double diff = 2000.0 - enterprise.getSheilds().getLevel();
                            enterprise.setEnergy(enterprise.getEnergy() - diff);
                            enterprise.getSheilds().setLevel(0.0);
                            enterprise.getSheilds().setStatus(ShieldStatus.DOWN);
                            con.printf("***Shields knocked out.\n");
                            damage(Device.SHIELDS,  0.005*damageFactor*randDouble(0,1)*diff);
                        }
                    } else {
                        enterprise.setEnergy(enterprise.getEnergy() - 2000.0);
                    }
                    if (enterprise.getEnergy()  <= 0) {
                        new Finish(this).finish(GameOverReason.NOVA);
                        return;
                    }

                    sector = enterprise.getPosition().getSector();
                    newY = 0;
                    newX = 0;
                    
                    if(sector.getY() == starSector.getY()) newY = sector.getY();
                    else if(sector.getY() < starSector.getY()) newY = sector.getY() - 1;
                    else newY = sector.getY() + 1;

                    if(sector.getX() == starSector.getX()) newX = sector.getX();
                    else if(sector.getX() < starSector.getX()) newX = sector.getX() - 1;
                    else newX = sector.getX() + 1;

                    con.printf("Force of nova displaces starship.\n");
                    newPosition = new Position(positions[i].getQuadrant(), new Coordinate(newY, newX));
                    new Move(this).wrapperMove(enterprise.getPosition(), newPosition);
                    break;
                case STAR:
                    nova((Star) getEntityAtPosition(positions[i]));
                    break;
                case PLANET:
                case STARBASE:
                case KLINGON:
                    destroyEntityAtPosition(positions[i]);
                    break;
                case COMMANDER:
                case SUPER_COMMANDER:
                case ROMULAN:
                    Enemy enemy = (Enemy) getEntityAtPosition(positions[i]);
                    sector = enemy.getPosition().getSector();
                    enemy.setPower(enemy.getPower() - 800.0);
                    if (enemy.getPower() <= 0.0) {
                        destroyEntityAtPosition(positions[i]);
                        break;
                    }
                    con.printf("***%s at %d - %d damaged", entityType.getName(), sector.getY()+1, sector.getX()+1);
                    newY = 0;
                    newX = 0;
                    
                    if(sector.getY() == starSector.getY()) newY = sector.getY();
                    else if(sector.getY() < starSector.getY()) newY = sector.getY() - 1;
                    else newY = sector.getY() + 1;

                    if(sector.getX() == starSector.getX()) newX = sector.getX();
                    else if(sector.getX() < starSector.getX()) newX = sector.getX() - 1;
                    else newX = sector.getX() + 1;

                    if(newY < 0 || newY > 9 || newX < 0 || newX > 9){
                        con.printf("\n");
                        break;
                    }
                    newPosition = new Position(positions[i].getQuadrant(), new Coordinate(newY, newX));

                    if(getEntityTypeAtPosition(newPosition) == EntityType.BLACK_HOLE) {
                        con.printf(", blasted into %s at %d - %d\n", EntityType.BLACK_HOLE.getName(), newY + 1, newX + 1);
                        destroyEntityAtPosition(newPosition);
                        break;
                    }
                    if(getEntityTypeAtPosition(newPosition) != EntityType.NOTHING) {
                        con.printf("\n");
                        break;
                    }
                    con.printf(", buffeted to %d - %d\n", newY+1, newX+1);
                    enemy.setPosition(newPosition);
                    updateMap();
                    break;
                default:
                    break;
            }
        }
    }
}
