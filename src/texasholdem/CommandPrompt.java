package texasholdem;

public class CommandPrompt {
    private boolean enable;
    HoldemGame game;
    private boolean end;
    private int numbersOfPlayers;
    InputHandleSystem inputHandleSystem = new InputHandleSystem();
    InGameDebugger debugger;
    GameVisualizer gv;
    GameStatus gameStatus = GameStatus.BREAK;

    CommandPrompt() {
        enable = false;
        numbersOfPlayers = 4;
        end = false;
        game = new HoldemGame();
        debugger = new InGameDebugger(game);
        gv = new GameVisualizer();
    }

    public boolean isEnded() {
        return end;
    }

    public void comm(String string) throws Exception {
        if (enable) {
            System.out.print("debug:");
        }
        String comm = string;
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
            case "restart":
                try {
                    System.out.println();
                    game.newGame();
                    game.setStartsAt(game.getGameCount() % numbersOfPlayers + 1);
                    game.run();
                    System.out.println("Game Start!");
                    System.out.println(gv.startRoundMessage(GameStatus.ROUNDONE));;
                    gameStatus = GameStatus.ROUNDONE;
                    viewGameTable();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                break;
            case "run":
            case "r":
                try {
                    play();
                    if (game.run()) {
                        System.out.println("Successfully run.");
                        if (game.getStatus() != gameStatus) {
                            gameStatus = game.getStatus();
                            System.out.println(gv.startRoundMessage(gameStatus));
                        }
                        viewGameTable();
                    } else {
                        System.out.println("Run unsuccessful");
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                break;
            case "continuerun":
            case "crun":
                try {
                    boolean condition = true;
                    while (condition) {
                        play();
                        if (game.run()) {
                            if (game.getStatus() != gameStatus) {
                                gameStatus = game.getStatus();
                                System.out.println(gv.startRoundMessage(gameStatus));
                            }
                            viewGameTable();
                        } else {
                            condition = false;
                            System.out.println("Run unsuccessful");
                        }
                    }
                } catch (Exception e) {
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
                        System.out.println(GameVisualizer.viewPlayer(game.getStatus(), game.getPlayers().get(Integer.parseInt(rawComm[1]))));
                        viewGameTable();
                    }
                } catch (Exception e) {
                    System.out.println("set player\'s credit unsuccessful, try again.");
                }
                break;
            case "addcrdit":
            case "addcash":
            case "addc":
                try {
                    if (game.addPlayersCredit(Integer.parseInt(rawComm[1]), Integer.parseInt(rawComm[2]))) {
                        System.out.println("Successfully add credit to player");
                        System.out.println(GameVisualizer.viewPlayer(game.getStatus(), game.getPlayers().get(Integer.parseInt(rawComm[1]))));
                        viewGameTable();
                    }
                } catch (Exception e) {
                    System.out.println("add player\'s credit unsuccessful, try again.");
                }
                break;
            case "print":
                game.viewCardSet();
                break;
            case "viewplayer":
            case "viewp":
                try {
                    System.out.println(GameVisualizer.viewPlayer(game.getStatus(), game.getPlayers().get(Integer.parseInt(rawComm[1]))));
                } catch (Exception e) {
                    System.out.println("view player unsuccessful, please try again.");
                }
                break;
            case "viewcurrentplayer":
            case "viewcp":
            case "vcp":
                try {
                    System.out.println(GameVisualizer.viewPlayer(game.getStatus(), game.getCurrentPlayer()));
                } catch (Exception e) {
                    System.out.println("view player unsuccessful, please try again.");
                }
                break;
            case "display":
            case "dis":
                viewGameTable();
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
                try {
                    game.setName(Integer.parseInt(rawComm[1]), rawComm[2]);
                    System.out.print("Set name successful! player " + rawComm[1] + " new name: " + game.getName(Integer.parseInt(rawComm[1])) + "\n");
                    viewGameTable();
                } catch (Exception e) {
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

    public void play() throws Exception {
        roundStart(game.getCurrentPlayer());

        viewGameTable();
        playRound(game.getCurrentPlayer(), game.getRoundHighest(), game.getStatus());
    }

    public void viewGameTable() {
        gv.deskVisualizer(game.getDeskPlayer());
        gv.playersVisualizer(game.getPlayers(), game.getStatus(), game.getCurrentPlayer());
    }

    public void playRound(Player player, int highest, GameStatus round) throws Exception {
        System.out.print(player.getName() + " (Player " + player.getINDEX() + ") Card:  ");
        gv.cardVisualizer(player.getCards(), true);
        System.out.println("Your credit: " + player.getCredit() + "  round highest bet: " + highest);
        System.out.println("Your current bet: " + player.getRoundCredit(round));

        while (!(game.placeBet(inputHandleSystem.getInt("how many bets you want to add? (at least " + (game.getRoundHighest() - player.getRoundCredit(round)) + "): \n", Domain.hasMinimum, 0)))) {
            if (inputHandleSystem.getChar("bad input, you sure you want to exit this game? (y/n)\n", 'y', 'n') == 'y') {
                player.setActive(false);
            }
        }
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
    }

    public void roundStart(Player currentPlayer) throws Exception {
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");

        debugger.getInput("\n\n\nPass to player " + currentPlayer.getINDEX() + "\n\n\ntype enter to continue.\n\n\n"); //todo remove debug

        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
    }
}
