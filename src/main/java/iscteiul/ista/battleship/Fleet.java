package iscteiul.ista.battleship;

import java.util.ArrayList;
import java.util.List;

public class Fleet implements IFleet {

    public static final int BOARD_SIZE = 10;

    private final List<IShip> ships;
    private final Board board;

    public Fleet() {
        this.ships = new ArrayList<>();
        this.board = new Board();
    }

    public List<IShip> getShips() {
        return ships;
    }

    @Override
    public boolean addShip(IShip ship) {
        if (ship == null) throw new NullPointerException("Ship cannot be null");

        if (hasCollision(ship) || !isWithinBoardLimits(ship)) {
            return false;
        }

        ships.add(ship);
        board.placeFleet(this);
        return true;
    }

    private boolean hasCollision(IShip ship) {
        for (IShip existing : ships) {
            if (existing.collidesWith(ship)) {
                return true;
            }
        }
        return false;
    }

    private boolean isWithinBoardLimits(IShip ship) {
        return ship.getPositions().stream()
                .allMatch(p -> p.getRow() >= 0 && p.getRow() < BOARD_SIZE &&
                              p.getColumn() >= 0 && p.getColumn() < BOARD_SIZE);
    }

    @Override
    public IShip shipAt(IPosition pos) {
        for (IShip ship : ships) {
            if (ship.occupies(pos)) {
                return ship;
            }
        }
        return null;
    }

    @Override
    public List<IShip> getFloatingShips() {
        return ships.stream().filter(IShip::stillFloating).toList();
    }

    @Override
    public List<IShip> getShipsLike(String type) {
        return ships.stream().filter(s -> s.getCategory().equalsIgnoreCase(type)).toList();
    }

    public void printStatus() {
        board.printVisual();
    }

    public void printAllShips() {
        board.printVisual();
    }

    public void printFloatingShips() {
        for (IShip s : getFloatingShips()) {
            System.out.println(s);
        }
    }
}