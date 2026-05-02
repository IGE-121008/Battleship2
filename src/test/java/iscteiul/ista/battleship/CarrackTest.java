package iscteiul.ista.battleship;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CarrackTest {

    @Test
    @DisplayName("Carrack has size three")
    void carrackHasSizeThree() {
        Carrack carrack = new Carrack(Compass.NORTH, new Position(5, 5));

        assertEquals(3, carrack.getSize());
    }

    @Test
    @DisplayName("Carrack facing north fills expected vertical positions")
    void carrackFacingNorthFillsExpectedVerticalPositions() {
        Carrack carrack = new Carrack(Compass.NORTH, new Position(5, 5));

        assertEquals(3, carrack.getPositions().size());
        assertContainsPosition(carrack, 5, 5);
        assertContainsPosition(carrack, 6, 5);
        assertContainsPosition(carrack, 7, 5);
    }

    @Test
    @DisplayName("Carrack facing south fills expected vertical positions")
    void carrackFacingSouthFillsExpectedVerticalPositions() {
        Carrack carrack = new Carrack(Compass.SOUTH, new Position(5, 5));

        assertEquals(3, carrack.getPositions().size());
        assertContainsPosition(carrack, 5, 5);
        assertContainsPosition(carrack, 6, 5);
        assertContainsPosition(carrack, 7, 5);
    }

    @Test
    @DisplayName("Carrack facing east fills expected horizontal positions")
    void carrackFacingEastFillsExpectedHorizontalPositions() {
        Carrack carrack = new Carrack(Compass.EAST, new Position(5, 5));

        assertEquals(3, carrack.getPositions().size());
        assertContainsPosition(carrack, 5, 5);
        assertContainsPosition(carrack, 5, 6);
        assertContainsPosition(carrack, 5, 7);
    }

    @Test
    @DisplayName("Carrack facing west fills expected horizontal positions")
    void carrackFacingWestFillsExpectedHorizontalPositions() {
        Carrack carrack = new Carrack(Compass.WEST, new Position(5, 5));

        assertEquals(3, carrack.getPositions().size());
        assertContainsPosition(carrack, 5, 5);
        assertContainsPosition(carrack, 5, 6);
        assertContainsPosition(carrack, 5, 7);
    }

    @Test
    @DisplayName("Carrack with null bearing throws AssertionError")
    void carrackWithNullBearingThrowsAssertionError() {
        assertThrows(
                AssertionError.class,
                () -> new Carrack(null, new Position(5, 5))
        );
    }

    private static void assertContainsPosition(Carrack carrack, int row, int column) {
        boolean found = carrack.getPositions()
                .stream()
                .anyMatch(position ->
                        position.getRow() == row &&
                                position.getColumn() == column
                );

        assertTrue(found, "Expected position (" + row + ", " + column + ") was not found.");
    }
}