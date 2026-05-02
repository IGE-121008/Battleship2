package iscteiul.ista.battleship;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GalleonTest {

    @Test
    @DisplayName("Galleon has size five")
    void galleonHasSizeFive() {
        Galleon galleon = new Galleon(Compass.NORTH, new Position(5, 5));

        assertEquals(5, galleon.getSize());
    }

    @Test
    @DisplayName("Galleon facing north fills expected positions")
    void galleonFacingNorthFillsExpectedPositions() {
        Galleon galleon = new Galleon(Compass.NORTH, new Position(5, 5));

        assertEquals(5, galleon.getPositions().size());
        assertContainsPosition(galleon, 5, 5);
        assertContainsPosition(galleon, 5, 6);
        assertContainsPosition(galleon, 5, 7);
        assertContainsPosition(galleon, 6, 6);
        assertContainsPosition(galleon, 7, 6);
    }

    @Test
    @DisplayName("Galleon facing south fills expected positions")
    void galleonFacingSouthFillsExpectedPositions() {
        Galleon galleon = new Galleon(Compass.SOUTH, new Position(5, 5));

        assertEquals(5, galleon.getPositions().size());
        assertContainsPosition(galleon, 5, 5);
        assertContainsPosition(galleon, 6, 5);
        assertContainsPosition(galleon, 7, 4);
        assertContainsPosition(galleon, 7, 5);
        assertContainsPosition(galleon, 7, 6);
    }

    @Test
    @DisplayName("Galleon facing east fills expected positions")
    void galleonFacingEastFillsExpectedPositions() {
        Galleon galleon = new Galleon(Compass.EAST, new Position(5, 5));

        assertEquals(5, galleon.getPositions().size());
        assertContainsPosition(galleon, 5, 5);
        assertContainsPosition(galleon, 6, 3);
        assertContainsPosition(galleon, 6, 4);
        assertContainsPosition(galleon, 6, 5);
        assertContainsPosition(galleon, 7, 5);
    }

    @Test
    @DisplayName("Galleon facing west fills expected positions")
    void galleonFacingWestFillsExpectedPositions() {
        Galleon galleon = new Galleon(Compass.WEST, new Position(5, 5));

        assertEquals(5, galleon.getPositions().size());
        assertContainsPosition(galleon, 5, 5);
        assertContainsPosition(galleon, 6, 5);
        assertContainsPosition(galleon, 6, 6);
        assertContainsPosition(galleon, 6, 7);
        assertContainsPosition(galleon, 7, 5);
    }

    @Test
    @DisplayName("Galleon with null bearing throws AssertionError")
    void galleonWithNullBearingThrowsAssertionError() {
        assertThrows(
                AssertionError.class,
                () -> new Galleon(null, new Position(5, 5))
        );
    }

    private static void assertContainsPosition(Galleon galleon, int row, int column) {
        boolean found = galleon.getPositions()
                .stream()
                .anyMatch(position ->
                        position.getRow() == row &&
                                position.getColumn() == column
                );

        assertTrue(found, "Expected position (" + row + ", " + column + ") was not found.");
    }
}