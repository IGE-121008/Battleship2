package iscteiul.ista;

import iscteiul.ista.battleship.Scoreboard;
import iscteiul.ista.battleship.Tasks;

/**
 * @author britoeabreu
 * @author adrianolopes
 * @author miguelgoulao
 */
public class App {
    public static void main(String[] args) {

        System.out.printf("\n*** Battleship Game ***\n");
        System.out.println("Running Task D...");
        System.out.println("Type one of the valid commands when asked.");

        Tasks.taskD();

        Scoreboard scoreboard = Scoreboard.loadScoreboard();
        scoreboard.addPlayer1Win();
        scoreboard.printScoreboard();
        scoreboard.saveScoreboard();
    }
}