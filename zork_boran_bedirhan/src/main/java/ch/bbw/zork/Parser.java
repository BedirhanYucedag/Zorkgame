package ch.bbw.zork;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class Parser {
    private CommandWords validCommandWords;
    private BufferedReader reader;

    public Parser() {
        validCommandWords = new CommandWords(); // Initialisierung der gültigen Befehle
        reader = new BufferedReader(new InputStreamReader(System.in));
    }

    public Command getCommand() {
        System.out.print("> "); // Eingabeaufforderung

        String inputLine = null;
        try {
            inputLine = reader.readLine();
        } catch (IOException e) {
            System.out.println("Error reading input: " + e.getMessage());
        }

        if (inputLine != null) {
            String[] words = inputLine.trim().split(" ");
            String commandWord = words[0];
            String secondWord = words.length > 1 ? words[1] : null;

            // Überprüft, ob das erste Wort ein gültiger Befehl ist
            if (validCommandWords.isCommand(commandWord)) {
                return new Command(commandWord, secondWord);
            } else {
                return new Command(null, secondWord);
            }
        } else {
            return new Command(null, null);
        }
    }

    public String showCommands() {
        return validCommandWords.showAll();
    }

    // Neue parse-Methode für die GUI-Eingabe
    public Command parse(String commandInput) {
        String[] words = commandInput.trim().split(" ");
        String commandWord = words[0];
        String secondWord = words.length > 1 ? words[1] : null;

        if (validCommandWords.isCommand(commandWord)) {
            return new Command(commandWord, secondWord);
        } else {
            return new Command(null, secondWord);
        }
    }

}
