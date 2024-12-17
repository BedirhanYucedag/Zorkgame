package ch.bbw.zork;

import javax.swing.*;
import java.awt.*;
import java.util.Stack;

public class Game extends JFrame {
    private Room outside, lab, tavern, gBlock, office;
    private Room currentRoom;
    private Player player;
    private Parser parser;
    private Stack<Room> roomHistory;
    private JTextArea gameOutput;
    private JPanel buttonPanel;
    private JPanel actionPanel;
    private boolean windowMessageShown = false; // Kontrollvariable für das Fenster
    private boolean hasKey = false; // Kontrollvariable für den Schlüssel
    private boolean hasBook = false; // Kontrollvariable für das Buch
    private boolean hasManuscript = false; // Kontrollvariable für das Manuskript

    public Game() {
        setTitle("Adventure Game");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        gameOutput = new JTextArea();
        gameOutput.setEditable(false);
        gameOutput.setBackground(Color.BLACK);
        gameOutput.setForeground(Color.GREEN);
        add(new JScrollPane(gameOutput), BorderLayout.CENTER);

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        add(buttonPanel, BorderLayout.SOUTH);

        actionPanel = new JPanel();
        actionPanel.setLayout(new GridLayout(1, 6)); // Größe auf 6 ändern
        add(actionPanel, BorderLayout.NORTH);

        parser = new Parser();
        roomHistory = new Stack<>();
        player = new Player(10);
        createRooms();

        showTutorial();
        updateGameOutput("Willkommen zum Abenteuer in der verlassenen Universität!\n");
        updateGameOutput("Sie befinden sich " + currentRoom.getDescription());

        updateExitButtons();
        addActionButtons();
    }

    private void showTutorial() {
        updateGameOutput("Tutorial:\n"
                + "- Verwenden Sie die Schaltflächen, um sich zu bewegen.\n"
                + "- Verwenden Sie 'Untersuchen' für eine Raumübersicht.\n"
                + "- Verwenden Sie 'Inventar', um Ihr Inventar anzusehen.\n"
                + "- Verwenden Sie 'Zurück' für den letzten Raum.\n"
                + "- Verwenden Sie 'Nehmen' oder 'Ablegen' für Gegenstände.\n"
                + "- Verwenden Sie 'Karte', um eine Übersicht der Räume anzuzeigen.\n\n");
    }

    private void createRooms() {
        outside = new Room("Außenbereich", "Sie stehen vor dem G-Block des Campus.");
        lab = new Room("Labor", "Hier ist das Labor");
        tavern = new Room("Cafeteria", "Das ist die Universitäts-Cafeteria.");
        gBlock = new Room("Hauptkorridor", "Der Hauptkorridor der Universität.");
        office = new Room("Büro", "Das Büro, in dem das Buch versteckt ist. Die Tür scheint verschlossen.");

        // Gegenstände im Labor hinzufügen
        lab.addItem(new Item("Schlüssel", "Ein kleiner, glänzender Schlüssel.", 1));
        // Manuskript als SpecialItem erstellen und zur Cafeteria hinzufügen
        spItem manuscript = new spItem("Manuskript", "Ein mysteriöses Manuskript mit geheimen Inhalten.", 1, "Kann den Charakter vor Gefahren warnen.");
        tavern.addItem(manuscript); // Manuskript in der Cafeteria hinzufügen

        // Buch im Büro hinzufügen
        office.addItem(new Item("Buch", "Ein geheimes Buch mit interessanten Inhalten.", 2));

        //Sandwich in tavern
        tavern.addItem(new Item("Sandwich", "Ein altes Sandwich, das zurückgelassen wurde.", 2));

        outside.setExits(lab, gBlock, tavern);
        lab.setExits(outside);
        tavern.setExits(outside);
        gBlock.setExits(outside, office);
        office.setExits(gBlock);

        currentRoom = outside;
    }

    private void updateExitButtons() {
        buttonPanel.removeAll();

        for (String direction : currentRoom.getExits().keySet()) {
            Room nextRoom = currentRoom.getExit(direction);
            JButton exitButton = new JButton(nextRoom.shortDescription());
            exitButton.addActionListener(e -> processCommand("go " + direction));
            buttonPanel.add(exitButton);
        }

        buttonPanel.revalidate();
        buttonPanel.repaint();
    }

    private void addActionButtons() {
        JButton backButton = new JButton("Zurück");
        backButton.addActionListener(e -> processCommand("back"));
        actionPanel.add(backButton);

        JButton takeButton = new JButton("Nehmen");
        takeButton.addActionListener(e -> {
            String itemName = JOptionPane.showInputDialog(this, "Gegenstand zum Nehmen eingeben:");
            if (itemName != null) processCommand("take " + itemName);
        });
        actionPanel.add(takeButton);

        JButton dropButton = new JButton("Ablegen");
        dropButton.addActionListener(e -> {
            String itemName = JOptionPane.showInputDialog(this, "Gegenstand zum Ablegen eingeben:");
            if (itemName != null) processCommand("drop " + itemName);
        });
        actionPanel.add(dropButton);

        JButton inventoryButton = new JButton("Inventar");
        inventoryButton.addActionListener(e -> updateGameOutput(player.getInventoryDescription())); // Inventar anzeigen
        actionPanel.add(inventoryButton);

        JButton helpButton = new JButton("Hilfe");
        helpButton.addActionListener(e -> processCommand("help"));
        actionPanel.add(helpButton);

        JButton mapButton = new JButton("Karte");
        mapButton.addActionListener(e -> processCommand("map"));
        actionPanel.add(mapButton);

        actionPanel.revalidate();
        actionPanel.repaint();
    }

    private void processCommand(String commandInput) {
        Command command = parser.parse(commandInput);
        if (command.isUnknown()) {
            updateGameOutput("Ich verstehe diesen Befehl nicht...\n");
            return;
        }

        String commandWord = command.getCommandWord();
        if (commandWord.equals("go")) {
            processGoCommand(command);
        } else if (commandWord.equals("back")) {
            goBack();
        } else if (commandWord.equals("look")) {
            updateGameOutput(currentRoom.getDescription() + "\n" + currentRoom.getItemsDescription() + "\n");
        } else if (commandWord.equals("take")) {
            takeItem(command);
        } else if (commandWord.equals("drop")) {
            dropItem(command);
        } else if (commandWord.equals("inventory")) {
            updateGameOutput(player.getInventoryDescription());
        } else if (commandWord.equals("help")) {
            showTutorial();
        } else if (commandWord.equals("map")) {
            showMap();
        }
    }

    private void processGoCommand(Command command) {
        if (!command.hasSecondWord()) {
            updateGameOutput("Wohin möchten Sie gehen?\n");
            return;
        }

        String direction = command.getSecondWord();
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            updateGameOutput("Sie können in diese Richtung nicht gehen!\n");
        } else {
            roomHistory.push(currentRoom);
            currentRoom = nextRoom;
            updateGameOutput("Sie sind jetzt " + currentRoom.getDescription() + "\n");

            // Nachricht über offenes Fenster
            if (currentRoom == lab && !windowMessageShown) {
                updateGameOutput("Das Fenster ist offen, brrrrrrr...\n");
                windowMessageShown = true; // Nur einmal anzeigen
            }

            // Überprüfen, ob der Schlüssel im Raum ist
            Item keyItem = currentRoom.getItemByName("Schlüssel");
            if (keyItem != null && !hasKey) {
                updateGameOutput("Der Schlüssel liegt im Raum.\n");
            }

            // Überprüfen, ob das Buch im Büro liegt
            Item bookItem = office.getItemByName("Buch");
            if (currentRoom == office && bookItem != null && !hasKey) {
                updateGameOutput("Die Tür ist verschlossen, ohne den Schlüssel können Sie das Buch nicht aufnehmen.\n");
            } else if (currentRoom == office && bookItem != null) {
                updateGameOutput("Das Buch liegt im Raum. Sie können es aufnehmen.\n");
            }

            // Überprüfen, ob das Manuskript im Raum ist
            Item manuscriptItem = tavern.getItemByName("Manuskript");
            if (currentRoom == tavern && manuscriptItem != null && !hasManuscript) {
                updateGameOutput("Das Manuskript liegt im Raum.\n");
            }
            Item sandwich = tavern.getItemByName("Sandwich");
            if (currentRoom == tavern && sandwich != null && !hasManuscript) {
                updateGameOutput("Ein Sandwich liegt auf dem Tisch.\n");
            }

            // Überprüfen, ob der Spieler das Buch hat und in den Außenbereich kommt
            if (currentRoom == outside && hasBook) {
                updateGameOutput("Du hast das Buch erfolgreich gefunden und den Außenbereich erreicht. Du hast gewonnen!\n");
                // Spiel für 5 Sekunden offen halten, bevor es sich schließt
                Timer timer = new Timer(5000, e -> System.exit(0));
                timer.setRepeats(false);
                timer.start();
            }

            updateExitButtons();
        }
    }

    private void goBack() {
        if (roomHistory.isEmpty()) {
            updateGameOutput("Sie können nicht weiter zurückgehen!\n");
        } else {
            currentRoom = roomHistory.pop();
            updateGameOutput("Sie gehen zurück in den vorherigen Raum.\nSie befinden sich " + currentRoom.getDescription() + "\n");
            updateExitButtons();
        }
    }

    private void takeItem(Command command) {
        if (!command.hasSecondWord()) {
            updateGameOutput("Welchen Gegenstand möchten Sie nehmen?\n");
            return;
        }

        String itemName = command.getSecondWord();
        Item item = currentRoom.getItemByName(itemName);

        if (item == null) {
            updateGameOutput("Dieser Gegenstand ist hier nicht vorhanden.\n");
        } else if (player.addItem(item)) {
            currentRoom.removeItem(item);
            updateGameOutput("Sie haben den Gegenstand aufgenommen: " + item.getName() + "\n");

            // Überprüfen, ob der Spieler den Schlüssel aufgenommen hat
            if (item.getName().equalsIgnoreCase("Schlüssel")) {
                hasKey = true;
                updateGameOutput("Sie haben den Schlüssel gefunden!\n");
            } else if (item.getName().equalsIgnoreCase("Buch")) {
                hasBook = true; // Setze hasBook auf true
                updateGameOutput("Sie haben das Buch aufgenommen!\n"); // Buchaufnahme bestätigen
            } else if (item.getName().equalsIgnoreCase("Manuskript")) {
                hasManuscript = true; // Setze hasManuscript auf true
                updateGameOutput("Sie haben das Manuskript aufgenommen!\n"); // Manuskriptaufnahme bestätigen
            }
        } else {
            updateGameOutput("Sie können diesen Gegenstand nicht tragen.\n");
        }
    }

    private void dropItem(Command command) {
        if (!command.hasSecondWord()) {
            updateGameOutput("Welchen Gegenstand möchten Sie ablegen?\n");
            return;
        }

        String itemName = command.getSecondWord();
        Item item = player.getItemByName(itemName);

        if (item == null) {
            updateGameOutput("Sie haben diesen Gegenstand nicht.\n");
        } else {
            player.removeItem(item);
            currentRoom.addItem(item);
            updateGameOutput("Sie haben den Gegenstand abgelegt: " + item.getName() + "\n");

            if (item.getName().equalsIgnoreCase("Buch")) {
                hasBook = false; // Setze hasBook auf false, wenn das Buch abgelegt wird
            } else if (item.getName().equalsIgnoreCase("Schlüssel")) {
                hasKey = false; // Setze hasKey auf false, wenn der Schlüssel abgelegt wird
            } else if (item.getName().equalsIgnoreCase("Manuskript")) {
                hasManuscript = false; // Setze hasManuscript auf false, wenn das Manuskript abgelegt wird
            }
        }
    }

    private void showMap() {
        updateGameOutput("\n--- Campus Karte ---\n");
        Room[] rooms = {outside, lab, tavern, gBlock, office};
        for (Room room : rooms) {
            updateGameOutput("- " + room.getName() + ": " + room.getDescription() + "\n");
        }
        updateGameOutput("--- Ende der Karte ---\n");
    }

    private void updateGameOutput(String text) {
        gameOutput.append(text + "\n");
        gameOutput.setCaretPosition(gameOutput.getDocument().getLength());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Game gameGUI = new Game();
            gameGUI.setVisible(true);
        });
    }
}
