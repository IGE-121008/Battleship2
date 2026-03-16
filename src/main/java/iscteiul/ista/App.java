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

        Scoreboard scoreboard = Scoreboard.loadScoreboard();
        scoreboard.addPlayer1Win();
        scoreboard.printScoreboard();
        scoreboard.saveScoreboard();

        // Tasks.taskA();
        Tasks.taskB();
        // Tasks.taskC();
        // Tasks.taskD();
    }
}