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

    // Marca um tiro no tabuleiro
    public void markShot(IPosition pos, boolean hit) {
        if (hit)
            grid[pos.getRow()][pos.getColumn()] = 'X';
        else
            grid[pos.getRow()][pos.getColumn()] = 'o';
    }

    // Coloca a frota no tabuleiro usando letras específicas
    public void placeFleet(IFleet fleet) {
        for (int i = 0; i < Fleet.BOARD_SIZE; i++) {
            for (int j = 0; j < Fleet.BOARD_SIZE; j++) {
                if (grid[i][j] != 'X' && grid[i][j] != 'o') {
                    grid[i][j] = '-';
                }
            }
        }

        for (IShip ship : fleet.getShips()) {
            char symbol = getShipSymbol(ship);
            for (IPosition pos : ship.getPositions()) {
                if (grid[pos.getRow()][pos.getColumn()] != 'X' && grid[pos.getRow()][pos.getColumn()] != 'o') {
                    grid[pos.getRow()][pos.getColumn()] = symbol;
                }
            }
        }
    }

    // Converte navio em letra
    private char getShipSymbol(IShip ship) {
        switch (ship.getCategory().toLowerCase()) {
            case "caravel": return 'c';
            case "nau":     return 'C';
            case "frigate": return 'F';
            case "barca":   return 'B';
            case "galeao":  return 'G';
            default: return '#';
        }
    }

    // 🟦 NORMAL BOARD (your board)
    public void printVisual() {
        System.out.print("  ");
        for (int j = 0; j < Fleet.BOARD_SIZE; j++) {
            System.out.print((j + 1) + " ");
        }
        System.out.println();
        System.out.println("****************************************");

        for (int i = 0; i < Fleet.BOARD_SIZE; i++) {
            System.out.print((char)('A' + i) + " * ");
            for (int j = 0; j < Fleet.BOARD_SIZE; j++) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println("*");
        }

        System.out.println("****************************************");
    }

    // 🟥 OPPONENT BOARD (YOUR TASK 🔥)
    public void printOpponentBoard() {
        System.out.print("  ");
        for (int j = 0; j < Fleet.BOARD_SIZE; j++) {
            System.out.print((j + 1) + " ");
        }
        System.out.println();
        System.out.println("****************************************");

        for (int i = 0; i < Fleet.BOARD_SIZE; i++) {
            System.out.print((char)('A' + i) + " * ");
            for (int j = 0; j < Fleet.BOARD_SIZE; j++) {

                char c = grid[i][j];

                // 🔥 ONLY show known info
                if (c == 'X' || c == 'o') {
                    System.out.print(c + " ");
                } else {
                    System.out.print("- ");
                }
            }
            System.out.println("*");
        }

        System.out.println("****************************************");
    }
}