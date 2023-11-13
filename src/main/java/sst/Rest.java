package sst;

import static Utils.Utils.parseIntegers;
import static Utils.Utils.readCommands;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import Model.Game;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * passes the time
 */
@RequiredArgsConstructor
public class Rest {
    @NonNull
    private Game game;

    /**
     * executes the REST command. Resting will allow the player to pass time
     * 
     * @author Matthias Schrock
     * @param params
     */
    public void ExecREST(List<String> params) {
        List<Integer> iParams = new ArrayList<Integer>();
        Integer dur = null;
        if (params.size() == 0) {
            game.con.printf("How long? ");
            params = readCommands(game.con.readLine()).orElse(null);

            if (params == null) {
                game.begPardon();
                return;
            }
        }

        iParams = parseIntegers(params);
        if (iParams.size() == 0) {
            game.begPardon();
            return;
        }

        dur = iParams.get(0);
        sure(dur);

        game.setStarDate(game.getStarDate() - dur);
    }

    /**
     * checks if the player is sure they want to rest for longer than time allows
     * 
     * @param duration the duration (in stardates) of the rest period
     */
    private void sure(Integer duration) {
        String cont;
        if (duration > game.getStarDate()) {
            game.con.printf("Are you sure? ");
            cont = readCommands(game.con.readLine()).orElse(Arrays.asList("No")).get(0);
            if (!cont.equals("YES")) {
                // TODO: call end of game due to time
            }
        }
    }
}
