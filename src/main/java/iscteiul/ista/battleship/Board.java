package iscteiul.ista.battleship;

public class Board
{
    private char[][] grid;

    public Board()
    {
        grid = new char[Fleet.BOARD_SIZE][Fleet.BOARD_SIZE];
        clear();
    }

    public void clear()
    {
        for(int i = 0; i < Fleet.BOARD_SIZE; i++){
            for(int j = 0; j < Fleet.BOARD_SIZE; j++) {
                grid[i][j] = '.';
            }
        }
    }
    public void placeFleet(IFleet fleet)
    {
        for(IShip ship : fleet.getShips())
        {
            for(IPosition pos : ship.getPositions())
            {
                grid[pos.getRow()][pos.getColumn()] = '#';
            }
        }
    }
    public void markShot(IPosition pos, boolean hit)
    {
        if(hit)
            grid[pos.getRow()][pos.getColumn()] = 'X';
        else
            grid[pos.getRow()][pos.getColumn()] = 'o';
    }
    public void print()
    {
        for(int i = 0; i < Fleet.BOARD_SIZE; i++)
        {
            for (int j = 0; j < Fleet.BOARD_SIZE; j++)
            {
                System.out.print(grid[i][j]);
            }
            System.out.println();
        }
    }
}
