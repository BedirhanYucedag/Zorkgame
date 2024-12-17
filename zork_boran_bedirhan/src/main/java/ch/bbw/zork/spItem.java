package ch.bbw.zork;

public class spItem extends Item {
    private String specialEffect; // Zusätzliche Eigenschaft für den Spezialeffekt

    public spItem(String name, String description, int weight, String specialEffect) {
        super(name, description, weight); // Ruft den Konstruktor der Superklasse auf
        this.specialEffect = specialEffect;
    }

    public String getSpecialEffect() {
        return specialEffect;
    }

    @Override
    public String getDescription() {
        return super.getDescription() + " This item has a special effect: " + specialEffect;
    }

    // Füge hier das geheime Manuskript hinzu
    public static spItem createSecretManuscript() {
        return new spItem("Manuskript", "Ein altes Manuskript mit geheimen Informationen.", 1, "Kann den Charakter vor Gefahren warnen.");
    }
}
