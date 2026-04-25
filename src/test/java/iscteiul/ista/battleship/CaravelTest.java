package iscteiul.ista.battleship;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CaravelTest {

    @Test
    void testCaravelSize() {
        Caravel caravel = new Caravel(Compass.NORTH, new Position(0, 0));
        assertEquals(2, caravel.getSize());
    }

    @Test
    void testCaravelHasTwoPositions() {
        Caravel caravel = new Caravel(Compass.NORTH, new Position(2, 3));
        assertEquals(2, caravel.getPositions().size());
    }

    @Test
    void testCaravelNorthDirection() {
        Caravel caravel = new Caravel(Compass.NORTH, new Position(5, 6));
        List<IPosition> positions = caravel.getPositions().stream().toList();

        assertEquals(2, positions.size());
        assertEquals(positions.get(0).getColumn(), positions.get(1).getColumn());
        assertEquals(1, Math.abs(positions.get(0).getRow() - positions.get(1).getRow()));
    }

    @Test
    void testCaravelSouthDirection() {
        Caravel caravel = new Caravel(Compass.SOUTH, new Position(5, 6));
        List<IPosition> positions = caravel.getPositions().stream().toList();

        assertEquals(2, positions.size());
        assertEquals(positions.get(0).getColumn(), positions.get(1).getColumn());
        assertEquals(1, Math.abs(positions.get(0).getRow() - positions.get(1).getRow()));
    }

    @Test
    void testCaravelEastDirection() {
        Caravel caravel = new Caravel(Compass.EAST, new Position(5, 6));
        List<IPosition> positions = caravel.getPositions().stream().toList();

        assertEquals(2, positions.size());
        assertEquals(positions.get(0).getRow(), positions.get(1).getRow());
        assertEquals(1, Math.abs(positions.get(0).getColumn() - positions.get(1).getColumn()));
    }

    @Test
    void testCaravelWestDirection() {
        Caravel caravel = new Caravel(Compass.WEST, new Position(5, 6));
        List<IPosition> positions = caravel.getPositions().stream().toList();

        assertEquals(2, positions.size());
        assertEquals(positions.get(0).getRow(), positions.get(1).getRow());
        assertEquals(1, Math.abs(positions.get(0).getColumn() - positions.get(1).getColumn()));
    }

    @Test
    void testCaravelStartingPositionIsIncluded() {
        Caravel caravel = new Caravel(Compass.NORTH, new Position(7, 4));

        assertTrue(caravel.getPositions().stream()
                .anyMatch(p -> p.getRow() == 7 && p.getColumn() == 4));
    }

    @Test
    void testCaravelGetPositionsIsConsistent() {
        Caravel caravel = new Caravel(Compass.EAST, new Position(3, 5));

        assertEquals(caravel.getPositions().size(), caravel.getPositions().size());
        assertEquals(2, caravel.getPositions().size());
    }

    @Test
    void testCaravelDifferentStartingPosition() {
        Caravel caravel = new Caravel(Compass.SOUTH, new Position(10, 2));

        assertEquals(2, caravel.getPositions().size());
        assertTrue(caravel.getPositions().stream()
                .anyMatch(p -> p.getRow() == 10 && p.getColumn() == 2));
    }

    @Test
    void testCaravelRowsOrColumnsChangeCorrectlyNorthSouth() {
        Caravel northCaravel = new Caravel(Compass.NORTH, new Position(8, 3));
        Caravel southCaravel = new Caravel(Compass.SOUTH, new Position(8, 3));

        List<IPosition> northPositions = northCaravel.getPositions().stream().toList();
        List<IPosition> southPositions = southCaravel.getPositions().stream().toList();

        assertEquals(2, northPositions.stream().map(p -> p.getRow()).distinct().count());
        assertEquals(2, southPositions.stream().map(p -> p.getRow()).distinct().count());

        assertEquals(1, northPositions.stream().map(p -> p.getColumn()).distinct().count());
        assertEquals(1, southPositions.stream().map(p -> p.getColumn()).distinct().count());
    }

    @Test
    void testCaravelRowsOrColumnsChangeCorrectlyEastWest() {
        Caravel eastCaravel = new Caravel(Compass.EAST, new Position(8, 3));
        Caravel westCaravel = new Caravel(Compass.WEST, new Position(8, 3));

        List<IPosition> eastPositions = eastCaravel.getPositions().stream().toList();
        List<IPosition> westPositions = westCaravel.getPositions().stream().toList();

        assertEquals(1, eastPositions.stream().map(p -> p.getRow()).distinct().count());
        assertEquals(1, westPositions.stream().map(p -> p.getRow()).distinct().count());

        assertEquals(2, eastPositions.stream().map(p -> p.getColumn()).distinct().count());
        assertEquals(2, westPositions.stream().map(p -> p.getColumn()).distinct().count());
    }
}