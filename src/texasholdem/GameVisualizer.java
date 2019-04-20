package texasholdem;

import jdk.internal.util.xml.impl.Input;

import java.util.ArrayList;

public class GameVisualizer {

    void cardVisualizer(ArrayList<Card> cards, boolean isForceViewCard) {
        for (Card card : cards) {
            System.out.print(card.printCard(isForceViewCard) + " ");
        }
    }

    void deskVisualizer(Player deskPlayer) {
        System.out.print(GameVisualizer.viewPlayerCard(deskPlayer));
        cardVisualizer(deskPlayer.getCards(), false);
        System.out.println("\n Total Pool: " + deskPlayer.getCredit());
    }

    void playersVisualizer(ArrayList<Player> players, GameStatus round, Player currentPlayer) {
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
                for (Player player : players) {
                    System.out.print(player.getName() + " (Player " + player.getINDEX() + ") :" + player.getCards() + "  Total credit: " + player.getCredit() + " Current Round Bet: " + player.getRoundCredit(round) + " Total Game Bet: " + player.getRoundCredit(GameStatus.CHECK) + " ");
                    if (currentPlayer.getINDEX() == player.getINDEX()) {
                        System.out.print("       >>>>>>>Your Turn<<<<<<<");
                    }
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
}
