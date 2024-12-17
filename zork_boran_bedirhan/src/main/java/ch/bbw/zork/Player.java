package ch.bbw.zork;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private List<Item> inventory; // Inventar des Spielers
    private int maxWeight; // Maximales Tragegewicht
    private int currentWeight; // Aktuelles Gewicht der Gegenstände im Inventar

    public Player(int maxWeight) {
        this.inventory = new ArrayList<>();
        this.maxWeight = maxWeight;
        this.currentWeight = 0;
    }

    public boolean addItem(Item item) {
        if (currentWeight + item.getWeight() <= maxWeight) {
            inventory.add(item);
            currentWeight += item.getWeight();
            return true;
        } else {
            System.out.println("You can't carry this item, it's too heavy.");
            return false;
        }
    }

    public boolean removeItem(Item item) {
        if (inventory.remove(item)) {
            currentWeight -= item.getWeight();
            return true;
        } else {
            return false;
        }
    }

    public String getInventoryDescription() {
        if (inventory.isEmpty()) {
            return "Your inventory is empty.";
        }
        StringBuilder description = new StringBuilder("You are carrying: ");
        for (Item item : inventory) {
            description.append(item.getName()).append(" (Weight: ").append(item.getWeight()).append("), ");
        }
        return description.toString();
    }

    public Item getItemByName(String name) {
        for (Item item : inventory) {
            if (item.getName().equalsIgnoreCase(name)) {
                return item;
            }
        }
        return null;
    }
    public boolean hasItem(String itemName) {
        for (Item item : inventory) { // inventory ist die Liste der Items im Inventar des Spielers
            if (item.getName().equalsIgnoreCase(itemName)) {
                return true;
            }
        }
        return false;
    }


}
