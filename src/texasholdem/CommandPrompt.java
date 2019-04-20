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
    private boolean hasRan = false;

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
            case "shuffle":
            case "srun":
                game.newCard();
                game.shuffle();
                game.shuffle();
                game.shuffle();
                System.out.println("Successfully shuffled.");
                if (rawComm.length > 1) {
                    if (!(rawComm[1].equals("run") || rawComm[0].equals("srun"))) {
                        System.out.println(rawComm[0]);
                        break;
                    }
                } else {
                    if (!rawComm[0].equals("srun")) {
                        break;
                    }
                }
            case "run":
                hasRan = true;
                try {
                    System.out.println();
                    game.newGame();
                    game.setStartsAt(game.getGameCount() % numbersOfPlayers);
                    game.run();
                    System.out.println("Game Start!");
                    System.out.println(gv.startRoundMessage(GameStatus.ROUNDONE));

                    viewGameTable();
                    gameStatus = GameStatus.ROUNDONE;
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                if (rawComm.length > 2) {
                    if (!rawComm[2].equals("start")) {
                        break;
                    }
                } else if (rawComm.length > 1) {
                    if (!rawComm[1].equals("start")) {
                        break;
                    }
                } else {
                    break;
                }
            case "start":
                if (!hasRan) {
                    System.out.println("Please \"run\" first.");
                    break;
                }
                try {
                    boolean condition = true;
                    while (condition) {
                        if (!play()) {
                            condition = false;
                            continue;
                        }
                        if (game.run()) {
                            if (game.getStatus() != gameStatus) {
                                gameStatus = game.getStatus();
                                System.out.println(gv.startRoundMessage(gameStatus));
                                if (gameStatus == GameStatus.CHECK) {
                                    condition = false;
                                }
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
            case "runOnce":
            case "r1":
                try {
                    if (!play()) {
                        break;
                    }
                    if (game.run()) {
                        System.out.println("Successfully run.");
                        if (game.getStatus() != gameStatus) {
                            gameStatus = game.getStatus();
                            System.out.println(gv.startRoundMessage(gameStatus));
                            if (gameStatus == GameStatus.CHECK) {
                                viewGameTable();
                            }
                        }
                        viewGameTable();
                    } else {
                        System.out.println("Run unsuccessful");
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                break;
            case "end":
                end = true;
                break;
            case "help":
                System.out.println("Main game commands:\n\nshuffle (shuffle cards)\nsrun (shuffle and initial the game)\nrun (initial the game, can be initial more times to change the starter player)\nstart (start until the game ends)\nrunOnce (only run one turn)\nsmallblindbet [amount of blind bet] (change the amount of the small blind bet)\n\n\nCard sets commands:\n\nprint (display card set at the board)\nnewcard (replace current card set with a new card set)\nviewplayer [player index starts at 1] (view specific player)\n\n\n" +
                        "Player commands:\n\ndisplay (view all players)\nset [numbers of player] (set the player amount)\ncredit [player index starts at 1] [amounts of credit] (set credit for player)\n" +
                "addcredit [player index starts at 1] [amount of credit] (add credit for player)\nsetname [player index starts at 1] [name] (set name for the player)\n" +
                        "startsat [player index] (change the first start position, please re-initialize the game after change)\n\n\nEnvironment:\n\nasciimode (if your computer not support emoji)\nemojimode (default mode)\n");
                break;
            case "set":
                try {
                    numbersOfPlayers = Integer.parseInt(rawComm[1]);
                    game.setPlayers(numbersOfPlayers);
                    System.out.println("Successfully set to " + numbersOfPlayers + " players.");
                } catch (Exception e) {
                    System.out.println("set number of players unsuccessful (min players: 2, max players: 10), try again.");
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
            case "setNames":
            case "setns":
            case "stns":
                try {
                    for (int i = 1; i < rawComm.length; i++) {
                        game.setName(i, rawComm[i]);
                    }
                    System.out.print("Set name successful!\n");
                    viewGameTable();
                } catch (Exception e) {
                    System.out.println("Set names fail, please try again.");
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
            case "asciimode":
            case "ascii":
                gv.setASCIIMode(true);
                System.out.println("Enter ASCII mode.");
                break;
            case "emojimode":
            case "emoji":
                gv.setASCIIMode(false);
                System.out.println("Enter Emoji mode.");
                break;
            default:
                System.out.println("Unknown Command, type \"help\" for more.");

        }
    }

    public boolean play() throws Exception {
        if (roundStart(game.getCurrentPlayer())) {
            viewGameTable();
            if (game.getCurrentPlayer().getCredit() > 0) {
                playRound(game.getCurrentPlayer(), game.getRoundHighest(), game.getStatus());
            }
            return true;
        }
        return false;
    }

    public void playRound(Player player, int highest, GameStatus round) throws Exception {
        System.out.print(player.getName() + " (Player " + player.getINDEX() + ") Card:  ");
        System.out.print(gv.cardVisualizer(player.getCards(), true));
        System.out.println(" Your credit: " + player.getCredit() + "  round highest bet: " + highest);
        System.out.println("Your current bet: " + player.getRoundCredit(round));

        while (!(game.placeBet(inputHandleSystem.getInt("how many bets you want to add? (at least " + (game.getRoundHighest() - player.getRoundCredit(round)) + "): \n", Domain.hasMinimum, 0)))) {
            if (inputHandleSystem.getChar("bad input, you sure you want to exit this game? (y/n)\n", 'y', 'n') == 'y') {
                player.setActive(false);
                break;
            }
        }
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
    }

    public boolean roundStart(Player currentPlayer) throws Exception {

        debugger.getInput("\n\n\nPass to " + currentPlayer.getName() + " (player " + currentPlayer.getINDEX() + ")\n\n\ntype enter to continue.\n\n\n"); //todo remove debug
        if (game.getStatus() == GameStatus.BREAK) {
            return false;
        }

        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        return true;
    }

    public void viewGameTable() {
        gv.viewGameTable(game.getDeskPlayer(), game.getPlayers(), game.getStatus(), game.getCurrentPlayer(), game.getPlacedCredit());
    }
}
