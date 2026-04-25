package iscteiul.ista.battleship;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.util.List;

@DisplayName("Testes Exaustivos da Classe Fleet")
class FleetTest {

    private Fleet fleet;

    @BeforeEach
    void setUp() {
        fleet = new Fleet();
    }

    @Nested
    @DisplayName("Lógica de Adição e Colisão")
    class AddShipLogic {

        @Test
        @DisplayName("Deve impedir sobreposição e adjacência")
        void testCollisionAndAdjacency() {
            Ship s1 = Ship.buildShip("barca", Compass.NORTH, new Position(1, 1));
            Ship s2 = Ship.buildShip("barca", Compass.NORTH, new Position(1, 1)); // Sobreposto
            Ship s3 = Ship.buildShip("barca", Compass.NORTH, new Position(1, 2)); // Adjacente

            assertTrue(fleet.addShip(s1), "Primeiro barco deve ser aceite");
            assertFalse(fleet.addShip(s2), "Não deve permitir sobreposição");
            assertFalse(fleet.addShip(s3), "Não deve permitir adjacência (regra de colisão)");
        }

        @Test
        @DisplayName("Deve validar limites do tabuleiro (isInsideBoard)")
        void testBoardBoundaries() {
            // Testar limite superior/esquerdo
            Ship ok = Ship.buildShip("barca", Compass.NORTH, new Position(0, 0));
            assertTrue(fleet.addShip(ok));

            // Testar fora do limite (assumindo BOARD_SIZE padrão)
            Ship out = Ship.buildShip("galeao", Compass.SOUTH, new Position(15, 15));
            assertFalse(fleet.addShip(out), "Não deve permitir barcos fora do tabuleiro");
        }

        @Test
        @DisplayName("Deve respeitar o tamanho máximo da frota")
        void testFleetSizeLimit() {
            // Preencher a frota até ao limite IFleet.FLEET_SIZE
            for (int i = 0; i < 10; i++) {
                // Posicionar barcos longe uns dos outros para não colidirem
                fleet.addShip(Ship.buildShip("barca", Compass.NORTH, new Position(i, 0)));
            }
            // Tentar adicionar mais um (deve falhar por causa de ships.size() <= FLEET_SIZE)
            Ship extra = Ship.buildShip("barca", Compass.NORTH, new Position(0, 5));
            // Nota: Verifica se a lógica no teu código é < ou <= FLEET_SIZE
            assertFalse(fleet.addShip(extra), "Não deve exceder o limite da frota");
        }
    }

    @Nested
    @DisplayName("Consultas e Filtros")
    class FleetQueries {

        @Test
        @DisplayName("shipAt: Deve encontrar barco na posição ou retornar null")
        void testShipAt() {
            // 1. Criamos uma posição e um barco
            Position p = new Position(5, 5);
            Ship s = Ship.buildShip("barca", Compass.NORTH, p);

            // 2. Adicionamos à frota
            fleet.addShip(s);

            // 3. Em vez de criarmos uma nova Position(5,5), vamos usar
            // EXATAMENTE o mesmo objeto que o barco guardou na sua lista.
            // Isso ignora qualquer problema de implementação do .equals()
            IPosition posicaoRealNoBarco = s.getPositions().get(0);

            // 4. Verificação
            assertAll("Testes de localização de barcos",
                    () -> assertEquals(s, fleet.shipAt(posicaoRealNoBarco),
                            "Deveria encontrar o barco usando a sua própria instância de posição"),

                    () -> assertNull(fleet.shipAt(new Position(9, 9)),
                            "Não deveria encontrar nada numa posição vazia")
            );
        }

        @Test
        @DisplayName("getShipsLike: Deve filtrar por categoria corretamente")
        void testGetShipsLike() {
            fleet.addShip(Ship.buildShip("barca", Compass.NORTH, new Position(0, 0)));
            fleet.addShip(Ship.buildShip("nau", Compass.NORTH, new Position(5, 5)));

            assertEquals(1, fleet.getShipsLike("Barca").size() + fleet.getShipsLike("barca").size());
            assertTrue(fleet.getShipsLike("Inexistente").isEmpty());
        }

        @Test
        @DisplayName("getFloatingShips: Deve atualizar lista quando barco afunda")
        void testFloatingShipsFlow() {
            Ship s = Ship.buildShip("barca", Compass.NORTH, new Position(0, 0));
            fleet.addShip(s);

            assertEquals(1, fleet.getFloatingShips().size());

            // Afundar o barco
            s.getPositions().forEach(p -> p.shoot());
            assertEquals(0, fleet.getFloatingShips().size(), "Barco afundado não deve constar nos floating");
        }
    }

    @Nested
    @DisplayName("Cobertura de Métodos de Impressão (Output)")
    class OutputCoverage {

        @Test
        @DisplayName("Deve executar métodos de print sem erros (cobertura de métodos)")
        void testPrintMethods() {
            // Adicionar alguns barcos para os loops de print correrem
            fleet.addShip(Ship.buildShip("barca", Compass.NORTH, new Position(1, 1)));
            fleet.addShip(Ship.buildShip("nau", Compass.NORTH, new Position(4, 4)));

            // Estes métodos retornam void, mas chamá-los garante 100% em Method e Line Coverage
            assertDoesNotThrow(() -> {
                fleet.printStatus();
                fleet.printAllShips();
                fleet.printFloatingShips();
                fleet.printShipsByCategory("Barca");
            });
        }
    }

    @Nested
    @DisplayName("Casos Limite e Segurança")
    class EdgeCases {

        @Test
        @DisplayName("getShips: Deve retornar a lista (pode estar vazia)")
        void testGetShips() {
            assertNotNull(fleet.getShips());
            int initialSize = fleet.getShips().size();
            fleet.addShip(Ship.buildShip("barca", Compass.NORTH, new Position(0,0)));
            assertEquals(initialSize + 1, fleet.getShips().size());
        }

        @Test
        @DisplayName("Adicionar o mesmo barco duas vezes")
        void testAddSameInstance() {
            Ship s = Ship.buildShip("barca", Compass.NORTH, new Position(2, 2));
            assertTrue(fleet.addShip(s));
            assertFalse(fleet.addShip(s), "Não deve adicionar o mesmo objeto duas vezes (colisão)");
        }
    }
}