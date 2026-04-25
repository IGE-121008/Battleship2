package iscteiul.ista.battleship;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    @Test
    void testBoardCreation() {
        Board board = new Board();
        assertNotNull(board);
    }

    @Test
    void testMarkShotHitShowsX() {
        Board board = new Board();

        board.markShot(new Position(0, 0), true);

        String output = capturePrint(board::printVisual);

        assertTrue(output.contains("X"));
    }

    @Test
    void testMarkShotMissShowsO() {
        Board board = new Board();

        board.markShot(new Position(1, 1), false);

        String output = capturePrint(board::printVisual);

        assertTrue(output.contains("o"));
    }

    @Test
    void testClearRemovesShots() {
        Board board = new Board();

        board.markShot(new Position(0, 0), true);
        board.markShot(new Position(1, 1), false);

        board.clear();

        String output = capturePrint(board::printVisual);

        assertFalse(output.contains("X"));
        assertFalse(output.contains("o"));
    }

    @Test
    void testOpponentBoardShowsHitsAndMisses() {
        Board board = new Board();

        board.markShot(new Position(0, 0), true);
        board.markShot(new Position(1, 1), false);

        String output = capturePrint(board::printOpponentBoard);

        assertTrue(output.contains("X"));
        assertTrue(output.contains("o"));
    }

    @Test
    void testOpponentBoardPrintsOnlyHiddenCellsWhenNoShots() {
        Board board = new Board();
        Fleet fleet = new Fleet();

        fleet.addShip(new Barge(Compass.NORTH, new Position(0, 0)));
        board.placeFleet(fleet);

        String output = capturePrint(board::printOpponentBoard);

        assertTrue(output.contains("-"));
        assertFalse(output.contains("X"));
        assertFalse(output.contains("o"));
    }

    @Test
    void testPlaceFleetShowsBargeSymbol() {
        Board board = new Board();
        Fleet fleet = new Fleet();

        fleet.addShip(new Barge(Compass.NORTH, new Position(0, 0)));

        board.placeFleet(fleet);

        String output = capturePrint(board::printVisual);

        assertTrue(output.contains("B"));
    }

    @Test
    void testPlaceFleetPrintsSomethingOnVisualBoard() {
        Board board = new Board();
        Fleet fleet = new Fleet();

        fleet.addShip(new Caravel(Compass.NORTH, new Position(0, 0)));
        board.placeFleet(fleet);

        String output = capturePrint(board::printVisual);

        assertNotNull(output);
        assertFalse(output.isEmpty());
    }

    @Test
    void testPlaceFleetKeepsShotsVisible() {
        Board board = new Board();
        Fleet fleet = new Fleet();

        board.markShot(new Position(0, 0), true);
        board.markShot(new Position(1, 1), false);

        fleet.addShip(new Caravel(Compass.EAST, new Position(0, 0)));

        board.placeFleet(fleet);

        String output = capturePrint(board::printVisual);

        assertTrue(output.contains("X"));
        assertTrue(output.contains("o"));
    }

    private String capturePrint(Runnable action) {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        System.setOut(new PrintStream(output));

        try {
            action.run();
        } finally {
            System.setOut(originalOut);
        }

        return output.toString();
    }
}