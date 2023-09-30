package sst;

import java.io.Console;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * implements the computer command.
 * Hopefully methods in the computer command can be
 * used for calculating time in the move command
 * 
 * @author Matthias Schrock
 */
@RequiredArgsConstructor
public class Computer {
    private Console con;
    @NonNull
    private Game game;

    /**
     * COMPUTER command implementation
     */
    public void ExecCOMPUTER() {
        // Initialize console
        con = System.console();
        if (con == null)
            return;

        System.out.println("Hello, Computer");
    }
}
