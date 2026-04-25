package iscteiul.ista.battleship;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.util.List;

@DisplayName("Testes Unitários - Fleet & Ship Integration")
class FleetTest {

    private Fleet fleet;

    @BeforeEach
    void setUp() {
        fleet = new Fleet();
        // Garante que a lista está limpa
        if (fleet.getShips() != null) {
            fleet.getShips().clear();
        }
    }

    @Test
    @DisplayName("Garantir que os barcos têm posições antes de adicionar")
    void testShipFormation() {
        Ship s = Ship.buildShip("barca", Compass.NORTH, new Position(2, 2));

        assertNotNull(s, "O buildShip falhou em criar o barco.");
        assertFalse(s.getPositions().isEmpty(),
                "ERRO: As subclasses (ex: Barge) não estão a preencher a lista 'positions'!");
    }

    @Test
    @DisplayName("Adicionar barcos em posições seguras")
    void testAddShipSuccess() {
        // Usamos posições distantes para evitar o tooCloseTo
        Ship s1 = Ship.buildShip("barca", Compass.NORTH, new Position(0, 0));
        Ship s2 = Ship.buildShip("barca", Compass.NORTH, new Position(5, 5));

        assertTrue(fleet.addShip(s1), "Deveria adicionar a primeira barca.");
        assertTrue(fleet.addShip(s2), "Deveria adicionar a segunda barca.");
    }

    @Test
    @DisplayName("Validar shipAt com correção de Equals")
    void testShipAt() {
        Position p = new Position(3, 3);
        Ship s = Ship.buildShip("barca", Compass.NORTH, p);
        fleet.addShip(s);

        // O shipAt usa o método occupies() que agora usa .equals() na tua classe Ship
        IShip found = fleet.shipAt(new Position(3, 3));

        assertNotNull(found, "O shipAt devolveu null. Verifica se o occupies() está a comparar bem.");
        assertEquals(s, found);
    }

    @Test
    @DisplayName("Testar se o barco afunda corretamente")
    void testFloatingStatus() {
        Ship s = Ship.buildShip("barca", Compass.NORTH, new Position(1, 1));
        fleet.addShip(s);

        assertTrue(s.stillFloating(), "O barco deve começar a flutuar.");

        // Disparar em todas as posições
        for (IPosition pos : s.getPositions()) {
            pos.shoot();
        }

        assertFalse(s.stillFloating(), "O barco deveria ter afundado (stillFloating = false).");
        assertTrue(fleet.getFloatingShips().isEmpty(), "A lista de barcos flutuantes da Fleet deve estar vazia.");
    }

    @Test
    @DisplayName("Cobertura: Testar barcos fora do tabuleiro")
    void testOutOfBounds() {
        // Galeão no limite sul sai do tabuleiro (9+3 = 12)
        Ship s = Ship.buildShip("galeao", Compass.SOUTH, new Position(8, 8));
        assertFalse(fleet.addShip(s), "O Fleet não deve aceitar barcos que saem do tabuleiro.");
    }

    @Test
    @DisplayName("Imprimir Frota (Cobertura de Métodos Void)")
    void testPrintCoverage() {
        fleet.addShip(Ship.buildShip("barca", Compass.NORTH, new Position(0, 0)));
        assertDoesNotThrow(() -> {
            fleet.printStatus();
            fleet.printAllShips();
        }, "Os métodos de impressão lançaram exceção!");
    }
}
//Classe teste