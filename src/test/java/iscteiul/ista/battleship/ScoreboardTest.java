package iscteiul.ista.battleship;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ScoreboardTest {

    private static final Path SCOREBOARD_PATH = Path.of("scoreboard.json");

    private boolean originalExisted;
    private boolean originalWasDirectory;
    private byte[] originalContent;

    @BeforeEach
    void setUp() throws IOException {
        originalExisted = Files.exists(SCOREBOARD_PATH);
        originalWasDirectory = Files.isDirectory(SCOREBOARD_PATH);

        if (originalExisted && !originalWasDirectory) {
            originalContent = Files.readAllBytes(SCOREBOARD_PATH);
        }

        deleteScoreboardPath();
    }

    @AfterEach
    void tearDown() throws IOException {
        deleteScoreboardPath();

        if (originalExisted) {
            if (originalWasDirectory) {
                Files.createDirectories(SCOREBOARD_PATH);
            } else {
                Files.write(SCOREBOARD_PATH, originalContent);
            }
        }
    }

    @Test
    @DisplayName("New scoreboard starts with zero wins")
    void newScoreboardStartsWithZeroWins() throws Exception {
        Scoreboard scoreboard = new Scoreboard();

        assertEquals(0, getPrivateInt(scoreboard, "player1Wins"));
        assertEquals(0, getPrivateInt(scoreboard, "player2Wins"));
    }

    @Test
    @DisplayName("Adding player 1 win increments only player 1 wins")
    void addPlayer1WinIncrementsOnlyPlayer1Wins() throws Exception {
        Scoreboard scoreboard = new Scoreboard();

        scoreboard.addPlayer1Win();

        assertEquals(1, getPrivateInt(scoreboard, "player1Wins"));
        assertEquals(0, getPrivateInt(scoreboard, "player2Wins"));
    }

    @Test
    @DisplayName("Adding player 2 win increments only player 2 wins")
    void addPlayer2WinIncrementsOnlyPlayer2Wins() throws Exception {
        Scoreboard scoreboard = new Scoreboard();

        scoreboard.addPlayer2Win();

        assertEquals(0, getPrivateInt(scoreboard, "player1Wins"));
        assertEquals(1, getPrivateInt(scoreboard, "player2Wins"));
    }

    @Test
    @DisplayName("Print scoreboard displays both players' wins")
    void printScoreboardDisplaysBothPlayersWins() {
        Scoreboard scoreboard = new Scoreboard();
        scoreboard.addPlayer1Win();
        scoreboard.addPlayer2Win();
        scoreboard.addPlayer2Win();

        String output = captureOutput(scoreboard::printScoreboard);

        assertTrue(output.contains("=== Scoreboard ==="));
        assertTrue(output.contains("Player 1 Wins: 1"));
        assertTrue(output.contains("Player 2 Wins: 2"));
    }

    @Test
    @DisplayName("Save scoreboard creates JSON file and prints success message")
    void saveScoreboardCreatesJsonFileAndPrintsSuccessMessage() throws IOException {
        Scoreboard scoreboard = new Scoreboard();
        scoreboard.addPlayer1Win();
        scoreboard.addPlayer2Win();

        String output = captureOutput(scoreboard::saveScoreboard);

        assertTrue(output.contains("Scoreboard saved."));
        assertTrue(Files.exists(SCOREBOARD_PATH));

        String json = Files.readString(SCOREBOARD_PATH);
        assertTrue(json.contains("player1Wins"));
        assertTrue(json.contains("player2Wins"));
    }

    @Test
    @DisplayName("Load scoreboard returns saved scoreboard when file exists")
    void loadScoreboardReturnsSavedScoreboardWhenFileExists() throws Exception {
        Scoreboard scoreboard = new Scoreboard();
        scoreboard.addPlayer1Win();
        scoreboard.addPlayer1Win();
        scoreboard.addPlayer2Win();
        scoreboard.saveScoreboard();

        Scoreboard loadedScoreboard = Scoreboard.loadScoreboard();

        assertEquals(2, getPrivateInt(loadedScoreboard, "player1Wins"));
        assertEquals(1, getPrivateInt(loadedScoreboard, "player2Wins"));
    }

    @Test
    @DisplayName("Load scoreboard returns new scoreboard when file does not exist")
    void loadScoreboardReturnsNewScoreboardWhenFileDoesNotExist() throws Exception {
        Scoreboard loadedScoreboard = Scoreboard.loadScoreboard();

        assertNotNull(loadedScoreboard);
        assertEquals(0, getPrivateInt(loadedScoreboard, "player1Wins"));
        assertEquals(0, getPrivateInt(loadedScoreboard, "player2Wins"));
    }

    @Test
    @DisplayName("Load scoreboard returns new scoreboard when JSON file contains null")
    void loadScoreboardReturnsNewScoreboardWhenJsonFileContainsNull() throws Exception {
        Files.writeString(SCOREBOARD_PATH, "null");

        Scoreboard loadedScoreboard = Scoreboard.loadScoreboard();

        assertNotNull(loadedScoreboard);
        assertEquals(0, getPrivateInt(loadedScoreboard, "player1Wins"));
        assertEquals(0, getPrivateInt(loadedScoreboard, "player2Wins"));
    }

    @Test
    @DisplayName("Save scoreboard prints error message when file cannot be written")
    void saveScoreboardPrintsErrorMessageWhenFileCannotBeWritten() throws IOException {
        Files.createDirectory(SCOREBOARD_PATH);

        Scoreboard scoreboard = new Scoreboard();

        String output = captureOutput(scoreboard::saveScoreboard);

        assertTrue(output.contains("Error saving scoreboard."));
    }

    private static int getPrivateInt(Scoreboard scoreboard, String fieldName) throws Exception {
        Field field = Scoreboard.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.getInt(scoreboard);
    }

    private static String captureOutput(Runnable runnable) {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            System.setOut(new PrintStream(outputStream));
            runnable.run();
            return outputStream.toString();
        } finally {
            System.setOut(originalOut);
        }
    }

    private static void deleteScoreboardPath() throws IOException {
        if (Files.notExists(SCOREBOARD_PATH)) {
            return;
        }

        if (Files.isDirectory(SCOREBOARD_PATH)) {
            try (Stream<Path> paths = Files.walk(SCOREBOARD_PATH)) {
                paths.sorted(Comparator.reverseOrder())
                        .forEach(path -> {
                            try {
                                Files.deleteIfExists(path);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });
            }
        } else {
            Files.deleteIfExists(SCOREBOARD_PATH);
        }
    }
}