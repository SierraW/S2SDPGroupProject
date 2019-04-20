package texasholdem;

public class CommandPrompt {
    private boolean enable;
    HoldemGame game;
    private boolean end;
    private int numbersOfPlayers;
    private GameStatus gameStatus;

    CommandPrompt() {
        enable = false;
        numbersOfPlayers = 4;
        game = new HoldemGame(numbersOfPlayers);
        end = false;
        gameStatus = GameStatus.BREAK;
    }

    public boolean isEnded() {
        return end;
    }

    public void comm(String string) throws Exception{
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
                    if (Integer.parseInt(rawComm[1]) > 17){
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
}
