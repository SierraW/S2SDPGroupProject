package texasholdem;

public class CommandPrompt {
    private static boolean enable;
    private static HoldemGame game;
    private static boolean end;
    private static int numbersOfPlayers;

    CommandPrompt() {
        enable = false;
        numbersOfPlayers = 4;
        game = new HoldemGame(numbersOfPlayers);
        end = false;
    }

    public boolean isEnded() {
        return end;
    }

    public static void comm(String string) throws Exception {
        if (enable) {
            System.out.print("debug:");
        }
        String comm = string.toLowerCase();
        String[] rawComm = comm.split(" ");
        String command = rawComm[0];


        switch (command) {
            case "exit":
                System.out.println("Debug mode disable");
                enable = false;
                break;
            case "enable":
            case "en":
                System.out.println("Debug mode enable");
                enable = true;
                break;
            case "start":
            case "run":
                game.setStatus(GameStatus.ROUNDONE);
                game.run();
                break;
            case "restart":
                System.out.println();
                game = new HoldemGame(numbersOfPlayers);
                game.run();
                break;
            case "end":
                end = true;
                break;
            case "help":
                System.out.println("start, restart, end");
                break;
            case "set":
                if (rawComm[1].matches("\\d{1,2}")) {
                    if (Integer.parseInt(rawComm[1]) > 17) {
                        System.out.println("set player unsuccessful, number too large.");
                        break;
                    }
                    numbersOfPlayers = Integer.parseInt(rawComm[1]);
                    System.out.println("Successfully set to " + numbersOfPlayers + " players.");
                    game = new HoldemGame(numbersOfPlayers);
                } else {
                    System.out.println("set player unsuccessful, try again.");
                }
                break;
            default:
                System.out.println("Unknown Command, type \"help\" for more.");

        }
    }

    public static boolean askForAns(String message) throws Exception {
        InputHandleSystem reader = new InputHandleSystem();
        String command = reader.getLine(message).toLowerCase();
        String[] rawComm = command.split(" ");
        String comm = rawComm[0];

        switch (comm) {
            case "y":
                return true;
            case "n":
                return false;
            case "comm":
                enable = true;
                while (enable) {
                    inGameComm(reader.getLine("comm:\\"));
                }
                break;
            default:
                System.out.println("Unknown Command, type \"help\" for more.");
        }
        return false;
    }

    public static int askForInt(String message) throws Exception {
        InputHandleSystem reader = new InputHandleSystem();
        String[] command = reader.getLine(message).toLowerCase().split(" ");

        switch (command[0]) {
            case "comm":
                enable = true;
                while (enable) {
                    inGameComm(reader.getLine("comm:\\"));
                }
                break;
            default:
                if (command[0].matches("\\d{1,3}")) {
                    return Integer.parseInt(command[0]);
                }
        }
        return 0;
    }

    public static void inGameComm(String comm) throws Exception{
        String[] rawC = comm.split(" ");

        switch (rawC[0]) {
            case "shuffle":
                game.shuffleCards();
                break;
            case "new":
                if (rawC[1].equals("cards")) {
                    game.newCards();
                }
                break;
            case "restart":
                CommandPrompt.comm(rawC[0]);
                break;
            case "stop":
                game.setStatus(GameStatus.BREAK);
                break;
            case "exit":
                enable = false;
                break;
            default:
                System.out.println("Unknown Command, type \"help\" for more.");

        }
    }
}
