package iscteiul.ista.battleship;

import java.util.Scanner;

public class Tasks {
    private static final int NUMBER_SHOTS = 3;

    private static final String GOODBYE_MESSAGE = "Bons ventos!";

    private static final String NOVAFROTA = "nova";
    private static final String DESISTIR = "desisto";
    private static final String RAJADA = "rajada";
    private static final String VERTIROS = "ver";
    private static final String BATOTA = "mapa";
    private static final String STATUS = "estado";

    public static void taskD() {
        Scanner in = new Scanner(System.in);

        IFleet fleet = null;

        Game player1 = null;
        Game player2 = null;
        Game current = null;
        Game opponent = null;

        String command = in.next();

        while (!command.equals(DESISTIR)) {
            switch (command) {

                case NOVAFROTA:
                    fleet = buildFleet(in);

                    if (player1 == null) {
                        player1 = new Game(fleet);
                        System.out.println("Player 1 ready");
                    } else {
                        player2 = new Game(fleet);
                        System.out.println("Player 2 ready");

                        current = player1;
                        opponent = player2;

                        System.out.println("Type 'rajada' to start the game");
                    }
                    break;

                case STATUS:
                case BATOTA:
                    if (current != null)
                        current.printFleet();
                    break;

                // FIXED GAME LOOP + TURN MESSAGE
                case RAJADA:
                    if (current != null && opponent != null) {

                        while (player1.getRemainingShips() > 0 && player2.getRemainingShips() > 0) {

                            printTaskDTurnHeader(current, player1);

                            printBoards(current, opponent);

                            printShotInstructions(current, player1);
                            firingRound(in, opponent);

                            printRoundStats(opponent);

                            if (isGameOver(opponent)) {
                                System.out.println("Maldito sejas, Java Sparrow...");
                                break;
                            }

                            //  SWITCH PLAYERS
                            Game temp = current;
                            current = opponent;
                            opponent = temp;
                        }

                        // SHOW BOARDS AT THE END
                        System.out.println("===== PLAYER 1 VIEW =====");
                        player1.getBoard().printOpponentBoard();

                        System.out.println("===== PLAYER 2 VIEW =====");
                        player2.getBoard().printOpponentBoard();
                    }
                    break;

                case VERTIROS:
                    if (player1 != null && player2 != null) {

                        System.out.println("===== PLAYER 1 VIEW =====");
                        player1.getBoard().printOpponentBoard();

                        System.out.println("===== PLAYER 2 VIEW =====");
                        player2.getBoard().printOpponentBoard();
                    }
                    break;

                default:
                    System.out.println("Que comando é esse??? Repete ...");
            }

            command = in.next();
        }

        System.out.println(GOODBYE_MESSAGE);
    }

    private static void printRoundStats(Game opponent) {
        System.out.println(
                "Hits: " + opponent.getHits()
                        + " Inv: " + opponent.getInvalidShots()
                        + " Rep: " + opponent.getRepeatedShots()
                        + " Restam " + opponent.getRemainingShips() + " navios.");
    }

    private static void printShotInstructions(Game current, Game player1) {
        if (current == player1) {
            System.out.println("=== Jogador 1, é a sua vez! ===");
            System.out.println("Pode jogar " + NUMBER_SHOTS + " vezes");
        } else {
            System.out.println("=== Jogador 2, é a sua vez! ===");
            System.out.println("Pode jogar " + NUMBER_SHOTS + " vezes");
        }
    }

    private static void printTaskDTurnHeader(Game current, Game player1) {
        if (current == player1) {
            System.out.println("\n Player 1, your turn!");
            System.out.println("===== PLAYER 1 TURN =====");
        } else {
            System.out.println("\n Player 2, your turn!");
            System.out.println("===== PLAYER 2 TURN =====");
        }
    }

    private static void printBoards(Game current, Game opponent) {
        System.out.println("Seu tabuleiro:");
        current.getBoard().printVisual();

        System.out.println("Tabuleiro do adversário:");
        opponent.getBoard().printOpponentBoard();
    }

    static Fleet buildFleet(Scanner in) {
        Fleet fleet = new Fleet();
        int i = 0;

        while (i < Fleet.FLEET_SIZE) {
            IShip s = readShip(in);
            if (s != null) {
                boolean success = fleet.addShip(s);
                if (success)
                    i++;
                else
                    System.out.println("Falha na criacao de " + s.getCategory());
            } else {
                System.out.println("Navio desconhecido!");
            }
        }

        System.out.println(i + " navios adicionados com sucesso!");
        return fleet;
    }

    static Ship readShip(Scanner in) {
        String shipKind = in.next();
        Position pos = readPosition(in);
        char c = in.next().charAt(0);
        Compass bearing = Compass.charToCompass(c);
        return Ship.buildShip(shipKind, bearing, pos);
    }

    static Position readPosition(Scanner in) {
        int row = in.nextInt();
        int column = in.nextInt();
        return new Position(row, column);
    }

    static void firingRound(Scanner in, IGame game) {
        for (int i = 0; i < NUMBER_SHOTS; i++) {

            IPosition pos = readPosition(in);

            boolean hit = ((Game) game).wasHit(pos);
            IShip sh = game.fire(pos);

            ((Game) game).getBoard().printOpponentBoard();
            System.out.println("-------------------");

            if (sh != null) {
                System.out.println("Navio afundado: " + sh.getCategory());
            } else if (hit) {
                System.out.println("Acerto!");
            } else {
                System.out.println("Água!");
            }
        }
    }

    public static void taskE() {
        Scanner in = new Scanner(System.in);

        System.out.println("=== PLAYER 1 FLEET ===");
        Fleet fleet1 = buildFleet(in);

        System.out.println("=== PLAYER 2 FLEET ===");
        Fleet fleet2 = buildFleet(in);

        Game game1 = new Game(fleet2);
        Game game2 = new Game(fleet1);

        boolean player1Turn = true;

        while (true) {

            printTurnHeader(player1Turn);

            if (player1Turn) {

                System.out.println("=== PLAYER 1 BOARD ===");
                game2.getBoard().printVisual();

                System.out.println("=== PLAYER 2 BOARD (KNOWN) ===");
                game1.getBoard().printOpponentBoard();

                firingRound(in, game1);

                if (checkWinner(game1)) break;

            } else {

                System.out.println("=== PLAYER 2 BOARD ===");
                game1.getBoard().printVisual();

                System.out.println("=== PLAYER 1 BOARD (KNOWN) ===");
                game2.getBoard().printOpponentBoard();

                firingRound(in, game2);

                if (checkWinner(game2)) {
                    break;
                }
            }

            player1Turn = !player1Turn;
        }
    }

    private static boolean checkWinner(Game game) {
        if (isGameOver(game)) {
            System.out.println("PLAYER 1 WINS!");
            return true;
        }
        return false;
    }

    private static boolean isGameOver(Game game) {
        return game.getRemainingShips() == 0;
    }

    private static void printTurnHeader(boolean player1Turn) {
        if (player1Turn) {
            System.out.println("----- PLAYER 1 TURN -----");
        } else {
            System.out.println("----- PLAYER 2 TURN -----");
        }
    }
}