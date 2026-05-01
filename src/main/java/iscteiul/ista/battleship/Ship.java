/**
 *
 */
package iscteiul.ista.battleship;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class Ship implements IShip {

    /**
     * @param shipType
     * @param bearing
     * @param pos
     * @return
     */
    static Ship buildShip(ShipType shipType, Compass bearing, Position pos) {
        if (shipType == null) {
            return null;
        }
        Ship s;
        switch (shipType) {
            case BARCA:
                s = new Barge(bearing, pos);
                break;
            case CARAVELA:
                s = new Caravel(bearing, pos);
                break;
            case NAU:
                s = new Carrack(bearing, pos);
                break;
            case FRAGATA:
                s = new Frigate(bearing, pos);
                break;
            case GALEAO:
                s = new Galleon(bearing, pos);
                break;
            default:
                s = null;
        }
        return s;
    }


    private String category;
    private Compass bearing;
    private IPosition pos;
    private List<IPosition> positions;


    /**
     * @param category
     * @param bearing
     * @param pos
     */
    public Ship(String category, Compass bearing, IPosition pos) {
        assert bearing != null;
        assert pos != null;

        this.category = category;
        this.bearing = bearing;
        this.pos = pos;
        positions = new ArrayList<>();
    }

    /*
     * (non-Javadoc)
     *
     * @see battleship.IShip#getCategory()
     */
    @Override
    public String getCategory() {
        return category;
    }

    /**
     * @return the positions
     */
    public List<IPosition> getPositions() {
        return positions;
    }

    /*
     * (non-Javadoc)
     *
     * @see battleship.IShip#getPosition()
     */
    @Override
    public IPosition getPosition() {
        return pos;
    }

    /*
     * (non-Javadoc)
     *
     * @see battleship.IShip#getBearing()
     */
    @Override
    public Compass getBearing() {
        return bearing;
    }

    /*
     * (non-Javadoc)
     *
     * @see battleship.IShip#stillFloating()
     */
    @Override
    public boolean stillFloating() {
        return getPositions().stream().anyMatch(p -> !p.isHit());
    }

    /*
     * (non-Javadoc)
     *
     * @see battleship.IShip#getTopMostPos()
     */
    @Override
    public int getTopMostPos() {
        return getPositions().stream().mapToInt(IPosition::getRow).min().orElse(0);
    }

    /*
     * (non-Javadoc)
     *
     * @see battleship.IShip#getBottomMostPos()
     */
    @Override
    public int getBottomMostPos() {
        return getPositions().stream().mapToInt(IPosition::getRow).max().orElse(0);
    }

    /*
     * (non-Javadoc)
     *
     * @see battleship.IShip#getLeftMostPos()
     */
    @Override
    public int getLeftMostPos() {
        return getPositions().stream().mapToInt(IPosition::getColumn).min().orElse(0);
    }

    /*
     * (non-Javadoc)
     *
     * @see battleship.IShip#getRightMostPos()
     */
    @Override
    public int getRightMostPos() {
        return getPositions().stream().mapToInt(IPosition::getColumn).max().orElse(0);
    }

    /*
     * (non-Javadoc)
     *
     * @see battleship.IShip#occupies(battleship.IPosition)
     */
    @Override
    public boolean occupies(IPosition pos) {
        assert pos != null;

        return getPositions().stream().anyMatch(p -> p.equals(pos));
    }

    /*
     * (non-Javadoc)
     *
     * @see battleship.IShip#tooCloseTo(battleship.IShip)
     */
    @Override
    public boolean tooCloseTo(IShip other) {
        assert other != null;

        Iterator<IPosition> otherPos = other.getPositions().iterator();
        while (otherPos.hasNext())
            if (tooCloseTo(otherPos.next()))
                return true;

        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see battleship.IShip#tooCloseTo(battleship.IPosition)
     */
    @Override
    public boolean tooCloseTo(IPosition pos) {
        return getPositions().stream().anyMatch(p -> p.isAdjacentTo(pos));
    }

    /**
     * Checks if this ship collides with another ship (occupies the same positions).
     * @param other the other ship
     * @return true if they collide, false otherwise
     */
    @Override
    public boolean collidesWith(IShip other) {
        if (other == null) return false;
        return getPositions().stream().anyMatch(p -> other.occupies(p));
    }


    /*
     * (non-Javadoc)
     *
     * @see battleship.IShip#shoot(battleship.IPosition)
     */
    @Override
    public void shoot(IPosition pos) {
        assert pos != null;

        for (IPosition position : getPositions()) {
            if (position.equals(pos))
                position.shoot();
        }
    }


    @Override
    public String toString() {
        return "[" + category + " " + bearing + " " + pos + "]";
    }

}
