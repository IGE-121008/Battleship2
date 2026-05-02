package iscteiul.ista.battleship;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PositionTest {

    @Test
    @DisplayName("Position stores row and column")
    void positionStoresRowAndColumn() {
        Position position = new Position(3, 7);

        assertEquals(3, position.getRow());
        assertEquals(7, position.getColumn());
    }

    @Test
    @DisplayName("New position starts not occupied and not hit")
    void newPositionStartsNotOccupiedAndNotHit() {
        Position position = new Position(1, 2);

        assertFalse(position.isOccupied());
        assertFalse(position.isHit());
    }

    @Test
    @DisplayName("Occupy marks position as occupied")
    void occupyMarksPositionAsOccupied() {
        Position position = new Position(1, 2);

        position.occupy();

        assertTrue(position.isOccupied());
    }

    @Test
    @DisplayName("Shoot marks position as hit")
    void shootMarksPositionAsHit() {
        Position position = new Position(1, 2);

        position.shoot();

        assertTrue(position.isHit());
    }

    @Test
    @DisplayName("Position equals itself")
    void positionEqualsItself() {
        Position position = new Position(4, 5);

        assertEquals(position, position);
    }

    @Test
    @DisplayName("Positions with same row and column are equal")
    void positionsWithSameRowAndColumnAreEqual() {
        Position position1 = new Position(4, 5);
        Position position2 = new Position(4, 5);

        assertEquals(position1, position2);
    }

    @Test
    @DisplayName("Positions with different row are not equal")
    void positionsWithDifferentRowAreNotEqual() {
        Position position1 = new Position(4, 5);
        Position position2 = new Position(6, 5);

        assertNotEquals(position1, position2);
    }

    @Test
    @DisplayName("Positions with different column are not equal")
    void positionsWithDifferentColumnAreNotEqual() {
        Position position1 = new Position(4, 5);
        Position position2 = new Position(4, 6);

        assertNotEquals(position1, position2);
    }

    @Test
    @DisplayName("Position is not equal to null")
    void positionIsNotEqualToNull() {
        Position position = new Position(4, 5);

        assertNotEquals(null, position);
    }

    @Test
    @DisplayName("Position is not equal to object of another type")
    void positionIsNotEqualToObjectOfAnotherType() {
        Position position = new Position(4, 5);

        assertNotEquals("not a position", position);
    }

    @Test
    @DisplayName("Position can equal another IPosition implementation with same coordinates")
    void positionCanEqualAnotherIPositionImplementationWithSameCoordinates() {
        Position position = new Position(4, 5);

        IPosition other = new IPosition() {
            @Override
            public int getRow() {
                return 4;
            }

            @Override
            public int getColumn() {
                return 5;
            }

            @Override
            public boolean isAdjacentTo(IPosition other) {
                return false;
            }

            @Override
            public void occupy() {
            }

            @Override
            public void shoot() {
            }

            @Override
            public boolean isOccupied() {
                return false;
            }

            @Override
            public boolean isHit() {
                return false;
            }
        };

        assertEquals(position, other);
    }

    @Test
    @DisplayName("Adjacent position returns true")
    void adjacentPositionReturnsTrue() {
        Position position = new Position(4, 5);
        Position adjacent = new Position(5, 6);

        assertTrue(position.isAdjacentTo(adjacent));
    }

    @Test
    @DisplayName("Same position is considered adjacent")
    void samePositionIsConsideredAdjacent() {
        Position position = new Position(4, 5);
        Position same = new Position(4, 5);

        assertTrue(position.isAdjacentTo(same));
    }

    @Test
    @DisplayName("Position with distant row is not adjacent")
    void positionWithDistantRowIsNotAdjacent() {
        Position position = new Position(4, 5);
        Position distant = new Position(6, 5);

        assertFalse(position.isAdjacentTo(distant));
    }

    @Test
    @DisplayName("Position with distant column is not adjacent")
    void positionWithDistantColumnIsNotAdjacent() {
        Position position = new Position(4, 5);
        Position distant = new Position(4, 7);

        assertFalse(position.isAdjacentTo(distant));
    }

    @Test
    @DisplayName("Hash code can be calculated")
    void hashCodeCanBeCalculated() {
        Position position = new Position(4, 5);

        assertDoesNotThrow(position::hashCode);
    }

    @Test
    @DisplayName("To string returns row and column")
    void toStringReturnsRowAndColumn() {
        Position position = new Position(4, 5);

        assertEquals("Linha = 4 Coluna = 5", position.toString());
    }
}