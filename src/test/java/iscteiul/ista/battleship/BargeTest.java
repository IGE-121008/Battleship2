package iscteiul.ista.battleship;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BargeTest {

    @Test
    void testBargeSize() {
        Barge barge = new Barge(Compass.NORTH, new Position(0, 0));
        assertEquals(1, barge.getSize());
    }

    @Test
    void testBargeHasOnePosition() {
        Barge barge = new Barge(Compass.NORTH, new Position(2, 3));
        assertEquals(1, barge.getPositions().size());
    }

    @Test
    void testBargePositionValues() {
        Barge barge = new Barge(Compass.NORTH, new Position(5, 6));

        Position p = (Position) barge.getPositions().iterator().next();

        assertEquals(5, p.getRow());
        assertEquals(6, p.getColumn());
    }
}