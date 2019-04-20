package texasholdem;

import java.util.ArrayList;

public class HoldemGame {

    private PlayingCards playingCards;
    private ArrayList<Player> players;
    private Player deskCards;
    private GameStatus status;
    private int pool;
    private int sBlindBets;
    private InputHandleSystem inputHandleSystem = new InputHandleSystem();

    HoldemGame(int numOfPlayers) {
        playingCards = new PlayingCards();
        players = new ArrayList<>();
        Player.indexFactory = 0;
        deskCards = new Player();
        status = GameStatus.BREAK;
        pool = 0;
        sBlindBets = 2;

        for (int i = 0; i< numOfPlayers; i++) {
            players.add(new Player());
        }
    }

    public void run() throws Exception{
        status = GameStatus.ROUNDONE;
        roundOne(GameStatus.ROUNDONE);
        if (status != GameStatus.BREAK) {
            roundTwo();
        }
        if (status != GameStatus.BREAK) {
            roundThree();
        }
        if (status != GameStatus.BREAK) {

        }
    }

    private void roundOne(GameStatus round) throws Exception{

        playingCards.shuffleCards();
        int roundHighest = sBlindBets;

        for (int i = 0; i < 5; i++) {
            deskCards.passCard(playingCards.getFirstCard());

            if (i < 3) {
                deskCards.changeCardFaceAt(i);
            }
        }

        for (int i = 0; i < 2; i++) {
            for (Player player : players) {
                Card card = playingCards.getFirstCard();
                player.passCard(card);
            }
        }

        displayGameTable(round);

        playerLoops(round, roundHighest);



        status = GameStatus.ROUNDTWO;
    }

    private void roundTwo() throws Exception{
        deskCards.changeCardFaceAt(3);
        displayGameTable(GameStatus.ROUNDTWO);
        int roundHighest = 0;

        playLoop(GameStatus.ROUNDTWO, roundHighest);

        status = GameStatus.ROUNDTHREE;
    }

    private void roundThree() throws Exception {
        deskCards.changeCardFaceAt(4);
        displayGameTable(GameStatus.ROUNDTHREE);
        int roundHighest = 0;

        playLoop(GameStatus.ROUNDTHREE, roundHighest);

        status = GameStatus.CHECK;
    }

    private void checkCards() {
        displayGameTable(GameStatus.CHECK);
        ArrayList<Card> cards = new ArrayList<>();
    }

    private void playerLoops(GameStatus round, int roundHighest) throws Exception{
        int i = 1;
        int count = 0;
        while (players.get(i-1).getRoundCredit(round) != roundHighest && players.get(i-1).isActive()) {
            //big blind bets
            if (count++==1) {
                if (roundHighest < sBlindBets * 2) {
                    roundHighest = sBlindBets * 2;
                }
            }
            //check active
            if (!players.get(i-1).isActive()) {
                for (Player player : players) {
                    if (player.isActive()) {
                        i = counter(i, players.size());
                        continue;
                    }
                }
                status = GameStatus.BREAK;
            }
            //start
            inputHandleSystem.getLine("Pass to player " + i +"\n enter to continue.\n\n\n");
            displayGameTable(round);
            players.get(i-1).playRound(roundHighest, pool, round);
            if (!players.get(i-1).isActive()){
                i = counter(i, players.size());
                System.out.print("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
                continue;
            }
            roundHighest = players.get(i-1).getRoundCredit(round);

            i = counter(i, players.size());
            System.out.print("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        }
    }

    private void playLoop(GameStatus round, int roundHighest) throws Exception{
        int i = 1;
        int count = 0;
        ArrayList<Player> activePlayers = new ArrayList<>();

        for(Player player: players) {
            if (player.isActive()) {
                activePlayers.add(player);
            }
        }

        while (count++ < activePlayers.size() || activePlayers.get(i-1).getRoundCredit(round) != roundHighest) {
            inputHandleSystem.getLine("Pass to player " + activePlayers.get(i-1).getINDEX() +"\n enter to continue.\n\n\n");
            displayGameTable(round);
            activePlayers.get(i-1).playRound(roundHighest, pool, round);
            if (!activePlayers.get(i-1).isActive()){
                activePlayers.remove(i-1);
                System.out.print("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
                continue;
            }

            roundHighest = activePlayers.get(i-1).getRoundCredit(round);

            i = counter(i, activePlayers.size());
            System.out.print("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        }
    }

    private void displayGameTable(GameStatus round) {
        System.out.println("Round " + round.ordinal());
        deskCards.viewPlayerCard();

        for (Player player: players) {
            player.viewPlayer(round);
        }

        System.out.println();
    }

    private int counter(int count, int highest) {
        if (count + 1 > highest) {
            return 1;
        } else {
            return ++count;
        }
    }
}
