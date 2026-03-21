package iscteiul.ista.battleship;

import java.util.ArrayList;
import java.util.List;

public class Game implements IGame {
    private IFleet fleet;
    private List<IPosition> shots;

    private Integer countInvalidShots;
    private Integer countRepeatedShots;
    private Integer countHits;
    private Integer countSinks;

    public Game(IFleet fleet) {
        shots = new ArrayList<>();
        countInvalidShots = 0;
        countRepeatedShots = 0;
        countHits = 0;
        countSinks = 0;
        this.fleet = fleet;
    }

    @Override
    public IShip fire(IPosition pos) {
        if (!validShot(pos))
            countInvalidShots++;
        else {
            if (repeatedShot(pos))
                countRepeatedShots++;
            else {
                shots.add(pos);
                IShip s = fleet.shipAt(pos);
                if (s != null) {
                    s.shoot(pos);
                    countHits++;
                    if (!s.stillFloating()) {
                        countSinks++;
                        return s;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public List<IPosition> getShots() {
        return shots;
    }

    @Override
    public int getRepeatedShots() {
        return this.countRepeatedShots;
    }

    @Override
    public int getInvalidShots() {
        return this.countInvalidShots;
    }

    @Override
    public int getHits() {
        return this.countHits;
    }

    @Override
    public int getSunkShips() {
        return this.countSinks;
    }

    @Override
    public int getRemainingShips() {
        List<IShip> floatingShips = fleet.getFloatingShips();
        return floatingShips.size();
    }

    private boolean validShot(IPosition pos) {
        return (pos.getRow() >= 0 && pos.getRow() < Fleet.BOARD_SIZE
                && pos.getColumn() >= 0 && pos.getColumn() < Fleet.BOARD_SIZE);
    }

    private boolean repeatedShot(IPosition pos) {
        for (int i = 0; i < shots.size(); i++)
            if (shots.get(i).equals(pos))
                return true;
        return false;
    }

    public void printBoard(List<IPosition> positions, Character marker) {
        char[][] map = new char[Fleet.BOARD_SIZE][Fleet.BOARD_SIZE];

        for (int r = 0; r < Fleet.BOARD_SIZE; r++)
            for (int c = 0; c < Fleet.BOARD_SIZE; c++)
                map[r][c] = '.';

        for (IPosition pos : positions)
            map[pos.getRow()][pos.getColumn()] = marker;

        for (int row = 0; row < Fleet.BOARD_SIZE; row++) {
            for (int col = 0; col < Fleet.BOARD_SIZE; col++)
                System.out.print(map[row][col]);
            System.out.println();
        }
    }

    @Override
    public void printValidShots() {
        printBoard(getShots(), 'X');
    }

    @Override
    public void printFleet() {
        List<IPosition> shipPositions = new ArrayList<IPosition>();

        for (IShip s : fleet.getShips())
            shipPositions.addAll(s.getPositions());

        printBoard(shipPositions, '#');
    }

    /**
     * Prints the opponent board:
     * X = hit
     * O = water
     * - = unknown
     */
    @Override
    public void printOpponentBoard() {
        char[][] map = new char[Fleet.BOARD_SIZE][Fleet.BOARD_SIZE];

        for (int r = 0; r < Fleet.BOARD_SIZE; r++) {
            for (int c = 0; c < Fleet.BOARD_SIZE; c++) {
                map[r][c] = '-';
            }
        }

        for (IPosition pos : shots) {
            IShip ship = fleet.shipAt(pos);
            if (ship != null) {
                map[pos.getRow()][pos.getColumn()] = 'X';
            } else {
                map[pos.getRow()][pos.getColumn()] = 'O';
            }
        }

        System.out.println();
        System.out.print("    ");
        for (int col = 1; col <= Fleet.BOARD_SIZE; col++) {
            System.out.print(col + " ");
        }
        System.out.println();

        for (int i = 0; i < 2 * Fleet.BOARD_SIZE + 18; i++) {
            System.out.print("*");
        }
        System.out.println();

        for (int row = 0; row < Fleet.BOARD_SIZE; row++) {
            char rowLetter = (char) ('A' + row);
            System.out.print(rowLetter + " * ");

            for (int col = 0; col < Fleet.BOARD_SIZE; col++) {
                System.out.print(map[row][col] + " ");
            }

            System.out.println("*");
        }

        for (int i = 0; i < 2 * Fleet.BOARD_SIZE + 18; i++) {
            System.out.print("*");
        }
        System.out.println();
    }
}