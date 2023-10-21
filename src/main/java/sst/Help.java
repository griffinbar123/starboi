package sst;

import java.io.File;
import java.io.FileNotFoundException;
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

        switch (c) {
            case SRSCAN:
                game.con.printf("%s", helpText.get(Command.SRSCAN));
                break;
            case COMMANDS:
                game.con.printf("%s", helpText.get(Command.COMMANDS));
                break;
            case STATUS:
                game.con.printf("%s", helpText.get(Command.STATUS));
                break;
            case LRSCAN:
                game.con.printf("%s", helpText.get(Command.LRSCAN));
                break;
            case COMPUTER:
                game.con.printf("%s", helpText.get(Command.COMPUTER));
                break;
            case CHART:
                game.con.printf("%s", helpText.get(Command.CHART));
                break;
            case QUIT:
                game.con.printf("%s", helpText.get(Command.QUIT));
                break;
            case FREEZE:
                game.con.printf("%s", helpText.get(Command.FREEZE));
                break;
            default:
                break;
        }
    }

    private void compileHelpData() throws FileNotFoundException {
        StringBuilder sb = new StringBuilder();
        Command c = Command.undefined;
        helpText = new HashMap<Command, String>();
        File doc = new File("sst.doc");
        Scanner scanner = new Scanner(doc);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.contains("Mnemonic:")) {
                c = handler.matchCommand(line.replace("Mnemonic:", "").trim());
                sb.append(line);

                while (scanner.hasNextLine()) {
                    String read = scanner.nextLine();

                    if (read.matches("\\s{2,}\\d+")) {
                        break;
                    }

                    sb.append("\n" + read);
                }
                helpText.put(c, sb.toString());
            }
        }
        scanner.close();
    }

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
            if (c == Command.undefined) {
                printValidCommands();
                params.clear();
            } else {
                return c;
            }
        }
    }

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
