package sst;

import Model.Condition;
import Model.Game;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Handles the finish boi
 * 
 * @author Griffin Barnard
 */
@RequiredArgsConstructor
public class Finish {
    @NonNull
    private Game game;

    public static enum GameOverReason {
        WON,
        DEPLETED,
        LIFESUPPORT,
        ENERGY,
        BATTLE,
        BARRIER,
        NOVA,
        SUPERNOVA,
        ABANDON,
        DILITHIUM,
        MATERIALIZE,
        PHASER,
        LOST,
        MINING,
        PLANET,
        FSSC,
        FPNOVA,
        STRACTOR,
        DRAY,
        TRIBBLE,
        HOLE,
        CLOAK
    }

    /**
     * handles finishing the game
     * 
     * @author Griffin Barnard
     */
    public void finish(GameOverReason gameOverReason) {
        game.con.printf("\n\n\nIt is stardate %.1f .\n\n", this.game.getStarDate());
        Score score = new Score(game);
        game.setIsOver(true);
        
        switch (gameOverReason) {
            case WON:
                if(game.getRomulansCount() !=  0)
                    game.con.printf("The remaining %d Romulan ships surrender to the Starfleet Command.\n", game.getRomulansCount());
                
                game.con.printf("You have smashed the Klingon invasion fleet and saved\nthe Federation.\n");

                //TODO: implement capturing the remaining klingons if they r suppposed to be

                if(game.getEnterprise().getCondition() != Condition.DEAD) {
                    //TODO: promote the player

                    game.con.printf("\nLIVE LONG AND PROSPER.\n");
                }
                score.ExecSCORE(true, true);
                return;
            case DEPLETED:
                game.con.printf("Your time has run out and the Federation has been\nconquered.  Your starship is now Klingon property,\nand you are put on trial as a war criminal. On the\nbasis of your record, you are %s\n", (game.getRemainingKlingonCount() * 3) > game.getScore().getInitTotKlingons() ? "aquitted.\n\nLIVE LONG AND PROSPER." : "found guilty and\nsentenced to death by slow torture.");
                game.getEnterprise().setCondition(Condition.DEAD);
                score.ExecSCORE(true, true);
                return;
            case LIFESUPPORT:
                game.con.printf("Your life support reserves have run out, and\nyou die of thirst, starvation, and asphyxiation.\nYour starship is a derelict in space.\n");
                break;
            case ENERGY:
                game.con.printf("Your energy supply is exhausted.\n\nYour starship is a derelict in space.\n");
                break;
            case BATTLE:
                game.con.printf("The %s has been destroyed in battle.\n\nDulce et decorum est pro patria mori.\n", "Enterprise");
                break;
            case BARRIER:
                game.con.printf("You have made three attempts to cross the negative energy\nbarrier which surrounds the galaxy.\n\nYour navigation is abominable.\n");
                return;
            case NOVA:
                game.con.printf("Your starship has been destroyed by a nova.\nThat was a great shot.\n\n");
                break;
            case SUPERNOVA:
                game.con.printf("The %s has been fried by a supernova.\n...Not even cinders remain...\n", "Enterprise");
                break;
            case ABANDON:
                game.con.printf("You have been captured by the Klingons. If you still\nhad a starbase to be returned to, you would have been\nrepatriated and given another chance. Since you have\nno starbases, you will be mercilessly tortured to death.\n");
                break;
            case DILITHIUM:
                game.con.printf("Your starship is now an expanding cloud of subatomic particles\"\n");
                break;
            case MATERIALIZE:
                game.con.printf("Starbase was unable to re-materialize your starship.\nSic transit gloria muntdi\n");
                break;
            case PHASER:
                game.con.printf("The %s has been cremated by its own phasers.\n", "Enterprise");
                break;
            case LOST:
                game.con.printf("You and your landing party have been\nconverted to energy, dissipating through space.\n");
                break;
            case MINING:
                game.con.printf("You are left with your landing party on\na wild jungle planet inhabited by primitive cannibals.\n\nThey are very fond of \"Captain Kirk\" soup.\n\nWithout your leadership, the %s is destroyed.\n", "Enterpise");
                break;
            case PLANET:
                game.con.printf("You and your mining party perish.\n\nThat was a great shot.\n\n");
                break;
            case FSSC:
                game.con.printf("The Galileo is instantly annihilated by the supernova.\n");
                break;
            case FPNOVA:
                game.con.printf("You and your mining party are atomized.\n\nMr. Spock takes command of the %s and\njoins the Romulans, reigning terror on the Federation.\n", "Enterprise");
                break;
            case STRACTOR:
                game.con.printf("The shuttle craft Galileo is also caught,\nand breaks up under the strain.\n\nYour debris is scattered for millions of miles.\nWithout your leadership, the %s is destroyed.\n", "Enterprise");
                break;
            case DRAY:
                game.con.printf("The mutants attack and kill Spock.\nYour ship is captured by Klingons, and\nyour crew is put on display in a Klingon zoo.\n");
                break;
            case TRIBBLE:
                game.con.printf("Tribbles consume all remaining water,\nfood, and oxygen on your ship.\n\nYou die of thirst, starvation, and asphyxiation.\nYour starship is a derelict in space.\n");
                break;
            case HOLE:
                game.con.printf("Your ship is drawn to the center of the black hole.\nYou are crushed into extremely dense matter.\n");
                break;
            case CLOAK:
                game.con.printf("You have violated the Treaty of Algeron.\nThe Romulan Empire can never trust you again.\n");
                break;
        }
        score.ExecSCORE(true, false);
    }
}
