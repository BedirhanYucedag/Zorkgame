package ch.bbw.zork;

/*
 * author:  Michael Kolling, Version: 1.0, Date: July 1999
 * refactoring: Rinaldo Lanza, September 2020
 */

import java.util.Arrays;
import java.util.List;

public class CommandWords {

    // Füge alle benötigten Befehle zur Liste hinzu
    private List<String> validCommands = Arrays.asList(
            "go", "quit", "help", "back", "look", "take", "drop", "inventory", "map"
    );

    public boolean isCommand(String commandWord) {
        return validCommands.contains(commandWord);
    }

    public String showAll() {
        return String.join(" ", validCommands);
    }
}
