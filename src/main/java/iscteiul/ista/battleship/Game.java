/**
 *
 */
package iscteiul.ista.battleship;

import java.util.ArrayList;
import java.util.List;


/**
 * @author fba
 *
 */
public class Game implements IGame {

    private IFleet fleet;
    private List<IPosition> shots;

    private Integer countInvalidShots;
    private Integer countRepeatedShots;
    private Integer countHits;
    private Integer countSinks;
    private Board board;


    /**
     * @param fleet
     */
    public Game(IFleet fleet) {
        shots = new ArrayList<>();
        countInvalidShots = 0;
        countRepeatedShots = 0;
        countHits = 0;
        countSinks = 0;
        this.fleet = fleet;
        this.board = new Board();
        board.placeFleet(fleet);
    }

    /*
     * (non-Javadoc)
     *
     * @see battleship.IGame#fire(battleship.IPosition)
     */

    @Override
    public IShip fire(IPosition pos) {

        if (!validateShot(pos)) {
            return null;
        }

        shots.add(pos);

        IShip s = fleet.shipAt(pos);

        if (s != null) {
            return handleHit(pos, s);
        } else {
            handleMiss(pos);
        }

        return null;
    }

    private IShip handleHit(IPosition pos, IShip ship) {
        ship.shoot(pos);
        countHits++;
        board.markShot(pos, true);

        if (!ship.stillFloating()) {
            countSinks++;
            return ship;
        }

        return null;
    }

    private void handleMiss(IPosition pos) {
        board.markShot(pos, false);
    }

    private boolean validateShot(IPosition pos) {
        if (!validShot(pos)) {
            countInvalidShots++;
            return false;
        }

        if (repeatedShot(pos)) {
            countRepeatedShots++;
            return false;
        }

        return true;
    }

    /*
     * (non-Javadoc)
     *
     * @see battleship.IGame#getShots()
     */
    @Override
    public List<IPosition> getShots() {
        return shots;
    }

    /*
     * (non-Javadoc)
     *
     * @see battleship.IGame#getRepeatedShots()
     */
    @Override
    public int getRepeatedShots() {
        return this.countRepeatedShots;
    }

    /*
     * (non-Javadoc)
     *
     * @see battleship.IGame#getInvalidShots()
     */
    @Override
    public int getInvalidShots() {
        return this.countInvalidShots;
    }

    /*
     * (non-Javadoc)
     *
     * @see battleship.IGame#getHits()
     */
    @Override
    public int getHits() {
        return this.countHits;
    }

    /*
     * (non-Javadoc)
     *
     * @see battleship.IGame#getSunkShips()
     */
    @Override
    public int getSunkShips() {
        return this.countSinks;
    }

    /*
     * (non-Javadoc)
     *
     * @see battleship.IGame#getRemainingShips()
     */
    @Override
    public int getRemainingShips() {
        List<IShip> floatingShips = fleet.getFloatingShips();
        return floatingShips.size();
    }
    public boolean isGameOver()
    {
        return getRemainingShips() == 0;
    }

    private boolean validShot(IPosition pos) {
        return isInsideBoard(pos.getRow()) && isInsideBoard(pos.getColumn());
    }

    private boolean isInsideBoard(int coordinate) {
        return coordinate >= 0 && coordinate < Fleet.BOARD_SIZE;
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

    /**
     * Prints the board showing valid shots that have been fired
     */
    public void printValidShots() {
        printBoard(getShots(), 'X');
    }


    /**
     * Prints the board showing the fleet
     */
    public void printFleet() {
        List<IPosition> shipPositions = new ArrayList<IPosition>();

        for (IShip s : fleet.getShips())
            shipPositions.addAll(s.getPositions());

        printBoard(shipPositions, '#');
    }
    public Board getBoard()
    {
        return board;
    }

    public boolean wasHit(IPosition pos) {
        IShip s = fleet.shipAt(pos);
        return s != null;
    }
}


