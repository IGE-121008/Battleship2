package iscteiul.ista.battleship;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FrigateTest {

    @Test
    @DisplayName("Frigate has size four")
    void frigateHasSizeFour() {
        Frigate frigate = new Frigate(Compass.NORTH, new Position(5, 5));

        assertEquals(4, frigate.getSize());
    }

    @Test
    @DisplayName("Frigate facing north fills expected vertical positions")
    void frigateFacingNorthFillsExpectedVerticalPositions() {
        Frigate frigate = new Frigate(Compass.NORTH, new Position(5, 5));

        assertEquals(4, frigate.getPositions().size());
        assertContainsPosition(frigate, 5, 5);
        assertContainsPosition(frigate, 6, 5);
        assertContainsPosition(frigate, 7, 5);
        assertContainsPosition(frigate, 8, 5);
    }

    @Test
    @DisplayName("Frigate facing south fills expected vertical positions")
    void frigateFacingSouthFillsExpectedVerticalPositions() {
        Frigate frigate = new Frigate(Compass.SOUTH, new Position(5, 5));

        assertEquals(4, frigate.getPositions().size());
        assertContainsPosition(frigate, 5, 5);
        assertContainsPosition(frigate, 6, 5);
        assertContainsPosition(frigate, 7, 5);
        assertContainsPosition(frigate, 8, 5);
    }

    @Test
    @DisplayName("Frigate facing east fills expected horizontal positions")
    void frigateFacingEastFillsExpectedHorizontalPositions() {
        Frigate frigate = new Frigate(Compass.EAST, new Position(5, 5));

        assertEquals(4, frigate.getPositions().size());
        assertContainsPosition(frigate, 5, 5);
        assertContainsPosition(frigate, 5, 6);
        assertContainsPosition(frigate, 5, 7);
        assertContainsPosition(frigate, 5, 8);
    }

    @Test
    @DisplayName("Frigate facing west fills expected horizontal positions")
    void frigateFacingWestFillsExpectedHorizontalPositions() {
        Frigate frigate = new Frigate(Compass.WEST, new Position(5, 5));

        assertEquals(4, frigate.getPositions().size());
        assertContainsPosition(frigate, 5, 5);
        assertContainsPosition(frigate, 5, 6);
        assertContainsPosition(frigate, 5, 7);
        assertContainsPosition(frigate, 5, 8);
    }

    @Test
    @DisplayName("Frigate with null bearing throws AssertionError")
    void frigateWithNullBearingThrowsAssertionError() {
        assertThrows(
                AssertionError.class,
                () -> new Frigate(null, new Position(5, 5))
        );
    }

    private static void assertContainsPosition(Frigate frigate, int row, int column) {
        boolean found = frigate.getPositions()
                .stream()
                .anyMatch(position ->
                        position.getRow() == row &&
                                position.getColumn() == column
                );

        assertTrue(found, "Expected position (" + row + ", " + column + ") was not found.");
    }
}