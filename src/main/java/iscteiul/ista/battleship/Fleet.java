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

        // 1. verificar limites + colisão real
        for (IShip existing : ships) {
            for (IPosition p1 : existing.getPositions()) {
                for (IPosition p2 : ship.getPositions()) {

                    // colisão REAL (mesma posição)
                    if (p1.equals(p2)) {
                        return false;
                    }
                }
            }
        }

        // 2. verificar limites do tabuleiro
        for (IPosition p : ship.getPositions()) {
            if (p.getRow() < 0 || p.getRow() >= BOARD_SIZE ||
                    p.getColumn() < 0 || p.getColumn() >= BOARD_SIZE) {
                return false;
            }
        }

        ships.add(ship);
        board.placeFleet(this);
        return true;
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
        List<IShip> floating = new ArrayList<>();
        for (IShip s : ships) {
            if (s.stillFloating()) {
                floating.add(s);
            }
        }
        return floating;
    }

    @Override
    public List<IShip> getShipsLike(String type) {
        List<IShip> result = new ArrayList<>();
        for (IShip s : ships) {
            if (s.getCategory().equalsIgnoreCase(type)) {
                result.add(s);
            }
        }
        return result;
    }

    public void printStatus() {
        board.printVisual();
    }

    public void printAllShips() {
        board.placeFleet(this);
        board.printVisual();
    }

    public void printFloatingShips() {
        for (IShip s : getFloatingShips()) {
            System.out.println(s);
        }
    }
}