package iscteiul.ista.battleship;

public class Board {

    private char[][] grid;          // Estado dos tiros (acertos, falhas, vazio)
    private char[][] shipLayer;     // Letras dos navios
    private static final char EMPTY = '-';

    public Board() {
        grid = new char[Fleet.BOARD_SIZE][Fleet.BOARD_SIZE];
        shipLayer = new char[Fleet.BOARD_SIZE][Fleet.BOARD_SIZE];
        clear();
    }

    // Limpa o tabuleiro
    public void clear() {
        for (int i = 0; i < Fleet.BOARD_SIZE; i++) {
            for (int j = 0; j < Fleet.BOARD_SIZE; j++) {
                grid[i][j] = EMPTY;
                shipLayer[i][j] = EMPTY;
            }
        }
    }

    // Coloca a frota no tabuleiro, atribuindo letras específicas
    public void placeFleet(IFleet fleet) {
        for (IShip ship : fleet.getShips()) {
            char symbol = getShipSymbol(ship);
            for (IPosition pos : ship.getPositions()) {
                shipLayer[pos.getRow()][pos.getColumn()] = symbol;
            }
        }
    }

    // Marca um tiro no tabuleiro
    public void markShot(IPosition pos, boolean hit) {
        if (hit)
            grid[pos.getRow()][pos.getColumn()] = 'X';
        else
            grid[pos.getRow()][pos.getColumn()] = 'o';
    }

    // Determina a letra do navio
    private char getShipSymbol(IShip ship) {
        String type = ship.getCategory().toLowerCase();
        switch (type) {
            case "caravel": return 'c';
            case "carrack": return 'C';
            case "frigate": return 'F';
            case "barge": return 'B';
            default: return '#';
        }
    }

    // Imprime o tabuleiro visual
    public void printVisual() {
        // Cabeçalho superior
        System.out.print("  ");
        for (int j = 0; j < Fleet.BOARD_SIZE; j++) {
            System.out.print((j + 1) + " ");
        }
        System.out.println();
        System.out.println("****************************************");

        for (int i = 0; i < Fleet.BOARD_SIZE; i++) {
            System.out.print((char) ('A' + i) + " * ");
            for (int j = 0; j < Fleet.BOARD_SIZE; j++) {
                char shot = grid[i][j];
                if (shot == EMPTY) {
                    // mostra navio se existe, senão vazio
                    System.out.print((shipLayer[i][j] != EMPTY ? shipLayer[i][j] : EMPTY) + " ");
                } else {
                    // mostra acerto ou falha
                    System.out.print(shot + " ");
                }
            }
            System.out.println("*");
        }

        System.out.println("****************************************");
    }
}