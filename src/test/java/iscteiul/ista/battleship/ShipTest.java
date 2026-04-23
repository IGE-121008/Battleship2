package iscteiul.ista.battleship;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes Exaustivos da Classe Ship")
class ShipTest {

    private Ship ship;
    private IPosition pos;

    @BeforeEach
    void setUp() {
        // Usamos uma Fragata para ter várias posições e testar ciclos
        pos = new Position(2, 2);
        ship = Ship.buildShip("fragata", Compass.NORTH, (Position) pos);
    }

    @Nested
    @DisplayName("Cobertura do Método Factory (buildShip)")
    class FactoryCoverage {
        @ParameterizedTest
        @DisplayName("Deve cobrir todos os ramos do switch case")
        // Corrigido para começar com maiúsculas para bater com as classes concretas
        @ValueSource(strings = {"Barca", "Caravela", "Nau", "Fragata", "Galeao", "invalido"})
        void testBuildShipBranches(String type) {
            // O buildShip espera minúsculas, por isso usamos toLowerCase()
            Ship s = Ship.buildShip(type.toLowerCase(), Compass.NORTH, new Position(0, 0));

            if (type.equals("invalido")) {
                assertNull(s, "O ramo default deve retornar null");
            } else {
                assertNotNull(s);
                // Corrigido: Ignora diferenças entre maiúsculas e minúsculas
                assertTrue(type.equalsIgnoreCase(s.getCategory()),
                        "A categoria esperada era " + type + " mas obteve-se " + s.getCategory());
            }
        }
    }

    @Nested
    @DisplayName("Cobertura de Estado e Flutuação")
    class StateCoverage {
        @Test
        @DisplayName("stillFloating: Cobrir ramos (True, False e Ciclo)")
        void testStillFloatingBranches() {
            // Ramo 1: Ainda flutua (nenhum tiro)
            assertTrue(ship.stillFloating());

            // Ramo 2: Ainda flutua (tiro parcial)
            ship.getPositions().get(0).shoot();
            if (ship.getSize() > 1) {
                assertTrue(ship.stillFloating(), "Deve flutuar se apenas uma parte foi atingida");
            }

            // Ramo 3: Afundado (todos os tiros) - Garante que o ciclo termina
            for (IPosition p : ship.getPositions()) {
                p.shoot();
            }
            assertFalse(ship.stillFloating(), "Deve retornar false quando todas as posições têm isHit() true");
        }

        @Test
        @DisplayName("shoot: Deve cobrir o branch de encontrar a posição")
        void testShootBranches() {
            // Corrigido: Vamos buscar o objeto real que está dentro da lista do barco
            IPosition targetInList = ship.getPositions().get(0);

            ship.shoot(targetInList);

            // Agora a verificação vai dar true porque disparámos sobre a referência correta
            assertTrue(targetInList.isHit(), "A posição interna do barco deve estar atingida");

            // Testar tiro falhado (posição que não pertence ao barco)
            IPosition miss = new Position(9, 9);
            ship.shoot(miss);
            assertFalse(miss.isHit(), "Uma posição fora do barco não deve ser afetada");
        }
    }

    @Nested
    @DisplayName("Cobertura de Limites (Boundaries)")
    class BoundaryCoverage {
        @Test
        @DisplayName("Deve percorrer todos os ciclos de comparação de limites")
        void testAllBoundaryMethods() {
            assertAll("Verificação de métodos de limites e utilitários",
                    () -> assertDoesNotThrow(() -> ship.getTopMostPos()),
                    () -> assertDoesNotThrow(() -> ship.getBottomMostPos()),
                    () -> assertDoesNotThrow(() -> ship.getLeftMostPos()),
                    () -> assertDoesNotThrow(() -> ship.getRightMostPos()),
                    () -> assertNotNull(ship.toString()),
                    () -> assertEquals(Compass.NORTH, ship.getBearing())
            );
        }
    }

    @Nested
    @DisplayName("Cobertura de Colisão e Ocupação")
    class CollisionCoverage {
        @Test
        @DisplayName("occupies: Testar ramos Verdadeiro e Falso")
        void testOccupiesBranches() {
            assertTrue(ship.occupies(new Position(2, 2)), "Deve ocupar a posição inicial");
            assertFalse(ship.occupies(new Position(9, 9)), "Não deve ocupar posição distante");
        }

        @Test
        @DisplayName("tooCloseTo (IPosition): Testar ramos de Adjacência")
        void testTooCloseToPosition() {
            // Posição adjacente (ex: (2,3) é adjacente a (2,2))
            assertTrue(ship.tooCloseTo(new Position(2, 3)), "Deve detectar adjacência");
            // Posição longe
            assertFalse(ship.tooCloseTo(new Position(8, 8)), "Não deve detectar adjacência longe");
        }

        @Test
        @DisplayName("tooCloseTo (IShip): Testar ramos de colisão entre barcos")
        void testTooCloseToShip() {
            Ship otherClose = Ship.buildShip("barca", Compass.NORTH, new Position(2, 3));
            Ship otherFar = Ship.buildShip("barca", Compass.NORTH, new Position(8, 8));

            assertTrue(ship.tooCloseTo(otherClose), "Deve detectar que outro barco está perto");
            assertFalse(ship.tooCloseTo(otherFar), "Não deve detectar colisão com barco longe");
        }
    }
}