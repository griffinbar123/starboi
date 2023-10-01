package sst;

import java.io.Console;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import Utils.Utils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Implement the FREEZE command
 * @author Matthias Schrock
 */
@RequiredArgsConstructor
public class Freeze {
    private Console con;
    @NonNull
    private Game game;

    /**
     * Save the current state of the game before quitting to session.txt
     */
    public void ExecFREEZE() {
        // Initialize console
        con = System.console();
        if (con == null)
            return;

        boolean write = true;
        try {
            File file = new File("session.txt");
            if (!file.createNewFile()) {
                con.printf("Prompt for rewrite OK?\n");
            }
            if (write) {
                FileWriter writer = new FileWriter("session.txt");
                writer.write(Utils.serialize(this.game));
                writer.close();
                con.printf("Session successfully saved\n");
            }
        } catch (IOException e) {
            con.printf("An error occurred.\n");
            e.printStackTrace();
        }
    }
}
