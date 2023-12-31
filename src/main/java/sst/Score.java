package sst;


import Model.Condition;
import Model.Game;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Score {
    @NonNull
    private Game game;

    public void ExecSCORE(boolean finalScore, boolean gameWon) {
        StringBuilder sb = new StringBuilder();
        int score = (int) calcScore(finalScore);

        if (finalScore) {
            sb.append("\n\nYour score --\n\n");

            // TODO: add the rest of the score for game win

            sb.append(String.format("TOTAL SCORE %35s\n", score));
        } else {
            sb.append("\n\nYour score so far --\n\n");
            
            sb.append(String.format("TOTAL SCORE %35s\n", score));
            sb.append("\nREMEMBER--The score doesn't really matter until the mission is accomplished!\n\n");
        }

        game.con.printf("%s", sb.toString());
    }
    
    private double calcScore(boolean finalScore) {
        double timeUsed = game.getStarDate() - game.getScore().getInitStarDate();
        
        // gains
        int klDest = game.getKlingonsKilled();
        int klCmdDest = game.getCommandersKilled(); 
        int klSCmdDest = game.getSuperCommandersKilled();
        int klCap = 0; 
        int romDest = game.getRomulansKilled();
        int romSur = game.getRomulansCount(); 
        // perdate = (d.killc + d.killk + d.nsckill)/timused; 
        double perDate = (game.getKlingonsKilled() + game.getCommandersKilled() + game.getSuperCommandersKilled() + game.getRomulansKilled()) /
                (game.getInitStarDate() - game.getStarDate());
        int win = (finalScore ? game.getSkill().getSkillValue() * 100 : 0);

        // losses
        int deathPen = game.getEnterprise().getCondition() == Condition.DEAD ? 200 : 0;
        int basePen = 100 * game.getDestroyedBases();
        int shipPen = 100 * 0;
        int treatyPen = 100 * 0;
        int helpPen = Call.calls * 45;
        int planetPen = 10 * game.getDestroyedPlanets();
        int starPen = 5 * game.getDestroyedStars();

        timeUsed = ((timeUsed == 0 || game.getRemainingKlingonCount() != 0) && timeUsed < 5.0) ? 5.0 : timeUsed;

        return (10 * klDest) +
                (50 * klCmdDest) +
                (200 * klSCmdDest) +
                (3 * klCap) +
                (20 * romDest) +
                (romSur) +
                (500 * perDate) +
                win - deathPen - basePen - shipPen - treatyPen - helpPen - planetPen - starPen - game.getCasualties();
    }
}
