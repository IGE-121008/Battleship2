package iscteiul.ista.battleship;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

@DisplayName("Testes da Classe Ship")
class FleetTest {
    private Fleet fleet;

    @BeforeEach
    void setUp() {
        fleet = new Fleet();
    }

    @Nested
    @DisplayName("Adição de Barcos e Colisões")
    class AddShipLogic {
        @Test
        @DisplayName("Não deve permitir barcos sobrepostos ou adjacentes")
        void testCollisionRisk() {
            Ship s1 = Ship.buildShip("barca", Compass.NORTH, new Position(1, 1));
            Ship s2 = Ship.buildShip("barca", Compass.NORTH, new Position(1, 1)); // Mesma posição

            fleet.addShip(s1);
            assertFalse(fleet.addShip(s2), "Deve falhar devido ao colisionRisk");
        }

        @Test
        @DisplayName("Não deve permitir barcos fora dos limites do tabuleiro")
        void testBoardBoundaries() {
            // Testar o método privado isInsideBoard através do addShip
            Ship out = Ship.buildShip("galeao", Compass.SOUTH, new Position(15, 15));
            assertFalse(fleet.addShip(out));
        }
    }

    @Nested
    @DisplayName("Consultas e Filtros")
    class FleetQueries {
        @Test
        @DisplayName("Deve filtrar corretamente barcos que ainda flutuam")
        void testGetFloatingShips() {
            Ship s = Ship.buildShip("barca", Compass.NORTH, new Position(0, 0));
            fleet.addShip(s);
            assertEquals(1, fleet.getFloatingShips().size());
            // Simular destruição
            s.getPositions().get(0).shoot();
            assertEquals(0, fleet.getFloatingShips().size());
        }
    }
}