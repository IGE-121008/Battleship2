package iscteiul.ista.battleship;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @Test
    @DisplayName("New game starts with empty shots and zero counters")
    void newGameStartsWithEmptyShotsAndZeroCounters() {
        Game game = new Game(emptyFleet(1));

        assertTrue(game.getShots().isEmpty());
        assertEquals(0, game.getInvalidShots());
        assertEquals(0, game.getRepeatedShots());
        assertEquals(0, game.getHits());
        assertEquals(0, game.getSunkShips());
        assertNotNull(game.getBoard());
    }

    @Test
    @DisplayName("Invalid shots increment invalid shot counter and are not stored")
    void invalidShotsIncrementInvalidShotCounterAndAreNotStored() {
        Game game = new Game(emptyFleet(1));

        game.fire(new Position(-1, 0));
        game.fire(new Position(Fleet.BOARD_SIZE, 0));
        game.fire(new Position(0, -1));
        game.fire(new Position(0, Fleet.BOARD_SIZE));

        assertEquals(4, game.getInvalidShots());
        assertTrue(game.getShots().isEmpty());
        assertEquals(0, game.getHits());
        assertEquals(0, game.getRepeatedShots());
    }

    @Test
    @DisplayName("Valid shot that misses is stored but does not count as hit")
    void validShotThatMissesIsStoredButDoesNotCountAsHit() {
        Game game = new Game(emptyFleet(1));
        Position shot = new Position(0, 0);

        IShip result = game.fire(shot);

        assertNull(result);
        assertEquals(1, game.getShots().size());
        assertEquals(shot, game.getShots().get(0));
        assertEquals(0, game.getInvalidShots());
        assertEquals(0, game.getRepeatedShots());
        assertEquals(0, game.getHits());
        assertEquals(0, game.getSunkShips());
    }

    @Test
    @DisplayName("Repeated valid shot increments repeated counter and is not stored twice")
    void repeatedValidShotIncrementsRepeatedCounterAndIsNotStoredTwice() {
        Game game = new Game(emptyFleet(1));
        Position shot = new Position(0, 0);

        game.fire(shot);
        game.fire(new Position(0, 0));

        assertEquals(1, game.getShots().size());
        assertEquals(1, game.getRepeatedShots());
        assertEquals(0, game.getInvalidShots());
    }

    @Test
    @DisplayName("Shot that hits a floating ship increments hit counter but does not sink")
    void shotThatHitsFloatingShipIncrementsHitCounterButDoesNotSink() {
        Position target = new Position(2, 2);
        AtomicInteger shipShots = new AtomicInteger(0);
        IShip ship = testShip(List.of(target), false, shipShots);
        Game game = new Game(fleetWithShipAt(ship, target, 1));

        IShip result = game.fire(new Position(2, 2));

        assertNull(result);
        assertEquals(1, shipShots.get());
        assertEquals(1, game.getHits());
        assertEquals(0, game.getSunkShips());
        assertEquals(1, game.getShots().size());
    }

    @Test
    @DisplayName("Shot that sinks a ship returns the sunk ship and increments sink counter")
    void shotThatSinksShipReturnsSunkShipAndIncrementsSinkCounter() {
        Position target = new Position(3, 3);
        AtomicInteger shipShots = new AtomicInteger(0);
        IShip ship = testShip(List.of(target), true, shipShots);
        Game game = new Game(fleetWithShipAt(ship, target, 0));

        IShip result = game.fire(new Position(3, 3));

        assertSame(ship, result);
        assertEquals(1, shipShots.get());
        assertEquals(1, game.getHits());
        assertEquals(1, game.getSunkShips());
    }

    @Test
    @DisplayName("Remaining ships and game over depend on floating ships")
    void remainingShipsAndGameOverDependOnFloatingShips() {
        Game gameWithRemainingShip = new Game(emptyFleet(1));
        Game gameWithoutRemainingShips = new Game(emptyFleet(0));

        assertEquals(1, gameWithRemainingShip.getRemainingShips());
        assertFalse(gameWithRemainingShip.isGameOver());

        assertEquals(0, gameWithoutRemainingShips.getRemainingShips());
        assertTrue(gameWithoutRemainingShips.isGameOver());
    }

    @Test
    @DisplayName("Was hit returns true when fleet has ship at position")
    void wasHitReturnsTrueWhenFleetHasShipAtPosition() {
        Position target = new Position(4, 4);
        IShip ship = testShip(List.of(target), false, new AtomicInteger(0));
        Game game = new Game(fleetWithShipAt(ship, target, 1));

        assertTrue(game.wasHit(new Position(4, 4)));
    }

    @Test
    @DisplayName("Was hit returns false when fleet has no ship at position")
    void wasHitReturnsFalseWhenFleetHasNoShipAtPosition() {
        Game game = new Game(emptyFleet(1));

        assertFalse(game.wasHit(new Position(4, 4)));
    }

    @Test
    @DisplayName("Print board prints marker at given positions")
    void printBoardPrintsMarkerAtGivenPositions() {
        Game game = new Game(emptyFleet(1));

        String output = captureOutput(() ->
                game.printBoard(List.of(new Position(0, 0), new Position(1, 2)), 'X')
        );

        String[] lines = output.strip().split("\\R");

        assertEquals(Fleet.BOARD_SIZE, lines.length);
        assertEquals('X', lines[0].charAt(0));
        assertEquals('X', lines[1].charAt(2));
        assertEquals('.', lines[0].charAt(1));
    }

    @Test
    @DisplayName("Print valid shots prints X for fired shots")
    void printValidShotsPrintsXForFiredShots() {
        Game game = new Game(emptyFleet(1));
        game.fire(new Position(0, 0));

        String output = captureOutput(game::printValidShots);

        String[] lines = output.strip().split("\\R");

        assertEquals('X', lines[0].charAt(0));
    }

    @Test
    @DisplayName("Print fleet prints # for ship positions")
    void printFleetPrintsMarkerForShipPositions() {
        Position shipPosition = new Position(0, 0);
        IShip ship = testShip(List.of(shipPosition), false, new AtomicInteger(0));
        Game game = new Game(fleetWithShipAt(ship, shipPosition, 1));

        String output = captureOutput(game::printFleet);

        String[] lines = output.strip().split("\\R");

        assertEquals('#', lines[0].charAt(0));
    }

    private static IFleet emptyFleet(int floatingShipsCount) {
        return fleet(Collections.emptyList(), null, null, floatingShipsCount);
    }

    private static IFleet fleetWithShipAt(IShip ship, IPosition target, int floatingShipsCount) {
        return fleet(List.of(ship), target, ship, floatingShipsCount);
    }

    private static IFleet fleet(List<IShip> ships, IPosition target, IShip shipAtTarget, int floatingShipsCount) {
        List<IShip> floatingShips = new ArrayList<>();

        for (int i = 0; i < floatingShipsCount; i++) {
            if (i < ships.size()) {
                floatingShips.add(ships.get(i));
            } else {
                floatingShips.add(testShip(List.of(new Position(i, 0)), false, new AtomicInteger(0)));
            }
        }

        return (IFleet) Proxy.newProxyInstance(
                GameTest.class.getClassLoader(),
                new Class<?>[]{IFleet.class},
                (proxy, method, args) -> {
                    String methodName = method.getName();

                    if (methodName.equals("getShips")) {
                        return ships;
                    }

                    if (methodName.equals("getFloatingShips")) {
                        return floatingShips;
                    }

                    if (methodName.equals("shipAt")) {
                        IPosition shot = (IPosition) args[0];
                        return target != null && target.equals(shot) ? shipAtTarget : null;
                    }

                    if (methodName.equals("toString")) {
                        return "TestFleet";
                    }

                    if (methodName.equals("hashCode")) {
                        return System.identityHashCode(proxy);
                    }

                    if (methodName.equals("equals")) {
                        return proxy == args[0];
                    }

                    return defaultValue(method.getReturnType());
                }
        );
    }

    private static IShip testShip(List<IPosition> positions, boolean sinksAfterShot, AtomicInteger shotCounter) {
        return (IShip) Proxy.newProxyInstance(
                GameTest.class.getClassLoader(),
                new Class<?>[]{IShip.class},
                (proxy, method, args) -> {
                    String methodName = method.getName();

                    if (methodName.equals("getPositions")) {
                        return positions;
                    }

                    if (methodName.equals("shoot")) {
                        shotCounter.incrementAndGet();
                        return null;
                    }

                    if (methodName.equals("stillFloating")) {
                        if (shotCounter.get() == 0) {
                            return true;
                        }
                        return !sinksAfterShot;
                    }

                    if (methodName.equals("getSize")) {
                        return positions.size();
                    }

                    if (methodName.equals("getName")) {
                        return "TestShip";
                    }

                    if (methodName.equals("getCategory")) {
                        return "galleon";
                    }

                    if (methodName.equals("toString")) {
                        return "TestShip";
                    }

                    if (methodName.equals("hashCode")) {
                        return System.identityHashCode(proxy);
                    }

                    if (methodName.equals("equals")) {
                        return proxy == args[0];
                    }

                    return defaultValue(method.getReturnType());
                }
        );
    }

    private static Object defaultValue(Class<?> returnType) {
        if (returnType.equals(boolean.class)) {
            return false;
        }

        if (returnType.equals(int.class)) {
            return 0;
        }

        if (returnType.equals(Integer.class)) {
            return 0;
        }

        if (returnType.equals(void.class)) {
            return null;
        }

        if (List.class.isAssignableFrom(returnType)) {
            return Collections.emptyList();
        }

        return null;
    }

    private static String captureOutput(Runnable runnable) {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            System.setOut(new PrintStream(outputStream));
            runnable.run();
            return outputStream.toString();
        } finally {
            System.setOut(originalOut);
        }
    }
}