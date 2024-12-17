package ch.bbw.zork;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Room {

    private String name; // Kurzer Name des Raums
    private String description;
    private HashMap<String, Room> exits; // Speichert die Ausgänge nach Raumname
    private List<Item> items; // Liste für die Gegenstände im Raum

    public Room(String name, String description) {
        this.name = name;
        this.description = description;
        this.exits = new HashMap<>();
        this.items = new ArrayList<>(); // Initialisierung der Gegenstandsliste
    }

    // Methode zum Hinzufügen von Ausgängen mit Raumnamen
    public void setExits(Room... adjacentRooms) {
        for (Room room : adjacentRooms) {
            if (room != null) {
                exits.put(room.getName(), room); // Verwende den kurzen Namen als Schlüssel
            }
        }
    }

    // Methode zum Hinzufügen von Gegenständen zum Raum
    public void addItem(Item item) {
        items.add(item);
    }

    // Methode zum Abrufen der Gegenstandsliste als Beschreibung
    public String getItemsDescription() {
        if (items.isEmpty()) {
            return "There are no items here.";
        }
        StringBuilder itemsDescription = new StringBuilder("You see the following items: ");
        for (Item item : items) {
            itemsDescription.append(item.getName())
                    .append(" (Weight: ").append(item.getWeight()).append("), ");
        }
        itemsDescription.setLength(itemsDescription.length() - 2); // Entfernt das letzte Komma und Leerzeichen
        return itemsDescription.toString();
    }

    public String getName() {
        return name;
    }

    public String shortDescription() {
        return name; // Verwende nur den kurzen Namen für die Beschreibung
    }

    public String longDescription() {
        StringBuilder stringBuilder = new StringBuilder("You are in " + description + ".\n");
        stringBuilder.append(getItemsDescription()).append("\n");
        stringBuilder.append(exitString());
        return stringBuilder.toString();
    }

    // Aktualisierte Methode exitString(), um Ausgänge in neuer Zeile anzuzeigen
    private String exitString() {
        StringBuilder exitDesc = new StringBuilder("Exits:\n");
        for (String roomName : exits.keySet()) {
            exitDesc.append("- ").append(roomName).append("\n"); // Jeder Ausgang in neuer Zeile
        }
        return exitDesc.toString();
    }

    public Room nextRoom(String roomName) {
        return exits.get(roomName);
    }

    public Room getExit(String roomName) {
        return exits.get(roomName);
    }

    public String getDescription() {
        return description;
    }

    public String getExitString() {
        return exitString();
    }

    public Item getItemByName(String itemName) {
        for (Item item : items) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                return item;
            }
        }
        return null;
    }

    public void removeItem(Item item) {
        items.remove(item);
    }

    public Map<String, Room> getExits() {
        return exits;
    }
}
