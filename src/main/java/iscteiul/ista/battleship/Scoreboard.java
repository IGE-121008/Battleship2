package iscteiul.ista.battleship;

import com.google.gson.Gson;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Scoreboard {

    private int player1Wins;
    private int player2Wins;

    private static final String FILE_NAME = "scoreboard.json";

    public void addPlayer1Win() {
        player1Wins++;
    }

    public void addPlayer2Win() {
        player2Wins++;
    }

    public void printScoreboard() {
        System.out.println("=== Scoreboard ===");
        System.out.println("Player 1 Wins: " + player1Wins);
        System.out.println("Player 2 Wins: " + player2Wins);
    }

    public void saveScoreboard() {
        Gson gson = new Gson();
        try (FileWriter writer = new FileWriter(FILE_NAME)) {
            gson.toJson(this, writer);
            System.out.println("Scoreboard saved.");
        } catch (IOException e) {
            System.out.println("Error saving scoreboard.");
        }
    }

    public static Scoreboard loadScoreboard() {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(FILE_NAME)) {
            Scoreboard scoreboard = gson.fromJson(reader, Scoreboard.class);
            return scoreboard != null ? scoreboard : new Scoreboard();
        } catch (IOException e) {
            return new Scoreboard();
        }
    }
}