/**
 *
 */
package iscteiul.ista.battleship;

public class Frigate extends Ship {
    private static final Integer SIZE = 4;
    private static final String NAME = "Fragata";

    /**
     * @param bearing
     * @param pos
     */
    public Frigate(Compass bearing, IPosition pos) throws IllegalArgumentException {
        super(Frigate.NAME, bearing, pos);

        if (bearing == null)
            throw new IllegalArgumentException("ERROR! invalid bearing for the frigate");

        switch (bearing) {
            case NORTH:
                for (int r = 0; r < SIZE; r++)
                    getPositions().add(new Position(pos.getRow() - r, pos.getColumn()));
                break;
            case SOUTH:
                for (int r = 0; r < SIZE; r++)
                    getPositions().add(new Position(pos.getRow() + r, pos.getColumn()));
                break;
            case EAST:
                for (int c = 0; c < SIZE; c++)
                    getPositions().add(new Position(pos.getRow(), pos.getColumn() + c));
                break;
            case WEST:
                for (int c = 0; c < SIZE; c++)
                    getPositions().add(new Position(pos.getRow(), pos.getColumn() - c));
                break;
            default:
                throw new IllegalArgumentException("ERROR! invalid bearing for the frigate");
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see battleship.Ship#getSize()
     */
    @Override
    public Integer getSize() {
        return Frigate.SIZE;
    }

}
