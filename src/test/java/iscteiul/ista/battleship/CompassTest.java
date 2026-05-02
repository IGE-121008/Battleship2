package iscteiul.ista.battleship;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CompassTest {

    @Test
    @DisplayName("Each compass direction has the expected character")
    void eachCompassDirectionHasExpectedCharacter() {
        assertEquals('n', Compass.NORTH.getDirection());
        assertEquals('s', Compass.SOUTH.getDirection());
        assertEquals('e', Compass.EAST.getDirection());
        assertEquals('o', Compass.WEST.getDirection());
        assertEquals('u', Compass.UNKNOWN.getDirection());
    }

    @Test
    @DisplayName("Compass toString returns direction character as string")
    void compassToStringReturnsDirectionCharacterAsString() {
        assertEquals("n", Compass.NORTH.toString());
        assertEquals("s", Compass.SOUTH.toString());
        assertEquals("e", Compass.EAST.toString());
        assertEquals("o", Compass.WEST.toString());
        assertEquals("u", Compass.UNKNOWN.toString());
    }

    @Test
    @DisplayName("Character n converts to NORTH")
    void characterNConvertsToNorth() {
        assertEquals(Compass.NORTH, Compass.charToCompass('n'));
    }

    @Test
    @DisplayName("Character s converts to SOUTH")
    void characterSConvertsToSouth() {
        assertEquals(Compass.SOUTH, Compass.charToCompass('s'));
    }

    @Test
    @DisplayName("Character e converts to EAST")
    void characterEConvertsToEast() {
        assertEquals(Compass.EAST, Compass.charToCompass('e'));
    }

    @Test
    @DisplayName("Character o converts to WEST")
    void characterOConvertsToWest() {
        assertEquals(Compass.WEST, Compass.charToCompass('o'));
    }

    @Test
    @DisplayName("Unknown character converts to UNKNOWN")
    void unknownCharacterConvertsToUnknown() {
        assertEquals(Compass.UNKNOWN, Compass.charToCompass('x'));
        assertEquals(Compass.UNKNOWN, Compass.charToCompass('N'));
        assertEquals(Compass.UNKNOWN, Compass.charToCompass(' '));
    }

    @Test
    @DisplayName("Compass enum contains five values")
    void compassEnumContainsFiveValues() {
        assertArrayEquals(
                new Compass[]{
                        Compass.NORTH,
                        Compass.SOUTH,
                        Compass.EAST,
                        Compass.WEST,
                        Compass.UNKNOWN
                },
                Compass.values()
        );
    }
}
