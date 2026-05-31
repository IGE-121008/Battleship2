/**
 *
 */
package iscteiul.ista.battleship;

/**
 * Enum for ship types.
 */
public enum ShipType {
    BARCA("barca"),
    CARAVELA("caravela"),
    NAU("nau"),
    FRAGATA("fragata"),
    GALEAO("galeao");

    private final String name;

    ShipType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static ShipType fromString(String name) {
        for (ShipType type : values()) {
            if (type.name.equals(name)) {
                return type;
            }
        }
        return null;
    }
}
