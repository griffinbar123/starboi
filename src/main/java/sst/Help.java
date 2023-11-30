package sst;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Stream;
import Model.Game;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
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
    private Map<Command, String> helpText;
    @NonNull
    private final String FILENAME;

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
                String error = "Spock-  \"Captain, I'm sorry, but I can't find SST.DOC.\"\n" +
                        "   computer. You need to find SST.DOC and put it in the\n" +
                        "   current directory.\"\n";
                game.con.printf("%s", error);
                return;
            }
        }

        if (!helpText.containsKey(c)) {
            return;
        }

        game.con.printf("\nSpock- \"Captain, I've found the following information:\"\n\n");

        String[] help = helpText.get(c).split("\n\n");
        int len = help.length;

        for (int i = 0; i < len; i++) {
            if ((i + 1) % 5 == 0) {
                game.con.readLine("\n[PRESS ENTER TO CONTINUE]\n");
            }

            game.con.printf("%s\n\n", help[i]);
        }
    }

    /**
     * compiles the help data from the sst.doc file into a map
     * @throws FileNotFoundException
     * @author Matthias Schrock
     */
    private void compileHelpData() throws FileNotFoundException{
        helpText = new HashMap<Command, String>();
        StringBuilder sb = new StringBuilder();
        File doc = new File(FILENAME);
        Scanner scanner = new Scanner(doc);
        
        while (scanner.hasNextLine()) {
            sb.append("\n" + scanner.nextLine());
        }
        scanner.close();

        Stream.of(sb.toString().split("Mnemonic:")).skip(1).forEach(s -> {
            Command c = handler.matchCommand(s.split("\n")[0].trim());
            helpText.put(c, s.split("\\s+\\*{11,}|\n\\s{20,}")[0].trim());
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
                params = readCommands(cmdStr).orElse(Arrays.asList(""));
            }
            cmdStr = params.get(0);

            c = handler.matchCommand(cmdStr);
            if (c == Command.undefined || !c.Implemented()) {
                ExecCOMMANDS();
                params = Arrays.asList();
            } else {
                return c;
            }
        }
    }

    /**
     * prints a list of valid, implemented commands to the console
     * @see Command
     * @author Matthias Schrock
     */
    public void ExecCOMMANDS() {
        int columns = 4;
        List<String> commands = Stream.of(Command.values())
                .filter(c -> c != Command.undefined)
                .filter(c -> c.Implemented())
                .map(c -> c.toString())
                .toList();
        StringBuilder sb = new StringBuilder();

        sb.append("\nValid commands:\n");
        for (int i = 0; i < commands.size(); i++) {
            sb.append(String.format("%-11s", commands.get(i)));
            if ((i + 1) % columns == 0) {
                sb.append("\n");
            }
        }
        sb.append("\n\n");
        game.con.printf(sb.toString());
    }
}
