package sst;

import java.io.Console;

import Model.Enterprise;
import Model.Entity;
import Model.Position;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Handles the long range scan (LRSCAN) command
 * 
 * @author Griffin Barnard
 */
@RequiredArgsConstructor
public class LrScan {
    @NonNull
    private Console con;
    @NonNull
    private Game game;

    /**
     * Prints a long range scan report to console during the game
     */
    public void ExecLRSCAN() {
        Enterprise enterprise = game.getEnterprise();
        int row = enterprise.getPosition().getQuadrant().getX();
        int column = enterprise.getPosition().getQuadrant().getY();
        con.printf("\nLong-range scan for Quadrant %d - %d:\n", row + 1, column + 1);
        con.printf("%-5d%-5d%-5d\n", getQuadrantNumber(row-1, column-1), getQuadrantNumber(row, column-1), getQuadrantNumber(row+1, column-1));
        con.printf("%-5d%-5d%-5d\n", getQuadrantNumber(row-1, column), getQuadrantNumber(row, column), getQuadrantNumber(row+1, column));
        con.printf("%-5d%-5d%-5d\n", getQuadrantNumber(row-1, column+1), getQuadrantNumber(row, column+1), getQuadrantNumber(row+1, column+1));

    }

    private int getQuadrantNumber(int row, int column) {
        // int thousands = getNumberOfEntiesInMapQuadrant(row, column, Supernova); TODO: add supernova
        if(row < 0 || row >= 8 || column < 0 || column >= 8) {
            return -1;
        }
        int thousands = 0;
        // System.out.println(row + " " + column);
        int hundreds  = getNumberOfEntiesInMapQuadrant(row, column, 'K')*100;
        int tens = getNumberOfEntiesInMapQuadrant(row, column, 'B')*10;
        int ones = getNumberOfEntiesInMapQuadrant(row, column, '*');

        return  thousands + hundreds + tens + ones;
    }

    private int getNumberOfEntiesInMapQuadrant(int row, int column, char entity) {
        int numberOfElements = 0;
        for(int i = 0; i < this.game.getMap()[row][column].length; i++){
            for(int j = 0; j < this.game.getMap()[row][column][i].length; j++){
                if(this.game.getMap()[row][column][i][j] == entity) {
                    numberOfElements += 1;
                }
            }
        }
        return numberOfElements;
    }

    private int getNumberOfEntiesBeforeMapUpdate(Entity[] entities, Position position) {
        // function to get number of entities in a qudrant before a map update, used in iniitialzation to make
        // sure a quadrant doesn't get too many of an entity type
        int numberOfElements = 0;
        for(int i = 0; i < entities.length; i++){
            if(entities[i] != null && entities[i].getPosition().getQuadrant().getX() == position.getQuadrant().getX() &&
            entities[i].getPosition().getQuadrant().getY() == position.getQuadrant().getY()) {
                numberOfElements += 1;
            }
        }
        return numberOfElements;
    }
}
