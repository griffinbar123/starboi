package sst;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import Model.Game;
import Utils.Utils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Implement the FREEZE command
 * @author Matthias Schrock
 */
@RequiredArgsConstructor
public class Freeze {
    @NonNull
    private Game game;

    /**
     * Save the current state of the game before quitting to session.txt
     * @author Matthias Schrock
     */
    public void ExecFREEZE() {
        boolean write = true;
        try {
            File file = new File("session.txt");
            if (!file.createNewFile()) {
                this.game.con.printf("Prompt for rewrite OK?\n");
            }
            if (write) {
                FileWriter writer = new FileWriter("session.txt");
                writer.write(Utils.serialize(this.game));
                writer.close();
                this.game.con.printf("Session successfully saved\n");
            }
        } catch (IOException e) {
            this.game.con.printf("An error occurred.\n");
            e.printStackTrace();
        }
    }
}
