package texasholdem;

import java.util.ArrayList;

public class GameVisualizer {

    private boolean isASCIIMode = false;

    public void setASCIIMode(boolean ASCIIMode) {
        isASCIIMode = ASCIIMode;
    }

    String cardVisualizer(ArrayList<Card> cards, boolean isForceViewCard) {
        StringBuffer outputStr = new StringBuffer();
        for (Card card : cards) {
            if (isASCIIMode) {
                outputStr.append(" " + card.printCardText(isForceViewCard) + " ");
            } else {
                outputStr.append(" " + card.printCard(isForceViewCard) + " ");
            }
        }
        return outputStr.toString();
    }

    void deskVisualizer(Player deskPlayer) {
        System.out.println("Board:");
        System.out.print(cardVisualizer(deskPlayer.getCards(), false));
        System.out.println("\n Total Pool: " + deskPlayer.getCredit());
    }

    void playersVisualizer(ArrayList<Player> players, GameStatus round, Player currentPlayer, int placedBet) {
        switch (round) {
            case BREAK:
                for (Player player : players) {
                    System.out.print(player.getName() + " (Player " + player.getINDEX() + ") Total credit: " + player.getCredit());
                    if (player == currentPlayer) {
                        System.out.println(" <<");
                    } else {
                        System.out.println();
                    }
                }
                break;
            case ROUNDONE:
            case ROUNDTWO:
            case ROUNDTHREE:
                int previousPlayerIndex;
                System.out.println(round.name());
                for (int i = 0; i < players.size(); i++) {
                    System.out.printf("%7s (Player %2d): %s    Total credit: %5d,  Current round bet: %5d,  Total game bet: %5d  ", players.get(i).getName(), players.get(i).getINDEX(), cardVisualizer(players.get(i).getCards(),false) , players.get(i).getCredit(),players.get(i).getRoundCredit(round),players.get(i).getRoundCredit(GameStatus.CHECK));
                    if (currentPlayer.getINDEX() == players.get(0).getINDEX()) {
                        previousPlayerIndex = players.size() - 1;
                    } else {
                        previousPlayerIndex = currentPlayer.getINDEX() - 2;
                    }

                    if (i == previousPlayerIndex) {
                        if (placedBet > 0) {
                            System.out.print("       >>>>>> Placed " + placedBet + " <<<<<<");
                        } else if (placedBet == 0) {
                            System.out.print("       >>>>> check <<<<<");
                        } else if (placedBet == -1){
                            System.out.print("       >>>> out <<<<<");
                        }
                    }

                    if (currentPlayer.getINDEX() == players.get(i).getINDEX()) {
                        System.out.print("       >>>>>>>Your Turn<<<<<<<");
                    }
                    System.out.println();
                    System.out.println();
                }
                break;
            case CHECK:
                for (Player player : players) {
                    System.out.print(player.getName() + " (Player " + player.getINDEX() + ") Total credit: " + player.getCredit() + " Total Game Bet: " + player.getRoundCredit(round) + " ");
                    System.out.println(player.getCards() + " ");
                }
                break;
        }
    }


    public String startRoundMessage(GameStatus round) {
        String str = "";
        switch (round) {
            case ROUNDONE:
                str += "=========================================" + "\n" +
                        "====       =================   ==========" + "\n" +
                        "====  =====  ==============    ==========" + "\n" +
                        "====  ======  ============  =  ==========" + "\n" +
                        "====  =======  ==========  ==  ==========" + "\n" +
                        "====  ======  ===============  ==========" + "\n" +
                        "====  ====  =================  ==========" + "\n" +
                        "====      ===================  ==========" + "\n" +
                        "====   ======================  ==========" + "\n" +
                        "====  =  ====================  ==========" + "\n" +
                        "====  ===  ==================  ==========" + "\n" +
                        "====  ====  =================  ==========" + "\n" +
                        "====  =====  ================  ==========" + "\n" +
                        "====  ======  ===============  ==========" + "\n" +
                        "====  =======  ============      ========" + "\n" +
                        "=========================================";

                break;
            case ROUNDTWO:
                str += "=========================================" + "\n" +
                        "====       =============       ==========" + "\n" +
                        "====  =====  =========  ======  =========" + "\n" +
                        "====  ======  =======  ========  ========" + "\n" +
                        "====  =======  =================  =======" + "\n" +
                        "====  ======  =================  ========" + "\n" +
                        "====  ====  ==================  =========" + "\n" +
                        "====      ===================  ==========" + "\n" +
                        "====   =====================  ===========" + "\n" +
                        "====  =  ==================  ============" + "\n" +
                        "====  ===  ===============  =============" + "\n" +
                        "====  ====  =============  ==============" + "\n" +
                        "====  =====  ===========  ===============" + "\n" +
                        "====  ======  =========  ================" + "\n" +
                        "====  =======  =======           ========" + "\n" +
                        "=========================================";
                break;
            case ROUNDTHREE:
                str += "=========================================" + "\n" +
                        "====       =============       ==========" + "\n" +
                        "====  =====  =========  ======  =========" + "\n" +
                        "====  ======  =======  ========  ========" + "\n" +
                        "====  =======  =================  =======" + "\n" +
                        "====  ======  =================  ========" + "\n" +
                        "====  ====  ==================  =========" + "\n" +
                        "====      ===================  ==========" + "\n" +
                        "====   =====================  ===========" + "\n" +
                        "====  =  ====================  ==========" + "\n" +
                        "====  ===  ===================  =========" + "\n" +
                        "====  ====  ===================  ========" + "\n" +
                        "====  =====  ========  =========  =======" + "\n" +
                        "====  ======  ========  =======  ========" + "\n" +
                        "====  =======  ========         =========" + "\n" +
                        "=========================================";
                break;
            case CHECK:
                str += "Game FIN!";
                break;
            case BREAK:
                str += "Game Break.";
        }
        return str;
    }

    static String viewPlayer(GameStatus round, Player player) {
        String outStr = "";
        if (player.getINDEX() != 0) {
            outStr += (player.getName() + " (Player " + player.getINDEX() + "): ");
        }
        for (Card card : player.getCards()) {
            if (card != null && player.isActive()) {
                outStr += (card.printCard(false) + " ");
            } else {
                outStr += ("  ");
            }
        }
        switch (round) {
            case BREAK:
                outStr += ("    Player total credit: " + player.getCredit() + "\n");
                break;
            case CHECK:
                outStr += ("Total bet: " + player.getRoundCredit(GameStatus.CHECK) + "    Player total credit: " + player.getCredit() + "\n");
                break;
            default:
                outStr += ("Total bet: " + player.getRoundCredit(GameStatus.CHECK) + "    current round credit: " + player.getRoundCredit(round) + "    Player total credit: " + player.getCredit() + "\n");
        }
        return outStr;
    }

    static String viewPlayerCard(Player player) {
        String outSt = "";
        if (player.getINDEX() == 0) {
            outSt += "Desk:    ";
        } else {
            outSt += (player.getName() + " (Player " + player.getINDEX() + "): ");
        }
        for (Card card : player.getCards()) {
            if (card != null) {
                outSt += (card.printCard(false) + " ");
            }
        }
        if (player.getINDEX() == 0) {
            outSt += "Pool: " + player.getCredit() + "\n";
        }
        return outSt + "\n";
    }

    static String viewCard(ArrayList<Card> cards) {
        String outStr = "";
        for (Card card : cards) {
            outStr += (card.printCard(true) + " ");
        }
        return outStr;
    }

    public void viewGameTable(Player deskPlayer, ArrayList<Player> players, GameStatus round, Player currentPlayer, int placedCredit) {
        deskVisualizer(deskPlayer);
        playersVisualizer(players, round, currentPlayer, placedCredit);
    }
}
