package iscteiul.ista.battleship;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.util.List;

@DisplayName("Testes Fleet - versão estável")
class FleetTest {

    private Fleet fleet;

    @BeforeEach
    void setUp() {
        fleet = new Fleet();
    }

    @Nested
    class AddShipTests {

        @Test
        void addSingleShip() {
            Ship s = Ship.buildShip(ShipType.BARCA, Compass.NORTH, new Position(1, 1));

            assertTrue(fleet.addShip(s));
            assertEquals(1, fleet.getShips().size());
        }

        @Test
        void preventOverlap() {
            Ship s1 = Ship.buildShip(ShipType.BARCA, Compass.NORTH, new Position(2, 2));
            Ship s2 = Ship.buildShip(ShipType.BARCA, Compass.NORTH, new Position(2, 2));

            assertTrue(fleet.addShip(s1));
            assertFalse(fleet.addShip(s2));
        }

        @Test
        void allowDifferentPositions() {
            Ship s1 = Ship.buildShip(ShipType.BARCA, Compass.NORTH, new Position(0, 0));
            Ship s2 = Ship.buildShip(ShipType.BARCA, Compass.NORTH, new Position(5, 5));

            assertTrue(fleet.addShip(s1));
            assertTrue(fleet.addShip(s2));

            assertEquals(2, fleet.getShips().size());
        }

        @Test
        void rejectOutOfBounds() {
            Ship s = Ship.buildShip(ShipType.GALEAO, Compass.SOUTH, new Position(9, 9));

            assertFalse(fleet.addShip(s));
        }
    }

    @Nested
    class QueryTests {

        @Test
        void shipAtExists() {
            Ship s = Ship.buildShip(ShipType.BARCA, Compass.NORTH, new Position(3, 3));
            fleet.addShip(s);

            IShip found = fleet.shipAt(new Position(3, 3));

            assertNotNull(found);
            assertEquals(s, found);
        }

        @Test
        void shipAtEmpty() {
            assertNull(fleet.shipAt(new Position(0, 0)));
        }

        @Test
        void getShipsLikeCaseInsensitive() {
            fleet.addShip(Ship.buildShip(ShipType.BARCA, Compass.NORTH, new Position(0, 0)));

            List<IShip> result = fleet.getShipsLike("BARCA");

            assertFalse(result.isEmpty());
        }
    }

    @Nested
    class ShipStateTests {

        @Test
        void shipStartsFloating() {
            Ship s = Ship.buildShip(ShipType.BARCA, Compass.NORTH, new Position(1, 1));
            fleet.addShip(s);

            assertTrue(s.stillFloating());
        }

        @Test
        void shipSinksAfterHits() {
            Ship s = Ship.buildShip(ShipType.BARCA, Compass.NORTH, new Position(1, 1));
            fleet.addShip(s);

            for (IPosition p : s.getPositions()) {
                p.shoot();
            }

            assertFalse(s.stillFloating());
            assertTrue(fleet.getFloatingShips().isEmpty());
        }
    }

    @Nested
    class PrintTests {

        @Test
        void printsDoNotCrash() {
            fleet.addShip(Ship.buildShip(ShipType.BARCA, Compass.NORTH, new Position(1, 1)));

            assertDoesNotThrow(() -> {
                fleet.printStatus();
                fleet.printAllShips();
                fleet.printFloatingShips();
            });
        }
    }
}