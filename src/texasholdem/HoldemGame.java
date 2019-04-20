package texasholdem;

import java.util.ArrayList;

public class HoldemGame {

    private PlayingCards playingCards;
    ArrayList<Player> players;
     Player deskCards;
    private GameStatus status;
    private int pool;
    private int sBlindBets;
    private InGameDebugger debugger;
    private InputHandleSystem inputHandleSystem = new InputHandleSystem();
    private Player currentPlayer;
    private int count;

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

        currentPlayer = players.get(0);
        reset();
        debugger = new InGameDebugger(this);
    }

    private void reset() {
        count = 0;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    public void run() throws Exception{
        if (status == GameStatus.ROUNDONE) {
            roundOne(GameStatus.ROUNDONE);
        }
        if (status == GameStatus.ROUNDTWO) {
            roundTwo();
        }
        if (status == GameStatus.ROUNDTHREE) {
            roundThree();
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
        reset();
        while (currentPlayer.getRoundCredit(round) != roundHighest && currentPlayer.isActive()) {
            //big blind bets
            if (count++==1) {
                if (roundHighest < sBlindBets * 2) {
                    roundHighest = sBlindBets * 2;
                }
            }
            //check active
            if (!currentPlayer.isActive()) {
                for (Player player : players) {
                    if (player.isActive()) {
                        currentPlayer = getNextPlayer();
                        break;
                    }
                }
                status = GameStatus.BREAK;
            }
            //start
            debugger.getInput("Pass to player " + currentPlayer.getINDEX() +"\n enter to continue.\n\n\n"); //todo remove debug
            if (status == GameStatus.BREAK) {
                break;
            }
            displayGameTable(round);
            currentPlayer.playRound(roundHighest, pool, round);
            if (!currentPlayer.isActive()){
                currentPlayer = getNextPlayer();
                System.out.print("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
                continue;
            }
            roundHighest = currentPlayer.getRoundCredit(round);

            currentPlayer = getNextPlayer();
            System.out.print("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        }

        if (currentPlayer.getRoundCredit(round) == roundHighest && currentPlayer.isActive()) {
            status = GameStatus.ROUNDTWO;
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

    void displayGameTable(GameStatus round) {
        if (round == GameStatus.CHECK) {
            deskCards.viewCard();

            for (Player player: players) {
                player.viewCard();
            }

            System.out.println();
            return;
        }

        System.out.println("Round " + round.ordinal());
        deskCards.viewPlayerCard();

        for (Player player: players) {
            player.viewPlayer(round);
        }

        System.out.println();
    }

    public void viewCardSet() {
        playingCards.viewCardSet();
    }

    public void shuffle() {
        playingCards.shuffleCards();
    }

    public void newCard() {
        playingCards = new PlayingCards();
    }

    private int counter(int count, int highest) {
        if (count + 1 > highest) {
            return 1;
        } else {
            return ++count;
        }
    }

    private Player getNextPlayer() {
        if (currentPlayer.getINDEX() + 1 > players.size()) {
            // index 0
            return players.get(0);
        } else {
            // index ++
            return players.get(currentPlayer.getINDEX());
        }
    }

    private void gameEnd(HandRanking hr) {
        ArrayList<Player> remainingPlayers = new ArrayList<>();
        for (Player player: players) {
            if (player.isActive()) {
                player.getCards().addAll(deskCards.getCards());
                remainingPlayers.add(player);
            }
        }


    }
}
