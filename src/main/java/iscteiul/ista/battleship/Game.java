/**
 *
 */
package iscteiul.ista.battleship;

import java.util.ArrayList;
import java.util.List;
import java.io.FileWriter;
import java.io.IOException;


/**
 * @author fba
 *
 */
public class Game implements IGame {

    private IFleet fleet;
    private List<IPosition> shots;
    private static final String SAVE_FILE = "game_state.json";

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

        if (!validShot(pos)) {
            countInvalidShots++;
        } else {

            if (repeatedShot(pos)) {
                countRepeatedShots++;
            } else {

                shots.add(pos);

                IShip s = fleet.shipAt(pos);

                if (s != null) {
                    //  HIT
                    s.shoot(pos);
                    countHits++;
                    board.markShot(pos, true);

                    if (!s.stillFloating()) {
                        countSinks++;
                        saveToJson(SAVE_FILE);
                        return s;
                    }

                } else {
                    //  MISS
                    board.markShot(pos, false);
                }
            }
        }

        saveToJson(SAVE_FILE);
        return null;
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
        return (pos.getRow() >= 0 && pos.getRow() < Fleet.BOARD_SIZE && pos.getColumn() >= 0
                && pos.getColumn() < Fleet.BOARD_SIZE);
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

    public String toJson() {
        StringBuilder json = new StringBuilder();

        json.append("{\n");
        json.append("  \"invalidShots\": ").append(countInvalidShots).append(",\n");
        json.append("  \"repeatedShots\": ").append(countRepeatedShots).append(",\n");
        json.append("  \"hits\": ").append(countHits).append(",\n");
        json.append("  \"sunkShips\": ").append(countSinks).append(",\n");
        json.append("  \"remainingShips\": ").append(getRemainingShips()).append(",\n");
        json.append("  \"gameOver\": ").append(isGameOver()).append(",\n");

        json.append("  \"shots\": [\n");
        for (int i = 0; i < shots.size(); i++) {
            IPosition p = shots.get(i);
            json.append("    { \"row\": ").append(p.getRow())
                    .append(", \"column\": ").append(p.getColumn()).append(" }");
            if (i < shots.size() - 1)
                json.append(",");
            json.append("\n");
        }
        json.append("  ],\n");

        json.append("  \"ships\": [\n");
        List<IShip> allShips = fleet.getShips();
        for (int i = 0; i < allShips.size(); i++) {
            IShip ship = allShips.get(i);

            json.append("    {\n");
            json.append("      \"category\": \"").append(ship.getCategory()).append("\",\n");
            json.append("      \"bearing\": \"").append(ship.getBearing()).append("\",\n");
            json.append("      \"floating\": ").append(ship.stillFloating()).append(",\n");

            json.append("      \"positions\": [\n");
            List<IPosition> positions = ship.getPositions();
            for (int j = 0; j < positions.size(); j++) {
                IPosition pos = positions.get(j);
                json.append("        { \"row\": ").append(pos.getRow())
                        .append(", \"column\": ").append(pos.getColumn()).append(" }");
                if (j < positions.size() - 1)
                    json.append(",");
                json.append("\n");
            }
            json.append("      ]\n");

            json.append("    }");
            if (i < allShips.size() - 1)
                json.append(",");
            json.append("\n");
        }
        json.append("  ]\n");

        json.append("}\n");

        return json.toString();
    }

    public void saveToJson(String fileName) {
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(toJson());
        } catch (IOException e) {
            System.out.println("Erro ao guardar estado do jogo em JSON.");
        }
    }

}


