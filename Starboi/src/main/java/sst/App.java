package sst;
import java.util.Scanner;
public class App {
    private Scanner scanner;
    public App() {
        scanner = new Scanner(System.in);
    }
    public void gameLevel(String levelChoice) {
        //System.out.println("Here is the player choice after passed in: "+levelChoice);
        switch (levelChoice) {
            case "novice":
            case "fair":
            case "good":
            case "expert":
            case "emeritus":
                System.out.print("Please type in a secret password (9 characters maximum)- ");
                break;
            default:
                System.out.println("Invalid choice. Please choose Novice, Fair, Good, Expert, or Emeritus");
                break;
        }
    }

    public void gameType(String gameChoice) {
        switch (gameChoice) {
            case "short":
            case "medium":
            case "long":
                System.out.print("Are you a Novice, Fair, Good, Expert, or Emeritus player? ");
                String playerChoiceOne = scanner.nextLine().trim().toLowerCase();
                System.out.println();
                //System.out.println("Here is the player choice before passed in: "+playerChoice);
                gameLevel(playerChoiceOne); 
                break;
            default:
                System.out.println("Invalid choice. Please choose short, medium, or long.");
                break;
        }
    }
    
    public static void main(String[] args) {
        App app = new App();

        System.out.print("Would you like a regular, tournament, or frozen game? ");
        String choice = app.scanner.nextLine().trim().toLowerCase();
        System.out.println();
      
        switch (choice) {
            case "regular":
                System.out.print("Would you like a short, medium, or long game? ");
                String regularChoice = app.scanner.nextLine().trim().toLowerCase();
                System.out.println();
                app.gameType(regularChoice);
                // Add code for a regular game here
                break;
            case "tournament":
                System.out.print("Type in a tournament number- ");
                String tournNumber = app.scanner.nextLine();//indicate what happens for each number typed in
                System.out.println();
                System.out.print("Would you like a short, medium, or long game? ");
                String tournChoice = app.scanner.nextLine().trim().toLowerCase();
                System.out.println();
                app.gameType(tournChoice);
                break;
            //implement this later
            case "frozen":
                System.out.println("You chose a frozen game.");
                // Add code for a frozen game here
                break;
            default:
                System.out.println("Invalid choice. Please choose regular, tournament, or frozen.");
                break;
        }
        app.scanner.close();
    }
}
