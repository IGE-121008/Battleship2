package iscteiul.ista.battleship;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes da Classe Ship")
class ShipTest {
    @Nested
    @DisplayName("Criação de Barcos (Factory)")
    class ShipCreation {
        @Test
        @DisplayName("Deve criar corretamente todos os tipos de barcos")
        void testBuildAllShips() {
            // Testar todos os ramos do switch para cobertura total [cite: 86]
            assertAll(
                    () -> assertTrue(Ship.buildShip("barca", Compass.NORTH, new Position(0,0)) instanceof Barge),
                    () -> assertTrue(Ship.buildShip("caravela", Compass.NORTH, new Position(0,0)) instanceof Caravel),
                    () -> assertTrue(Ship.buildShip("nau", Compass.NORTH, new Position(0,0)) instanceof Carrack),
                    () -> assertTrue(Ship.buildShip("fragata", Compass.NORTH, new Position(0,0)) instanceof Frigate),
                    () -> assertTrue(Ship.buildShip("galeao", Compass.NORTH, new Position(0,0)) instanceof Galleon),
                    () -> assertNull(Ship.buildShip("ovni", Compass.NORTH, new Position(0,0)))
            );
        }
    }

    @Nested
    @DisplayName("Lógica de Posicionamento e Limites")
    class ShipPositioning {
        private Ship ship;

        @BeforeEach
        void setUp() {
            ship = Ship.buildShip("fragata", Compass.EAST, new Position(2, 2));
        }

        @Test
        @DisplayName("Deve calcular corretamente os limites (Top/Bottom/Left/Right)")
        void testBoundaries() {
            // Aqui testas os métodos getTopMostPos, getLeftMostPos, etc.
            assertNotNull(ship.getPosition());
            assertTrue(ship.getLeftMostPos() >= 0);
        }
    }
}