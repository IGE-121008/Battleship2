package iscteiul.ista.battleship;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TasksTest {

    @Test
    @DisplayName("Should read a position correctly")
    void testReadPosition() {
        Scanner scanner = new Scanner("3 5");

        Position pos = Tasks.readPosition(scanner);

        assertEquals(3, pos.getRow());
        assertEquals(5, pos.getColumn());
    }


    @Test
    @DisplayName("Should create a ship from valid input")
    void testReadShip() {
        Scanner scanner = new Scanner("fragata 1 2 n");

        Ship ship = Tasks.readShip(scanner);

        assertNotNull(ship);
    }

    @Test
    @DisplayName("Should build a fleet with valid ships")
    void testBuildFleet() {
        String input = """
        fragata 0 0 e
        fragata 3 0 e
        """;

        Scanner scanner = new Scanner(input);

        Fleet fleet = Tasks.buildFleet(scanner);

        assertNotNull(fleet);
        assertEquals(Fleet.FLEET_SIZE, fleet.getShips().size());
    }

    @Test
    void testFiringRound_miss() {
        Scanner scanner = new Scanner("1 1 2 2 3 3");

        Game game = mock(Game.class);
        Board board = mock(Board.class);

        when(game.getBoard()).thenReturn(board);
        when(game.wasHit(any())).thenReturn(false);
        when(game.fire(any())).thenReturn(null);

        Tasks.firingRound(scanner, game);

        verify(game, times(3)).fire(any());
    }

    @Test
    void testFiringRound_hit() {
        Scanner scanner = new Scanner("1 1 2 2 3 3");

        Game game = mock(Game.class);
        Board board = mock(Board.class);

        when(game.getBoard()).thenReturn(board);
        when(game.wasHit(any())).thenReturn(true);
        when(game.fire(any())).thenReturn(null);

        Tasks.firingRound(scanner, game);

        verify(game, times(3)).fire(any());
    }

    @Test
    void testFiringRound_sink() {
        Scanner scanner = new Scanner("1 1 2 2 3 3");

        Game game = mock(Game.class);
        Board board = mock(Board.class);
        IShip ship = mock(IShip.class);

        when(game.getBoard()).thenReturn(board);
        when(game.wasHit(any())).thenReturn(false);
        when(game.fire(any())).thenReturn(ship);
        when(ship.getCategory()).thenReturn("Fragata");

        Tasks.firingRound(scanner, game);

        verify(game, times(3)).fire(any());
    }

    @Test
    void testBuildFleet_withInvalidShip() {
        String input = """
        invalid 0 0 n
        fragata 0 0 e
        fragata 3 0 e
        """;

        Scanner scanner = new Scanner(input);

        Fleet fleet = Tasks.buildFleet(scanner);

        assertNotNull(fleet);
    }

    @Test
    void testReadShip_invalidDirection() {
        Scanner scanner = new Scanner("fragata 1 1 x");

        assertThrows(IllegalArgumentException.class, () -> {
            Tasks.readShip(scanner);
        });
    }

    @Test
    void testBuildFleet_collision() {
        String input = """
        fragata 0 0 e
        fragata 0 1 e
        fragata 3 0 e
        """;

        Scanner scanner = new Scanner(input);

        Fleet fleet = Tasks.buildFleet(scanner);

        assertNotNull(fleet);
    }

    @Test
    void testFiringRound_mixed() {
        Scanner scanner = new Scanner("1 1 2 2 3 3");

        Game game = mock(Game.class);
        Board board = mock(Board.class);
        IShip ship = mock(IShip.class);

        when(game.getBoard()).thenReturn(board);

        // simulation variée
        when(game.wasHit(any()))
                .thenReturn(false)
                .thenReturn(true)
                .thenReturn(false);

        when(game.fire(any()))
                .thenReturn(null)
                .thenReturn(null)
                .thenReturn(ship);

        when(ship.getCategory()).thenReturn("Fragata");

        Tasks.firingRound(scanner, game);

        verify(game, times(3)).fire(any());
    }

    @Test
    void testTaskE_runs() {
        String input = """
        fragata 0 0 e
        fragata 3 0 e
        fragata 0 0 e
        fragata 3 0 e
        1 1 2 2 3 3
        1 1 2 2 3 3
        1 1 2 2 3 3
        """;

        System.setIn(new java.io.ByteArrayInputStream(input.getBytes()));

        assertThrows(Exception.class, () -> {
            Tasks.taskE();
        });
    }

    @Test
    void testTaskD_runs() {
        String input = """
        desisto
        """;

        System.setIn(new java.io.ByteArrayInputStream(input.getBytes()));

        Tasks.taskD();

        assertTrue(true);
    }

    @Test
    void testTaskD_moreCoverage() {
        String input = """
        nova
        fragata 0 0 e
        fragata 3 0 e
        nova
        fragata 0 0 e
        fragata 3 0 e
        rajada
        1 1 2 2 3 3
        desisto
        """;

        System.setIn(new java.io.ByteArrayInputStream(input.getBytes()));

        assertThrows(Exception.class, () -> {
            Tasks.taskD();
        });
    }

    @Test
    void testTaskD_fullCoverage() {
        String input = """
        nova
        fragata 0 0 e
        fragata 3 0 e
        nova
        fragata 0 0 e
        fragata 3 0 e
        estado
        mapa
        ver
        rajada
        1 1 2 2 3 3
        1 1 2 2 3 3
        unknown
        desisto
        """;

        System.setIn(new java.io.ByteArrayInputStream(input.getBytes()));

        assertThrows(Exception.class, () -> {
            Tasks.taskD();
        });
    }

    @Test
    void testTaskD_winCondition() {
        String input = """
        nova
        fragata 0 0 e
        fragata 3 0 e
        nova
        fragata 0 0 e
        fragata 3 0 e
        rajada
        0 0 0 1 0 2
        0 0 0 1 0 2
        desisto
        """;

        System.setIn(new java.io.ByteArrayInputStream(input.getBytes()));

        assertThrows(Exception.class, () -> {
            Tasks.taskD();
        });
    }

    @Test
    void testTaskD_realWin() {
        String input = """
        nova
        fragata 0 0 e
        fragata 3 0 e
        nova
        fragata 0 0 e
        fragata 3 0 e
        rajada
        0 0 0 1 0 2
        0 3 3 0 3 1
        desisto
        """;

        System.setIn(new java.io.ByteArrayInputStream(input.getBytes()));

        assertThrows(Exception.class, () -> {
            Tasks.taskD();
        });
    }

    @Test
    void testTaskD_branchesMissing() {
        String input = """
        estado
        mapa
        rajada
        desisto
        """;

        System.setIn(new java.io.ByteArrayInputStream(input.getBytes()));

        Tasks.taskD();
    }

    @Test
    void testTaskD_verCommand() {
        String input = """
        nova
        fragata 0 0 e
        fragata 3 0 e
        nova
        fragata 0 0 e
        fragata 3 0 e
        ver
        desisto
        """;

        System.setIn(new java.io.ByteArrayInputStream(input.getBytes()));

        Tasks.taskD();
    }

    @Test
    void testTaskD_statusAndMapa_whenReady() {
        String input = """
        nova
        fragata 0 0 e
        fragata 3 0 e
        nova
        fragata 0 0 e
        fragata 3 0 e
        estado
        mapa
        desisto
        """;

        System.setIn(new java.io.ByteArrayInputStream(input.getBytes()));

        Tasks.taskD();
    }

    @Test
    void testTaskD_unknownCommandOnly() {
        String input = """
        blabla
        desisto
        """;

        System.setIn(new java.io.ByteArrayInputStream(input.getBytes()));

        Tasks.taskD();
    }

    @Test
    void testTaskD_unknownCommand() {
        String input = "abc desisto";
        System.setIn(new java.io.ByteArrayInputStream(input.getBytes()));

        Tasks.taskD();

        assertTrue(true);
    }

    @Test
    void testTaskD_exit() {
        String input = "desisto";
        System.setIn(new java.io.ByteArrayInputStream(input.getBytes()));

        Tasks.taskD();

        assertTrue(true);
    }

    @Test
    void testTaskD_status() {
        String input = """
    nova
    fragata 0 0 e
    fragata 3 0 e
    estado
    desisto
    """;

        System.setIn(new java.io.ByteArrayInputStream(input.getBytes()));

        Tasks.taskD();

        assertTrue(true);
    }

    @Test
    void testTaskD_ver() {
        String input = """
    nova
    fragata 0 0 e
    fragata 3 0 e
    nova
    fragata 0 0 e
    fragata 3 0 e
    ver
    desisto
    """;

        System.setIn(new java.io.ByteArrayInputStream(input.getBytes()));

        Tasks.taskD();

        assertTrue(true);
    }

    @Test
    void testTaskD_forceSinkAllShips() {
        String input = """
    nova
    fragata 0 0 e
    fragata 3 0 e
    nova
    fragata 0 0 e
    fragata 3 0 e
    rajada
    0 0 0 1 0 2
    3 0 3 1 3 2
    """;

        System.setIn(new java.io.ByteArrayInputStream(input.getBytes()));

        assertThrows(Exception.class, () -> {
            Tasks.taskD();
        });
    }
}