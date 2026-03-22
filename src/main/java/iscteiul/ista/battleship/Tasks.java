package iscteiul.ista.battleship;

import java.util.Scanner;

public class Tasks {
    private static final int NUMBER_SHOTS = 3;

    private static final String GOODBYE_MESSAGE = "Bons ventos!";

    /**
     * Strings to be used by the user
     */
    private static final String NOVAFROTA = "nova";
    private static final String DESISTIR = "desisto";
    private static final String RAJADA = "rajada";
    private static final String VERTIROS = "ver";
    private static final String BATOTA = "mapa";
    private static final String STATUS = "estado";

    /**
     * This task tests the building up of ships: For each ship, reads positions and
     * indicates whether the ship occupies each one of such positions or not
     */
    public static void taskA() {
        Board board = new Board();        // cria tabuleiro
        IFleet fleet = new Fleet();       // cria frota vazia

        Scanner in = new Scanner(System.in);

        while (in.hasNext()) {
            Ship s = readShip(in);        // lê navio do utilizador
            if (s != null) {
                fleet.addShip(s);         // adiciona navio à frota
                board.placeFleet(fleet);  // atualiza o tabuleiro
                board.printVisual();      // imprime tabuleiro atualizado

                // opcional: ler posições de tiro para teste
                for (int i = 0; i < NUMBER_SHOTS; i++) {
                    Position p = readPosition(in);
                    System.out.println(p + " " + s.occupies(p));
                }
            }
        }
    }

    /**
     * This task tests the building up of fleets
     */
    public static void taskB() {
        Scanner in = new Scanner(System.in);
        IFleet fleet = null;
        String command = in.next();
        while (!command.equals(DESISTIR)) {
            switch (command) {
                case NOVAFROTA:
                    fleet = buildFleet(in);
                    break;
                case STATUS:
                    if (fleet != null)
                        fleet.printStatus();
                    break;
                default:
                    System.out.println("Que comando é esse??? Repete lá ...");
            }
            command = in.next();
        }
        System.out.println(GOODBYE_MESSAGE);
    }

    /**
     * This task tests the building up of fleets and takes into consideration the
     * possibility of cheating
     */
    public static void taskC() {
        Scanner in = new Scanner(System.in);
        IFleet fleet = null;
        String command = in.next();
        while (!command.equals(DESISTIR)) {
            switch (command) {
                case NOVAFROTA:
                    fleet = buildFleet(in);
                    break;
                case STATUS:
                    if (fleet != null)
                        fleet.printStatus();
                    break;
                case BATOTA:
                    System.out.println(fleet);
                    break;
                default:
                    System.out.println("Que comando é esse??? Repete lá ...");
            }
            command = in.next();
        }
        System.out.println(GOODBYE_MESSAGE);
    }

    /**
     * This task also tests the fighting element of a round of three shots
     */
    public static void taskD() {
        Scanner in = new Scanner(System.in);
        IFleet fleet = null;
        IGame game = null;
        String command = in.next();
        while (!command.equals(DESISTIR)) {
            switch (command) {
                case NOVAFROTA:
                    fleet = buildFleet(in);
                    game = new Game(fleet);
                    ((Game) game).saveToJson("game_state.json");
                    break;
                case STATUS:
                    if (fleet != null)
                        fleet.printStatus();
                    break;
                case BATOTA:
                    if (game != null)
                        game.printFleet();
                    break;
                case RAJADA:
                    if (game != null) {
                        firingRound(in, game);

                        System.out.println(
                                "Hits: " + game.getHits()
                                        + " Inv: " + game.getInvalidShots()
                                        + " Rep: " + game.getRepeatedShots()
                                        + " Restam " + game.getRemainingShips() + " navios.");

                        if (game.getRemainingShips() == 0)
                            System.out.println("Maldito sejas, Java Sparrow, eu voltarei, glub glub glub...");
                    }
                    break;
                case VERTIROS:
                    if (game != null)
                        game.printValidShots();
                    break;
                default:
                    System.out.println("Que comando é esse??? Repete ...");
            }
            command = in.next();
        }
        System.out.println(GOODBYE_MESSAGE);
    }

    /**
     * This operation allows the build up of a fleet, given user data
     *
     * @param in The scanner to read from
     * @return The fleet that has been built
     */
    static Fleet buildFleet(Scanner in) {
        assert in != null;

        Fleet fleet = new Fleet();
        int i = 0;

        while (i < Fleet.FLEET_SIZE) {
            IShip s = readShip(in);
            if (s != null) {
                boolean success = fleet.addShip(s);
                if (success)
                    i++;
                else
                    System.out.println("Falha na criacao de " + s.getCategory() + " " + s.getBearing() + " " + s.getPosition());
            } else {
                System.out.println("Navio desconhecido!");
            }
        }
        System.out.println(i + " navios adicionados com sucesso!");
        return fleet;
    }

    /**
     * This operation reads data about a ship, build it and returns it
     *
     * @param in The scanner to read from
     * @return The created ship based on the data that has been read
     */
    static Ship readShip(Scanner in) {
        String shipKind = in.next();
        Position pos = readPosition(in);
        char c = in.next().charAt(0);
        Compass bearing = Compass.charToCompass(c);
        return Ship.buildShip(shipKind, bearing, pos);
    }

    /**
     * This operation allows reading a position in the map
     *
     * @param in The scanner to read from
     * @return The position that has been read
     */
    static Position readPosition(Scanner in) {
        int row = in.nextInt();
        int column = in.nextInt();
        return new Position(row, column);
    }

    /**
     * This operation allows firing a round of shots (three) over a fleet, in the
     * context of a game
     *
     * @param in   The scanner to read from
     * @param game The context game while fleet is being attacked
     */


    static void firingRound(Scanner in, IGame game) {
        for (int i = 0; i < NUMBER_SHOTS; i++) {

            IPosition pos = readPosition(in);

            boolean hit = ((Game) game).wasHit(pos);
            IShip sh = game.fire(pos);

            //Mostrar o Board de cada jogada
            ((Game) game).getBoard().printVisual();
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
}
