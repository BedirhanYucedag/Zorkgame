package ch.bbw.zork;

/**
 * Class Command - Part of the "Zork" game.
 *
 * author: Michael Kolling version: 1.0 date: July 1999
 * refactoring: Rinaldo Lanza, September 2020
 */

public class Command {

    private String commandWord;
    private String secondWord;

    public Command(String commandWord) {
        this(commandWord, null);
    }
    public Command(String commandWord, String secondWord) {
        this.commandWord = commandWord;
        this.secondWord = secondWord;
    }

    public String getCommandWord() {
        return commandWord;
    }
    public String getSecondWord() {
        return secondWord;
    }
    public boolean isUnknown() {
        return (commandWord == null);
    }
    public boolean hasSecondWord() {
        return (secondWord != null);
    }
}
