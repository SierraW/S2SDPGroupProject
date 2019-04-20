package texasholdem;

public class CommandPrompt {
    private boolean enable;
    HoldemGame game;
    private boolean end;
    private int numbersOfPlayers;

    CommandPrompt() {
        enable = false;
        numbersOfPlayers = 4;
        end = false;
        game = new HoldemGame();
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
            case "restart":
                try {
                    System.out.println();
                    game.setStartsAt(game.getGameCount() % numbersOfPlayers);
                    game.newGame();
                    game.setStatus(GameStatus.ROUNDONE);
                    game.run();
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
                break;
            case "end":
                end = true;
                break;
            case "help":
                System.out.println("start\nend\nhelp\nset [numbers of player]\ncredit [player index starts at 1] [amounts of credit]\n" +
                        "addcredit [player index starts at 1] [amount of credit]\nprint (display card set at borad)\nviewplayer [player index starts at 1]\n" +
                        "display (view all players)\nshuffle\nnewcard\nsetname [player index starts at 1] [name]\nsmallblindbet [amount of blind bet]\n" +
                        "startsat [player index]\n");
                break;
            case "set":
                try {
                    numbersOfPlayers = Integer.parseInt(rawComm[1]);
                    game.setPlayers(numbersOfPlayers);
                    System.out.println("Successfully set to " + numbersOfPlayers + " players.");
                } catch (Exception e) {
                    System.out.println("set number of players unsuccessful, try again.");
                }
                break;
            case "credit":
            case "cash":
                try {
                    if (game.setPlayersCredit(Integer.parseInt(rawComm[1]), Integer.parseInt(rawComm[2]))) {
                        System.out.println("Successfully set credit");
                        System.out.println(game.viewPlayer(Integer.parseInt(rawComm[1])));
                    }
                }catch (Exception e){
                    System.out.println("set player\'s credit unsuccessful, try again.");
                }
                break;
            case "addcrdit":
            case "addcash":
            case "addc":
                try {
                    if (game.addPlayersCredit(Integer.parseInt(rawComm[1]), Integer.parseInt(rawComm[2]))) {
                        System.out.println("Successfully add credit to player");
                        System.out.println(game.viewPlayer(Integer.parseInt(rawComm[1])));
                    }
                }catch (Exception e){
                    System.out.println("add player\'s credit unsuccessful, try again.");
                }
                break;
            case "print":
                game.viewCardSet();
                break;
            case "viewplayer":
            case "viewp":
                try {
                    System.out.println(game.viewPlayer(Integer.parseInt(rawComm[1])));
                } catch (Exception e) {
                    System.out.println("view player unsuccessful, please try again.");
                }
                break;
            case "display":
            case "dis":
                System.out.println(game.displayGameTable(GameStatus.BREAK));
                break;
            case "shuffle":
                game.shuffle();
                System.out.println("Successfully shuffled.");
                break;
            case "newcard":
                game.newCard();
                break;
            case "setname":
            case "setn":
                try{
                    game.setName(Integer.parseInt(rawComm[1]), rawComm[2]);
                    System.out.print("Set name successful! player " + rawComm[1] + " new name: " + game.getName(Integer.parseInt(rawComm[1])) + "\n");
                }catch (Exception e) {
                    System.out.println("Set name fail, please try again.");
                }
                break;
            case "smallblindbet":
            case "sbb":
                try {
                    game.setsBlindBets(Integer.parseInt(rawComm[1]));
                } catch (Exception e) {
                    System.out.println("Set blind bet fail, please try again.");
                }
                break;
            case "startsat":
            case "sa":
                try {
                    game.setStartsAt(Integer.parseInt(rawComm[1]));
                } catch (Exception e) {
                    System.out.println("Set starts at fail, please try again.");
                }
                break;
            default:
                System.out.println("Unknown Command, type \"help\" for more.");

        }
    }
}
