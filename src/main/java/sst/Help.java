package sst;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Stream;

import Model.Game;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import sst.CommandHandler.Command;
import static Utils.Utils.readCommands;

/**
 * implements the help command and functionality
 * @author Matthias Schrock
 */
@RequiredArgsConstructor
public class Help {
    @NonNull
    private Game game;
    @NonNull
    private CommandHandler handler;
    private static Map<Command, String> helpText;

    /**
     * HELP command implementation
     * @param params additional command line arguments. If null, user will
     * be prompted for input. Else, the first element will be treated as
     * the desired command for help inquiry
     * @author Matthias Schrock
     */
    public void ExecHELP(List<String> params) {
        Command c = pollUserForCommand(params);

        if (helpText == null) {
            try {
                compileHelpData();
            } catch (FileNotFoundException e) {
                game.con.printf("Spock-  \"Captain, that information is missing from the\n");
                game.con.printf("   computer. You need to find SST.DOC and put it in the\n");
                game.con.printf("   current directory.\"\n");
                return;
            }
        }

        game.con.printf("\nSpock- \"Captain, I've found the following information:\"\n\n");

        String[] help = helpText.get(c).split("\n\n");
        int len = help.length;
        int i = 0;

        while(i < len) {
            if (i % 4 == 0) {
                // TODO: wait until spacebar is pressed
                while (game.con.readLine("\n[SPACEBAR TO CONTINUE]\n").charAt(0) != ' '){};
            }
            game.con.printf("%s\n", help[i]);
            i++;
        }

        game.con.printf("%s\n", helpText.get(c));
    }

    /**
     * compiles the help data from the sst.doc file into a map
     * @throws FileNotFoundException
     * @author Matthias Schrock
     */
    private void compileHelpData() throws FileNotFoundException{
        helpText = new HashMap<Command, String>();
        StringBuilder sb = new StringBuilder();
        File doc = new File("sst.doc");
        Scanner scanner = new Scanner(doc);
        
        while (scanner.hasNextLine()) {
            sb.append("\n" + scanner.nextLine());
        }
        scanner.close();

        Stream.of(sb.toString().split("Mnemonic:")).skip(1).forEach(s -> {
            Command c = handler.matchCommand(s.split("\n")[0].trim());
            helpText.put(c, s.split("\\s{20,}\\d+")[0].trim());
        });
    }

    /**
     * prompts the user for a command to get help on
     * @param params additional command line arguments.
     * If null or invalid, user will be prompted to enter a command
     * @return the command to get help on
     * @author Matthias Schrock
     */
    private Command pollUserForCommand(List<String> params) {
        Command c;
        String cmdStr = "";

        while (true) {
            if (params.size() < 1) {
                cmdStr = game.con.readLine("Help on what command?");
                params = readCommands(cmdStr).orElse(List.of(""));
            }
            cmdStr = params.get(0);

            c = handler.matchCommand(cmdStr);
            if (c == Command.undefined || c == Command.HELP) {
                printValidCommands();
                params.clear();
            } else {
                return c;
            }
        }
    }

    /**
     * prints a list of valid commands to the console
     * @author Matthias Schrock
     */
    private void printValidCommands() {
        int columns = 4;
        game.con.printf("\nValid commands:\n");
        for (int i = 0; i < (int) Command.values().length / columns; i++) {
            for (int j = 0; j < columns; j++) {
                int index = i + j * (int) Command.values().length / columns;
                if (index < Command.values().length - 2) {
                    game.con.printf("%-11s", Command.values()[index].toString());
                }
            }
            game.con.printf("\n");
        }
        game.con.printf("\n");
    }
}
