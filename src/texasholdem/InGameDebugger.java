package texasholdem;

import java.util.ArrayList;

public class InGameDebugger {
    InputHandleSystem inputHandleSystem = new InputHandleSystem();
    private HoldemGame game;
    private boolean enable;

    InGameDebugger(HoldemGame game) {
        this.game = game;
        enable = false;
    }

    public String getInput(String message) throws Exception {
        String command = inputHandleSystem.getLine(message).toLowerCase();
        String[] rawComm = command.split(" ");
        switch (rawComm[0]) {
            case "comm":
                enable = true;
                while (enable) {
                    switchCommand(inputHandleSystem.getLine("comm:\\"));
                }
                break;
            default:

        }
        return command;
    }


    private void switchCommand(String comm) {
        String[] rawC = comm.split(" ");

        switch (rawC[0]) {
            case "player":
                if (rawC[1].matches("\\d{1,2}")) {
                    int pIndex = Integer.parseInt(rawC[1]);
                    if (!(pIndex > game.players.size() - 1)) {
                        if (rawC[2].equals("cards")) {
                            System.out.println(game.players.get(pIndex).viewPlayer(GameStatus.ROUNDONE));
                            System.out.println(game.players.get(pIndex).viewPlayer(GameStatus.ROUNDTWO));
                            System.out.println(game.players.get(pIndex).viewPlayer(GameStatus.ROUNDTHREE));

                        } else {
                            System.out.println("Bad Command, InGameDebugger switchCommand player num ???");
                        }
                    }
                }
            case "display":
            case "dis":
                game.viewCardSet();
                break;
            case "view":
                if (rawC[1].matches("\\d{1,2}")) {
                    int pIndex = Integer.parseInt(rawC[1]);
                    if (!(pIndex > game.players.size() - 1)) {
                        ArrayList<Card> newCards = new ArrayList<>(game.deskCards.getCards());
                        newCards.addAll(game.players.get(pIndex).getCards());
                        CardCheck cardCheck = new CardCheck();
                        cardCheck.checkRanking(newCards);
                    }
                } else {
                    System.out.println("Unknown Command.");
                }
                break;
            case "sort":
                if (rawC[1].matches("\\d{1,2}")) {
                    int pIndex = Integer.parseInt(rawC[1]);
                    if (!(pIndex > game.players.size() - 1)) {
                        ArrayList<Card> newCards = new ArrayList<>(game.deskCards.getCards());
                        newCards.addAll(game.players.get(pIndex).getCards());
                        CardCheck cardCheck = new CardCheck();
                        cardCheck.sortByIndex(newCards);
                        for (Card card : newCards) {
                            System.out.print(card.getSuit().toString() + card.getValue().toString() + "\t");
                        }
                    }
                } else {
                    System.out.println("Unknown Command.");
                }
                System.out.println();
                break;
            case "sortr":
                try {
                    CardCheck cardCheck = new CardCheck();
                    printCards(cardCheck.sortByRank(combineCards(Integer.parseInt(rawC[1]))));
                } catch (Exception e) {
                    System.out.println("Unknown Command");
                }
                System.out.println();
                break;
            case "sorts":
                try {
                    CardCheck cardCheck = new CardCheck();
                    printCards(cardCheck.sortBySuit(combineCards(Integer.parseInt(rawC[1]))));
                    System.out.println();
                } catch (Exception e) {
                    System.out.println("Unknown Command");
                }
                break;
            case "exit":
                enable = false;
                break;
            case "set":
                try {
                    game.players.get(Integer.parseInt(rawC[1])).debugGetCards().set(Integer.parseInt(rawC[2]), new Card(Integer.parseInt(rawC[3])));
                } catch (Exception e){
                    System.out.println("Unknown Command");
                }
                break;
            case "print":
            case "pt":
                System.out.println(game.displayGameTable(GameStatus.DEBUG));
                break;
            case "desk":
                try {
                    game.deskCards.debugGetCards().set(Integer.parseInt(rawC[1]), new Card(Integer.parseInt(rawC[2])));
                } catch (Exception e){
                    System.out.println("Unknown Command");
                }
                break;
            case "isst":
                try {
                    CardCheck cardCheck = new CardCheck();
                    printCards(cardCheck.getStraight(combineCards(Integer.parseInt(rawC[1]))));
                } catch (Exception e) {
                    System.out.println("Unknown Command");
                }
                break;
            case "check":
                try {
                    CardCheck cardCheck1 = new CardCheck();
                    printCards(cardCheck1.check(combineCards(Integer.parseInt(rawC[1]))).CARDS);
                } catch (Exception e) {
                    System.out.println("Unknown Command");
                }
                break;
            case "isfl":
                try {
                    CardCheck cardCheck = new CardCheck();
                    printCards(cardCheck.getFlush(combineCards(Integer.parseInt(rawC[1]))));
                } catch (Exception e) {
                    System.out.println("Unknown Command");
                }
                break;
            case "ispr":
            case "ispa":
                try {
                    CardCheck cardCheck = new CardCheck();

                    printCards(cardCheck.debugGetKind(combineCards(Integer.parseInt(rawC[1]))));
                } catch (Exception e) {
                    System.out.println("Unknown Command");
                }
                break;
            case "end":
                enable = false;
                game.setStatus(GameStatus.BREAK);
                break;
            case "combine":
            case "comb":
            case "com":
                try {
                    printCards(combineCards(Integer.parseInt(rawC[1])));
                } catch (Exception e) {
                    System.out.println("Unknown Command");
                }
                break;
            case "checkall":
            case "chall":
                System.out.println(game.gameEnd());
                break;
            default:
                System.out.println("Unknown Command.");

        }
    }
    
    private ArrayList<Card> combineCards(int playerIndex) {
        ArrayList<Card> newCards = new ArrayList<>(game.deskCards.getCards());
        newCards.addAll(game.players.get(playerIndex).getCards());
        return newCards;
    }

    private void printCards(ArrayList<Card> cards) {
        System.out.println("elements: " + cards.size());
        for (Card card : cards) {
            System.out.print(card.getSuit().toString() + card.getValue().toString() + " ");
        }
        System.out.println();
    }
}
